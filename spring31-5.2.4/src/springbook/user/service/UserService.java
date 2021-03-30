package springbook.user.service;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService implements UserLevelUpgradePolicy{
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	/**
	 * PlatformTransactionManager란 긴 이름이 붙은 이유는 JTA에 있는 TransactionManager인터페이스와 혼동을 방지하기 위함이다.
	 * 너무 긴 이름이라 관레적으로 짧게 줄여 setter를 설정한다.
	 */
	private PlatformTransactionManager txManager;
	
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	/**
	 * 서비스 추상화 적용
	 * 
	 * 특정 트랜잭션 방법에 의존적이지 않고 독립적으로 작동해야한다.
	 * JDBC, JTA, Hibernate, JPA, JDO, JMS의 공통적으로 PlatformTransactionManager를 구현하고 있다.
	 * - JDBC : DataSourceTxManager - Connection
	 * - JTA : DataSourceTxManager - UserTransaction
	 * - Hibernate : HibernateTxManager - Transaction
	 * 
	 * 
	 * <img src="https://d.pr/i/Lqn88C+" alt="" width="100%"/>
	 * 
	 */
	public void upgradeLevels() throws Exception {
//		PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource); // JDBC
//		PlatformTransactionManager txManager = new JTATransactionManager(dataSource); // JTA
		
		//트랜잭션 경계시작 : DB Connection객체 가져오는 작업역시 자동으로 처리된다.
		//DefaultTransactionDefinition : 트랜잭션 속성 정보
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		
				
		try {
			List<User> users = userDao.getAll();  
			for(User user : users) {  
				if (canUpgradeLevel(user)) {  
					upgradeLevel(user);  
				}
			}
			txManager.commit(status);
		} catch (Exception e) {
			txManager.rollback(status);
			throw e;
		} 
	}
	
	
	
	public boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel(); 
		switch(currentLevel) {                                   
		case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER); 
		case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
		case GOLD: return false;
		default: throw new IllegalArgumentException("Unknown Level: " + currentLevel); 
		}
	}

	public void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}
	
	public void add(User user) {
		if (user.getLevel() == null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}

}

