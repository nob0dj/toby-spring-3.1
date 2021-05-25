package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/testApplicationContext.xml")
public class UserServiceTest {
	
	@Autowired UserService userService;	
	@Autowired UserServiceImpl userServiceImpl; // 순수하게 UserSevice 업무로직만 테스트	
	@Autowired UserDao userDao;
	@Autowired PlatformTransactionManager txManager;
	
	List<User> users;	// test fixture
	
	/**
	 * 경계값 전후의 데이터를 fixture로 생성
	 * 
	 * 1. Level.BASIC 로그인수 50회
	 * 2. Level.SILVER 추천수 30회
	 */
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("bumjin", "최범진", "1234", "bumjin@hello.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
				new User("joytouch", "하즐만", "1234", "dongxuan09@naver.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
				new User("erwins", "이어윈", "1234", "erwins@hello.com", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD - 1),
				new User("madnite1", "야심찬", "1234", "dongxuan1863@gmail.com", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
				new User("green", "푸르른", "1234", "green@hello.com", Level.GOLD, 100, Integer.MAX_VALUE)
			);
	}

	@Test 
	@DirtiesContext // 이 테스트케이스만 별도의 context를 사용할 것임.
	public void upgradeLevels() throws Exception{
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		MockMailSender mockMailSender = new MockMailSender();
		// 순수하게 UserSevice 업무로직만 테스트
		userServiceImpl.setMailSender(mockMailSender);  
		
		userServiceImpl.upgradeLevels();
		
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
		
		// 테스트 종료후, mockMailSender객체에 저장된 필드값을 확인해서 테스트대상객체 UserService와 의존객체 MailSender와의 관계를 검증
		List<String> request = mockMailSender.getRequests();  
		assertThat(request.size(), is(2));  
		assertThat(request.get(0), is(users.get(1).getEmail()));  
		assertThat(request.get(1), is(users.get(3).getEmail()));  
	}
	
	static class MockMailSender implements MailSender {
		private List<String> requests = new ArrayList<String>();	
		
		public List<String> getRequests() {
			return requests;
		}

		public void send(SimpleMailMessage mailMessage) throws MailException {
			requests.add(mailMessage.getTo()[0]);  
		}

		public void send(SimpleMailMessage[] mailMessage) throws MailException {
		}
	}


	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}
		else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}

	@Test 
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);	  // GOLD레벨
		User userWithoutLevel = users.get(0);  
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);	  
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel())); 
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
	}
	
	/**
	 * 여러회원의 레벨 체크 및 업그레이드 기능은 전체 성공 또는 전체 실패여야 한다.
	 * 
	 * 트랜잭션 실패용 클래스는 별도롤 생성하지 말고, UserService를 상속해 내부클래스로 만든다.
	 *  
	 */
	@Test
	public void upgradeLevelsTransaction() throws Exception {
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		//업무로직 전용 UserService : 트랜잭션테스트에 필요한 중간 예외던짐 코드 오버라딩함.
		TransactionTestUserService userServiceImpl = new TransactionTestUserService(users.get(3).getId());
		//테스트만 진행하면 되니, xml등록 없이 수동 DI한다.
		userServiceImpl.setUserDao(userDao);
		userServiceImpl.setMailSender(new MockMailSender());
		
		//UserService빈(트랜잭션전용)에 TransacetionTestUserService빈 수동 DI
		UserServiceTx userService = (UserServiceTx) this.userService;
		userService.setUserService(userServiceImpl);
		
		try {
			userService.upgradeLevels();
			fail("upgradeLevels가 정상종료 되어 실패!");
		} catch (UserServiceTransactionException e) {
			//UserServiceTransactionException외 의 예외가 발생할 경우, 테스트실패이므로  catch하면 안된다.
			e.printStackTrace();
		}
		
		//해당 사용자는 트랜잭션 처리 중 실패했으므로, upgrade가 되면 안된다. 모두 실패해야 한다.
		checkLevelUpgraded(users.get(1), false);
	}
	
	/**
	 * 업무로직 구현클래스 UserServiceImpl 상속
	 *
	 */
	class TransactionTestUserService extends UserServiceImpl {
		private String id;
		
		public TransactionTestUserService(String id) {
			this.id = id;
		}
		public void upgradeLevel(User user) {
			if(this.id.equals(user.getId()))
				throw new UserServiceTransactionException("[" + id + "] Upgade Error!");
			
			user.upgradeLevel();
			userDao.update(user);
		}
	}
	
	class UserServiceTransactionException extends RuntimeException {

		public UserServiceTransactionException(String msg) {
			super(msg);
		}
		
	}
}

