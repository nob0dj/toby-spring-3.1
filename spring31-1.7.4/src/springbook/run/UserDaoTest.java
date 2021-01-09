package springbook.run;

import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.dao.ConnectionMaker;
import springbook.user.dao.CountingConnectionMaker;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

public class UserDaoTest {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
	
		//@Configuration 클래스 지정
		//@Bean 메소드의 리턴객체를 빈으로 관리하는 ApplicationContext를 구성한다. 
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		//getBean의 두번째 인자로 class를 전달하면, 해당타입의 빈 객체를 전달한다.(형변환 불필요)
		UserDao dao = context.getBean("userDao", UserDao.class);

		for(int i = 0; i < 10; i++) {
			
			User user = new User();
			user.setId("blueship" + i);
			user.setName("청기선");
			user.setPassword("married");
			
			dao.add(user);
			
			System.out.println(user.getId() + " 등록성공");
			
			User user2 = dao.get(user.getId());
			System.out.println(user2.getName());
			System.out.println(user2.getPassword());
			System.out.println(user2.getId() + " 조회성공");
		}
		
		//@Bean 메소드의 리턴타입이 아닌 실제 생성타입으로 참조할 수 있다.
		CountingConnectionMaker connectionMaker = context.getBean("connectionMaker", CountingConnectionMaker.class);
		System.out.println("총 연결 횟수 : " + connectionMaker.getCount());
			
	}
}
