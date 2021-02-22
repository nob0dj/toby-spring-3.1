package springbook.user.dao.strategy.pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import springbook.user.domain.User;

public class AddUserStatement implements StatementStrategy {

	private User user;
	
	public AddUserStatement(User user) {
		this.user = user;
	}

	@Override
	public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		return ps;
	}

}
