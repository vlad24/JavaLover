import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class MazeAnalyzer {

	private ArrayList<Point> getFreeNeighbours(Maze maze, int row, int column){
		ArrayList<Point> freeNeighbours = new ArrayList<Point>();
		if (isFree(maze, row - 1, column)){
			freeNeighbours.add(new Point(row - 1, column));
		}
		if (isFree(maze, row + 1, column)){
			freeNeighbours.add(new Point(row + 1, column));
		}
		if (isFree(maze, row, column - 1)){
			freeNeighbours.add(new Point(row, column - 1));
		}
		if (isFree(maze, row, column + 1)){
			freeNeighbours.add(new Point(row, column + 1));
		}
		return freeNeighbours;
	}

	private boolean isInside(Maze maze, int row, int column){
		return ((row < maze.rowsAmount) && (row >= 0) && (column < maze.columnsAmount) && (column >= 0));
	}

	private boolean isFree(Maze maze, int row, int column){
		if (isInside(maze, row, column)){
			return (maze.mazeArray[row][column]);
		}
		else{
			return false;
			//Considering that around the maze is a sea of 'one's
		}
	}	

	private boolean isNotVisited(int[][] mazeDistances, int row, int column){
		return (mazeDistances[row][column] == Constants.notVisited);
	}

	private boolean simpleValidatePointsForWayLength(Maze maze, Point start, Point finish){
		return (isFree(maze, start.row, start.column) && isFree(maze, finish.row, finish.column));
	}

	private int[][] getInitialDistancesArray(Maze maze, Point start){
		int[][] distances = new int[maze.rowsAmount][maze.columnsAmount];
		for (int i = 0; i < maze.rowsAmount; i++){
			for (int j = 0; j < maze.columnsAmount; j++){
				distances[i][j] = Constants.notVisited;
			}
		}
		distances[start.row][start.column] = 0;
		return distances;
	}

	public int findShortestWayLength(Maze maze, Point start, Point finish) throws Exception{
		int waveDistance = 0;
		Deque<Point> wavePoints = new LinkedList<Point>();
		Deque<Point> wavePointsBuffer = new LinkedList<Point>();
		if (simpleValidatePointsForWayLength(maze, start, finish)){
			//We have simply checked that our points are not isolated 
			wavePoints.add(start);
			//WavePoints consists of the 'start' first
			int[][] distances = getInitialDistancesArray(maze, start);
			//Distances to all points are set to Constants.notVisited value apart from the 'start' to which the distance is 0 
			do{
				while(!wavePoints.isEmpty()){
					Point currentPoint = wavePoints.pollFirst();
					for (Point neighbourPoint : getFreeNeighbours(maze, currentPoint.row, currentPoint.column)){
						//The wave reaches all unvisited free neighbors, incrementing the distance to them
						if (isNotVisited(distances, neighbourPoint.row, neighbourPoint.column)){
							distances[neighbourPoint.row][neighbourPoint.column] = waveDistance + 1;
							wavePointsBuffer.add(neighbourPoint);
							//Affected neighbors will be accounted on the next step
						}
					}
				}
				//while loop : Emmiting a "wave" from all wavePoints
				wavePoints.addAll(wavePointsBuffer);
				wavePointsBuffer.clear();
				waveDistance++;
				if (!isNotVisited(distances, finish.row, finish.column)){
					return distances[finish.row][finish.column];
					//Returning current wave distance if we have reached the destination
				}
			} while(!wavePoints.isEmpty());
			throw new Exception("Unreachable point!");
			//throwing Exception : wave cannot spread anywhere, but the destination has not been finished 
		}
		else{
			throw new Exception("Wrong points have been given!");
		}
	}

}
