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

public class lineBfs extends JFrame{
	//0:길,1:벽,2:도착지
	
	JLabel[][] labels = new JLabel[9][9];
	List<Data> datas = Connections.select("select * from srm");
	List<Point> lines = new ArrayList<>();
	Point start, end;
	Color[] colors = {Color.white, Color.gray, Color.blue, Color.green};
	int[][] map = new int[9][9];
	Data data;
	lineBfs(Data data){
		this.data = data;
		setLayout(new GridLayout(9, 9));
		for(int i = 0; i < datas.size(); i++) {
			int srm = Integer.parseInt(datas.get(i).get(1).toString());
			map[i / 9][i % 9] = srm;
			add(labels[i/9][i % 9] = new JLabel() {{
				setBorder(BorderFactory.createLineBorder(Color.black));
				setBackground(colors[srm]);
				setOpaque(true);
			}});
			if(srm == 3) start = new Point(i / 9, i % 9);
			if(srm == 2 && Integer.parseInt(datas.get(i).get(2).toString()) == Integer.parseInt(data.get(2).toString()) ) {
				end = new Point(i / 9, i % 9);
			}
			
		}
		bfz();
		red();
		new A_setFrame(this, "길찾기", 400, 400);
	}
	
	private void red() {
		new Thread(()->{
			try {
				for(int i = lines.size() - 1; i > 0; i--) {
					int x = lines.get(i).x;
					int y = lines.get(i).y;
					labels[x][y].setBackground(Color.red);
					Thread.sleep(200);
				}
				labels[end.x][end.y].setBackground(Color.green);
				for(int i = lines.size() - 1; i > 0; i--) {
					int x = lines.get(i).x;
					int y = lines.get(i).y;
					for(int j = 1; j < 5; j++) {
						labels[x][y].setBackground(j % 2 == 0 ? Color.red : Color.white);
						Thread.sleep(100);
					}
				}
				JOptionPane.showMessageDialog(null, data.get(2) + "관에 도착했습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
			}
		}).start();;
	}
	
	
	class Node {
		private List<Point> list = new ArrayList<>();
		public void add(Point p) {
			list.add(p);
		}
		public boolean checks(Point p) {
			for(int i = 0; i < list.size(); i++) {
				if(list.get(i).x == p.x && list.get(i).y == p.y)
					return false;
			}
			return true;
		}
	}
	private void bfz() {
		Node node = new Node();
		int[] x = {-1, 1, 0, 0};
		int[] y = {0, 0, -1, 1};
		Queue<Point> q = new LinkedList<>();
		Point[][] revers = new Point[9][9];
		q.add(start);
		node.add(start);
		while(!q.isEmpty()) {
			Point points = q.poll();
			for (int i = 0; i < 4; i++) {
				int xx = points.x + x[i];
				int yy = points.y + y[i];
				
				if (fieldOut(xx) && fieldOut(yy)) {
					Point pp = new Point(xx, yy);
					if (map[xx][yy] != 1 && node.checks(pp)) {
						revers[xx][yy] = points;
						q.add(pp);
						node.add(pp);
					}
				}
			}
		}
		
		Point ends = end;
		while(ends.x != 8 || ends.y != 0) {
			lines.add(ends);
			ends = revers[ends.x][ends.y];
		}
	}
	private boolean fieldOut(int n) {
		return n >= 0 && n < 9;
	}
	public static void main(String[] args) {
		new lineBfs(new Data() {{
			for(String string : "464,6,2,2025-09-12,03:00:00".split(",")) {
				add(string);
			}
		}} );
	}
}
