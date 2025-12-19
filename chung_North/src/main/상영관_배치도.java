package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import utils.*;
import utils.sp.*;

public class 상영관_배치도 extends BaseFrame{
// 0길, 1벽, 2도착지
	int[][] n = new int[9][9];
	List<Row> list = Query.select("SELECT * FROM moviedb.srm;");
	int s = 0;
	List<Integer[]> ints = new ArrayList<>();
	List<List<JLabel>> labelList = new ArrayList<List<JLabel>>();
	상영관_배치도(int s){
		this.s = s;
		setFrame("상영관 배치도", 405 + 11 + 16, 405 + 11 + 39, ()->{}); 
	}
	@Override
	protected void desing() {
		for(int i = 0; i < 6; i++) {
			ints.add(new Integer[2]);
		}
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				n[i][j] = list.get((i*9) + j).getInt(1);
				if(list.get((i*9) + j).get(2) != null) {
					Integer[] intz = {i, j};
					ints.set(list.get((i*9) + j).getInt(2)-1,intz);
				}
				System.out.print(n[i][j]);
			}
			System.out.println("");
		}
		
		getContentPane().setLayout(new GridLayout(9,9));
		for(int i = 0; i < 9; i++) {final int index = i;
			for(int j = 0; j < 9; j++) { final int jndex = j;
				labelList.add(new ArrayList<JLabel>());
				labelList.get(i).add(new cl("") {{
					setOpaque(true);
					Color color = Color.white;
					if(list.get((index*9) + jndex).getInt(1) == 1) {
						color = Color.gray;
					}else if(list.get((index*9) + jndex).getInt(1) == 2) {
						color = Color.blue;
					}else if(list.get((index*9) + jndex).getInt(1) == 3) {
						color = Color.green;
					}
					setBackground(color);
					setBorder(sp.line);
				}});
				add(labelList.get(i).get(j));
			}
		}
		Astar(n, ints.get(s-1)[0], ints.get(s-1)[1], 8, 0);
	}

	private void Astar(int[][] map, int ex, int ey, int sx, int sy) {
		
	}
	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new 상영관_배치도(1);
	}
}
