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
}
