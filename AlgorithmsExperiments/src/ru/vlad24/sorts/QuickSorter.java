package ru.vlad24.sorts;

public class QuickSorter implements Sorter{

	@Override
	public void sort(int[] a) {
		qsort(a, 0, a.length - 1);
	}

	public void qsort(int[] a, int left, int right) {
		if (left < right - 1){
			int m = settleCentral(a, left, right);
			qsort(a,  left, m);
			qsort(a, m, right);
		}
	}
	

	private int settleCentral(int[] a , int left, int right) {
		int i = left;
		int j = right;
		int m = (left + right) / 2;
		while (i < j){
			while(a[i] <= a[m] && i < m){
				i++;
			}
			while(a[j] >= a[m] && j > m){
				j--;
			}
			int tmp = a[i];
			a[i] = a[j];
			a[j] = tmp;
			if (i == m){
				m = j;
			}else if (j == m){
				m = i;
			}			
		}
		return m;
	}
}
