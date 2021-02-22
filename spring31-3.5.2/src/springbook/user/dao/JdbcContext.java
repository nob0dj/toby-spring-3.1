package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 
 * <pre>
 * template과 callback의 결합
 * workWithStatementStrategy과 executeSql(callback생성)을 한 클래스에서 처리해서 userDao에서 좀더 간결하게 호출할 수 있도록 한다.
 * </pre>
 * 
 * <img src="https://d.pr/i/jslTaC+" alt="" width="50%"/>
 */
public class JdbcContext {
	DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * template(context) : 변하지 않는 부분
	 * 
	 * @param stmt
	 * @throws SQLException
	 */
	public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;

		try {
			c = dataSource.getConnection();

			ps = stmt.makePreparedStatement(c);
		
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
			if (c != null) { try {c.close(); } catch (SQLException e) {} }
		}
	}
	
	
	/**
	 * client는 callback객체를 인자로 template를 호출한다.
	 * 
	 * template(callback);
	 * 
	 * @param query
	 * @throws SQLException
	 */
	public void executeSql(final String query) throws SQLException {
		workWithStatementStrategy(
			new StatementStrategy() {
				public PreparedStatement makePreparedStatement(Connection c)
						throws SQLException {
					return c.prepareStatement(query);
				}
			}
		);
	}	
	
	/**
	 * query에 대입할 값이 있는 경우
	 * 
	 * @param query
	 * @throws SQLException
	 */
	public void executeSql(final String query, final Object... params) throws SQLException {
		workWithStatementStrategy(
			new StatementStrategy() {
				public PreparedStatement makePreparedStatement(Connection c)
						throws SQLException {
					PreparedStatement ps = c.prepareStatement(query);
					for(int i = 0; i < params.length; i++) {
						//System.out.println(params[i].getClass());//class java.lang.String
						ps.setObject(i + 1, params[i]);
					}
					return ps;
				}
			}
		);
	}	
	
}

