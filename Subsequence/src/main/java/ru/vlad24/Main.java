package ru.vlad24;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		Subsequence s = new SubsequenceImpl();
		boolean b1 = s.find(Arrays.asList("a", "d", "s"), Arrays.asList("a", "d", "i", "d", "a", "s", "+") );
		boolean b2 = s.find(Arrays.asList("a", "i", "i"), Arrays.asList("a", "d", "i", "d", "a", "s", "+") );
		boolean b3 = s.find(Arrays.asList("a", "+", "s"), Arrays.asList("a", "d", "i", "d", "a", "s", "+") );
		System.out.println(b1 + " " + b2 + " " + b3);

	}

}
