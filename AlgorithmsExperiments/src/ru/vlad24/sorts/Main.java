package ru.vlad24.sorts;

import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		int[] a = new int[]{3,1,2,5,6,0,3};
		Sorter sorter = new MergeSorter();
		sorter.sort(a);
		System.out.println(Arrays.toString(a));
	}

}
