---------- ** 스프링 게시판 ** ----------

show user;
-- USER이(가) "MYORAUSER"입니다.

create table spring_test1
( no            number
, name          varchar2(100)
, writeday      date default sysdate
);

select *
from spring_test1;

delete from spring_test1;
commit;




------------------------------------------------------------------------------------

show user;
-- USER이(가) "MYORAUSER"입니다.

create table board_img_advertise
(imgno          number not null
,imgfilename    varchar2(100) not null
,constraint PK_board_img_advertise primary key(imgno)
);

create sequence seq_img_advertise
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into board_img_advertise values(seq_img_advertise.nextval, '미샤.png');
insert into board_img_advertise values(seq_img_advertise.nextval, '원더플레이스.png');
insert into board_img_advertise values(seq_img_advertise.nextval, '레노보.png');
insert into board_img_advertise values(seq_img_advertise.nextval, '동원.png');
commit;

select *
from board_img_advertise
order by imgno desc;


select *
from mymvc_shopping_member;

select *
from mymvc_shopping_member
where userid = 'jwjw';


-- 로그인하는 회원에게 등급을 부여해 접근권한 다르게 설정하기
alter table mymvc_shopping_member
add gradelevel number(2) default 1;

update mymvc_shopping_member set gradelevel = 10
where userid in ('jwjw', 'admin');

commit;

update mymvc_shopping_member set lastPwdChangeDate = '20/01/14'
where userid= 'rainbow';

select idx, userid, name, email, gradelevel
    , trunc( months_between(sysdate, lastPwdChangeDate) ) AS pwdchangegap 
	, trunc( months_between(sysdate, lastLoginDate) ) AS lastlogindategap 
from mymvc_shopping_member
where status = 1 and userid = 'jwjw' and pwd = '9695b88a59a1610320897fa84cb7e144cc51f2984520efb77111d94b402a8382';



    ------- **** 게시판(답변글쓰기가 없고, 파일첨부도 없는) 글쓰기 **** -------
desc mymvc_shopping_member;

create table tblBoard
(seq         number                not null    -- 글번호
,fk_userid   varchar2(20)          not null    -- 사용자ID
,name        varchar2(20)          not null    -- 글쓴이 
,subject     Nvarchar2(200)        not null    -- 글제목
,content     Nvarchar2(2000)       not null    -- 글내용   -- clob (최대 4GB까지 허용) 
,pw          varchar2(20)          not null    -- 글암호
,readCount   number default 0      not null    -- 글조회수
,regDate     date default sysdate  not null    -- 글쓴시간
,status      number(1) default 1   not null    -- 글삭제여부   1:사용가능한 글,  0:삭제된글
,constraint PK_tblBoard_seq primary key(seq)
,constraint FK_tblBoard_fk_userid foreign key(fk_userid) references mymvc_shopping_member(userid)
,constraint CK_tblBoard_status check( status in(0,1) )
);

create sequence boardSeq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

select *
from tblBoard
order by seq desc;


---- *** lag() , lead() *** --- ex) 게시판에서 이전글 보기, 다음글 보기를 작성하고자 할 때 사용한다.
  
-- lag  ==> 어떤행의 바로앞의 몇번째 행을 가리키는 것.
-- lead ==> 어떤행의 바로뒤의 몇번째 행을 가리키는 것.

select  previousseq, previoussubject, 
        seq, fk_userid, name, subject, content, readCount, regDate, 
        nextseq, nextsubject
from 
(
    select lag(seq, 1) over(order by seq desc)  as previousseq
        -- 한 칸 앞에       글 번호의 내림차순으로 정렬
            , lag(subject, 1) over(order by seq desc) as previoussubject --앞에 사람
           -- 한 칸 앞의 제목 보기
            , seq, fk_userid, name, subject, content, readCount
            , to_char(regDate, 'yyyy-mm-dd hh24:mi:ss') as regDate
           
            , lead(seq, 1) over(order by seq desc) as nextseq
            , lead(subject, 1) over(order by seq desc) as nextsubject --뒤에 사람
    from tblBoard
    where status = 1
) V
where V.seq = 2;

select *
from mymvc_shopping_member
where userid = 'jwjw';


------------------------------------------------------------------------
   ----- **** 댓글 게시판 **** -----

/* 
  댓글쓰기(tblComment 테이블)를 성공하면 원게시물(tblBoard 테이블)에
  댓글의 갯수(1씩 증가)를 알려주는 컬럼 commentCount 을 추가하겠다. 
*/

drop table tblBoard purge;
drop sequence boardSeq;

create table tblBoard
(seq            number                not null   -- 글번호
,fk_userid      varchar2(20)          not null   -- 사용자ID
,name           Nvarchar2(20)         not null   -- 글쓴이
,subject        Nvarchar2(200)        not null   -- 글제목
,content        Nvarchar2(2000)       not null   -- 글내용    -- clob
,pw             varchar2(20)          not null   -- 글암호
,readCount      number default 0      not null   -- 글조회수
,regDate        date default sysdate  not null   -- 글쓴시간
,status         number(1) default 1   not null   -- 글삭제여부  1:사용가능한글,  0:삭제된글 
,commentCount   number default 0      not null   -- 댓글의 갯수
,constraint  PK_tblBoard_seq primary key(seq)
,constraint  FK_tblBoard_userid foreign key(fk_userid) references mymvc_shopping_member(userid)
,constraint  CK_tblBoard_status check( status in(0,1) )
);

create sequence boardSeq
start with 1
increment by 1
nomaxvalue 
nominvalue
nocycle
nocache;


----- **** 댓글 테이블 생성 **** -----
create table tblComment
(seq           number               not null   -- 댓글번호
,fk_userid     varchar2(20)         not null   -- 사용자ID
,name          varchar2(20)         not null   -- 성명
,content       varchar2(1000)       not null   -- 댓글내용
,regDate       date default sysdate not null   -- 작성일자
,parentSeq     number               not null   -- 원게시물 글번호
,status        number(1) default 1  not null   -- 글삭제여부
                                               -- 1 : 사용가능한 글,  0 : 삭제된 글
                                               -- 댓글은 원글이 삭제되면 자동적으로 삭제되어야 한다.
,constraint PK_tblComment_seq primary key(seq)
,constraint FK_tblComment_userid foreign key(fk_userid)
                                    references mymvc_shopping_member(userid)
,constraint FK_tblComment_parentSeq foreign key(parentSeq) 
                                      references tblBoard(seq) on delete cascade
,constraint CK_tblComment_status check( status in(1,0) ) 
);

create sequence commentSeq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


select *
from tblComment
order by seq desc;


insert into tblBoard(seq, fk_userid, name, subject, content, pw, readCount, regDate, status)
values(boardSeq.nextval, 'jwjw', '지원지원', '죠니 입니다.', '안녕하세요? 곽지원입니다.', '1234', default, default, default);

commit;

----------------------------------------------------------------------------------------------------------------------
select *
from tblComment
order by seq desc;

select commentcount 
from tblBoard
where seq = '1';

--update tblBoard set commentcount = (select count(*) from tblComment where parentseq = 1 )
--where seq = 1;
--commit;

delete from tblComment where seq = 4;

select *
from tblBoard;

select *
from tblBoard
where subject like '%죠%';

select distinct subject
from tblBoard
where status = 1 and lower(subject) like '%' || lower('W') || '%';
-- lower써줘야 대소문자 구분없이 나옴

select distinct name
from tblBoard
where status = 1 and lower(name) like '%' || lower('지') || '%';


--------------------------------------------------------------------------

create table spring_testReservation
(fk_userid  varchar2(20)    not null    -- 사용자ID
,email      varchar2(200)   not null    -- 이메일
,vistdate   date not null               -- 방문일자
);

select *
from spring_testReservation;

select *
from mymvc_shopping_member;

insert into spring_testReservation(fk_userid, email, vistdate)
values('jwjw', 'gs/ch+Ao+KXpNg33IXBS26//FRNGSyrq/8P76tHymFs=', to_date('2020-07-23 10:00:00', 'yyyy-mm-dd hh24:mi:ss'));

commit;

select to_date(to_char(vistdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') - to_char(sysdate, 'yyyy-mm-dd')
from spring_testReservation;

select fk_userid, email, to_char(vistdate, 'yyyy-mm-dd hh24:mi:ss') as vistdate
from spring_testReservation
where to_date(to_char(vistdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') - to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd') = 2;

------------------------------------
    회원명     이메일      예약방문일자
------------------------------------