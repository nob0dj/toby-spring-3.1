package springbook.user.domain;

public enum Level {
	BASIC(1), SILVER(2), GOLD(3);

	private final int value;
	
	
	/**
	 * Enum생성자
	 * 
	 * 접근제한자는 private만 사용할 수 있다.
	 * @param value
	 */
	Level(int value) {
		this.value = value;
	}

	
	public int intValue() {
		return value;
	}
	
	/**
	 * 정수형을 Level타입으로 변환
	 * @param value
	 * @return
	 */
	public static Level valueOf(int value) {
		switch(value) {
		case 1: return BASIC;
		case 2: return SILVER;
		case 3: return GOLD;
		default: throw new AssertionError("Unknown value: " + value);
		}
	}
}

