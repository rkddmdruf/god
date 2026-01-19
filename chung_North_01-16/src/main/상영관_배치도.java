package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

public class 상영관_배치도 extends JFrame{
	int[][] map = new int[9][9];
	boolean[][] visit = new boolean[9][9];
	Point[][] back = new Point[9][9];
	
	Point start = new Point(8, 0), end;
	Color[] colors = {Color.white, Color.gray, Color.blue, Color.GREEN};
	JPanel[][]panels = new JPanel[9][9];
	List<Point> line = new ArrayList<>();
	
	Data data;
	public 상영관_배치도(Data data) {
		this.data = data;
		this.setLayout(new GridLayout(9,9));
		List<Data> srm = Connections.select("select * from srm");
		for(int i = 0; i < srm.size(); i++) {
			int x = i / 9;
			int y = i % 9;
			map[x][y] = srm.get(i).getInt(1);
			back[x][y] = new Point(0,0);
			JPanel p = new JPanel();
			p.setBorder(createLineBorder(Color.black));
			p.setBackground(colors[srm.get(i).getInt(1)]);
			
			if(srm.get(i).get(2) != null && srm.get(i).getInt(2) == data.getInt(2)) {
				end = new Point(x, y);
			}
			panels[x][y] = p;
			add(p);
		}
		
		bfs();
		go();
		SetFrames.setframe(this, "상영관 배치도", 500, 500);
	}
	
	private void go() {
		new Thread(()->{
			try {
				for(int i = line.size() - 1; i >= 0; i--) {
					Point p = line.get(i);
					panels[p.x][p.y].setBackground(Color.red);
					Thread.sleep(111);
				}
				panels[end.x][end.y].setBackground(Color.green);
				
				for(int i = line.size() - 1; i >= 0; i--) {
					Point p = line.get(i);
					for(int c = 0; c <= 5; c++) {
						panels[p.x][p.y].setBackground(c % 2 == 0 ? Color.white : Color.red);
						Thread.sleep(70);
					}
				}
				
				JOptionPane.showMessageDialog(null, data.get(2) + "관에 도착했습니다", "정보", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}).start();
	}
	
	private void bfs() {
		int[] x = {0, 0, -1, 1};
		int[] y = {-1, 1, 0, 0};
		Queue<Point> q = new LinkedList<>();
		q.add(start);
		visit[start.x][start.y] = true;
		
		while (!q.isEmpty()) {
			Point p = q.poll();
			for(int i = 0; i < 4; i++) {
				Point next = new Point(p.x + x[i], p.y + y[i]);
				if(isFieldIn(next) && map[next.x][next.y] != 1 && !visit[next.x][next.y]) {
					q.add(next);
					visit[next.x][next.y] = true;
					back[next.x][next.y] = p;
				}
			}
		}
		Point xy = back[end.x][end.y];
		while(!xy.equals(start)) {
			line.add(xy);
			xy = back[xy.x][xy.y];
		}
	}
	
	private boolean isFieldIn(Point n) {
		return n.x >= 0 && n.x < 9 && n.y >= 0 && n.y < 9;
	}
}
