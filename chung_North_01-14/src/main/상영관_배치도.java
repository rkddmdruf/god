package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class 상영관_배치도 extends JFrame{
	
	// 0길, 1벽, 2도, 3출
	int[][] map = new int[9][9];
	Point[][] back = new Point[9][9];
	List<Data> srm = new ArrayList<>();
	List<Point> line = new ArrayList<>();
	JLabel[][] labels = new JLabel[9][9];
	Point start = new Point(8, 0), end;
	
	Color[] colors = {Color.white, Color.GRAY, Color.blue, Color.green};
	
	boolean isFromMain;
	Data data;
	public 상영관_배치도(Data data, boolean isFromMain) {
		this.data = data;
		this.setLayout(new GridLayout(9,9));
		this.isFromMain = isFromMain;
		srm = Connections.select("select * from srm");
		for(int i = 0; i < 81; i++) {
			Data d = srm.get(i);
			int type = Integer.parseInt(d.get(1).toString());
			int x = i / 9;
			int y = i % 9;
			if(type == 2 && d.get(2).toString().equals(data.get(2).toString())) {
				end = new Point(x, y);
			}
			map[x][y] = type;
			back[x][y] = new Point(0,0);
			JLabel l = new JLabel();
			l.setBackground(colors[type]);
			l.setOpaque(true);
			l.setBorder(BorderFactory.createLineBorder(Color.black));
			add(l);
			
			labels[x][y] = l;
			
		}
		bfs();
		go();
		A_setFrame.setting(this, "상영관 배치도", 400,400);
	}
	
	private void go() {
		new Thread(()->{
			try {
				for(int i = line.size()-1; i >= 0; i--) {
					Point xy = line.get(i);
					labels[xy.x][xy.y].setBackground(Color.red);
					Thread.sleep(100);
				}
				labels[end.x][end.y].setBackground(Color.green);
				for(int i = line.size()-1; i >= 0; i--) {
					for(int j = 0; j < 5; j++) {
						Point xy = line.get(i);
						labels[xy.x][xy.y].setBackground(j % 2 == 0 ? Color.red : Color.white);
						Thread.sleep(111);
					}
				}
				JOptionPane.showMessageDialog(null, data.get(2) +  "관에 도착했습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}).start();
	}
	
	private void bfs() {
		int[] x = {0,0,-1,1};
		int[] y = {-1,1,0,0};
		Node node = new Node();
		Queue<Point> q = new LinkedList<>();
		Point start = new Point(8, 0);
		q.add(start);
		node.add(start);
		while(!q.isEmpty()) {
			Point p = q.poll();
			for(int i = 0; i < 4; i++) {
				Point nextPoint = new Point(p.x + x[i], p.y + y[i]);
				if(isFieldIn(nextPoint) && map[nextPoint.x][nextPoint.y] != 1 && node.check(nextPoint)) {
					back[nextPoint.x][nextPoint.y] = p;
					node.add(nextPoint);
					q.add(nextPoint);
				}
			}
		}
		Point xy = end;
		while(!xy.equals(new Point(8, 1))) {
			xy = back[xy.x][xy.y];
			line.add(xy);
		}
	}
	
	private boolean isFieldIn(Point point) {
		return point.x < 9 && 0 <= point.x 
				&& point.y < 9 && 0 <= point.y;
	}
	class Node {
		List<Point> list = new ArrayList<>();
		public void add(Point p) {
			list.add(p);
		}
		public boolean check(Point p) {
			if(list.contains(p)) {
				return false;
			}
			return true;
		}
	}
}
