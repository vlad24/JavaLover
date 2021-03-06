package ru.spbu.math.ais.calculator;

public class Lexer {

	enum LexState {START, DIGIT, SIGN, POINT, AFTER_POINT, SUCCESS, FAIL};
	
	public static int jumpOverNextToken(String expression, int i) {
		LexState state = LexState.START;
		boolean needToStop = false;
		boolean success = false;
		while (i < expression.length() && !needToStop && !success){
			char c = expression.charAt(i);
			switch (state) {
			case START:{
				if (isDigit(c)){
					state = LexState.DIGIT;
					i++;
				}else if(isPoint(c)){
					state = LexState.FAIL;
				}else if(isSign(c)){
					state = LexState.SIGN;
					i++;
				}else if(isSpecial(c)){
					state = LexState.SUCCESS;
					i++;
				}else{
					state = LexState.FAIL;
				}
				break;
			}
			case DIGIT:{
				if (isDigit(c)){
					state = LexState.DIGIT;
					i++;
				}else if(isPoint(c)){
					state = LexState.POINT;
					i++;
				}else if(isSpecial(c)){
					state = LexState.SUCCESS;
				}else{
					state = LexState.FAIL;
				}
				break;
			}
			case SIGN:{
				if (isDigit(c) || isBracket(c)){
					state = LexState.SUCCESS;
				}else{
					state = LexState.FAIL;
				}
				break;
			}
			case POINT:{
				if (isDigit(c)){
					state = LexState.AFTER_POINT;
					i++;					
				}else if(isPoint(c)){
					state = LexState.FAIL;
				}else if(isSpecial(c)){
					state = LexState.FAIL;
				}else{
					state = LexState.FAIL;
				}
				break;
			}
			case AFTER_POINT:{
				if (isDigit(c)){
					state = LexState.AFTER_POINT;
					i++;					
				}else if(isPoint(c)){
					state = LexState.FAIL;
				}else if(isSpecial(c)){
					state = LexState.SUCCESS;
				}else{
					state = LexState.FAIL;
				}
				break;
			}
			case SUCCESS:{
				success = true;
				needToStop = true;
				break;
			}
			case FAIL:{
				needToStop = true;
				i = -1;
				break;
			}
			}
		}
		return i;
	}
	
	public static boolean isPoint(char c) {
		return c == '.';
	}

	public  static boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	public static boolean isSpecial(char c) {
		return isBracket(c) || isSign(c);
	}
	
	public static boolean isBracket(char c) {
		return c == '(' || c == ')';
	}
	
	public static boolean isSign(char c) {
		return c == '+' || c == '-' || c == '/' || c == '*' ;
	}

	public static boolean isNumber(String token) {
		try {
			Double.parseDouble(token);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}

	public static boolean isSign(String token) {
		return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/"); 
	}

	public static boolean isOpenBracket(String token) {
		return token.equals("(");
	}

	public static boolean isCloseBracket(String token) {
		return token.equals(")");
	}

	public static int weight(String token) {
		if (isCloseBracket(token)){
			return 5;
		}else if (token.equals("+") || token.equals("-")){
			return 3;
		}else if (token.equals("*") || token.equals("/")){
			return 2;
		}else if (isOpenBracket(token)){
			return 4;
		}else{
			return 0;
		}
	}

	public static boolean isMinus(String token) {
		return token.equals("-");
	}
}
