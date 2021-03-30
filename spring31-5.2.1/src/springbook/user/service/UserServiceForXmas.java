package springbook.user.service;

import springbook.user.domain.User;

/**
 * 연말 한시적으로 완화된 User Level정책을 적용한 UserService
 * 빈으로 등록후 UserServiceTest에서 경계값을 달리해서 테스트해볼것.
 *
 */
public class UserServiceForXmas implements UserLevelUpgradePolicy {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 35;
	public static final int MIN_RECCOMEND_FOR_GOLD = 20;

	@Override
	public boolean canUpgradeLevel(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void upgradeLevel(User user) {
		// TODO Auto-generated method stub

	}

}
