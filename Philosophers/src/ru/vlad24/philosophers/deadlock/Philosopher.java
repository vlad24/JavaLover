package ru.vlad24.philosophers.deadlock;

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
	private static final int EATING_TIME_UPPER_BOUND_MS   = 400;
	private static final int EATING_TIME_LOWER_BOUND_MS   = 300;

	Logger log = Logger.getLogger(Philosopher.class.getName());

	private int position;
	private Random random;
	private boolean isFunctioning;
	private DeadlockTable table;


	/**
	 * @param position int number, representing position of a philosopher at a table
	 */
	public Philosopher(int position) {
		super();
		this.position = position;
		this.isFunctioning = false;
		this.random = new Random();
		this.table = null;
	}

	/**
	 * Sets a table for the philosopher.
	 * @param table at which philosopher will sit
	 */
	public void sit(DeadlockTable table){
		this.table = table;
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
			tryEat();
			think();
		}
	}

	/**
	 * If philosopher is ready to eat he makes an order to the waiter and waits until forks and food will be given.
	 * While forks and food are absent ph. becomes more hungry (hunger level increases). 
	 */
	private void tryEat() {
		table.takeForks(this.position);
		eat();
		table.releaseForks(this.position);
	}

	/**
	 * When food is finally provided philosopher starts to eat in front of the waiter.
	 * Hunger level falls to 0.
	 */
	public void eat() {
		try {
			log.log(Level.INFO, String.format(">>>>>> Philosopher%d: eating", position));
			Thread.sleep(SLOWNESS_FACTOR * getRandomInt(EATING_TIME_LOWER_BOUND_MS, EATING_TIME_UPPER_BOUND_MS));
		} catch (InterruptedException e) {
			isFunctioning = false;
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
