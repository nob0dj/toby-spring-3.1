package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.domain.User;

public class UserDaoTest {
	
	@Test 
	public void andAndGet() throws SQLException {
		//1.xml방식
//		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		//2.annotation방식
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		UserDao dao = context.getBean("userDao", UserDao.class);

		User user = new User();
		user.setId("honggd");
		user.setName("홍길동");
		user.setPassword("1234");

		dao.add(user);
			
		User user2 = dao.get(user.getId());
		
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
	}
	
	/**
	 * JUnit 테스트를 Java Application방식으로 실행하려면, JUnitCore.main 메소드에 테스트 클래스명(String)을 전달한다.
	 * @param args
	 */
	public static void main(String[] args) {
		JUnitCore.main("springbook.run.UserDaoTest");
	}
}
