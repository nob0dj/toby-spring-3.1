package springbook.user.dao.strategy.pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CountUserAllStatement implements StatementStrategy {

	@Override
	public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
		return c.prepareStatement("select count(*) from users");
	}

}
