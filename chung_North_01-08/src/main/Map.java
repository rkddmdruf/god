package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import main.Seats.Node;

public class Map extends JFrame {

	Connections c = new Connections();
	
	int[][] m = new int[9][9];
	int[][] visited = new int[9][9]; // 방문 전: 0, 방문 후: 거리값
	
	JLabel[][] labels = new JLabel[9][9];
	List<Data> data = c.select("select * from srm");
	
	int seat;
	Color[] colors = {Color.white, Color.gray, Color.blue, Color.green};
	
	Point start_point, end_point;
	
	List<Point> path = new ArrayList<>();
	
	Map(int s) {
		this.seat = s;
		design();
		new A_setFrame(this, "상영관 배치도2", 416, 416);
		bfs();
		go();
	}
	
	private void design() {
		setLayout(new GridLayout(9, 9));
		
		for (int i = 0; i < data.size(); i++) {
			int type = Integer.parseInt(data.get(i).get(1).toString());

			m[i/9][i%9] = type;
			labels[i/9][i % 9] = new JLabel() {{
				setBorder(BorderFactory.createLineBorder(Color.black));
				setBackground(colors[type]);
				setOpaque(true);
			}};
			
			if (type == 3) start_point = new Point(i / 9, i % 9);
			if (type == 2 && Integer.parseInt(data.get(i).get(2).toString()) == seat) end_point = new Point(i / 9, i % 9);
			
			add(labels[i/9][i%9]);
		}
		
	}
	
	private void go() {
		
		new Thread(() -> {
			try {
				
				for (int i = 1; i < path.size() - 1; i++) {
					Point xy = path.get(i);
					
					labels[xy.x][xy.y].setBackground(Color.red);
					Thread.sleep(250);
					repaint();
				}
				
				for (int i = 1; i < path.size() - 1; i++) {
					Point xy = path.get(i);
					
					for (int c = 1; c < 5; c++) {
						labels[xy.x][xy.y].setBackground(c % 2 == 0 ? Color.red : Color.white);
						Thread.sleep(100);
						repaint();
					}
				}
				
			} catch (Exception e) {
			}
		}).start();
		
	}
	
	private void bfs() {
		int[] dx = {-1, 1, 0, 0};
		int[] dy = {0, 0, -1, 1};
		
		Queue<Point> queue = new LinkedList<>();
		Point[][] parent = new Point[9][9];
		
		queue.add(start_point);
		visited[start_point.x][start_point.y] = 1;
		parent[start_point.x][start_point.y] = null;
		
		while (!queue.isEmpty()) {
			Point current = queue.poll();
			
			if (current.x == end_point.x && current.y == end_point.y) {
				Point curr = current;
				
				while (curr != null) {
					path.add(curr);
					curr = parent[curr.x][curr.y];
				}
				
				Collections.reverse(path);
				break;
			}
			
			for (int i = 0; i < 4; i++) {
				int next_row = current.x + dx[i];
				int next_col = current.y + dy[i];
				
				if (isAvalidRange(next_row) && isAvalidRange(next_col) && visited[next_row][next_col] == 0) {
					if (m[next_row][next_col] == 1) continue;
					
					visited[next_row][next_col] = 1;
					parent[next_row][next_col] = current;
					queue.offer(new Point(next_row, next_col));
				}
			}
		}
	}
	
	private boolean isAvalidRange(int value) {
		return value >= 0 && value < 9;
	}
	
	public static void main(String[] args) {
		new Map(1);
	}
	
}
