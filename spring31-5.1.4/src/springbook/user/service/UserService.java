package springbook.user.service;

import java.util.List;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void add(User user) {
		if (user.getLevel() == null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}

	/**
	 * User Level 업그레이드를 위한 batch 작업
	 * 
	 * 
	 * Refactoring시 고려사항
	 * 1. 코드에 중복된 부분은 없는가
	 * 2. 코드를 이해하는 데 불편함은 없는가
	 * 3. 코드는 적절한 위치에 있는가
	 * 4. 앞으로 변경이 일어난다면 어떤 것이 있을수 있고, 그 변화에 쉽게 대응할 수 있는가
	 * 
	 */
	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		boolean upgradable = false;
		for(User user : users) {
			if(user.getLevel() == Level.BASIC && user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER) {
				user.setLevel(Level.SILVER);
				upgradable = true;
			}
			else if(user.getLevel() == Level.SILVER && user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD) {
				user.setLevel(Level.GOLD);
				upgradable = true;
			}
			else if(user.getLevel() == Level.GOLD) {
				
			}
			
			if(upgradable)
				userDao.update(user);
		}
		
	}
	

//	public void upgradeLevels() {
//		List<User> users = userDao.getAll();  
//		for(User user : users) {  
//			if (canUpgradeLevel(user)) {  
//				upgradeLevel(user);  
//			}
//		}
//	}
//	private boolean canUpgradeLevel(User user) {
//		Level currentLevel = user.getLevel(); 
//		switch(currentLevel) {                                   
//		case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER); 
//		case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
//		case GOLD: return false;
//		default: throw new IllegalArgumentException("Unknown Level: " + currentLevel); 
//		}
//	}
//
//	private void upgradeLevel(User user) {
//		user.upgradeLevel();
//		userDao.update(user);
//	}

}

