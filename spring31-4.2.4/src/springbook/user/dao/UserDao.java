package springbook.user.dao;

import java.util.List;

import springbook.user.domain.User;

/**
 * DataAccess기술에 독립적인 dao interface
 * - JDBC
 * - JdbcTemplate
 * - JPA
 * - JDO
 * - Hibernate
 * - xBatis
 * 
 *  dataSource property에 대한 setter/getter등은 Dao 구현방식에 따라 달라질수 있으므로 interface에 등록하지 않는다.
 *
 */
public interface UserDao {

	void add(User user);

	User get(String id);

	List<User> getAll();

	void deleteAll();

	int getCount();

}