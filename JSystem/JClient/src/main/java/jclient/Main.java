package jclient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		int rCounts = Integer.parseInt(args[0]);
		int wCounts = Integer.parseInt(args[1]);
		String server = args[2]; //"127.0.0.1:22219";
		int[] ids = new int[args.length - 3];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = Integer.parseInt(args[3 + i]);
		}
		ExecutorService executor = Executors.newFixedThreadPool(rCounts + wCounts);
		for (int i = 0; i < rCounts; i++) {
			executor.submit(new TaskReader(server,ids));			
		}
		for (int i = 0; i < wCounts; i++) {
			executor.submit(new TaskWriter(server, ids));			
		}
		TimeUnit.SECONDS.sleep(10);
		executor.shutdownNow();
	}
	
	
}
