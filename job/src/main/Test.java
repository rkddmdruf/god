package main;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import utils.BaseFrame;

public class Test extends BaseFrame{

	Test(){
		setFrame("dsf", 500, 500, ()->{});
	}
	@Override
	protected void desing() {
		JPanel p = new JPanel(null) {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.fillOval(0,0,400,400);
			}
		};
		p.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if(e.getX() > 0 && e.getX() < 400) {
					if(e.getY() > 0 && e.getY() < 200 + 200) {
						System.out.println("sdf");
					}
				}
			}
		});
		add(p);
	}

	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new Test();
	}
}
