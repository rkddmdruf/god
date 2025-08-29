package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import utils.*;

public class Map extends BaseFrame{
	Row row;
	static boolean mouse = false;
	List<Row> list = Query.medisn.select();
	JTextField tf = new JTextField() {{
		setPreferredSize(new Dimension(350, 30));
	}};
	JButton but = new JButton("거리 확인") {{
		setForeground(Color.white);
		setPreferredSize(new Dimension(120, 30));
		setBackground(new Color(100, 200, 220, 200));
	}};
	JPanel northPanel = new JPanel() {{
		setBackground(Color.white);
	}};
	JPanel centerPanel = new JPanel(null) {{
		setBackground(Color.white);
	}};
	Map(Row row){
		System.out.println(list);
		this.row = row;
		setFrame("지도", 520,  580, ()->{});
	}
	@Override
	protected void desing() {
		northPanel.add(tf);
		northPanel.add(but);
		
		centerPanel.add(new 약국() {{
			setBounds(0, 0, 500, 500);
		}});
		centerPanel.add(new hospital() {{
			setBounds(0, 0, 500, 500);
		}});
		centerPanel.add(new JLabel(new ImageIcon(new ImageIcon("src/map.png").getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH))) {{
			setBounds(0,0, 500, 500);
		}});
		
		
		add(centerPanel);
		add(northPanel, sp.n);
	}
	
	@Override
	protected void action() {
		JPanel me = (JPanel) centerPanel.getComponent(0);
		me.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				showPopupMenu(e.getX(), e.getY());
			}
		});
		
		but.addActionListener(e -> {
			boolean C = true;
			for(Row row : list) {
				if(tf.getText().equals(row.get(1))) {
					sp.InforMes(this.row.get(1) + "에서 " + row.getString(1) + "까지의 거리 : " + 
					Math.sqrt(Math.pow(row.getInt(2) - this.row.getInt(2), 2) + Math.pow(row.getInt(3) - this.row.getInt(3), 2)));
					C = false;
				}
			}
			if(C) {
				sp.ErrMes("해당 약국이 존재한지 않습니다.");
			}
		});
	}
	
	class hospital extends JPanel{
		
		@Override
		protected void paintComponent(Graphics g) {
			g.drawString(row.getString(1), row.getInt(2) - (row.getString(1).toCharArray().length * 7), row.getInt(3) - 15);
			g.setColor(Color.blue);
			g.fillOval(row.getInt(2) - 10, row.getInt(3) - 10, 10, 10);
		}
		
	}
	
	class 약국 extends JPanel{
		@Override
		public void paint(Graphics g) {
			for(Row row : list) {
				g.fillOval(row.getInt(2), row.getInt(3), 10, 10);
			}
		}
	}
	private void showPopupMenu(int x, int y) {
	    JPopupMenu popupMenu = new JPopupMenu();
	    JLabel label = new JLabel();
	    for(Row row : list) {
			if(row.getInt(2) < x && row.getInt(2)+10 > x) {
				if(row.getInt(3) < y && row.getInt(3)+10 > y) {
					mouse = true;
					label.setText(row.getString(1));
				}
			}
		}
	    popupMenu.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
	    popupMenu.setBackground(Color.white);
	    popupMenu.add(label);
	    popupMenu.show(centerPanel, x, y - 20);
	    popupMenu.setVisible(mouse);
	    mouse = false;
	}
	public static void main(String[] args) {
		new Map(Query.hospitalUserPoint.select(1).get(0));
	}
}
