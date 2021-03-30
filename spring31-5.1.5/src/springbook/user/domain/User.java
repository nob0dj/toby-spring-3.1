package springbook.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	String id;
	String name;
	String password;
	
	Level level;
	int login;			// 로그인 횟수
	int recommend;		// 추천 횟수
	
	/**
	 * 업그레이드할 다음 레벨을 현재 레벨속성에 대입한다.
	 * GOLD회원이 메소드를 호출시 예외를 발생한다.
	 * 또한 level컬럼 외에 추가적으로 변경할 사항이 있을 경우도 대응이 수월하다.
	 * 
	 * 객체지향적으로 데이터를 가진 주체 User 에게 작업 처리
	 * 오브젝트에게 데이터가 아닌 작업을 요청한다.
	 */
	public void upgradeLevel() {
		Level nextLevel = this.level.nextLevel();	
		if (nextLevel == null) { 								
			throw new IllegalStateException(this.level + "은 업그레이드가 불가합니다.");
		}
		else {
			this.level = nextLevel;
		}	
	}
}
