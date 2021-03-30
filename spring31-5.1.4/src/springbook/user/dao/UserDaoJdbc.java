package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserDaoJdbc implements UserDao {
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<User> userMapper = 
		new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setLevel(Level.valueOf(rs.getInt("user_level")));
				user.setLogin(rs.getInt("login"));
				user.setRecommend(rs.getInt("recommend"));
				return user;
			}
		};

	public int add(User user) {
		return this.jdbcTemplate.update(
//				"insert into users(id, name, password, level, login, recommend) values(?,?,?,?,?,?)", 
				"insert into users values(?,?,?,?,?,?)", //level컬럼이 오라클 예약어라서 컬럼명으로 명시할 수 없다 (컬럼명은 "LEVEL" alias로 생성했어도 작동 하지 않음) 
				user.getId(), 
				user.getName(), 
				user.getPassword(), 
				user.getLevel().intValue(), 
				user.getLogin(), 
				user.getRecommend()
			);
	}

	public User get(String id) {
		return this.jdbcTemplate.queryForObject("select * from users where id = ?",
				new Object[] {id}, this.userMapper);
	} 

	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount() {
		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id",this.userMapper);
	}
	
	/**
	 * oracle에서 level은 예약어로 사용할 수 없어 user_level로 변결해서 진행한다.
	 */
	public int update(User user) {
		return this.jdbcTemplate.update(
				"update users set name = ?, password = ?, user_level = ?, login = ?, recommend = ? where id = ? ", 
				user.getName(), 
				user.getPassword(), 
				user.getLevel().intValue(), 
				user.getLogin(), 
				user.getRecommend(),
				user.getId()
			);
		
	}
}
