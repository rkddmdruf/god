package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static javax.swing.BorderFactory.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class 상영관_배치도 extends JFrame{
//	길: 0, 벽: 1, 도착지: 2
	Data data = null;
	Color[] colors = {Color.white, Color.gray, Color.blue, Color.green};
	int[][] map = new int[9][9];
	Point[][] backPoint = new Point[9][9];
	JLabel[][] labels = new JLabel[9][9];
	List<Point> load = new ArrayList<>();
	Point start = new Point(8, 0), end;
	public 상영관_배치도(int u_no, int m_no, LocalDate date, LocalTime time) {
		setLayout(new GridLayout(9,9));
		data = Connections.select("select * from schedule where m_no = ? and sc_date = ? and sc_time = ?",m_no, date, time).get(0);
		List<Data> srm = Connections.select("SELECT * FROM moviedb.srm;");
		for(int i = 0; i < 81; i++) {
			int x = i / 9, y = i % 9;
			int type = Integer.parseInt(srm.get(i).get(1).toString());
			map[x][y] = type;
			backPoint[x][y] = new Point(0,0);
			add(labels[x][y] = new JLabel() {{
				setBackground(colors[type]);
				setOpaque(true);
				setBorder(createLineBorder(Color.black));
			}});
			if(srm.get(i).get(2) != null && srm.get(i).get(2).toString().equals(data.get(2).toString())) {
				end = new Point(x, y);
			}
		}
		bfs();
		go();
		
		new A_setFrame(this, "상영관_배치도", 500, 500);
	}
	
	private void go() {
		new Thread(()->{
			try {
				for(int i = load.size() - 1; i >= 0; i--) {
					Point xy = load.get(i);
					labels[xy.x][xy.y].setBackground(Color.red);
					Thread.sleep(100);
				}
				labels[end.x][end.y].setBackground(Color.green);
				for(int i = load.size() - 1; i >= 0; i--) {
					Point xy = load.get(i);
					for(int j = 1; j < 5; j++) {
						labels[xy.x][xy.y].setBackground(j % 2 == 0 ? Color.red : Color.white);
						Thread.sleep(100);
					}
				}
				JOptionPane.showMessageDialog(null, data.get(2) + "관에 도착했습니다.", "정보",JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}).start();
	}
	
	private void bfs() {
		int[] x = {0,0,-1,1};
		int[] y = {-1,1,0,0};
		Node node = new Node();
		Queue<Point> q = new LinkedList<>();
		q.add(start);
		node.add(start);
		while(!q.isEmpty()) {
			Point p = q.poll();
			for(int i = 0; i < 4;i++) {
				int xx = p.x + x[i];
				int yy = p.y + y[i];
				if(infield(xx) && infield(yy) && map[xx][yy] != 1 && node.isEmpty(new Point(xx, yy))) {
					Point pp = new Point(xx, yy);
					backPoint[xx][yy] = p;
					q.add(pp);
					node.add(pp);
				}
			}
		}
		
		Point last = new Point(backPoint[end.x][end.y]);
		load.add(last);
		while(!last.equals(new Point(8, 1))) {
			load.add(last = new Point(backPoint[last.x][last.y]));
		}
		System.out.println(load);
	}
	
	private boolean infield(int n) {
		return n >= 0 && n < 9;
	}
	
	class Node {
		List<Point> list = new ArrayList<>();
		public void add(Point p) { list.add(p); }
		public boolean isEmpty(Point p) {
			for(Point p1 : list) 
				if(p.x == p1.x && p.y == p1.y)
					return false;
			return true;
		}
	}
}
