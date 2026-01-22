package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.*;

public class 상영관_배치도 extends JFrame{
	int[][] map = new int[9][9];
	boolean[][] visit = new boolean[9][9];
	Point[][] backPoint = new Point[9][9];
	JPanel[][] panels = new JPanel[9][9];
	Point start = new Point(8, 0), end;
	Color[] colors = {Color.white, Color.gray, Color.blue, Color.green};
	List<Point> line = new ArrayList<>();
	Data data;
	상영관_배치도(Data data) {
		this.data = data;
		this.setLayout(new GridLayout(9,9));
		List<Data> srm = Connections.select("select * from srm");
		for(int i = 0; i < srm.size(); i++) {
			Data d = srm.get(i);
			int x = i / 9;
			int y = i % 9;
			backPoint[x][y] = new Point(0, 0);
			map[x][y] = d.getInt(1);
			JPanel p = new JPanel();
			p.setBorder(BorderFactory.createLineBorder(Color.black));
			p.setBackground(colors[d.getInt(1)]);
			
			if(d.get(2) != null && d.getInt(2) == data.getInt(2))
				end = new Point(x, y);
			panels[x][y] = p;
			this.add(p);
		}
		
		bfs();
		lightOnOff();
		SetFrame.setFrame(this, "상영관 배치도", 150, 150);
	}
	
	private void lightOnOff() {
		new Thread(()->{
			try {
				for(int i = line.size() - 1; i >= 0; i--) {
					panels[line.get(i).x][line.get(i).y].setBackground(Color.red);
					Thread.sleep(111);
				}
				panels[end.x][end.y].setBackground(Color.green);
				for(int i = line.size() - 1; i >= 0; i--) {
					for(int j = 0; j <= 5; j++) {
						panels[line.get(i).x][line.get(i).y].setBackground(j % 2 == 0 ? Color.white : Color.red);
						Thread.sleep(75);
					}
				}
				getter.mg(data.get(2) + "관에 도착했습니다.", JOptionPane.INFORMATION_MESSAGE);
				dispose();
				return;
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
		while(!q.isEmpty()) {
			Point p = q.poll();
			for (int i = 0; i < 4; i++) {
				Point n = new Point(p.x + x[i], p.y + y[i]);
				if(isInField(n) && map[n.x][n.y] != 1 && !visit[n.x][n.y]) {
					q.add(n);
					visit[n.x][n.y] = true;
					backPoint[n.x][n.y] = p;
					System.out.println(n);
				}
			}
		}
		
		Point xy = backPoint[end.x][end.y];
		while(!xy.equals(new Point(start.x, start.y))) {
			line.add(xy);
			xy = backPoint[xy.x][xy.y];
		}
	}
	
	private boolean isInField(Point n) {
		return n.x < 9 && n.x >= 0 && n.y < 9 && n.y >= 0;
	}
}
