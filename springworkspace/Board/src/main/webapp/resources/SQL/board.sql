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
where userid= 'juju';

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



------------------------ >>>  페이징 처리하기 <<< -----------------------------------------
begin
    for i in 1..100 loop 
        insert into tblBoard(seq, fk_userid, name, subject, content, pw, readCount, regDate, status)
        values(boardSeq.nextval, 'jwjw', '지원지원', '좋은아침~'||i, '오늘도 힘차게 달려봅시다!'||i, '1234', default, default, default); 
    end loop;
end;   

begin
    for i in 1..100 loop 
        insert into tblBoard(seq, fk_userid, name, subject, content, pw, readCount, regDate, status)
        values(boardSeq.nextval, 'rainbow', '무지개', '쉽게만 살아가면 재미없어 빙고!'||i, '히히^.^'||i, '1234', default, default, default); 
    end loop;
end;

select *
from tblBoard
order by seq desc;

commit;

begin
    for i in 1..100 loop 
        insert into tblComment(seq, fk_userid, name, content, regdate, parentseq, status)
        values(commentSeq.nextval, 'jwjw', '지원지원', '나는 정말 내가 좋아!'||i, default, 6, default);
    end loop;
end; 

select *
from tblBoard

update tblBoard set commentCount = commentCount + 100
where seq = 1;

select *
from tblComment
where parentseq = 1
order by seq desc;

select seq, commentCount
from tblBoard
where seq = 1;

commit;

select count(content)
from tblBoard
where status =1;



select seq, fk_userid, name, subject,  
       readcount, to_char(regDate, 'yyyy-mm-dd hh24:mi:ss') as regDate,
       commentCount
from tblBoard
where status = 1
and subject like '%'|| '아침' ||'%'
order by seq desc   


select seq, fk_userid, name, subject, readcount, regDate, commentCount
from 
(
    select rownum AS rno,
           seq, fk_userid, name, subject, readcount, regDate, commentCount
    from 
    (
        select seq, fk_userid, name, subject,  
               readcount, to_char(regDate, 'yyyy-mm-dd hh24:mi:ss') as regDate,
               commentCount
        from tblBoard
        where status = 1
        and subject like '%'|| '아침' ||'%'
        order by seq desc
    ) V
) T
where rno between 1 and 10;   -- 1페이지   1   10

-- 또는

select seq, fk_userid, name, subject, readcount, regDate, commentCount
from 
(
select row_number() over(order by seq desc) AS rno, 
       seq, fk_userid, name, subject,  
       readcount, to_char(regDate, 'yyyy-mm-dd hh24:mi:ss') as regDate,
       commentCount
from tblBoard
where status = 1
and subject like '%'|| '아침' ||'%'
) V
where rno between 1 and 10;   -- 1페이지   1   10
        

select seq, fk_userid, name, subject, readcount, regDate, commentCount
from 
(
    select rownum AS rno,
           seq, fk_userid, name, subject, readcount, regDate, commentCount
    from 
    (
        select seq, fk_userid, name, subject,  
               readcount, to_char(regDate, 'yyyy-mm-dd hh24:mi:ss') as regDate,
               commentCount
        from tblBoard
        where status = 1
        and subject like '%'|| '아침' ||'%'
        order by seq desc
    ) V
) T
where rno between 11 and 20;   -- 2페이지   11  20
  
  select seq, fk_userid, name, subject, readcount, regDate, commentCount
  from 
  (
    select row_number() over(order by seq desc) AS rno, 
           seq, fk_userid, name, subject,  
           readcount, to_char(regDate, 'yyyy-mm-dd hh24:mi:ss') as regDate,
           commentCount
    from tblBoard
    where status = 1
    and subject like '%'|| '아침' ||'%'
  ) V
  where rno between 11 and 20;   -- 2페이지   11   10
  
                    
select seq, fk_userid, name, subject, readcount, regDate, commentCount
from 
(
    select rownum AS rno,
           seq, fk_userid, name, subject, readcount, regDate, commentCount
    from 
    (
        select seq, fk_userid, name, subject,  
               readcount, to_char(regDate, 'yyyy-mm-dd hh24:mi:ss') as regDate,
               commentCount
        from tblBoard
        where status = 1
        and subject like '%'|| '아침' ||'%'
        order by seq desc
    ) V
) T
where rno between 21 and 30;  -- 3페이지   21  30

select seq, fk_userid, name, subject, readcount, regDate, commentCount
from 
(
select row_number() over(order by seq desc) AS rno, 
       seq, fk_userid, name, subject,  
       readcount, to_char(regDate, 'yyyy-mm-dd hh24:mi:ss') as regDate,
       commentCount
from tblBoard
where status = 1
and subject like '%'|| '아침' ||'%'
) V
where rno between 21 and 30;   -- 3페이지   21   30



select name, content, regDate
from 
(
    select row_number() over(order by seq desc) AS rno, 
          name, content, to_char(regDate, 'yyyy-mm-dd hh24:mi:ss') as regDate
    from tblComment
    where status = 1 and parentSeq = '1'
) V
where rno between 1 and 5;      --- 1페이지


select name, content, regDate
from 
(
    select row_number() over(order by seq desc) AS rno, 
          name, content, to_char(regDate, 'yyyy-mm-dd hh24:mi:ss') as regDate
    from tblComment
    where status = 1 and parentSeq = '1'
) V
where rno between 6 and 10;      --- 2페이지





--------------------------------------------------------------------------------
     -------- **** 댓글 및 답변형 파일첨부가 있는 게시판 **** ---------
     
drop table tblComment purge;
drop table tblBoard purge;

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
,groupno        number                not null   -- 답변글쓰기에 있어서 그룹번호 
                                                 -- 원글(부모글)과 답변글은 동일한 groupno 를 가진다.
                                                 -- 답변글이 아닌 원글(부모글)인 경우 groupno 의 값은 groupno 컬럼의 최대값(max)+1 로 한다.

,fk_seq         number default 0      not null   -- fk_seq 컬럼은 절대로 foreign key가 아니다.!!!!!!
                                                 -- fk_seq 컬럼은 자신의 글(답변글)에 있어서 
                                                 -- 원글(부모글)이 누구인지에 대한 정보값이다.
                                                 -- 답변글쓰기에 있어서 답변글이라면 fk_seq 컬럼의 값은 
                                                 -- 원글(부모글)의 seq 컬럼의 값을 가지게 되며,
                                                 -- 답변글이 아닌 원글일 경우 0 을 가지도록 한다.

,depthno        number default 0       not null  -- 답변글쓰기에 있어서 답변글 이라면
                                                 -- 원글(부모글)의 depthno + 1 을 가지게 되며,
                                                 -- 답변글이 아닌 원글일 경우 0 을 가지도록 한다.

,fileName       varchar2(255)                    -- WAS(톰캣)에 저장될 파일명(20190725092715353243254235235234.png)                                       
,orgFilename    varchar2(255)                    -- 진짜 파일명(강아지.png)  // 사용자가 파일을 업로드 하거나 파일을 다운로드 할때 사용되어지는 파일명 
,fileSize       number                           -- 파일크기  

,constraint  PK_tblBoard_seq primary key(seq)
,constraint  FK_tblBoard_userid foreign key(fk_userid) references mymvc_shopping_member(userid)
,constraint  CK_tblBoard_status check( status in(0,1) )
);

drop sequence boardSeq;

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

drop sequence commentSeq;

create sequence commentSeq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

select *
from tblBoard;


begin
    for i in 1..100 loop 
        insert into tblBoard(seq, fk_userid, name, subject, content, pw, readCount, regDate, status, groupno)
        values(boardSeq.nextval, 'jwjw', '지원지원', '좋은아침~'||i, '오늘도 힘차게 달려봅시다!'||i, '1234', default, default, default, i); 
    end loop;
end;   

begin
    for i in 1..200 loop 
        insert into tblBoard(seq, fk_userid, name, subject, content, pw, readCount, regDate, status, groupno)
        values(boardSeq.nextval, 'rainbow', '무지개', '쉽게만 살아가면 재미없어 빙고!'||i, '히히^.^'||i, '1234', default, default, default, i); 
    end loop;
end;

select *
from tblBoard
order by seq desc;

commit;

select *
from tblBoard;

begin
    for i in 1..100 loop 
        insert into tblComment(seq, fk_userid, name, content, regdate, parentseq, status)
        values(commentSeq.nextval, 'jwjw', '지원지원', '나는 정말 내가 좋아!'||i, default, 1, default);
    end loop;
end; 

select *
from tblBoard

update tblBoard set commentCount = commentCount + 100
where seq = 1;

select *
from tblComment
where parentseq = 1
order by seq desc;

select seq, commentCount
from tblBoard
where seq = 1;

commit;

select *
from tblBoard
order by seq desc;

select *
from tblBoard
where seq = 198


-- 답변형 게시판의 계층형 쿼리문 작성하기
select seq, fk_userid, name, subject, readcount, regDate, commentCount
     , groupno, fk_seq, depthno 
from 
(
    select rownum AS rno
         , seq, fk_userid, name, subject, readcount, regDate, commentCount  
         , groupno, fk_seq, depthno 
    from
    (
        select seq, fk_userid, name, subject,  
               readcount, to_char(regDate, 'yyyy-mm-dd hh24:mi:ss') as regDate,
               commentCount,
               groupno, fk_seq, depthno
        from tblBoard
        where status = 1
        start with fk_seq = 0
        connect by prior seq = fk_seq
        order siblings by groupno desc, seq asc
    ) V
) T
where rno between 1 and 10
