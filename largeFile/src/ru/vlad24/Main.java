package ru.vlad24;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
	
	// BigFileManager Demonstration
	
	private final static String DEFAULT_SRC_FILEPATH = "./largeFile.txt";
	private static Logger log = Logger.getLogger(Main.class.getName()); 
	
	public static void main(String[] args) throws FileNotFoundException {
		String srcFilepath = DEFAULT_SRC_FILEPATH;
		if (args.length >= 1){
			srcFilepath = args[0];
		}
		/*
		 * Set chunksAmoubnt to 3 just in case.
		 * We can't be sure that target computer could definitely hold 512mb chunk in his 512 ram. 
		*/
		BigFileManager manager = new BigFileManager.ManagerBuilder(srcFilepath).setChunks(3).build();
		log.log(Level.INFO, "Big file manager created.");
		//Sorting demonstration
		log.log(Level.INFO, "Sorting will start now!");
		File sortResult = manager.sort();
		if (sortResult != null){
			log.log(Level.INFO, String.format("Sorting finished! Results are available at %s", sortResult.getAbsolutePath()));
		}else{
			log.log(Level.SEVERE, String.format("Sorting was unsuccessful! Check logs for more info"));
		}
		//Grep demonstration
		log.log(Level.INFO, "Greping will start now!");
		File grepResult = manager.grep(".*on.*", true);
		if (grepResult != null){
			log.log(Level.INFO, String.format("Greping finished! Results are available at %s", grepResult.getAbsolutePath()));
		}else{
			log.log(Level.SEVERE, String.format("Greping was unsuccessful! Check logs for more info"));
		}
		
	}
	
}
