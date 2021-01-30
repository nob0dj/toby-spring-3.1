package springbook.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;

/**
 * <pre>
 * class level @DirtiesContext
 * 현재 테스트클래스의 ApplicationContext의 상태를 변경하므로, 
 * TestCase간 ApplicationContext를 공유하지 않고 매번 새로 만든다.
 * 
 * - TestCase마다 context 갱신 : classMode = ClassMode.AFTER_EACH_TEST_METHOD
 * - TestClass마다 context 갱신 :  classMode = ClassMode.AFTER_CLASS 	
 * 
 * </pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDaoTest1 {
	@Autowired
	ApplicationContext context;
	
	@Autowired
	private UserDao dao; 
	
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		
		//테스트DB라고 가정. ApplicationContext를 변경하므로 @DirtiesContext 설정
		SimpleDriverDataSource another = new SimpleDriverDataSource();
		another.setDriverClass(oracle.jdbc.OracleDriver.class);
		another.setUrl("jdbc:oracle:thin:@127.0.0.1:1521:xe");
		another.setUsername("toby");
		another.setPassword("toby");
		dao.setDataSource(another);
		
		System.out.println(context);//동일
		System.out.println(this);//테스트케이스별로 다름.
		System.out.println(dao);//동일
		
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

	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknown_id");
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
}
