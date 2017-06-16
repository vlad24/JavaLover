package ru.spbu.math.ais.calculator;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Calculator {

private DecimalFormat outputFormatter;
	
	public Calculator() {
		super();
		outputFormatter = new DecimalFormat(("#.####"));
	}


	/**
	 * Evaluate statement represented as string.
	 *
	 * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
	 *                  parentheses, operations signs '+', '-', '*', '/'<br>
	 *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
	 * @return string value containing result of evaluation or null if statement is invalid
	 */
	public String evaluate(String exp) {
		Queue<String> inverseNotation;
		try {
			inverseNotation = toInverseNotation(exp);
			return outputFormatter.format(inverseCalculate(inverseNotation));
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}


	private Double inverseCalculate(Queue<String> inverseNotation) {
		Stack<Double> auxStack  = new Stack<Double>();
		while (!inverseNotation.isEmpty()){
			String nextToken = inverseNotation.poll();
			if (!Lexer.isSign(nextToken)){
				auxStack.push(Double.parseDouble(nextToken));
			}else{
				Double op1 = auxStack.pop();
				Double op2 = null;
				if (Lexer.isMinus(nextToken)){
					try{
						op2 = auxStack.pop();
					}catch(EmptyStackException e){
						op2 = 0.0;
					}
				}else{
					op2 = auxStack.pop();
				}
				auxStack.push( performOperation(op1, op2, nextToken));
			}
		}
		return auxStack.pop();
	}

	private Double performOperation(Double op1, Double op2, String sign) throws IllegalArgumentException{
		if (sign.equals("+")){
			return op1 + op2;
		}else if (sign.equals("-")){
			return op2 - op1;
		}else if (sign.equals("*")){
			return op1 * op2;
		}else if (sign.equals("/")){
			if (Math.abs(op1) < 1E-7){
				return null;
			}
			return op2 / op1;
		}else{
			return null;
		}
	}


	private Queue<String> toInverseNotation(String initialExp) throws ParseException {
		Stack<String> auxStack  = new Stack<String>();
		Queue<String> output  = new LinkedList<String>();
		int i = 0;
		String nextToken = null;
		String exp = initialExp.replace(" ", "");
		do{
			nextToken = getNextToken(exp, i);
			if (nextToken != null){
				if (Lexer.isNumber(nextToken)){
					output.add(nextToken);
				}else if (Lexer.isSign(nextToken)){
					while (!auxStack.isEmpty() &&
							Lexer.weight(auxStack.peek()) <= Lexer.weight(nextToken)){
						output.add(auxStack.pop());
					}
					auxStack.push(nextToken);
				}else if (Lexer.isOpenBracket(nextToken)){
					auxStack.add(nextToken);
				}else if (Lexer.isCloseBracket(nextToken)){
					boolean pairFound = false; 
					while (!auxStack.isEmpty()){
						String lowerToken = auxStack.pop();
						pairFound = Lexer.isOpenBracket(lowerToken);
						if (!pairFound){
							output.add(lowerToken);
						}else{
							break;
						}
					}
					if (!pairFound){
						throw new ParseException("Pair bracket not found! : " + exp, i);
					}
				}
				i += nextToken.length();
			}else{
				while (!auxStack.isEmpty()){
					output.add(auxStack.pop());
				}
			}
		} while (nextToken != null);
		return output;
	}


	private String getNextToken(String exp, int i) throws ParseException {
		int nextTokenBound = Lexer.jumpOverNextToken(exp, i);
		if (nextTokenBound != -1){
			if (nextTokenBound == i){
				return null;
			}else{
				return exp.substring(i, nextTokenBound);
			}
		}else{
			throw new ParseException("Error at parsing: " + exp, i);
		}
	}



}
