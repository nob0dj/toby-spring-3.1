package springbook.template.callback.calc4;

import java.io.IOException;

/**
 * 
 * 지역변수 선언을 위해 추상클래스로 선언
 * 
 * 초기값을 익명객체 생성시 인자로 전달해야 한다.
 *
 * @param <T>
 */
public abstract class GenericsLineCallBack<T> {
	protected T result;
	
	public GenericsLineCallBack(T initVal) {
		this.result = initVal;
	}

	public abstract void callBackProcess(String line) throws IOException;

	public T getResult() {
		return result;
	}
}
