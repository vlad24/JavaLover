package ru.vlad24.sorts;

public class QuickSorter implements Sorter{

	@Override
	 public void sort(int[] array) {
        quickSort(array, 0, array.length - 1);
    }


    public void quickSort(int[] array, int left, int right) {
        if (left < right - 1) {
            int pivot = settlePivot(array, left, right);
            quickSort(array, left, pivot);
            quickSort(array, pivot, right);
        }
    }


    private int settlePivot(int[] array, int left, int right) {
        int i = left;
        int j = right;
        int pivot = (left + right) / 2;
        while (i < j) {
            while (array[i] <= array[pivot] && i < pivot) {
                i++;
            }
            while (array[j] >= array[pivot] && j > pivot) {
                j--;
            }
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
            if (i == pivot) {
                pivot = j;
            } else if (j == pivot) {
                pivot = i;
            }
        }
        return pivot;
    }
}
