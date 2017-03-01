package ru.vlad24.sorts;

public class HeapSorter implements Sorter{

	@Override
	public void sort(int[] a) {
		int n = a.length;
		int[] heap = new int[n]; 
		System.arraycopy(a, 0, heap, 0, n);
		for (int i = n / 2; i >= 0 ; i--){
			putInHeap(heap, i, n);
		}
		for (int i = 0; i < n ; i++){
			a[i] = heap[0];
			heap[0] = heap[n - 1 - i];
			putInHeap(heap, 0, n - 1 - i); 
		}
	}

	private void putInHeap(int[] builtHeap, int i, int border) {
		int minKidPos = 2 * i + 1;
		if (minKidPos >= border)
			return;
		if (!(minKidPos + 1 >= border)){
			minKidPos = (builtHeap[minKidPos + 1] < builtHeap[minKidPos])? minKidPos + 1 : minKidPos;
		}
		int minKid = builtHeap[minKidPos];
		if (builtHeap[i] > minKid){
			int tmp = builtHeap[i];
			builtHeap[i] = minKid;
			builtHeap[minKidPos] = tmp;
			putInHeap(builtHeap, minKidPos, border);
		}
	}
}
