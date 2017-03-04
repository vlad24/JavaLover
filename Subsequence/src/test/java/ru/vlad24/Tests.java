package ru.vlad24;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class Tests {
	
	private static Subsequence subseqFinder;
	List<?> changed = null;
	List<?> fixed   = null;
	
	@BeforeClass
	public static void init(){
		subseqFinder = new SubsequenceImpl();
	}
	
	
	@Test
	public void testImpossibleDueToLength(){
		changed = Arrays.asList("a", "b");
		fixed   = Arrays.asList("a", "b", "c");
		org.junit.Assert.assertFalse(subseqFinder.find(fixed, changed));
	}
	
	@Test
	public void testPossibleStringLists(){
		changed = Arrays.asList("a", "|", "b", "|", "c");
		fixed   = Arrays.asList("a", "b");
		org.junit.Assert.assertTrue(subseqFinder.find(fixed, changed));
	}
	
	@Test
	public void testImpossibleStringLists(){
		changed = Arrays.asList("x", "y", "z", "?");
		fixed   = Arrays.asList("a", "b", "c");
		org.junit.Assert.assertFalse(subseqFinder.find(fixed, changed));
	}
	
	@Test
	public void testPossibleIntegerLists(){
		changed = Arrays.asList(1, 3, 2, 0, 3);
		fixed   = Arrays.asList(1, 2, 3);
		org.junit.Assert.assertTrue(subseqFinder.find(fixed, changed));
	}
	
	@Test
	public void testPossibleWithReps(){
		changed = Arrays.asList(1, 1, 1, 2, 2, 3, 3, 3);
		fixed   = Arrays.asList(1, 2, 3);
		org.junit.Assert.assertTrue(subseqFinder.find(fixed, changed));
	}
	
	@Test
	public void testFixedEmpty(){
		changed = new ArrayList<Object>(Arrays.asList("whatever".toCharArray()));
		fixed   = new ArrayList<Object>();
		org.junit.Assert.assertTrue(subseqFinder.find(fixed, changed));
	}
	
	@Test
	public void testChangedEmpty(){
		changed = new ArrayList<Object>(); 
		fixed   = new ArrayList<Object>(Arrays.asList("whatever".toCharArray())); 
		org.junit.Assert.assertFalse(subseqFinder.find(fixed, changed));
	}
	
	@Test
	public void testBothEmpty(){
		changed = new ArrayList<Object>();
		fixed   = new ArrayList<Object>();
		org.junit.Assert.assertTrue(subseqFinder.find(fixed, changed));
	}
	
	
	@Test
	public void testPossibleCharLists(){
		changed = Arrays.asList("impossible".toCharArray());
		fixed =   Arrays.asList("possible".toCharArray());
		org.junit.Assert.assertFalse(subseqFinder.find(fixed, changed));
	}
	
	@Test
	public void testFixedIsNull(){
		changed = new ArrayList<Object>(Arrays.asList("whatever".toCharArray()));
		fixed   = null;
		org.junit.Assert.assertFalse(subseqFinder.find(fixed, changed));
	}
	
	@Test
	public void testChangedIsNull(){
		changed = null;
		fixed   = new ArrayList<Object>(Arrays.asList("whatever".toCharArray()));
		org.junit.Assert.assertFalse(subseqFinder.find(fixed, changed));
	}
	
	@Test
	public void testBothNulls(){
		changed = null;
		fixed   = null;
		org.junit.Assert.assertFalse(subseqFinder.find(fixed, changed));
	}
	
}
