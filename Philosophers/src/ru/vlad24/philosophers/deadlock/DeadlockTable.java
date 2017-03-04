package ru.vlad24.philosophers.deadlock;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vlad24
 * Class representing a table that guarantees deadlock for philosophers trying eating at it 
 * 
 */
public class DeadlockTable {

	Logger log = Logger.getLogger(DeadlockTable.class.getName());

	private static boolean inited = false; 
	private static DeadlockTable instance = null; 

	/**
	 * Initializes the singleton instance.
	 * @param guestAmount amount of guests who will sit at this table
	 */
	public static void init(int guestsAmount){
		if (!inited){
			instance = new DeadlockTable(guestsAmount);
			inited = true;
		}
	}

	public static DeadlockTable getInstance(){
		if (inited){
			return instance;
		}else{
			throw new IllegalStateException("Table is not inited properly");
		}
	}

	private int guestAmount;
	private boolean isWorking;
	private Lock[] fork;
	private AtomicBoolean[] needToAct;


	private DeadlockTable(int guestsAmount) {
		super();
		this.isWorking = true;
		this.guestAmount = guestsAmount;
		this.fork = new Lock[guestAmount];
		this.needToAct = new AtomicBoolean[guestAmount];
		for (int i = 0; i < fork.length; i++) {
			fork[i] = new ReentrantLock();
			needToAct[i] = new AtomicBoolean(false);
		}
		//First philosopher needs to start
		needToAct[0].set(true);
	}

	/**
	 * Method that guarantees deadlock. When a philosopher tries to take a fork, it takes only
	 * left one and then notifies left guest that it needs also take forks.
	 * After left guest is ready(readiness ==  taking both forks) the current guest tries to occupy its right fork. 
	 * @param guestId position of the guest at the table, trying take his fork
	 */
	public void takeForks(int guestId){
		if (Thread.currentThread().isInterrupted() || !isWorking){
			return;
		}
		int left = leftForkOf(guestId);
		int right = rightForkOf(guestId);
		int leftNeighbour = getLeftNieghbor(guestId);
		
		while(!needToAct[guestId].get()){
			//sit calm until told to act 
		}
		//be active:
		fork[right].lock();
		log.log(Level.INFO, String.format("     P%d took left fork", guestId));
		needToAct[leftNeighbour].set(true);
		while(needToAct[leftNeighbour].get()){
			//sit calm until your neighbor takes all his forks
		}
		fork[left].lock();
		needToAct[guestId].set(false);

	}

	private int getLeftNieghbor(int guestId) {
		return (guestId + 4) % guestAmount;
	}

	private int rightForkOf(int guestId) {
		return (guestId + 1) % guestAmount;
	}

	private int leftForkOf(int guestId) {
		int left = guestId;
		return left;
	}

	public void releaseForks(int guestId) {
		if (Thread.currentThread().isInterrupted() || !isWorking){
			return;
		}
		int left = leftForkOf(guestId);
		int right = rightForkOf(guestId);
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
