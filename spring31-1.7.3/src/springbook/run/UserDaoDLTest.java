package springbook.run;

import java.sql.SQLException;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;

/**
 * 의존관계 검색 테스트
 *
 */
public class UserDaoDLTest {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
	
		UserDao dao = new UserDao();

		User user = new User();
		user.setId("blueship");
		user.setName("청기선");
		user.setPassword("married");

		dao.add(user);
			
		System.out.println(user.getId() + " 등록성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
			
		System.out.println(user2.getId() + " 조회성공");
	}
}
