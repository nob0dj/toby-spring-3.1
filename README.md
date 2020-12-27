# toby-spring-3.1
토비의 스프링 3.1 예제 테스트 workspace입니다.

# 환경
* oracle 11g xe

# toby계정 생성
관리자(system)계정으로 접속해서 toby계정을 생성한다.

```
----------------------------------------------------
-- 관리자(system)계정
----------------------------------------------------
--user toby 생성 및 권한 부여
create user toby
identified by toby
default tablespace users;

grant connect, resource to toby;

```
