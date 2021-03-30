package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/testApplicationContext.xml")
public class UserDaoTest {
	@Autowired
	ApplicationContext context;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	UserDao dao;
	
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		this.user1 = new User("honggd", "홍길동", "1234", Level.BASIC, 1, 0);
		this.user2 = new User("sinsa", "신사", "1234", Level.SILVER, 55, 10);
		this.user3 = new User("sejong", "세종", "1234", Level.GOLD, 100, 40);
		
	}

	
	@Test 
	public void addAndGet() throws SQLException {		
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
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknown_id");
	}
	
	
	@Test
	public void getAll() {
		dao.deleteAll();
		
		List<User> users0 = dao.getAll();
		assertThat(users0.size(), is(0));
		
		dao.add(user1); // id: honggd
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2); // Id: sinsa
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1, users2.get(0));  
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3); // Id: sejong
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user1, users3.get(0));  //honggd
		checkSameUser(user3, users3.get(1));  //sejoin
		checkSameUser(user2, users3.get(2));  //sinsa
		
	}

	/**
	 * @Test에서 반복실행할 코드를 별도의 메소드로 추출해 두는 것은 항상 옳다.
	 * 
	 * @param user1
	 * @param user2
	 */
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
	}
	
	/**
	 * <pre>
	 * 1. SQLIntegrityConstraintViolationException(JDBC) 이 최초발생
	 * 2. DuplicateKeyException(Spring)로 전환
	 * 
	 * 계층구조 DataAccessException - DataIntegrityViolationException - DuplicateKeyException  
	 * 
	 * </pre>
	 */
	@Test
	(expected=DuplicateKeyException.class) // 주석해서 실제발생 예외의 계층구조를 파악할 수 있다.
	public void duplciateKey() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user1);
	}
	
	/**
	 * 실제 발생 예외와 전환된 예외 비교 테스트
	 */
	@Test
	public void sqlExceptionTranslate() {
		dao.deleteAll();
		
		try {
			dao.add(user1);
			dao.add(user1);
		}
		catch(DuplicateKeyException ex) {
			SQLException sqlEx = (SQLException)ex.getCause();
			/**
			 * SQLExceptionTranslator인터페이스의 구현클래스 SQLErrorCodeSQLExceptionTranslator를 사용
			 * 
			 * 어떤 DB를 사용하는 지 알기 위해서 Translator객체는 dataSource를 인자로 한다.
			 */
			SQLExceptionTranslator exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);			
			DataAccessException transEx = exceptionTranslator.translate(null, null, sqlEx);
			assertThat(transEx, is(DuplicateKeyException.class));
		}
	}
	
	@Test
	public void update() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user2);
		
		user1.setName("전봉준");
		user1.setPassword("12345678");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		
		dao.update(user1);
		
		User user1update = dao.get(user1.getId());
		checkSameUser(user1, user1update);
		User user2same = dao.get(user2.getId());
		checkSameUser(user2, user2same);
	}
}
