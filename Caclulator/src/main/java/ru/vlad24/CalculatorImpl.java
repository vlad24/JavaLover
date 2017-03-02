package ru.vlad24;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class CalculatorImpl implements Calculator {

	public Double evaluate(String exp) {
		Queue<String> inverseNotation;
		try {
			inverseNotation = toInverseNotation(exp);
			return inverseCalculate(inverseNotation);
		} catch (ParseException e) {
			e.printStackTrace();
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
				Double op2 = auxStack.pop();
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
			if (Math.abs(op2) < 1E-10){
				return null;
			}
			return op2 / op1;
		}else{
			return null;
		}
	}


	private Queue<String> toInverseNotation(String exp) throws ParseException {
		Stack<String> auxStack  = new Stack<String>();
		Queue<String> output  = new LinkedList<String>();
		int i = 0;
		String nextToken = null;
		do{
			nextToken = getNextToken(exp, i);
			if (nextToken != null){
				if (Lexer.isNumber(nextToken)){
					output.add(nextToken);
				}else if (Lexer.isSign(nextToken)){
					while (!auxStack.isEmpty() && Lexer.weight(auxStack.peek()) < Lexer.weight(nextToken)){
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
		int nextTokenBound = Lexer.getNextTokenBound(exp, i);
		if (nextTokenBound != -1){
			if (nextTokenBound == i){
				return null;
			}else{
				return exp.substring(i, nextTokenBound);
			}
		}else{
			throw new ParseException("Error at parsing" + exp, i);
		}
	}





}
