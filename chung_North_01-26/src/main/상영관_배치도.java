package main;

import utils.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

public class 상영관_배치도 extends JFrame{
	
	Color[] colors = {Color.white, Color.gray, Color.blue, Color.green};
	private int[][] map = new int[9][9];
	boolean[][] visit = new boolean[9][9];
	JPanel[][] panels = new JPanel[9][9];
	Point start = new Point(8, 0), end;
	Point[][] line = new Point[9][9];
	List<Point> list = new ArrayList<>();
	
	Thread thread;
	Data data;
	public 상영관_배치도(Data data) {
		this.data = data;
		this.setLayout(new GridLayout(9, 9));
		List<Data> srm = Connections.select("select * from srm");
		for(int i = 0; i < srm.size(); i++) {
			Data d = srm.get(i);
			
			int x = i / 9;
			int y = i % 9;
			
			JPanel p = new JPanel();
			p.setBackground(colors[d.getInt(1)]);
			p.setBorder(createLineBorder(Color.black));
			
			if(d.get(2) != null && d.getInt(2) == data.getInt(2)) {
				end = new Point(x, y);
			}
			
			line[x][y] = new Point(0, 0);
			map[x][y] = d.getInt(1);
			panels[x][y] = p;
			add(p);
		}
		bfs();
		go();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				thread.interrupt();
				new Reservation(data.getInt(1));
				dispose();
			}
		});
		setFrame.setframe(this, "상영관 배치도", 150, 150);
	}
	
	private void go() {
		thread = new Thread(()->{
			try {
				for(int i = list.size() - 1; i >= 0; i--) {
					Point p = list.get(i);
					panels[p.x][p.y].setBackground(Color.red);
					Thread.sleep(111);
				}
				panels[end.x][end.y].setBackground(Color.green);
				for(int i = list.size() - 1; i >= 0; i--) {
					Point p = list.get(i);
					for(int s = 0; s <= 5; s++) {
						panels[p.x][p.y].setBackground(s % 2 == 0 ? Color.white : Color.red);
						Thread.sleep(75);
					}
				}
				
				getter.mg(data.get(2) + "관에 도착했습니다", JOptionPane.INFORMATION_MESSAGE);
				new SeatSelect(data);
				dispose();
				return;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
		thread.start();
	}
	
	private void bfs() {
		int[] x = {0,-1,0,1};
		int[] y = {1,0,-1,0};
		Queue<Point> q = new LinkedList<>();
		q.add(start);
		visit[start.x][start.y] = true;
		while(!q.isEmpty()) {
			Point p = q.poll();
			for(int i = 0; i < 4; i++) {
				Point n = new Point(p.x + x[i], p.y + y[i]);
				if(!isInField(n) || map[n.x][n.y] == 1 || visit[n.x][n.y]) continue;
				q.add(n);
				visit[n.x][n.y] = true;
				line[n.x][n.y] = p;
			}
		}
		
		Point xy = line[end.x][end.y];
		while(!xy.equals(new Point(8, 0))) {
			list.add(xy);
			xy = line[xy.x][xy.y];
		}
		System.out.println(list);
	}
	
	private boolean isInField(Point n) {
		return n.x >=0 && n.x < 9 && n.y >= 0 && n.y < 9;
	}
	
}
