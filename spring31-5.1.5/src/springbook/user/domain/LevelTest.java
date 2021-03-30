package springbook.user.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class LevelTest {

	@Test
	public void test() {
		assertThat(Level.BASIC.nextLevel(), is(Level.SILVER));
		assertThat(Level.SILVER.nextLevel(), is(Level.GOLD));
		assertThat(Level.GOLD.nextLevel(), is(nullValue()));
	}
	
	@Test
	public void getMaxValue() {
		assertThat(Level.getMaxLevel(),is(Level.GOLD));
	}

}
