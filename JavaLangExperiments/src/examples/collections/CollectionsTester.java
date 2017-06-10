package examples.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionsTester {
	public static void main(String[] args) {
		ArrayList<Integer> c = new ArrayList<>();
		int[][] a = { { 1, 2 }, { 2, 3, 4 }, { 5, 6 } };
		System.out.println(Arrays.deepToString(a));
		List<Integer> l = Arrays.asList(1, 2, 3, 4, 5,6);
		System.out.println(l.stream().filter((x) -> (x % 2 == 0)).collect(Collectors.toList()));
	}
}
