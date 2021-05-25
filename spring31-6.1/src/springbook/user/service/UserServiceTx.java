package springbook.user.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.domain.User;

public class UserServiceTx implements UserService {
	UserService userService;
	PlatformTransactionManager txManager;

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void add(User user) {
		this.userService.add(user);
	}

	public void upgradeLevels() {
		TransactionStatus status = 
				this.txManager.getTransaction(new DefaultTransactionDefinition());
		try {

			userService.upgradeLevels();

			this.txManager.commit(status);
		} catch (RuntimeException e) {
			this.txManager.rollback(status);
			throw e;
		}
	}
}
