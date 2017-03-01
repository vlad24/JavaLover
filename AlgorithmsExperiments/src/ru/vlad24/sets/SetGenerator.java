package ru.vlad24.sets;

import java.util.Arrays;

public class SetGenerator {
		public static void grayRec(int n){
			int[] s = new int[n + 1];
			for (int i = 1; i < n; i++) {
				grayCall(n, s);
			}
		}
		
		public static void grayCall(int m, int[] s){
			if (m != 0){
				grayCall(m - 1, s);
				s[m] = 1 - s[m];
				grayCall(m - 1, s);
			}else{
				System.out.println(Arrays.toString(s));
			}
		}
		
		public static void grayIterative(int n){
			int[] s = new int[1 + n];
			int setsGenerated = 0;
			int bit = 0;
			do{
				System.out.println(Arrays.toString(s));
				setsGenerated++;
				bit = 1;
				int j = setsGenerated;
				while (j % 2 == 0){
					j /= 2;
					bit++;
				}
				if (bit <= n){
					s[bit] = 1 - s[bit];
				}
			}while(bit <= n);
		}
		

}
