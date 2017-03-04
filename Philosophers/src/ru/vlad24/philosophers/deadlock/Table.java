package ru.vlad24.philosophers.deadlock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Table {

	Logger log = Logger.getLogger(Table.class.getName());

	private static boolean inited = false; 
	private static Table instance = null; 

	/**
	 * Initializes the singleton instance.
	 * @param guests lsit of guests who will be served by the waiter
	 */
	public static void init(int guestsAmount){
		if (!inited){
			instance = new Table(guestsAmount);
			inited = true;
		}
	}

	public static Table getInstance(){
		if (inited){
			return instance;
		}else{
			throw new IllegalStateException("Table is not inited properly");
		}
	}

	private int guestAmount;
	private boolean isWorking;
	private AtomicInteger lastLeftTaken;
	private Lock[] fork;
	private Object leftForkBlockNotifier;

	private Object rightForkBlockNotifier;

	private AtomicInteger lastRightTaken;


	private Table(int guestsAmount) {
		super();
		this.isWorking = true;
		this.guestAmount = guestsAmount;
		this.fork = new Lock[guestAmount];
		for (int i = 0; i < fork.length; i++) {
			fork[i] = new ReentrantLock();
		}
		this.leftForkBlockNotifier = new Object();
		this.rightForkBlockNotifier = new Object();
		this.lastLeftTaken = new AtomicInteger(guestsAmount - 1);
		this.lastRightTaken = new AtomicInteger(guestsAmount - 1);
	}

	/**
	 * Tries to take forks for a guest and feed him. 
	 * @param guestId position of the guest at the table
	 * @return true if feeding took place
	 */
	public void takeForks(int guestId){
		if (Thread.currentThread().isInterrupted() || !isWorking){
			return;
		}
		int left = guestId;
		int right = (guestId + 1) % guestAmount;
		// Only one eater can proceed at a time. Act only after previous took forks.
		synchronized (leftForkBlockNotifier) {
			while (lastLeftTaken.get() != right){
				try {
					log.log(Level.INFO, String.format("P%d: waiting for left........", guestId));
					leftForkBlockNotifier.wait(5 * 1000);
				} catch (InterruptedException ignored) {
				}
			}
		}
		try{
			fork[left].lock();
			//make others g+5-1 take right
			waitRight(guestId);
			fork[right].lock();
			lastRightTaken.set(right);
			log.log(Level.INFO, String.format("P%d: took my forks!!!", guestId));
		}catch(Exception e){
			log.log(Level.SEVERE, "Error while taking forks", e);
			isWorking = false;
		}

	}

	private void waitRight(int guestId) {
		int left = guestId;
		int right = (guestId + 1) % guestAmount;
		synchronized (leftForkBlockNotifier) {
			lastLeftTaken.set(left);
			leftForkBlockNotifier.notifyAll();
		}
		synchronized (rightForkBlockNotifier) {
			while (lastRightTaken.get() != right){
				try {
					log.log(Level.INFO, String.format("P%d: waiting for right........", guestId));
					rightForkBlockNotifier.wait(5 * 1000);
				} catch (InterruptedException ignored) {
				}
			}
		}
		
		
	}

	/**
	 * Makes an order for a guest. It means that this guest will be fed as soon as possible after time of invocation of the method.
	 * @param guestId position of the guest at the table
	 */
	public void releaseForks(int guestId) {
		if (Thread.currentThread().isInterrupted() || !isWorking){
			return;
		}
		int left = guestId;
		int right = (guestId + 1) % guestAmount;
		try{
			log.log(Level.INFO, String.format("P%d: releasing >>>>>>>", guestId));
			fork[left].unlock();
			fork[right].unlock();
		}catch(Exception e){
			log.log(Level.SEVERE, "Error while locking", e);
			isWorking = false;
		}
	}

}
