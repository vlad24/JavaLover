package ru.spbu.math.ais.testprobes;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ArrayUtilsBasicTest {

	private final static int BIG_NUMBER   = 10000;
	private final static int SMALL_NUMBER = 10;

	private static Integer[] GOOD_LONG_ARRAY;
	private static Integer[] GOOD_SHORT_ARRAY;
	private static Integer[] UNSORTED_ARRAY;
	private static Integer[] EMPTY_ARRAY;

	private ArrayUtils<Integer> aUtils;

	private Integer getRandomIndex(Integer[] array) {
		return new Random().nextInt(array.length);
	}

	@BeforeClass
	public static void init() {
		GOOD_LONG_ARRAY  = new Integer[BIG_NUMBER];
		GOOD_SHORT_ARRAY = new Integer[SMALL_NUMBER];
		for (int i = 0; i < BIG_NUMBER; i++) {
			GOOD_LONG_ARRAY[i] = 2 * i;
		}
		for (int i = 0; i < SMALL_NUMBER; i++) {
			GOOD_SHORT_ARRAY[i] = 3 * i;
		}
		UNSORTED_ARRAY = GOOD_LONG_ARRAY.clone();
		Collections.shuffle(Arrays.asList(UNSORTED_ARRAY));
		EMPTY_ARRAY = new Integer[0];
	}

	@Before
	public void initArrayUtils() {
		aUtils = new ArrayUtils<Integer>();
	}

	@Test(timeout=2000)
	public void testBinSearchExisting() {
		int expected = getRandomIndex(GOOD_SHORT_ARRAY);
		int result = aUtils.binSearch(GOOD_SHORT_ARRAY, GOOD_SHORT_ARRAY[expected]);
		Assert.assertEquals(expected, result);
	}

	@Test(timeout=4000)
	public void testBinSearchExistingLong() {
		int expected = getRandomIndex(GOOD_LONG_ARRAY);
		int result = aUtils.binSearch(GOOD_LONG_ARRAY, GOOD_LONG_ARRAY[expected]);
		Assert.assertEquals(expected, result);
	}

	@Test(timeout=4000)
	public void testBinSearchNotExisting() {
		int expected = -GOOD_LONG_ARRAY.length;
		int result = aUtils.binSearch(GOOD_LONG_ARRAY, 2 * GOOD_LONG_ARRAY[GOOD_LONG_ARRAY.length - 1]);
		Assert.assertEquals(expected, result);
	}

	@Test(timeout=2000)
	public void testBinSearchUnsorted() {
		int expected = getRandomIndex(UNSORTED_ARRAY);
		aUtils.binSearch(UNSORTED_ARRAY, UNSORTED_ARRAY[expected]);
	}

	@Test(expected=NullPointerException.class)
	public void testNullArray() {
		aUtils.binSearch(null, 24);
	}

	@Test(expected=NullPointerException.class)
	public void testNullTarget() {
		aUtils.binSearch(GOOD_SHORT_ARRAY, null);
	}

	@Test
	public void testEmpty() {
		Assert.assertEquals(-1, aUtils.binSearch(EMPTY_ARRAY, 24));
	}


}
