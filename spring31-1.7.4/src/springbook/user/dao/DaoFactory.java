package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
	
	/**
	 * 메소드명이 빈의 이름이 된다.
	 * 
	 * @return
	 */
	@Bean
	public UserDao userDao() {
		UserDao dao = new UserDao(connectionMaker());
		return dao;
	}

	/**
	 * 
	 * @return CountingConnectionMaker
	 */
	@Bean
	public ConnectionMaker connectionMaker() {
		return new CountingConnectionMaker(realConnectionMaker());
	}
	
	/**
	 * 
	 * @return DConnectionMaker
	 */
	@Bean
	public ConnectionMaker realConnectionMaker() {
		return new DConnectionMaker();
	}
}
