package ru.vlad24;

public class Main {

	public static void main(String[] args) {
		Calculator calculator = new CalculatorImpl();
		System.out.println(calculator.evaluate("(1+38)*4-5"));        // Result: 151
		System.out.println(calculator.evaluate("7*6/2+8"));           // Result: 29
		System.out.println(calculator.evaluate("-(2*3 -3*(2-1))"));   // Result: -3
		System.out.println(calculator.evaluate("19-2*(1+3)"));        // Result: 11
		System.out.println(calculator.evaluate("10.5-0.5"));     	  // Result: 10
		System.out.println(calculator.evaluate("112.2 / (1 - 1.0)")); // Result: infinity
		System.out.println(calculator.evaluate("100"));               // Result: 100
		System.out.println(calculator.evaluate("6/5 + 5/6"));         // Result: 100
		System.out.println(calculator.evaluate("-10"));               // Result: -10
		System.out.println(calculator.evaluate("-12 + )1("));         // Result: null
		System.out.println(calculator.evaluate("-12 * ()"));          // Result: null
	}

}
