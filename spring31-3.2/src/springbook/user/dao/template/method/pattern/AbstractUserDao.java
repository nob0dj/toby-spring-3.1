package springbook.user.dao.template.method.pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public abstract class AbstractUserDao<T> {
	
	protected DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	abstract protected PreparedStatement makeStatement(Connection c) throws SQLException;
	
	public T executeDMLQuery() {
		T t = null;
		return t;
	}
}
