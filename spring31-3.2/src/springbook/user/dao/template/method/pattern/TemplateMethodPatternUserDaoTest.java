package springbook.user.dao.template.method.pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/testTemplateMethodPatternApplicationContext.xml")
public class TemplateMethodPatternUserDaoTest {
	@Autowired
	ApplicationContext context;
	
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		this.user1 = new User("honggd", "홍길동", "1234");
		this.user2 = new User("sinsa", "신사", "1234");
		this.user3 = new User("sejong", "세종", "1234");
	}
	
	
	@Test
	public void count() throws SQLException {
		//매번 실행쿼리별 객체를 가져와야함.
		DeleteAllUserDao deleteAllUserDao = context.getBean("deleteAllUserDao", DeleteAllUserDao.class);
		deleteAllUserDao.deleteAll();
		
		CountUserDao countUserDao = context.getBean("countUserDao", CountUserDao.class);
		assertThat(countUserDao.getCount(), is(0));
				
		AddUserDao addUserDao = context.getBean("addUserDao", AddUserDao.class);
		addUserDao.add(user1);
		assertThat(countUserDao.getCount(), is(1));
		
		addUserDao.add(user2);
		assertThat(countUserDao.getCount(), is(2));
		
		addUserDao.add(user3);
		assertThat(countUserDao.getCount(), is(3));
	}
}
