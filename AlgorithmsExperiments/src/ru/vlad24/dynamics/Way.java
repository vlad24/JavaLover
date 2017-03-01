package ru.vlad24.dynamics;

import java.util.LinkedList;
import java.util.Queue;
public class Way{

	private static class Point{
		
		@Override
		public String toString() {
			return "Point [x=" + x + ", y=" + y + "]";
		}
		public Point(int xx, int yy){
			x = xx;
			y = yy;
		}
		int x;
		int y;
	}


	int[][] m;

	public Way(int[][] es){
		m = es;
	}

	public int findMinWay(){
		int N = m.length;
		int[][] d = new int[N][];
		for (int i = 0; i < N; i++){
			d[i] = new int[N];
		}
		for (int i = 0; i < N; i++){
			for (int j = 0; j < N; j++){
				d[i][j] = 0;
			}
		}
		d[0][0]= 0;
		Queue<Point> q = new LinkedList<Point>();
		q.add(new Point(0,0));
		while(!q.isEmpty()){
			Point p = q.remove();
			if (p.x == N-1 && p.y == N-1)
				break;
			System.out.println("on " + p.x + " " + p.y);
			int righter = p.y + 1;
			int lower = p.x + 1;
			if (righter < N){
				d[p.x][righter] = Math.max(d[p.x][righter], d[p.x][p.y] + m[p.x][righter]);
				q.add(new Point(p.x,righter));
			}
			if (lower < N){
				d[lower][p.y] = Math.max(d[lower][p.y], d[p.y][p.y] + m[lower][p.y]);
				q.add(new Point(lower, p.y));
			}
		}
		return d[N - 1][N - 1];
	}

	public static void main(String[] args){
		Way w = new Way( new int[][]{
			{0, 23, 10, 1},
			{2, 2, 4, 60},
			{9, 1, 45, 1},
			{32, 3, 23, 0}
		});
		System.out.println(w.findMinWay());
	}
}