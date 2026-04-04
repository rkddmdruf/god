package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Arc2D;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

import utils.*;

public class Chart extends CFrame{

	List<Data> list = Connections.select("SELECT teacher.tno, tname, count(teacher.tno) as counts FROM lecture.course_registration\r\n"
			+ "join certi on certi.cno = course_registration.cno\r\n"
			+ "join teacher on teacher.tno = certi.tno \r\n"
			+ "group by teacher.tno order by counts desc limit 5;");
	public Chart() {
		borderPanel.add(new JLabel("Skills Qualification Association", getter.getImageIcon("datafiles/icon/logo.png", 30, 30), JLabel.LEFT), BorderLayout.NORTH);
		borderPanel.setBorder(createEmptyBorder(5, 5, 5, 5));
		setChart();
		setFrame("관리자", 330, 300);
	}
	int total;
	private void setChart() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.white);
		p.setBorder(createCompoundBorder(createEmptyBorder(10, 20, 30,20), createLineBorder(Color.black)));
		Color[] colors = {Color.red, Color.orange, Color.green, Color.blue, Color.magenta};
		for(int i = 0; i < list.size(); i++) {
			total += list.get(i).getInt(2);
		}
		JLabel l = new JLabel() {
			double start;
			double end;
			double theta = 360.0 / total;
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				for(int i = 0; i < 5; i++) {
					g2.setColor(colors[i]);
					start += end;
					end = list.get(i).getInt(2) * theta;
					Arc2D arc = new Arc2D.Double(50, 10, 180, 180, start, end, Arc2D.PIE);
					double d =  Math.toRadians(-start);
					double x = 90 * Math.cos(d);
					double y = 90 * Math.sin(d);
					g2.fill(arc);
					g2.setColor(Color.black);
					//g2.drawString(i + "", (int) (x + (230 / (3.14 / 1.9))), (int)y + 100);
				}
				System.out.println("");
				start = 0;
				end = 0;
			}
		};
		p.add(l);
		borderPanel.add(p);
	}

	public static void main(String[] args) {
		new Chart();
	}
}
