package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 
 * client로부터 UserDao에 전달한 ConnectionMaker객체 선택하는 관심사를 분리하였다.
 *
 */
public class UserDaoFactory {
	
	public UserDao userDao() {
		UserDao dao = new UserDao(connectionMaker());
		return dao;
	}

	public ConnectionMaker connectionMaker() {
		ConnectionMaker connectionMaker = new DConnectionMaker();
		return connectionMaker;
	}

}
