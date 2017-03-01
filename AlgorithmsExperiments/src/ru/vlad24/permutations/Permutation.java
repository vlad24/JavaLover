package ru.vlad24.permutations;

import java.util.ArrayList;
import java.util.Arrays;

public class Permutation {
	
	public static void printArray(int[] array){
		for (int i = 0; i < array.length ; i++){
			System.out.print(array[i] + " ");
		}
		System.out.println();
	}
	
	public static void initArray(int[] array){
		for (int i = 0; i < array.length; i++)	{
			array[i] = i;
		}
	}
	
	public static void printAllPermutations(int n){
		if (n  <= 1){
			System.out.println("no permutations - too few elements");
			return;
		}
		int[] array = new int[n];
		initArray(array);
		printArray(array);
		boolean exiting = false;
		while(!exiting){
			int suffixEndIndex = n - 1;
			while(array[suffixEndIndex - 1] > array[suffixEndIndex]){
					suffixEndIndex--;
					if(suffixEndIndex == 0){
						exiting = true;
						break;
					}
			}
			if (!exiting){
				//Finding min element that is bigger in suffix
				int abyssPosition = suffixEndIndex - 1;
				int minOfLargerPosition = suffixEndIndex; 
				while (array[minOfLargerPosition] > array[abyssPosition]){
					minOfLargerPosition++;
					if (minOfLargerPosition == n){
						break;
					}
				}
				minOfLargerPosition--;
				//Swaping abyss and minOfLarger
				int temp = array[abyssPosition];
				array[abyssPosition] = array[minOfLargerPosition];
				array[minOfLargerPosition] = temp;
				// Reflecting the suffix
				for (int j = 0; j  < ((n - 1) - suffixEndIndex)/2 ; j++){
					temp = array[suffixEndIndex + j];
					array[suffixEndIndex + j] = array[(n - 1) - j];
					array[(n - 1) - j] = temp;				
				}
				//Printing the array
				printArray(array);
			}
		}
		System.out.println("Done");
	}
	
	public static void invertFrom(int from, int[] a){
		int i = from;
		int j = a.length - 1;
		while (i < j){
			int t = a[i];
			a[i] = a[j];
			a[j] = t;
			i++;
			j--;
		}
		
	}
	
	public static void printAllPermutationsRec(int n){
		int[] p = new int[1 + n];
		initArray(p);
		generateRecursive(1, n, p);
	}
	
	public static void generateRecursive(int k, int n, int[] array){
		if (k != n){
			for (int i = n; i >= k; i--){
				generateRecursive(k + 1, n, array);
				if (i > k){
					int r = array[i];
					array[i] = array[k];
					array[k] = r;
					invertFrom(k + 1, array);
				}
			}
		}else{
			printArray(array);
		} 
	}
	//////////////////////////////////
	
	private int[] elements;
	public int n = 0;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Permutation [ ");
		for (int i = 1; i <= n; i++) {
			builder.append(get(i) + " ");
		}
		builder.append("]");
		return builder.toString();
	}
	
	public Permutation(int n) {
		super();
		int[] numbers = new int[1 + n];
		for (int i = 1; i <= n; i++) {
			numbers[i] = i;
		}
		this.n = n;
		this.elements = numbers;
	}

	public Permutation(int[] elements) {
		super();
		this.n = elements.length - 1;
		this.elements = elements;
	}
	
	public int[] getElements() {
		return elements;
	}
	
	public Permutation superPos(Permutation b){
		Permutation c = new Permutation(n);
		for (int i = 1; i <= n; i++) {
			c.set(i, this.get(b.get(i)));
		}
		return c;
	}

	public void setElements(int[] elements) {
		this.elements = elements;
	}
	
	public int get(int i){
		return this.elements[i];
	}
	
	public void set(int i, int e){
		this.elements[i] = e;
	}

	public Permutation getInverted() {
		Permutation q = new Permutation(n);
		for (int i = 1; i <= n; i++) {
			q.set(this.get(i), i);
		}
		return q;
	}
	
	public void makeInverted(){
		boolean[] notVisited = new boolean[1 + n];
		int next = 0;
		int position = 0;
		int element = 0;
		for (int i = 0; i < notVisited.length; i++) {
			notVisited[i] = true;
		}
		for (int i = 1; i <= n; i++) {
			if (notVisited[i]){
				element = get(i);
				notVisited[i] = false;
				position = i;
				while (element != i){       // while our cycle has not finished
					next = get(element);    // choose the next element of cycle. It will be replaced now
					set(element, position); // put elements position to right place
					position = element;     // jump to that place
					element = next;         // set the next element
					notVisited[next] = false;
				}
				set(i, position);           // do not forget to: put element's position to right place
				System.out.println(Arrays.toString(elements));
			}
		}
	}
	
	public ArrayList<ArrayList<Integer>> getCycles(){
		boolean[] visited = new boolean[1 + n];
		ArrayList<ArrayList<Integer>> cycles = new ArrayList<>();
		for (int i = 1; i <= n; i++) {
			ArrayList<Integer> cycle = new ArrayList<>();
			if (!visited[i]){
				visited[i] = true;
				int element = get(i);
				cycle.add(element);
				int next =  get(element);
				while (!visited[element]){
					cycle.add(next);
					visited[element] = true;
					int superNext = get(next);
					element = next;
					next = superNext;
				}
				cycles.add(cycle);
			}
		}
		return cycles;
	}

	public ArrayList<ArrayList<Integer>> getCycleRepresentation(){
		ArrayList<ArrayList<Integer>> simpleCycles = new ArrayList<>();
		ArrayList<ArrayList<Integer>> cycles = getCycles();
		for (ArrayList<Integer> cycle : cycles) {
			if (cycle.size() != 1){
				Integer first = cycle.get(0);
				for (Integer element : cycle){
					if (element == first)
						continue;
					ArrayList<Integer> pair = new ArrayList<>();
					pair.add(first);
					pair.add(element);
					simpleCycles.add(pair);
				}
			}
		}
		return simpleCycles;
	}
	
	public int slowInvertions() {
		int invs = 0;
		for (int i = 2; i <= n; i++){
			int upper = get(i);
			for (int j = 0; j <= i ; j++){
				if (get(j) > upper){
					invs++;
				}
			}
		}
		return invs;
	}
	
}
