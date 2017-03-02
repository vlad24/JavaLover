package ru.vlad24;

public class Lexer {

	enum STATE {START, DIGIT, POINT, AFTER_POINT, SUCCESS, FAIL};
	
	public static  int getNextTokenBound(String exp, int i) {
		STATE state = STATE.START;
		boolean needToStop = false;
		boolean success = false;
		while (i < exp.length() && !needToStop && !success){
			char c = exp.charAt(i);
			switch (state) {
			case START:{
				if (isDigit(c)){
					state = STATE.DIGIT;
					i++;
				}else if(isPoint(c)){
					state = STATE.FAIL;
				}else if(isSpecial(c)){
					state = STATE.SUCCESS;
					i++;
				}
				break;
			}
			case DIGIT:{
				if (isDigit(c)){
					state = STATE.DIGIT;
					i++;
				}else if(isPoint(c)){
					state = STATE.POINT;
					i++;
				}else if(isSpecial(c)){
					state = STATE.SUCCESS;
				}
				break;
			}
			case POINT:{
				if (isDigit(c)){
					state = STATE.AFTER_POINT;
					i++;					
				}else if(isPoint(c)){
					state = STATE.FAIL;
				}else if(isSpecial(c)){
					state = STATE.FAIL;
				}
				break;
			}
			case AFTER_POINT:{
				if (isDigit(c)){
					state = STATE.AFTER_POINT;
					i++;					
				}else if(isPoint(c)){
					state = STATE.FAIL;
				}else if(isSpecial(c)){
					state = STATE.SUCCESS;
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
		return c == '+' || c == '-' || c == '/' || c == '*' || c == '(' || c == ')';
	}

	public static boolean isNumber(String nextToken) {
		try {
			Double.parseDouble(nextToken);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}

	public static boolean isSign(String nextToken) {
		return nextToken.equals("+") || nextToken.equals("-") || nextToken.equals("*") || nextToken.equals("/"); 
	}

	public static boolean isOpenBracket(String nextToken) {
		return nextToken.equals("(");
	}

	public static boolean isCloseBracket(String nextToken) {
		return nextToken.equals(")");
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
}
