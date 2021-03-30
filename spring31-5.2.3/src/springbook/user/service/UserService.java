package springbook.user.service;

import java.sql.Connection;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
	
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * transaction 동기화적용하기 
	 */
	public void upgradeLevels() throws Exception {
		//트랜잭션 경계시작 : 동기화작업 초기화
		TransactionSynchronizationManager.initSynchronization();
		//dataSource를 전달하고, 공유할 Connection를 가져오기
		Connection c = DataSourceUtils.getConnection(dataSource); 
		c.setAutoCommit(false); // 필수
		
		try {
			List<User> users = userDao.getAll();  
			for(User user : users) {  
				if (canUpgradeLevel(user)) {  
					upgradeLevel(user);  
				}
			}
			c.commit();
		} catch (Exception e) {
			c.rollback();
			throw e;
		} finally {
			//connection객체 닫기
			DataSourceUtils.releaseConnection(c, dataSource);
			//동기화 작업 마무리
			TransactionSynchronizationManager.unbindResource(this.dataSource);  
			TransactionSynchronizationManager.clearSynchronization();  
		}
	}
	
	/**
	 * 만약 global transaction처리를 해야 한다면...
	 * 
	 * JNDI + JTA Java Tranasaction Application 
	 * 
	 * @throws Exception
	 */
//	public void upgradeLevelsForGlobalTransaction() throws Exception {
//		//트랜잭션 경계시작 : 동기화작업 초기화
//		InitialContext ctx = new InitialContext();
//		UserTransaction tx = (UserTransaction)ctx.lookup(USER_TX_JNDI_NAME);
//		tx.begin();
//		DataSource dataSource = (DataSource)ctx.lookup("java:comp/env/jdbc/myoracle");//자바환경설정+server.xml의 추가코드 name
//		Connection conn = dataSource.getConnection();
//		conn.setAutoCommit(false);
//		
//		try {
//			List<User> users = userDao.getAll();  
//			for(User user : users) {  
//				if (canUpgradeLevel(user)) {  
//					upgradeLevel(user);  
//				}
//			}
//			tx.commit();
//		} catch (Exception e) {
//			tx.rollback();
//			throw e;
//		} finally {
//			conn.close();
//		}
//	}
	
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

