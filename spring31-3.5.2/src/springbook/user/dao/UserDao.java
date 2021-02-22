package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.domain.User;

public class UserDao {
	private DataSource dataSource;
	private JdbcContext jdbcContext;
		
	public void setDataSource(DataSource dataSource) {
		this.jdbcContext = new JdbcContext();
		this.jdbcContext.setDataSource(dataSource);

		this.dataSource = dataSource;
	}
	
	/**
	 * 변하지 않는 부분을 template쪽으로 이동시켰다. 
	 * template(context)에서 PrepareStatement객체 생성까지 처리하므로, 간단히 sql문만 전달한다.
	 * 
	 * jdbcContext.executeSql(String) -> jdbcContext.workWithStatementStrategy(StatementStrategy)
	 * 
	 * @throws SQLException
	 */
	public void deleteAll() throws SQLException {
		this.jdbcContext.executeSql("delete from users");
	}
	
	public void add(final User user) throws SQLException {
		this.jdbcContext.executeSql("insert into users(id, name, password) values(?,?,?)", user.getId(), user.getName(), user.getPassword());
	}


	public User get(String id) throws SQLException {
		Connection c = this.dataSource.getConnection();
		PreparedStatement ps = c
				.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();

		User user = null;
		if (rs.next()) {
			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}

		rs.close();
		ps.close();
		c.close();
		
		if (user == null) throw new EmptyResultDataAccessException(1);

		return user;
	}


	public int getCount() throws SQLException  {
		Connection c = dataSource.getConnection();
	
		PreparedStatement ps = c.prepareStatement("select count(*) from users");

		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);

		rs.close();
		ps.close();
		c.close();
	
		return count;
	}
}
