package ru.vlad24.philosophers.deadlock;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	private static final int PHILOSOPHER_AMOUNT     = 5;
	private static final int EXECUTION_TIME_SECONDS = 20;
	
	
	private static Logger log = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws InterruptedException {
		ExecutorService restaurant = Executors.newFixedThreadPool(PHILOSOPHER_AMOUNT);
		ArrayList<Future<?>> executionResults = new ArrayList<>();
		// Create philosophers
		Philosopher[] philosophers = new Philosopher[PHILOSOPHER_AMOUNT];
		for (int i = 0; i < 5; i++){
			philosophers[i] = new Philosopher(i);
		}
		// Create a waiter for these philosophers
		Table.init(PHILOSOPHER_AMOUNT);
		for (int i = 0; i < PHILOSOPHER_AMOUNT; i++){
			philosophers[i].sit(Table.getInstance());
			executionResults.add(restaurant.submit(philosophers[i]));
		}
		//
		try{
			TimeUnit.SECONDS.sleep(EXECUTION_TIME_SECONDS);
		}catch(Exception e){
			log.log(Level.SEVERE, "Error while execution:", e);
		}finally{
			log.log(Level.INFO, "Shutting down all threads...");
			restaurant.shutdownNow();
			restaurant.awaitTermination(2, TimeUnit.SECONDS);
		}
		if (checkResults(executionResults)){
			log.log(Level.INFO, "All philosophers are eventually alive and happy to be served by such a waiter");
		}else{
			log.log(Level.SEVERE, "Unfortunately some philosophers died from starvation");
		}
	}

	private static boolean checkResults(ArrayList<Future<?>> executionResults) {
		boolean isSuccess = true;
		for (Future<?> future : executionResults) {
			if (future.isCancelled()){
				isSuccess = false;
				break;
			}
		}
		return isSuccess;
	}
}
