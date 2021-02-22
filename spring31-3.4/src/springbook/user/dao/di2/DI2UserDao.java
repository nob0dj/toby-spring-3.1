package springbook.user.dao.di2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.dao.JdbcContext;
import springbook.user.dao.StatementStrategy;
import springbook.user.domain.User;

/**
 * 
 * <h2>DI2</h2>
 * <pre>
 * DataSource ---di---> JdbcContext ---di---> UserDao
 * 
 * 
 * 위와 같은 구조로 di가 진행되지만, 클래스레벨에서 결합된 JdbcContext는 빈으로 등록하지 않는다. 
 * 대신, JdbcContext에서 필요한 DataSource(빈등록)는 UserDao(빈등록)에서 수동으로 DI한다.
 *  
 * </pre>
 *
 */
public class DI2UserDao {
	private DataSource dataSource;
	private JdbcContext jdbcContext;
		
	/**
	 * 수동 DI
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.jdbcContext = new JdbcContext();
		this.jdbcContext.setDataSource(dataSource);

		this.dataSource = dataSource;
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
