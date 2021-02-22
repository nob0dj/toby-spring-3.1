package springbook.template.callback.calc4;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class Calculator4Test {
	Calculator4 calculator;
	String numFilepath;
	
	@Before 
	public void setUp() {
		this.calculator = new Calculator4();
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
	
	@Test 
	public void concatenateStrings() throws IOException {
		assertThat(calculator.concatenate(this.numFilepath), is("1234"));
	}

}

