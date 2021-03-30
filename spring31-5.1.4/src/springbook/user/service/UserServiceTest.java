package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/testApplicationContext.xml")
public class UserServiceTest {
	@Autowired 	UserService userService;	
	@Autowired UserDao userDao;
	
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
				new User("bumjin", "최범진", "1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
				new User("joytouch", "하즐만", "1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
				new User("erwins", "이어윈", "1234", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD - 1),
				new User("madnite1", "야심찬", "1234", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
				new User("green", "푸르른", "1234", Level.GOLD, 100, Integer.MAX_VALUE)
			);
	}

	@Test
	public void upgradeLevels() {
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		userService.upgradeLevels();
		
		//체크할 레벨을 일일이 적어준다면, 레벨값이 바뀌었을때(레벨값 중간에 추가 등) 수정이 불가피하다.
		checkLevelUpgraded(users.get(0), Level.BASIC);
		checkLevelUpgraded(users.get(1), Level.SILVER);
		checkLevelUpgraded(users.get(2), Level.SILVER);
		checkLevelUpgraded(users.get(3), Level.GOLD);
		checkLevelUpgraded(users.get(4), Level.GOLD);
	}

 
	private void checkLevelUpgraded(User user, Level expectedLevel) {
		User userAfterUpdate = userDao.get(user.getId());
		assertThat(userAfterUpdate.getLevel(), is(expectedLevel));
	}

	@Test 
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);	  // GOLD 레벨 
		User userWithoutLevel = users.get(0);  
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);	  
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel())); 
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
	}
}

