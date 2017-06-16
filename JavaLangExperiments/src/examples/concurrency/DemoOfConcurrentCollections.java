package examples.concurrency;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DemoOfConcurrentCollections {

	public static void main(String[] args) throws InterruptedException {
		demoOfConcurrentRW4List(new CopyOnWriteArrayList<Integer>());
		demoOfConcurrentRW4List(new ArrayList<>());
		demoOfConcurrentRW4List(new LinkedList<>());
	}

	private static void demoOfConcurrentRW4List(List<Integer> list) throws InterruptedException {
		boolean cmeExpected = (list instanceof ArrayList) || (list instanceof LinkedList);
		Thread writer = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 20000; i++) {
					list.add(i);
				}
			}
		});
		Thread reader = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
						iterator.next();
					}
					if (cmeExpected) {
						System.out.println("Bad: no expected CME");
					}
				}catch(ConcurrentModificationException e) {
					if (!cmeExpected) {
						System.out.println("Bad: unexpected CME");
					}
				}
			}
		});
		writer.start();
		reader.start();
		writer.join();
		reader.join();
	}
}
