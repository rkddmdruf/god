package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.*;

public class Chart extends CFrame{

	Color[] color = {Color.red, Color.orange, Color.green, Color.blue, Color.magenta};
	List<Data> list = Connections.select("SELECT count(certi.tno) FROM lecture.course_registration \r\n"
			+ "join certi on certi.cno = course_registration.cno\r\n"
			+ "join teacher on teacher.tno = certi.tno\r\n"
			+ "group by certi.tno order by count(certi.tno) desc, certi.tno;");
	int total = 0;
	public Chart() {
		borderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		borderPanel.add(new JLabel("Skills Qualifiaction Association", getter.getImage("datafiles/icon/logo.png", 30, 30), JLabel.LEFT), BorderLayout.NORTH);
		JPanel emPanel = new JPanel(new BorderLayout());
		emPanel.setBackground(Color.white);
		emPanel.setBorder(BorderFactory.createEmptyBorder(0,15, 50, 15));
		for(Data data : list) {
			total  += data.getInt(0);
		}
		JPanel p = new JPanel() {
			double start = 0, end = 0, theta = 360.0 / total;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				for(int i = 0; i < 5; i++) {
					g2.setColor(color[i]);
					start += end;
					end = theta * list.get(i).getInt(0);
					Arc2D.Double arc = new Arc2D.Double(50, 10, 170, 170, start, end, Arc2D.PIE);
					g2.fill(arc);
				}
				start = 0;
				end = 0;
			}
		};
		p.setBackground(Color.white);
		p.setBorder(BorderFactory.createLineBorder(Color.black));
		emPanel.add(p);
		borderPanel.add(emPanel);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				new Login();
				dispose();
			}
		});
		setFrame("관리자", 325, 300);
	}
	
	public static void main(String[] args) {
		new Chart();
	}
}
