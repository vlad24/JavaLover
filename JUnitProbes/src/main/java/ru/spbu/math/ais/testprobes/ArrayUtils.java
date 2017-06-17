package ru.spbu.math.ais.testprobes;

public class ArrayUtils<T extends Comparable<T>> {

	public int binSearch(T[] array, T target) {
		if (array == null || target == null) {
			throw new NullPointerException("Null parameter got");
		}
		int left = 0;
		int right = array.length - 1;
		int middle = 0;
		while (left <= right) {
			middle = (left + right) / 2;
			if (array[middle].compareTo(target) < 0) {
				left = middle + 1;
			}else if (array[middle].compareTo(target) > 0) {
				right = middle - 1;
			}else {
				return middle;
			}
		}
		return -middle - 1;
	}
}
