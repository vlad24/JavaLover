package ru.vlad24.philosophers.waiterapproach;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Waiter implements Runnable{

	Logger log = Logger.getLogger(Waiter.class.getName());

	private static boolean inited = false; 
	private static Waiter instance = null; 

	/**
	 * Initializes the singleton instance.
	 * @param guests list of guests who will be served by the waiter
	 */
	public static void init(Philosopher[] guests){
		if (!inited){
			instance = new Waiter(guests);
			inited = true;
		}
	}

	public static Waiter getInstance(){
		if (inited){
			return instance;
		}else{
			throw new IllegalStateException("Waiter is not inited properly");
		}
	}

	private Queue<Integer> orders;
	private Philosopher[] guests;
	private int guestAmount;
	private boolean isWorking;
	//Waiter tray
	private Lock trayLock; 
	private Lock[] fork;
	

	private Waiter(Philosopher[] guests) {
		super();
		this.isWorking = false;
		this.guests = guests;
		this.guestAmount = guests.length;
		this.trayLock = new ReentrantLock();
		this.fork = new Lock[guestAmount];
		for (int i = 0; i < fork.length; i++) {
			fork[i] = new ReentrantLock();
		}
		orders = new PriorityBlockingQueue<Integer>(guestAmount, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return -Integer.compare(guests[o1].getHunger(), guests[o2].getHunger());
			}
		});
	}

	/**
	 * Tries to take forks for a guest and feed him. 
	 * @param guestId position of the guest at the table
	 * @return true if feeding took place
	 */
	private boolean serve(int guestId){
		if (Thread.currentThread().isInterrupted() || !isWorking){
			return false;
		}
		int left = guestId;
		int right = (guestId + 1) % guestAmount;
		try{
			trayLock.lock();
			if (fork[left].tryLock()){
				if (fork[right].tryLock()){
					log.log(Level.INFO, String.format("Waiter feeding %d", guestId));
					guests[guestId].eat();
					fork[left].unlock();
					fork[right].unlock();
					return true;
				}else{
					fork[left].unlock();
				}
			}
			return false;
		}catch(Exception e){
			log.log(Level.SEVERE, "Waiter is tired! It needs some rest", e);
			isWorking = false;
			return false;
		}finally{
			trayLock.unlock();
		}

	}

	/**
	 * Makes an order for a guest. It means that this guest will be fed as soon as possible after time of invocation of the method.
	 * @param guestId position of the guest at the table
	 */
	public void makeOrder(int guestId) {
		if (Thread.currentThread().isInterrupted() || !isWorking){
			return;
		}
		orders.add(guestId);

	}

	@Override
	public void run() {
		isWorking = true;
		while (!Thread.currentThread().isInterrupted() && isWorking) {
			Integer nextGuest = orders.poll();
			if (nextGuest != null){
				serve(nextGuest);
			}
		}

	}

}
