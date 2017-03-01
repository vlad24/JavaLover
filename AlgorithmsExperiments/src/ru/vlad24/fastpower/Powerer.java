package ru.vlad24.fastpower;

class Powerer {
	
    public static int toPow(int a, int n) {
    	int result = 1;
    	int aEvenPower = a;
    	while(n > 0){
    		if (n % 2 != 0){
    			result *= aEvenPower;
    			//n--;
    		}
    		n /= 2;
    		aEvenPower *= aEvenPower;
    	}
    	return result;
    	
    }
}

	

