package ru.vlad24.philosophers.waiterapproach;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Philosopher implements Runnable{
	/**
	 * Number indicating how slow philosophers will change their states. The more it is the slower the time goes.
	 * Needed for demonstration and debugging.
	 */
	private static final int SLOWNESS_FACTOR = 1;
	/**
	 * Time bounds that limit possible times of philosophers' thinking and eating.
	 * Account SLOWNESS_FACTOR parameter, as it scales those values, unless it equals 1.
	 */
	private static final int THINKING_TIME_UPPER_BOUND_MS = 300;
	private static final int THINKING_TIME_LOWER_BOUND_MS = 200;
	private static final int EATING_TIME_UPPER_BOUND_MS   = 200;
	private static final int EATING_TIME_LOWER_BOUND_MS   = 100;
	/**
	 * Number indicating above which hunger level a philosopher will not be able to function.
	 */
	private static final int CRITICAL_HUNGER_LEVEL 		  = 10;
	
	Logger log = Logger.getLogger(Philosopher.class.getName());

	private int position;
	private int hunger;
	private Random random;
	private boolean isFunctioning;
	private boolean isEating;
	private Waiter waiter;


	/**
	 * @param position int number, representing position of a philosopher at a table
	 */
	public Philosopher(int position) {
		super();
		this.position = position;
		this.hunger = 0;
		this.isFunctioning = false;
		this.random = new Random();
		this.waiter = null;
	}

	/**
	 * Sets a waiter for the philosopher.
	 * @param waiter waiter to which philosophers will make orders
	 */
	public void inroduceWaiter(Waiter waiter){
		this.waiter = waiter;
	}
	

	/**
	 * Each philosopher has a level of hunger. It varies from 0 to 10 (all bounds inclusive). If it reaches 10 the philosopher dies.
	 * @return int level of hunger
	 */
	public int getHunger() {
		return hunger;
	}


	/**
	 * Get random int between tFrom and tTo. Used to choose time to think
	 * @param tFrom start timestamp
	 * @param tTo   end timestamp
	 * @return      timestamp between tFrom and tTo
	 */
	private int getRandomInt(int tFrom, int tTo){
		if (tFrom < tTo){
			return tFrom + random.nextInt(tTo- tFrom);
		}else{
			return 0;
		}
	}

	@Override
	public void run() {
		isFunctioning = true;
		while(!Thread.currentThread().isInterrupted() && isFunctioning){
			think();
			tryEat();
		}
	}

	/**
	 * If philosopher is ready to eat he makes an order to the waiter and waits until forks and food will be given.
	 * While forks and food are absent ph. becomes more hungry (hunger level increases). 
	 */
	private void tryEat() {
		if (!isEating){
			if (hunger == 0){
				waiter.order(this.position);
			}else{
				log.log(Level.INFO, String.format("Philosopher%d: hungry at %d", position, hunger));
				if (hunger >= CRITICAL_HUNGER_LEVEL){
					throw new IllegalStateException("Philosopher " + position + " died from starving.");
				}
			}
			hunger++;
		}
	}

	/**
	 * When food is finally provided philosopher starts to eat in front of the waiter.
	 * Hunger level falls to 0.
	 */
	public void eat() {
		isEating = true;
		try {
			log.log(Level.INFO, String.format(">>>>>> Philosopher%d(hungry at %d): eating", position, hunger));
			Thread.sleep(SLOWNESS_FACTOR * getRandomInt(EATING_TIME_LOWER_BOUND_MS, EATING_TIME_UPPER_BOUND_MS));
		} catch (InterruptedException e) {
			isFunctioning = false;
		}finally{
			hunger = 0;		
			isEating = false;
		}
	}

	/**
	 * Method that simulates philosoher's thinking process.
	 */
	private void think() {
		try {
			Thread.sleep(SLOWNESS_FACTOR * getRandomInt(THINKING_TIME_LOWER_BOUND_MS, THINKING_TIME_UPPER_BOUND_MS));
		} catch (InterruptedException e) {
			isFunctioning = false;
		}
	}


}
