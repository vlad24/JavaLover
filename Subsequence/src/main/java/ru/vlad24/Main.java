package ru.vlad24;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		Subsequence s = new SubsequenceImpl();
		boolean b = s.find(Arrays.asList("a", "d", "s"), Arrays.asList("a", "d", "i", "d", "a", "s") );
		System.out.println(b);

	}

}
