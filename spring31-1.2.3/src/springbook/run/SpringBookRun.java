package springbook.run;

import java.sql.SQLException;

import springbook.user.dao.NUserDao;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

public class SpringBookRun {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		UserDao dao = new NUserDao();

		User user = new User();
		user.setId("blackship");
		user.setName("흑기선");
		user.setPassword("1234");

		dao.add(user);

		System.out.println(user.getId() + " 등록 성공!");

		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());

		System.out.println(user2.getId() + " 조회성공!");
	}

}
