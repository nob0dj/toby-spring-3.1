package springbook.user.domain;

public enum Level {
	BASIC(1), SILVER(2), GOLD(3);

	private final int value;
	
	
	/**
	 * Enum생성자
	 * 
	 * 접근제한자는 private만 사용할 수 있다.
	 * new 연산자로 호출할 수 없다.
	 * 
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


	/**
	 * 다음 value값을 가진 enum객체를 리턴한다.
	 * GOLD 레벨은 최대값이므로 null을 리턴.
	 * 
	 * 객체지향적으로 데이터를 가진 주체 Level 에게 작업 처리
	 * 오브젝트에게 데이터가 아닌 작업을 요청한다.
	 * @return
	 */
	public Level nextLevel() {
		
//		return this != GOLD ? valueOf(this.value + 1) : null;
		return this != getMaxLevel() ? valueOf(this.value + 1) : null;
	}


	/**
	 * 최대value를 가진 Level 가져오기
	 * 
	 * Level.values()
	 * eum의 요소들을 순서대로 enum타입의 배열로 리턴. (ENUM$VALUES)의 카피임으로 자주 호출하지 않길) 
	 * @return
	 */
	public static Level getMaxLevel() {
		Level[] levels = values();
		Level maxLevel = Level.BASIC;
		for(Level level : levels) 
			//enum타입끼리 비교연산 불가
			if(maxLevel.intValue() < level.intValue())
				maxLevel = level;
		return maxLevel;
	}
}

