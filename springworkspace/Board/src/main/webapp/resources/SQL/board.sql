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
