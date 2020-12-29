package springbook.run;

import java.sql.SQLException;

import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoFactory;
import springbook.user.domain.User;

public class UserDaoTest {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		UserDao dao = new UserDaoFactory().userDao();

		User user = new User();
		user.setId("redship");
		user.setName("적기선");
		user.setPassword("1234");

		dao.add(user);

		System.out.println(user.getId() + " 등록 성공!");

		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());

		System.out.println(user2.getId() + " 조회성공!");
	}
}
