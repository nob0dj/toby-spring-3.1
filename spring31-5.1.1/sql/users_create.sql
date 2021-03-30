--drop table users;
CREATE TABLE USERS (
	ID VARCHAR2(10) PRIMARY KEY,	
	NAME VARCHAR2(20) NOT NULL,
	PASSWORD VARCHAR2(10) NOT NULL,
	"LEVEL" NUMBER NOT NULL, --oracle에서 예약어 level을 사용할 수 없으므로, ""로 감싼다. 이때 대소문자를 구분하므로 주의할 것.
	LOGIN NUMBER NOT NULL,
	RECOMMEND NUMBER NOT NULL
);

desc users;
