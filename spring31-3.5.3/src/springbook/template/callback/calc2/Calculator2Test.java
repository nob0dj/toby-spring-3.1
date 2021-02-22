package springbook.template.callback.calc2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class Calculator2Test {
	Calculator2 calculator;
	String numFilepath;
	
	@Before 
	public void setUp() {
		this.calculator = new Calculator2();
		this.numFilepath = getClass().getResource("/numbers.txt").getPath();
		// /D:/Dropbox/Coding/Workspaces/toby_workspace/spring31-3.5.3/bin/numbers.txt
	}
	
	@Test 
	public void sumOfNumbers() throws IOException {
		assertThat(calculator.calcSum(this.numFilepath), is(10));
	}
	
	@Test 
	public void multiplyOfNumbers() throws IOException {
		assertThat(calculator.calcMultiply(this.numFilepath), is(24));
	}
	
	/**
	 * BufferedReaderCallBack을 사용하면 리턴타입이 다른 기능은 사용할 수 없다.
	 * 
	 * @throws IOException
	 */
	@Ignore
	@Test 
	public void concatenateStrings() throws IOException {
		//assertThat(calculator.concatenate(this.numFilepath), is("1234"));
	}

}

