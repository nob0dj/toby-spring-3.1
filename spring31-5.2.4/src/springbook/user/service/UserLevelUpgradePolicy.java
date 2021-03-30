package springbook.user.service;

import springbook.user.domain.User;

/**
 * 
 * User Level을 upgrade하는 정책이 일시적으로 변경한다면?
 * 소스코드를 직접 수정하는 방법이 아닌 interface를 구현후 DI하는 객체를 변경하는 식으로 대응 가능하다.
 *
 */
public interface UserLevelUpgradePolicy {
	
	boolean canUpgradeLevel(User user);
	void upgradeLevel(User user);
}
