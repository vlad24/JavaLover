package ru.vlad24.sorts;

import java.util.Arrays;

public class MergeSorter implements Sorter{

	
   @Override
    public void sort(int[] array) {
        System.out.println("Sorting " + Arrays.toString(array));
        int length = array.length;
        mergeSort(array, 0, length, new int[length]);
    }


    private void mergeSort(int[] array, int left, int right, int[] buffer) {
        System.out.println("Sorting [" + left + "," + right + ")");
        if (right > left) {
            int middle = (left + right) / 2;
            mergeSort(array, left, middle, buffer);
            mergeSort(array, middle + 1, right, buffer);
            merge(array, left, middle, right, buffer);
        }
    }


    private void merge(int[] array, int left, int middle, int right, int[] buffer) {
        System.out.println("----> Merging [" + left + "," + middle + ") with [" + middle + "," + right + "): " + Arrays.toString(array));
        System.out.println("--> Left:" + arraySubstring(array, left, middle));
        System.out.println("--> Right:" + arraySubstring(array, middle, right));
        int i = left;
        int j = middle;
        int out = left;
        for (int w = left; w < right; w++) {
            buffer[w] = array[w];
        }
        while (i < middle && j < right) {
            if (buffer[i] <= buffer[j]) {
                array[out++] = buffer[i++];
            } else {
                array[out++] = buffer[j++];
            }
        }
        while (i < middle) {
            array[out++] = buffer[i++];
        }
        while (j < right) {
            array[out++] = buffer[j++];
        }
        System.out.println("--> Combined:" + arraySubstring(array, left, right));
        System.out.println("----> Merged [" + left + "," + middle + ") with [" + middle + "," + right + "): " + Arrays.toString(array));
    }


    private String arraySubstring(int[] array, int left, int middle) {
        StringBuilder b = new StringBuilder();
        for (int i = left; i < middle; i++) {
            b.append(array[i]).append(" ");
        }
        return b.toString();
    }

}

