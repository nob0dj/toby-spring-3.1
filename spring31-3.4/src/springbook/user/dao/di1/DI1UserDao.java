package springbook.user.dao.di1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.dao.JdbcContext;
import springbook.user.dao.StatementStrategy;
import springbook.user.domain.User;


/**
 * 
 * <h2>DI1</h2>
 * <pre>
 * DataSource ---di---> JdbcContext ---di---> UserDao
 * 
 * 
 * 위와 같은 구조로 di가 진행되며, 모두 빈으로 등록해둔다.
 * 단, UserDao와 JdbcContext는 클래스레벨에서 결합되므로, 전략패턴이라고 볼수는 없다. 
 *  
 * </pre>
 *
 */
public class DI1UserDao {
	private JdbcContext jdbcContext;
		
	public void setJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}

	public void add(final User user) throws SQLException {
		this.jdbcContext.workWithStatementStrategy(
				new StatementStrategy() {			
					public PreparedStatement makePreparedStatement(Connection c)
					throws SQLException {
						PreparedStatement ps = 
							c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
						ps.setString(1, user.getId());
						ps.setString(2, user.getName());
						ps.setString(3, user.getPassword());
						
						return ps;
					}
				}
		);
	}


	public User get(String id) throws SQLException {
		Connection c = jdbcContext.getDataSource().getConnection();
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

	public void deleteAll() throws SQLException {
		this.jdbcContext.workWithStatementStrategy(
			new StatementStrategy() {
				public PreparedStatement makePreparedStatement(Connection c)
						throws SQLException {
					return c.prepareStatement("delete from users");
				}
			}
		);
	}

	public int getCount() throws SQLException  {
		Connection c = jdbcContext.getDataSource().getConnection();
	
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
