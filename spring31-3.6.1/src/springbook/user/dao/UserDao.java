package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.User;

/**
 * jdbcTemplate은 SQLException을 던지지 않는다.
 */
public class UserDao {
	private JdbcTemplate jdbcTemplate;
		
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void deleteAll() {
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				return con.prepareStatement("delete from users");
			}
		});
		
		// int org.springframework.jdbc.core.JdbcTemplate.update(String sql) throws DataAccessException
//		this.jdbcTemplate.update("delete from users");
	}
	

	public void add(final User user) {
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement("insert into users(id, name, password) values(?,?,?)");
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				return ps;
			}
		});
		
//		this.jdbcTemplate.update(
//				"insert into users(id, name, password) values(?,?,?)",
//				user.getId(), user.getName(), user.getPassword()
//			);
	}

	
	/**
	 * <pre>
	 * RowMapper : template으로 부터 행단위로 ResultSet을 전달받아 데이터를 mapping한 객체를 리턴. 
	 * 			       여러번 호출될 수 있다.
	 * 
	 * queryForObject에는 ResultSetExtractor를 인자로 받지 않는다. queryForObject에는 RowMapper를 사용하자.
	 * queryForObject에서 리턴된 행이 0행이면 EmptyResultDataAccessException를 던진다.
	 * 
	 * </pre>
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public User get(String id) {
		return this.jdbcTemplate.queryForObject(
				"select * from users where id = ?", 
				new Object[] {id}, 
				new RowMapper<User>() {
					
					@Override
					public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//						System.out.println("rowNum = " + rowNum); // 0-basedrowIndex
						User user = new User();
						user.setId(rs.getString("id"));
						user.setName(rs.getString("name"));
						user.setPassword(rs.getString("password"));
						
						return user;
					}
				});
	}



	/**
	 * <pre>
	 * 
	 * query
	 * template호출시 두개의 callback을 전달
	 * 
	 * <T> T org.springframework.jdbc.core.JdbcTemplate.query(
	 * 		PreparedStatementCreator psc, 
	 * 		ResultSetExtractor<Integer> rse) 
	 * 			throws DataAccessException
	 * 
	 * ResultSetExtractor : template으로 부터 ResultSet을 한번만 전달받아 필요데이터를 추출해서 최종 결과만 리턴. 
	 * 
	 * queryForInt
	 * 
	 * </pre>
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getCount() {
		
		// T org.springframework.jdbc.core.JdbcTemplate.query( ResultSetExtractor<T> rse) throws DataAccessException
		return this.jdbcTemplate.query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				return con.prepareStatement("select count(*) from users") ;
			}
			
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();//데이터가 있는 1행에 접근
				return rs.getInt(1);//첫번째 컬러
			}
			
		});
		
		
//		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query(
				"select * from users order by id", 
				new RowMapper<User>() {

					@Override
					public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//						System.out.println("rowNum = " + rowNum); // 0-basedrowIndex
						User user = new User();
						user.setId(rs.getString("id"));
						user.setName(rs.getString("name"));
						user.setPassword(rs.getString("password"));
						return user;
					}
					
				});
	}
}
