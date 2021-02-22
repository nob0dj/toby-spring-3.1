package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.dao.strategy.pattern.DeleteAllStatement;
import springbook.user.dao.strategy.pattern.StatementStrategy;
import springbook.user.domain.User;

/**
 * 예외처리버젼
 */
public class UserDao {
	private DataSource dataSource;
		
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * DML 예외처리
	 * @throws SQLException
	 */
	public void add(User user) throws SQLException {
		/* context 시작 : 변하지않는 부분 */
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = dataSource.getConnection();
			
			/* strategy 시작 : 변하는 부분 */
			ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
			ps.setString(1, user.getId());
			ps.setString(2, user.getName());
			ps.setString(3, user.getPassword());
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
	 * DQL 예외처리
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
	 * DML 예외처리
	 * @throws SQLException
	 */
	public void deleteAll() throws SQLException {
		/* context 시작 : 변하지않는 부분 */
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = dataSource.getConnection();
			
			/* strategy 시작 : 변하는 부분 */
			ps = c.prepareStatement("delete from users");
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
	 * DQL 예외처리
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
