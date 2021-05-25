# 6.2 고립된 단위 테스트 
p.413

![UserService 의존구조](https://d.pr/i/bzKPMk+)
![UserService 고립된 테스트구조](https://d.pr/i/vbSP6Y+)

* client: UserServiceTest
* test주체: UserService
* 의존객체
	* UserDao -> MockUserDao
	* MailSender -> MockMailSender
	* PlatformTransactionManager - UserServiceTx가 의존

