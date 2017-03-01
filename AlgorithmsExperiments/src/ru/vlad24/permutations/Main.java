package ru.vlad24.permutations;
import ru.vlad24.sets.SetGenerator;

public class Main {

	public static void main(String[] args) {
		int n = 4;
		Permutation p = new Permutation(new int[]{0, 1,3,4,2});
		Permutation q = new Permutation(new int[]{0, 3,1,2,4});
		System.out.println("p = " + p);
		System.out.println("q = " + q);
		System.out.println("inverted for p = " + p.getInverted());
		System.out.println(p + " * " + q + " = " +  p.superPos(q));
		//p.makeInverted();
		System.out.println("cycles: " + p.getCycles());
		System.out.println("inversions: " + p.slowInvertions());
		System.out.println("cycle representation: " + p.getCycleRepresentation());
		//Permutation.printAllPermutationsRec(4);
		//System.out.println("------------------");
		//Permutation.printAllPermutations(4);
		//SetGenerator.grayRec(n);
		SetGenerator.grayIterative(n);
		
	}

}
