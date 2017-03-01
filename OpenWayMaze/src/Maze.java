import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Maze {
	
	public boolean[][] mazeArray;
	public int columnsAmount;
	public int rowsAmount;
	
	public Maze(String filePath) throws FileNotFoundException{
		System.out.println("Trying to construct the maze from " + filePath);
		try(Scanner scanner = new Scanner(new File(filePath))){
			//First row contains the dimensions of the maze-room
			rowsAmount = scanner.nextInt();
			System.out.println("rowsAmount has been captured : " + rowsAmount);
			columnsAmount = scanner.nextInt();
			System.out.println("columnsAmount has been captured : " + columnsAmount);
			mazeArray = new boolean[rowsAmount][columnsAmount];
			System.out.println("Maze with the dimensions (" + rowsAmount + " x " + columnsAmount + ") has been created. Initializing ...");
			for (int currentRow = 0; currentRow < rowsAmount; currentRow++){
				for (int currentColumn = 0; currentColumn < columnsAmount; currentColumn++){
					int currentElement = scanner.nextInt();
					System.out.print(currentElement + " ");
					mazeArray[currentRow][currentColumn] = (currentElement== 0) ? true : false;
				}
				System.out.println("");
			}
			System.out.println("The maze has been created.");
		} catch (FileNotFoundException error) {
			System.out.println("Problems in creating the maze : " + error.getMessage());
			throw error;
		}
	}
	
	public void printMaze(){
		int currentElement = -1;
		for (int currentRow = 0; currentRow < rowsAmount; currentRow++){
			for (int currentColumn = 0; currentColumn < columnsAmount; currentColumn++){
				currentElement = mazeArray[currentRow][currentColumn] ?  0 : 1;
				System.out.print(currentElement + " ");
			}
			System.out.println("");
		}
	}

	
}
