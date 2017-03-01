package ru.vlad24.interviewtasks.moneyterminal;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class ATMInterface {

	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("> ATM Interface 24. Ready for your commands.");
		ATM atm = new ATM();
		String output = null;
		do{
			String input = askCommand();
			output = atm.processCommand(input);
			if (output != null){
				System.out.println("> " + output );
			}
			else{
				System.out.println("> Goodbye!" );
			}
		}
		while (output != null);
	}

	private static String askCommand() {
		return scanner.nextLine();
	}

	
	private static final class Exchange {
		
		private Integer[] denominations;
		private int  targetSum;
		private int[] counts;
		private int   remainder;

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			for (int i = 0 ; i < denominations.length; i++){
				if (counts[i] != 0){
					builder.append(denominations[i]).append("=").append(counts[i]).append(",");
				}
			}
			builder.append(" всего: ").append(getFetchedSum());
			if (getRemainder() != 0){
				builder.append(", без ").append(getRemainder());
			}
			return builder.toString();
		}

		public Exchange(int targetSum, Integer[] ds) {
			super();
			this.targetSum = targetSum;
			this.denominations = Arrays.copyOf(ds, ds.length);
			this.counts = new int[ds.length];
		}

		public Integer[] getDenominations() {
			return denominations;
		}

		public int[] getCounts() {
			return counts;
		}

		public int getRemainder() {
			return remainder;
		}

		public int getTargetSum() {
			return targetSum;
		}

		public int getFetchedSum() {
			return targetSum - remainder;
		}

		public void setCount(Integer dIndex, int count) {
			counts[dIndex] = count;		
		}

		public void setTargetSum(int targetSum) {
			this.targetSum = targetSum;
		}

		public void setRemainder(int remainder) {
			this.remainder = remainder;
		}

		public void setDenomination(int position, int denomination) {
			denominations[position] = denomination;
			
		}

	}
	
	private final static class ATM {
		private static final int COMBINATIONS_UPPER_BOUND = 10000000; // just for safety
		private final static String OP_PUT = "put";
		private final static String OP_GET = "get";
		private final static String OP_DUMP = "dump";
		private final static String OP_STATE = "state";
		private final static String OP_QUIT = "quit";
		private final Integer[] denominations			    = { 5000, 1000, 500, 100, 50, 25, 10, 5, 3, 1};
		private final Integer[] denominationsGreedyOptimal  = { 5000, 1000, 500, 100, 50, 0 , 10, 5, 0, 1}; 
		private final int threePosition = Arrays.asList(denominations).indexOf(3);
		private final int tfPosition    = Arrays.asList(denominations).indexOf(25);

		private LinkedHashMap<Integer, Integer> amounts;
		private int balance;
		private StringBuffer resultStringBuffer = new StringBuffer();

		public ATM() {
			super();
			this.amounts = new LinkedHashMap<>();
			for (int i = 0; i < denominations.length; i++){
				amounts.put(denominations[i], 0);
			}
			this.balance = 0;
		}

		private void clearBuffer() {
			resultStringBuffer.setLength(0);
		}

		public String processCommand(String input) {
			try{
				if (input == null || input.isEmpty()){
					return "";
				}else{
					String[] commandParts = input.split(" ");
					String opcode = commandParts[0];
					if (opcode.equalsIgnoreCase(OP_QUIT)){
						return quit();
					}else if (opcode.equalsIgnoreCase(OP_GET)){
						return get(Integer.parseInt(commandParts[1]));
					}else if (opcode.equalsIgnoreCase(OP_PUT)){
						return put(Integer.parseInt(commandParts[1]), Integer.parseInt(commandParts[2]));
					}else if (opcode.equalsIgnoreCase(OP_DUMP)){
						return dump();
					}else if (opcode.equalsIgnoreCase(OP_STATE)){
						return state();
					}else{
						return "Unrecognized opcode:" + opcode;
					}
				}
			}catch(NumberFormatException e){
				return "Parsing problem occured! Supplied args could not be parsed to int. " + e.getMessage();
			}catch(IndexOutOfBoundsException e){
				return "Incorrect number of args for opperation. " + e.getMessage();
			}catch(Exception e){ // keep calm and do not let yourself suffer from the error
				return "Problem occured: " + e.getMessage();
			}
		}

		private String put(int denomination, int amount) {
			if (amount < 0 || denomination < 0){
				return "Error: numbers should be positive";
			}
			clearBuffer();
			if (amounts.containsKey(denomination)){
				if ((Integer.MAX_VALUE - balance) / denomination < amount){
					return "Error: Balance overflow risk. Operation declined.";
				}
				int dAvailable = amounts.get(denomination);
				amounts.put(denomination, dAvailable + amount);
				balance += denomination * amount; 
				return resultStringBuffer.append("всего: ").append(state()).toString();
			}else{
				return resultStringBuffer.append("No such denomination available: ").append(denomination)
						.append(". Available: ").append(Arrays.toString(denominations))
						.toString();
			}
		}

		private String get(int sum) {
			if (sum < 0){
				return "Error: number should be positive";
			}
			int amountOfThrees    = amounts.get(3);
			int amountofTwFives   = amounts.get(25);
			int minRemainder      = Integer.MAX_VALUE;
			Exchange bestExchange = exchangeGreedy(sum, denominations);
			int examinedCombinations = amountOfThrees * amountofTwFives;
			if (bestExchange.getRemainder() != 0 && examinedCombinations <= COMBINATIONS_UPPER_BOUND){
				boolean solutionFound = false;
				for (int i = 0; i <= amountOfThrees && !solutionFound; i++){
					for (int j = 0; j <= amountofTwFives && !solutionFound; j++) {
						int sumForGreedy = sum - i * 3 - j * 25;
						if (sumForGreedy < 0 || sumForGreedy == sum) // sumForGreedy <= 0 makes no sense, sumForGreedy == sum is already calculated
							continue;
						Exchange partialExchange = exchangeGreedy(sumForGreedy, denominationsGreedyOptimal);
						//Transform partial exchange result to real one
						partialExchange.setDenomination(threePosition, 3);
						partialExchange.setDenomination(tfPosition,   25);
						partialExchange.setCount(threePosition, i);
						partialExchange.setCount(tfPosition,    j);
						partialExchange.setTargetSum(sum);
						//Now partial exchange corresponds to real situation
						if (minRemainder > partialExchange.getRemainder()){
							minRemainder = partialExchange.getRemainder();
							bestExchange = partialExchange;
						}
						if (bestExchange.getRemainder() == 0){
							solutionFound = true;
						}
					}
				}
			}else{
				bestExchange = exchangeGreedy(sum, denominations);
			}
			commitExchange(bestExchange);
			return bestExchange.toString();
		}

		private void commitExchange(Exchange exchange) {
			Integer[] denominations = exchange.getDenominations();
			int[] counts = exchange.getCounts();
			for (int i = 0 ; i < denominations.length; i++){
				int dAvailable = amounts.get(denominations[i]);
				amounts.put(denominations[i], dAvailable - counts[i]);
			}
			balance -= exchange.getFetchedSum();
		}

		private Exchange exchangeGreedy(int sum, Integer[] denominations) {
			clearBuffer();
			int remainder = sum;
			Exchange report = new Exchange(sum, denominations);
			for (int i = 0; i < denominations.length; i++){
				if (amounts.containsKey(denominations[i])){
					int dAvailable = amounts.get(denominations[i]);
					int dFetched = Math.min(remainder / denominations[i], dAvailable);
					report.setCount(i, dFetched);
					remainder -= denominations[i] * dFetched;
				}
			}
			report.setRemainder(remainder);
			return report;
		}

		private String state() {
			return String.valueOf(balance);
		}

		private String dump() {
			clearBuffer();
			resultStringBuffer.append("\n");
			for (int i = 0; i < denominations.length; i++){
				resultStringBuffer.append(denominations[i]).append("\t").append(amounts.get(denominations[i])).append("\n");
			}
			return resultStringBuffer.toString();
		}
		
		private String quit() {
			// some clean up might go here
			return null;
		}

	}
}
