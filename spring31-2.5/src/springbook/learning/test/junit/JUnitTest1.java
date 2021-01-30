package springbook.learning.test.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class JUnitTest1 {
	
	static JUnitTest1 obj;
	
	/**
	 * TestCase객체는 매 @Test 메소드마다 새로 생성된다.
	 */
	@Test
	public void test1() {
		System.out.println(this); //springbook.learning.test.junit.JUnitTest1@192b07fd 
		assertThat(this, is(not(sameInstance(obj))));
		obj = this;
	}
	
	@Test
	public void test2() {
		System.out.println(this); //springbook.learning.test.junit.JUnitTest1@52a86356
		assertThat(this, is(not(sameInstance(obj))));
		obj = this;
	}

}
