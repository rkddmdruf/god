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
	Color[] colors = {Color.white, Color.gray, Color.blue, Color.green};
	int[][] map = new int[9][9];
	boolean[][] visit = new boolean[9][9];
	Point[][] back = new Point[9][9];
	
	List<Point> list = new ArrayList<>();
	Point start = new Point(8, 0), end;
	JPanel[][] panels = new JPanel[9][9];
	Data data;
	상영관_배치도(Data data){
		this.data = data;
		this.setLayout(new GridLayout(9,9));
		List<Data> srm = Connections.select("select * from srm");
		for(int i = 0; i < srm.size(); i++) {
			Data d = srm.get(i);
			int x = i / 9;
			int y = i % 9;
			map[x][y] = d.getInt(1);
			back[x][y] = new Point(0,0);
			
			if(d.get(2) != null && data.getInt(2) == d.getInt(2)) end = new Point(x, y);
			
			JPanel p = new JPanel();
			p.setBackground(colors[d.getInt(1)]);
			p.setBorder(BorderFactory.createLineBorder(Color.black));
			
			panels[x][y] = p;
			add(p);
		}
		System.out.println(end);
		
		bfs();
		lightOnOff();
		setFrame.setframe(this, "상영관 배치도", 150, 150);
	}
	
	private void lightOnOff() {
		new Thread(()->{
			try {
				for(int i = list.size() - 1; i >= 0; i--) {
					System.out.println(i);
					Point p = list.get(i);
					panels[p.x][p.y].setBackground(Color.red);
					Thread.sleep(111);
				}
				panels[end.x][end.y].setBackground(Color.green);
				for(int i = list.size() - 1; i >= 0; i--) {
					Point p = list.get(i);
					for(int j = 0; j <= 5; j++){
						panels[p.x][p.y].setBackground(j % 2 == 0 ? Color.white : Color.red);
						Thread.sleep(70);
					}
				}
				getter.mg(data.get(2) + "관에 도착했습니다.", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}).start();
	}
	
	private void bfs() {
		int[] x = {0,0,-1,1};
		int[] y = {-1,1,0,0};
		Queue<Point> q = new LinkedList<>();
		q.add(start);
		visit[start.x][start.y] = true;
		while(!q.isEmpty()) {
			Point p = q.poll();
			for(int i = 0; i < 4; i++) {
				Point n = new Point(p.x + x[i], p.y + y[i]);
				if(isInField(n) && map[n.x][n.y] != 1 && !visit[n.x][n.y]) {
					q.add(n);
					visit[n.x][n.y] = true;
					back[n.x][n.y] = new Point(p.x, p.y);
				}
			}
		}
		
		Point xy = back[end.x][end.y];
		while(!xy.equals(start)) {
			list.add(xy);
			xy = back[xy.x][xy.y];
		}
	}
	
	private boolean isInField(Point p) {
		return 0 <= p.x && 9 > p.x && 0 <= p.y && 9 > p.y;
	}
}
