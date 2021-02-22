package springbook.user.dao.strategy.pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.domain.User;

/**
 * 
 * 전략패턴 적용 했으나
 * cotext에 전략클래스가 구체화되어 실패!
 *
 */
public class UserDao1 {
	private DataSource dataSource;
		
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void deleteAll() throws SQLException {
		/* context 시작 : 변하지않는 부분 */
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = dataSource.getConnection();
			
			/* strategy 시작 : 변하는 부분 */
			//context에서 전략클래스가 고정되어 OCP원칙 위배 : 실패!
			StatementStrategy strategy = new DeleteAllStatement();
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

}
