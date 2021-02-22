package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.domain.User;

/**
 * 
 * client에서 context사용시 내부익명 클래스를 이용해 전략객체 전달.  
 *
 */
public class UserDao {
	private DataSource dataSource;
		
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void jdbcContextWithStatementStrategy(StatementStrategy strategy) throws SQLException {
		/* context 시작 : 변하지않는 부분 */
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = dataSource.getConnection();
			
			/* strategy 시작 : 변하는 부분 */
			ps = strategy.makePreparedStatement(c);
			
			ps.executeUpdate();
			/* strategy 끝*/
			
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if(ps != null) 
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(c != null) 
					c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/* context 끝 */
	}
	
	/**
	 * client : 전략객체와 함께 context호출
	 * 
	 * @throws SQLException
	 */
	public void deleteAll() throws SQLException {
		jdbcContextWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				return c.prepareStatement("delete from users");
			}
		});
	}	
	
	/**
	 * 익명 내부클래스안에서 외부의 값을 참조하는 경우에는 final만 가능하다.
	 * 
	 * @param user
	 * @throws SQLException
	 */
	public void add(final User user) throws SQLException {
		jdbcContextWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				return ps;
			}
		});
	}

	
	/**
	 * 
	 * @throws SQLException
	 */
	public User get(String id) throws SQLException {
		/* context 시작 : 변하지않는 부분 */
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = dataSource.getConnection();
			
			/* strategy 시작 : 변하는 부분 */
			ps = c.prepareStatement("select * from users where id = ?");
			ps.setString(1, id);
			rs = ps.executeQuery();
			User user = null;
			if (rs.next()) {
				user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
			}
			if (user == null) throw new EmptyResultDataAccessException(1);

			return user;
			/* strategy 끝*/
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if(rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(ps != null) 
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(c != null) 
					c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/* context 끝 */
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getCount() throws SQLException  {
		/* context 시작 : 변하지않는 부분 */
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = dataSource.getConnection();
			/* strategy 시작 : 변하는 부분 */
			ps = c.prepareStatement("select count(*) from users");
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
			/* strategy 끝*/
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if(rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(ps != null) 
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(c != null) 
					c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/* context 끝 */
	}

}
