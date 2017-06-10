package ru.vlad24.sorts;

import java.util.Arrays;

public class MergeSorter implements Sorter{

	
	@Override
	public void sort(int[] a) {
		int l = a.length;
		sortHelp(a, 0, l, new int[l]);
	}

	private void sortHelp(int[] src, int left, int right, int[] buf) {
		if (right  > left){
			int middle = (left + right) / 2;
			sortHelp(src, left, middle, buf);
			sortHelp(src, middle + 1, right, buf);
			merge   (src, left, middle, right, buf);
		}
	}

	private void merge(int[] src, int left, int middle, int right, int[] buf) {
		int i = left;
		int j = middle;
		int k = left;
		for (int w = left; w < right; w++){
			buf[w] = src[w];
		}
		while (i < middle && j < right){
			if (buf[i] <= buf[j]){
				src[k++] = buf[i++];
			}else{
				src[k++] = buf[j++];
			}
		}
		
		while(i < middle){
			src[k++] = buf[i++];
		}
	}

}
