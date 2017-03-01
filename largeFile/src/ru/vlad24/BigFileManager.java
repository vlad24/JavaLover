package ru.vlad24;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @author vlad24 
 * <br>
 * Class for sorting big file and performing grep operations through it.
 * Sorting is done by splitting the file into chunks, sorts it independently and then merges it in another big file.
 */
public class BigFileManager {

	public static class ManagerBuilder{

		private static final int DEFAULT_CHUNKS_AMOUNT = 2;

		private Path srcPath;
		private int chunksAmount = DEFAULT_CHUNKS_AMOUNT;

		public ManagerBuilder(String srcFilename){
			Path srcPath = Paths.get(srcFilename);
			if (Files.exists(srcPath)){
				this.srcPath = srcPath;
			}else{
				throw new IllegalArgumentException("Not existing file provided");
			}
		}
		public ManagerBuilder setChunks(int chunksAmount){
			if (chunksAmount >= 1){
				this.chunksAmount = chunksAmount;
				return this;
			}else{
				throw new IllegalArgumentException("Chunk amount should be >= 1");
			}
		}

		public BigFileManager build() throws FileNotFoundException{
			return new BigFileManager(srcPath, chunksAmount);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////

	private static Logger log = Logger.getLogger(BigFileManager.class.getName());

	private Path srcPath;
	private int chunksAmount;

	/**
	 * @param srcPath file that manager will work with
	 * @param chunksAmount amount of chunks to which the file will be split
	 */
	private BigFileManager(Path srcPath, int chunksAmount){
		super();
		this.srcPath  = srcPath;
		this.chunksAmount = chunksAmount;
	}

	/**
	 * Sorts the srcFile of the file sorter and stores it near the srcFile with "sorted_" prefix. 
	 * Sorting is performed in ascending order
	 * Equivalent to sort(true) 
	 * @return new file that contains sorted contents of the srcFile. 
	 */
	public File sort(){
		return sort(true);
	}

	/**
	 * Sorts the srcFile of the file sorter and stores it near the srcFile with "sorted_" prefix.
	 * 
	 * @param isAsc flag indicating whether sorting should be performed in ascending order
	 * @return new file that contains sorted contents of the srcFile
	 */
	public File sort(boolean isAsc){
		try(Scanner scanner = new Scanner(srcPath.toFile());){
			long byteAmount = srcPath.toFile().length();
			long chunkSize = 1 + byteAmount / chunksAmount;
			log.log(Level.INFO, String.format("Dealing with file with %d byteAmount", byteAmount));
			log.log(Level.INFO, String.format("Chunk size is %d bs", chunkSize));
			ArrayList<File> chunkFiles = new ArrayList<>(chunksAmount);
			Comparator<String> comparator = new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					int ascSign = isAsc ? 1 : -1;
					//null always loses
					if (o2 == null){
						return -ascSign;
					}
					if (o1 == null){
						return ascSign;
					}
					System.out.println(o1 + "/" + o2 + "/" + o1.compareTo(o2));
					return ascSign * o1.compareTo(o2);
				}
			};

			for (int i = 0; i < chunksAmount; i++) {
				ArrayList<String> lines = extractChunkLines(scanner, chunkSize);
				lines.sort(comparator);
				chunkFiles.add(writeChunkFile(lines));
			}
			log.log(Level.INFO, "All chunk files are ready! Merging...");
			Path destPath = Paths.get(srcPath.getParent().toString(), "sorted_" + srcPath.getFileName().toString());
			return merge(chunkFiles, comparator, destPath);
		}	
		catch(Exception e){
			log.log(Level.SEVERE, String.format("Error occured: %s", e.getMessage()));
			return null;
		}
	}

	/**
	 * Filters the big file so that each line contains s-string. Equivalent to grep(s, false).
	 * @param s string to be found in each line. 
	 * @param isRegexp flag indicating whether s-parameter should be treated as regular expression
	 * @return file containing filtered contents (near the srcFile with <greped_> prefix
	 */
	public File grep(String s){
		return grep(s, false);
	}

	/**
	 * Filters the big file using provided parameters and saves results near the src file with <greped_> prefix
	 * @param s string to be found in each line or reg. exp. against which all strings will be tested 
	 * @param isRegexp flag indicating whether s-parameter should be treated as regular expression
	 * @return file containing filtered contents
	 */
	public File grep(String s, boolean isRegexp){
		try(Scanner scanner = new Scanner(srcPath.toFile());){
			long byteAmount = srcPath.toFile().length();
			long chunkSize = 1 + byteAmount / chunksAmount;
			log.log(Level.INFO, String.format("Dealing with file with %d byteAmount", byteAmount));
			log.log(Level.INFO, String.format("Chunk size is %d bs", chunkSize));
			ArrayList<File> chunkFiles = new ArrayList<>(chunksAmount);
			for (int i = 0; i < chunksAmount; i++) {
				ArrayList<String> lines = extractChunkLines(scanner, chunkSize);
				ArrayList<String> filteredLines;
				if (isRegexp){
					filteredLines = findLinesForRegexp(s, lines);
				}else{
					filteredLines = findLinesWithSubstring(s, lines);
				}
				chunkFiles.add(writeChunkFile(filteredLines));
			}
			log.log(Level.INFO, "All chunk files are ready! Merging...");
			Path destPath = Paths.get(srcPath.getParent().toString(), "greped_" + srcPath.getFileName().toString());
			return concat(chunkFiles, destPath);
		}	
		catch(Exception e){
			log.log(Level.SEVERE, String.format("Error occured: %s", e.getMessage()));
			return null;
		}
	}

	/**
	 * Filters lines so that each line contains provided substring
	 * @param substring string that will be searched n each line
	 * @param lines lines through which substring would be searched
	 * @return list of filtered lines
	 */
	private ArrayList<String> findLinesWithSubstring(String substring, ArrayList<String> lines) {
		ArrayList<String> result = new ArrayList<>();
		for (String line : lines){
			if (line.contains(substring)) {
				result.add(line);
			}
		}
		return result;
	}

	/**
	 * @param regexp the regular expression by which lines will be filterd
	 * @param lines the strings that will be filtered
	 * @return the array containing filtered lines
	 */
	private ArrayList<String> findLinesForRegexp(String regexp, ArrayList<String> lines) {
		ArrayList<String> result = new ArrayList<>();
		for (String line : lines){
			if (Pattern.matches(regexp, line)){
				result.add(line);
			}
		}
		return result;
	}

	/**
	 * @param chunkFiles files to concat
	 * @param destPath path to which all files will be concatenated
	 * @return file under destPath
	 */
	private File concat(ArrayList<File> chunkFiles, Path destPath) {
		File dest = new File(destPath.toString());
		try(PrintWriter writer = new PrintWriter(new FileWriter(dest));){
			for (File chunk : chunkFiles){
				try(Scanner scanner = new Scanner(chunk);){
					while (scanner.hasNextLine()){
						writer.println(scanner.nextLine());
					}
					writer.flush();
				}catch (Exception e) {
					log.log(Level.SEVERE, String.format("Error while reading from chunk files (%s)!", e.getMessage()));
				}
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, String.format("Error while concating (%s)!", e.getMessage()));
		}
		return dest;
	}

	/**
	 * @param chunkFiles files that should be merged
	 * @param comparator comparator for comparing strings of files
	 * @param destPath location to save sorted file under
	 * @return file under destPath
	 * @throws FileNotFoundException if a file under destPath could not be created
	 */
	private File merge(ArrayList<File> chunkFiles, Comparator<String> comparator, Path destPath) throws FileNotFoundException {
		File dest = new File(destPath.toString());
		//Scanners each of which is responsible for separate chunk file
		Scanner[] scanners = new Scanner[chunkFiles.size()];
		//Lines that scanners are currently stand on
		String[] currentLines = new String[scanners.length];
		//Flag indicating whether any of scanners provides more lines
		boolean scannersProvideLines = false;
		//Index of scanner to yield the data
		int affected = 0;
		for (int i = 0; i < chunkFiles.size(); i++){
			scanners[i] = new Scanner(chunkFiles.get(i));
			if (scanners[i].hasNextLine()){
				currentLines[i] = scanners[i].nextLine();
				scannersProvideLines = true;
			}else{
				currentLines[i] = null;
			}
		}
		try(PrintWriter writer = new PrintWriter(new FileWriter(dest))){
			while(scannersProvideLines){
				String currentOutput = null;
				scannersProvideLines = false;
				for (int j = 0; j < currentLines.length; j++){
					//if we meet some non null line remember it and take a note that scanners still provide lines
					if (currentLines[j] != null){
						scannersProvideLines = true;
						if (comparator.compare(currentLines[j], currentOutput) <= 0){
							//relax output and remember the scanner that was chosen as provider
							currentOutput = currentLines[j];
							affected = j;
						}
					}
				}
				//If something was found write it to output, move scanner position and set new guess line  
				if (scannersProvideLines){
					writer.println(currentOutput);
					currentLines[affected] = scanners[affected].hasNextLine()? scanners[affected].nextLine() : null;
				}
			}
			writer.flush();
		}catch(Exception e){
			log.log(Level.SEVERE, String.format("Error while merging (%s)!", e.getMessage()));
		}finally{
			//Close all scanners
			for (Scanner s : scanners){
				s.close();
			}
		}
		return dest;
	}


	/**
	 * Writes lines to chunk file that is a temporary file deleted on VM exit
	 * @param lines strings to be written
	 * @return file representing a chunk file
	 * @throws IOException if creating temporary chunk file was not successful
	 */
	private File writeChunkFile(ArrayList<String> lines) throws IOException {
		File chunkFile = File.createTempFile("chunk", ".tmp");
		chunkFile.deleteOnExit();
		log.log(Level.INFO, String.format("Created chunk file %s. \n Writting data... ", chunkFile.getAbsolutePath()));
		try(PrintWriter writer = new PrintWriter(new FileWriter(chunkFile));){
			for (String line : lines) {
				writer.println(line);
			}
		}
		return chunkFile;
	}


	/**
	 * Extracts lines that are approximately of <chunkSize> bytes taking those from scanner
	 * @param scanner scanner from which lines are read
	 * @param chunkSize size of chunk in bytes
	 * @return list of lines for target chunk
	 */
	private ArrayList<String> extractChunkLines(Scanner scanner, long chunkSize) {
		ArrayList<String> lines = new ArrayList<>();
		long bytesRead = 0;
		while(scanner.hasNextLine() && bytesRead < chunkSize){
			String nextLine = scanner.nextLine();
			lines.add(nextLine);
			bytesRead += nextLine.getBytes().length;
		}
		log.log(Level.INFO, String.format("Read %d in total for this chunk", bytesRead));
		return lines;
	}


}
