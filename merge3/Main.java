package ru.vlad24.interviewtasks.merge3;

import java.util.Arrays;

public class Main {
	
	private static int[] merge(int[] a, int[] b, int[] c ){
		int len = a.length + b.length + c.length;
		int[] arr = new int[len];
		int i = 0,j = 0,l = 0;
		int aa = Integer.MAX_VALUE;
		int bb = Integer.MAX_VALUE;
		int cc  = Integer.MAX_VALUE;
		for (int k = 0; k < arr.length; k++){
			aa = (i == a.length)? Integer.MAX_VALUE : a[i];
			bb = (j == b.length)? Integer.MAX_VALUE : b[j];
			cc = (l == c.length)? Integer.MAX_VALUE : c[l];
			if(aa < bb) 
				if (aa < cc){
					arr[k] = aa;
					i++;
				}else {
					arr[k] = cc;
					l++;
				}
			else if(bb < cc){
				arr[k] = bb;
				j++;
			} else {
				arr[k] = cc;
				l++;
			}
					
		}
		return arr;
	}
	
	public static void main(String[] args) {
//		int[] a = new int[]{1, 20, 30, 44};
//		int[] b = new int[]{12,35, 55,100,202,203,205};
//		int[] c = new int[]{0, 2, 3, 4, 5, 6, 7 ,8, 9, 505, 999};
//		System.out.println(Arrays.toString(merge(a,b,c)));
		int x = -22;
		int y = -19;
		x = x - y;
		y = y + x;
		x = y - x;
		System.out.println(x);
		System.out.println(y);
	}
	
}
