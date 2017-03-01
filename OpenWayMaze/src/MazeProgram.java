import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Scanner;

public class MazeProgram {
	public static Scanner reader = new Scanner(System.in);;
	public static void showInstructions(){
		System.out.println("Hello, Dear User!");
		System.out.println("First, let me introduce what I am expecting: ");
		System.out.println("1. The maze-room should be given as a two-dimensional array, where zero-cell represents free space and one-cell represents a wall.");
		System.out.println("# For example :\n 0 1 1 0 \n 0 0 1 0 \n 1 1 0 0");
		System.out.println("2. I don't need your maze-room to be surrounded with walls compulsively, but if you insist on surrounding it, I will get along with that.");
		System.out.println("3. I like when everything is exact, so don't draw a maze for me, I need a file.");
		System.out.println("4. I am ready to look at your attempts to give me wrong input.");
	}
	public static void askForFilePath(){
		System.out.println("Lets's begin!");
		System.out.println("To begin our work I need to take the maze from file. For example : C://testMaze.txt .\n !!! Warning : Russian names in the path can cause file openning problems in console.");
		System.out.println("Give me the path to your file, please : ");
	}

	public static int askForPointCoordinate(String pointType, String rowOrColumn){
		System.out.println("Specify the " + pointType + " position with " + rowOrColumn + " coordinate, please : ");
		return reader.nextInt();
	}

	public static void main(String[] args) {
		showInstructions();
		askForFilePath();
		String filePath = reader.nextLine();
		try {
			Maze maze = new Maze(filePath);
			MazeAnalyzer mazeAnalyzer = new MazeAnalyzer();
			System.out.println("Let's specify start and finish points. The (0,0) is in the top left corner.");
			Point startPoint = new Point(askForPointCoordinate("start", "row"), askForPointCoordinate("start", "column"));
			Point finishPoint = new Point(askForPointCoordinate("finish", "row"), askForPointCoordinate("finish", "column"));
			try {
				int shortestWay = mazeAnalyzer.findShortestWayLength(maze, startPoint, finishPoint);
				System.out.println("The length of the shortest way has been found : " + shortestWay + ".");
			} catch (Exception error) {
				System.out.println("The shortest way cannot be found : " + error.getMessage());
				System.out.println("Check the correctness of the input data, please.");
			}
			reader.close();
		} catch (FileNotFoundException fileError) {
			System.out.println("Problems in opening the file : " + fileError.getMessage());
			System.out.println("Let's try again!");
		}
	}

}
