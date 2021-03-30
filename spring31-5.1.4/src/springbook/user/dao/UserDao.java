package springbook.user.dao;

import java.util.List;

import springbook.user.domain.User;

public interface UserDao {

	int add(User user);

	User get(String id);

	List<User> getAll();

	void deleteAll();

	int getCount();

	int update(User user);

}