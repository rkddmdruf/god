package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.*;

import utils.*;

public class 상영관_배치도 extends CFrame{
	Color[] colors = {Color.white, Color.gray, Color.blue, Color.green};
	int[][] map = new int[9][9];
	boolean[][] visit = new boolean[9][9];
	JPanel[][] panels = new JPanel[9][9];
	Point[][] back = new Point[9][9];
	List<Point> line = new ArrayList<>();
	Point start = new Point(8, 0), end;
	Data data;
	
	public 상영관_배치도(Data data) {
		this.data = data;
		this.setLayout(new GridLayout(9 ,9));
		List<Data> srm = Connections.select("select * from srm");
		for(int i = 0; i < 81; i++) {
			Data d = srm.get(i);
			
			int x = i / 9;
			int y = i % 9;
			
			back[x][y] = new Point(0, 0);
			map[x][y] = d.getInt(1);
			if(d.get(2) != null && data.getInt(2) == d.getInt(2)) {
				end = new Point(x, y);
			}
			JPanel p = new JPanel();
			p.setBackground(colors[d.getInt(1)]);
			p.setBorder(BorderFactory.createLineBorder(Color.black));
			
			panels[x][y] = p;
			add(p);
		}
		
		getLine();
		go();
		setFrame("상영관 배치도", 1080 - 19, 1080 - 39);
	}
	
	private void go() {
		new Thread(()->{
			try {
				for(int i = line.size() - 1; i >= 0; i--) {
					panels[line.get(i).x][line.get(i).y].setBackground(Color.red);
					Thread.sleep(111);
				}
				panels[end.x][end.y].setBackground(Color.green);
				for(int i = line.size() - 1; i >= 0; i--) {
					for(int is = 0; is <= 5; is++) {
						panels[line.get(i).x][line.get(i).y].setBackground(is % 2 == 0 ? Color.white: Color.red);
						Thread.sleep(75);
					}
					Thread.sleep(75);
				}
				getter.mg(data.get(2) + "관에 도착했습니다.", JOptionPane.INFORMATION_MESSAGE);
				new SeatSelect(data);
				dispose();
			} catch (Exception e) {
				
				getter.mg(e.getMessage(), JOptionPane.ERROR_MESSAGE);
			}
		}).start();
	}

	private void getLine() {
		int[] x = {-1, 0, 1, 0};
		int[] y = {0, 1, 0, -1};
		
		Queue<Point> q = new LinkedList<>();
		q.add(start);
		visit[start.x][start.y] = true;
		while(!q.isEmpty()) {
			Point p =  q.poll();
			for(int i = 0; i < 4; i++) {
				Point n = new Point(p.x + x[i], p.y + y[i]);
				if(isInField(n) && map[n.x][n.y] != 1 && !visit[n.x][n.y]) {
					q.add(n);
					back[n.x][n.y] = p;
					visit[n.x][n.y] = true;
				}
			}
		}
		for(Point p = back[end.x][end.y]; !p.equals(new Point(8, 0)); p = back[p.x][p.y]) {
			line.add(p);
		}
	}
	
	private boolean isInField(Point n) {
		return n.x < 9 && n.x >= 0 && n.y < 9 && n.y >= 0;
	}
}
