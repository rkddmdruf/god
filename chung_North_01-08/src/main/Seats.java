package main;


import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import static javax.swing.BorderFactory.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.*;

public class Seats extends JFrame{
// 0길, 1벽, 2도착지
	Connections c = new Connections();
	int[][] n = new int[9][9];
	Point tt[][] = new Point[9][9];
	List<Point> pointList = new ArrayList<>();
	int listSize = 0, number = 0;
	boolean colors = true;
	List<Data> list = c.select("SELECT * FROM moviedb.srm;");
	int s = 0, m_no = 0;
	LocalDate date;
	LocalTime time;
	List<Point> ints = new ArrayList<>();
	List<List<JLabel>> labelList = new ArrayList<List<JLabel>>();
	Timer t;
	
	Seats(int s){
		this.s = s;
		desing();
		new A_setFrame(this, "상영관 배치도", 416, 416);
	}

	private void desing() {
		for(int i = 0; i < 6; i++) {
			ints.add(new Point());
		}
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				n[i][j] = Integer.parseInt(list.get((i*9) + j).get(1).toString());
				tt[i][j] = new Point(0, 0);
				if(list.get((i*9) + j).get(2) != null) {
					Integer[] intz = {i, j};
					ints.set(Integer.parseInt(list.get((i*9) + j).get(2).toString()) - 1, new Point(i , j));
				}
			}
		}
		
		getContentPane().setLayout(new GridLayout(9,9));
		for(int i = 0; i < 9; i++) {final int index = i;
			for(int j = 0; j < 9; j++) { final int jndex = j;
				labelList.add(new ArrayList<JLabel>());
				labelList.get(i).add(new JLabel("") {{
					setOpaque(true);
					Color color = Color.white;
					int s = Integer.parseInt(list.get((index*9) + jndex).get(1).toString());
					if(s == 1) {
						color = Color.gray;
					}else if(s == 2) {
						color = Color.blue;
					}else if(s == 3) {
						color = Color.green;
					}
					setBackground(color);
					setBorder(createLineBorder(Color.black));
				}});
				add(labelList.get(i).get(j));
			}
		}
		bfs(ints.get(s-1).x, ints.get(s-1).y);
		listSize = pointList.size()-2;
		number = pointList.size() -1;
		setColor();
		revalidate();
		repaint();
	}
	private void setColor() {
		t = new Timer(200, e->{
			if(0 <= listSize) {
				labelList.get(pointList.get(listSize).x).get(pointList.get(listSize).y).setBackground(Color.red);//출발-도착
			}
			listSize = listSize - 1;
			if(listSize == -1) {
				System.out.println(listSize);
				labelList.get(pointList.get(0).x).get(pointList.get(0).y).setBackground(Color.green);//도착지

				new Thread() {
					public void run() {
						for(int i = 0; i < pointList.size()-1; i++) {
							number = number - 1;
							Timer t2 = null;
							if(number > 0) {
								t2 = new Timer(100, e2->{
									if(colors) colors = false;
									else colors = true;
									if(number > 0) 
										labelList.get(pointList.get(number).x).get(pointList.get(number).y).setBackground(colors ? Color.white : Color.red);
								});
								t2.start();
							}
							try {
								Thread.sleep(500);
								if(t2 != null) {
									t2.stop();
									labelList.get(pointList.get(number).x).get(pointList.get(number).y).setBackground(Color.red);
								}
							} catch (Exception e2) {
								// TODO: handle exception
							}
						}
						JOptionPane.showMessageDialog(null, s+"관에 도착했습니다", "정보", JOptionPane.INFORMATION_MESSAGE);
					}
				}.start();
			}
		});
		t.start();
	}
	static class Node{
		List<Point> list = new ArrayList<>();
		private void addNode(Point p) {
			list.add(p);
		}
		private boolean check(Point p) {
			for(Point pp : list) {
				if(pp.x == p.x && pp.y == p.y) {
					return true;
				}
			}
			return false;
		}
	}
	
	private void bfs(int ex, int ey) {
		int[] x = {-1,1,0,0};
		int[] y = {0,0,-1,1};
		Node node = new Node();
		Queue<Point> q = new LinkedList<>();
		Point p = new Point(8,0);
		q.add(p);
		node.addNode(p);
		while(!q.isEmpty()) {
			p = q.poll();
			for(int i = 0; i < 4; i++) {
				if(p.x + x[i] >= 0 && p.x + x[i] < 9 && p.y + y[i] >= 0 && p.y + y[i] < 9) {
					Point pp = new Point(p.x + x[i], p.y + y[i]);
					if(!node.check(pp) && n[pp.x][pp.y] != 1) {
						q.add(pp);
						tt[pp.x][pp.y] = new Point(p.x, p.y);
						node.addNode(pp);
					}
				}
			}
		}

		Point xx = new Point(ex,ey);
		while(xx.x + xx.y != 0) {
			pointList.add(xx);
			xx = tt[xx.x][xx.y];
		}
	}
	public static void main(String[] args) {
		new Seats(1);
	}
}