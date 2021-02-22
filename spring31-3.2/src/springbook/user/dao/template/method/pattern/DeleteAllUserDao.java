package springbook.user.dao.template.method.pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllUserDao extends AbstractUserDao {

	@Override
	protected PreparedStatement makeStatement(Connection c) throws SQLException {
		return c.prepareStatement("delete from users");
	}
	
	public void deleteAll() throws SQLException {
		/* context 시작 : 변하지않는 부분 */
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = dataSource.getConnection();
			
			/* strategy 시작 : 변하는 부분 */
			ps = makeStatement(c);
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
