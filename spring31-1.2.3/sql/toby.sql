----------------------------------------------------
-- 관리자(system)계정
----------------------------------------------------
--user toby 생성 및 권한 부여
create user toby
identified by toby
default tablespace users;

grant connect, resource to toby;

----------------------------------------------------
-- toby계정
----------------------------------------------------
--table users 생성
create table users (
	id varchar(10) primary key,	
	name varchar(20) not null,
	password varchar(10) not null
);

--table users 조회
select 
    *
from 
    users;
    
--모든 행 삭제
truncate table users;
    
    
    
