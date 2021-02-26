package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import springbook.user.domain.User;

public class UserDaoTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		//1. 자바코드 설정 방식 @Configuration @Bean
//		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		//2. xml 설정 방식
		//GenericXmlApplicationContext에서는 절대주소, 상대주소 모두 classpath root directory 기준으로 검색한다.
//		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

		//ClassPathXmlApplicationContext는 지정한 클래스객체 위치로부터 상대적으로 xml파일을 조회한다.
		ApplicationContext context = new ClassPathXmlApplicationContext("userDaoContext.xml", UserDao.class);
		
		
		UserDao dao = context.getBean("userDao", UserDao.class);

		User user = new User();
		user.setId("grayship");
		user.setName("회기선");
		user.setPassword("married");
		
		dao.add(user);
		
		System.out.println(user.getId() + " 등록성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		System.out.println(user2.getId() + " 조회성공");
	}
}
