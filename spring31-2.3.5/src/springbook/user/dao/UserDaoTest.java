package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.domain.User;

public class UserDaoTest {
	/**
	 * applicationContext는 모든 테스트케이스가 공유하도록 static변수로 지정
	 */
	private static ApplicationContext context;
	
	//Fixture는 instance변수로 정의한다
	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;
	
	@BeforeClass
	public static void beforeClass() {
		context = new GenericXmlApplicationContext("applicationContext.xml");
	}
	
	@Before
	public void setUp() {
		this.dao = context.getBean("userDao", UserDao.class);
		this.user1 = new User("honggd", "홍길동", "1234");
		this.user2 = new User("sinsa", "신사", "1234");
		this.user3 = new User("sejong", "세종", "1234");

	}
	
	@Test 
	public void andAndGet() throws SQLException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));
	}
	
	
	@Test
	public void count() throws SQLException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
				
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}
	
	
	/**
	 * 스프링창시자 Rod Johnson가 말하길, negative테스트를 만들어라.
	 * 
	 * org.spring.springframework.transaction-3.1.2.RELEASE.jar 프로젝트에 추가할 것.
	 *  
	 * @throws SQLException
	 */
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknown_id");
	}
	
	


}
