package springbook.learning.test.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.either;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("junit.xml")
public class JUnitTest2 {
	@Autowired 
	ApplicationContext context;
	
	static Set<JUnitTest2> testSet = new HashSet<JUnitTest2>();
	static ApplicationContext contextObject = null;
	
	@Test public void test1() {
		//hasItem 해당 요소를 가지고 있는지 여부 리턴
		assertThat(testSet, not(hasItem(this)));
		assertThat(testSet.add(this), is(true));
		
		//<Boolean> void org.junit.Assert.assertThat(Boolean actual, Matcher<Boolean> matcher)
		assertThat(contextObject == null || contextObject == this.context, is(true));
		contextObject = this.context;
	}
	
	@Test public void test2() {
		assertThat(testSet, not(hasItem(this)));
		testSet.add(this);
		
		//void org.junit.Assert.assertTrue(boolean condition)
		assertTrue(contextObject == null || contextObject == this.context);
		contextObject = this.context;
	}
	
	@Test public void test3() {
		assertThat(testSet, not(hasItem(this)));
		testSet.add(this);
		
		//either().or() 두 Matcher중 하나만 참이면 테스트성공
		assertThat(contextObject, either(is(nullValue())).or(is(contextObject)));
		contextObject = this.context;
	}
	
	
}

