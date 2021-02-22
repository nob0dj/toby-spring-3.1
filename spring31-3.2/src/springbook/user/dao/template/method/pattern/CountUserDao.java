package springbook.user.dao.template.method.pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountUserDao extends AbstractUserDao {

	@Override
	protected PreparedStatement makeStatement(Connection c) throws SQLException {
		return c.prepareStatement("select count(*) from users");
	}
	
	public int getCount() throws SQLException  {
		/* context 시작 : 변하지않는 부분 */
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = dataSource.getConnection();
			/* strategy 시작 : 변하는 부분 */
			ps = makeStatement(c);
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
