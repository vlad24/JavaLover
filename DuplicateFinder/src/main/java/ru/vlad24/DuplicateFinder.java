package ru.vlad24;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class DuplicateFinder {

	/**
	 * Processes the specified file and puts into another sorted and unique
	 * lines each followed by number of occurrences.
	 *
	 * @param sourceFile file to be processed
	 * @param targetFile output file; append if file exist, create if not.
	 * @return <code>false</code> if there were any errors, otherwise
	 * <code>true</code>
	 */
	public boolean process(File sourceFile, File targetFile) {
		if (sourceFile == null || targetFile == null){
			throw new IllegalArgumentException();
		}
		Map<String, Integer> occurences = new TreeMap<String, Integer>();
		try(PrintWriter writer = new PrintWriter(new FileWriter(targetFile));){
			try(Scanner reader = new Scanner(sourceFile);){
				while (reader.hasNextLine()){
					String line = reader.nextLine();
					Integer currentLineOccurences = occurences.get(line);
					if (currentLineOccurences != null){
						occurences.put(line, currentLineOccurences + 1);
					}else{
						occurences.put(line, 1);
					}
				}
				for (Map.Entry<String, Integer> entry: occurences.entrySet()){
					writer.println(String.format("%s[%d]", entry.getKey(), entry.getValue()));
				}
				writer.flush();
			}
			return true;
		}catch (Exception e) {
			return false;
		}
	}


}
