package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import utils.CFrame;
import utils.UserU;

public class AdminMain extends CFrame{
	boolean[] bool = new boolean[3];
	int gap = 50;
	JPanel borderPanel = new JPanel(new GridLayout(0, 3, gap, gap)) {{
		setBorder(BorderFactory.createEmptyBorder(90, gap, 90, gap));
		setBackground(Color.white);
	}};
	
	JPanel gameManeger = new JPanel(new BorderLayout(20, 20)) {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.yellow);
			if(bool[0])
			g.fillOval(0, 0, getWidth(), getHeight());
		};{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
	}};
	
	JPanel chart = new JPanel(new BorderLayout(20, 20)) {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.yellow);
			if(bool[1])
			g.fillOval(0, 0, getWidth(), getHeight());
		};{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
	}};
	
	JPanel logOut = new JPanel(new BorderLayout(20, 20)) {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.yellow);
			if(bool[2])
			g.fillOval(0, 0, getWidth(), getHeight());
		};{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
	}};
	
	public AdminMain() {
		UIManager.put("Label.font", new Font("맑은 고딕", 1, 15));
		setting();
		borderPanel.add(gameManeger);
		borderPanel.add(chart);
		borderPanel.add(logOut);
		
		add(borderPanel);
		MouseAdapter m = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getSource() == logOut) {
					UserU.admin = false;
					new Main();
				}
				if(e.getSource() == chart)
					new Chart();
				if(e.getSource() == gameManeger) 
					new Serch();
				dispose();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				int i = e.getSource() == gameManeger ? 0 : (e.getSource() == chart ? 1 : 2);
				setbool(i, true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				int i = e.getSource() == gameManeger ? 0 : (e.getSource() == chart ? 1 : 2);
				setbool(i, false);
			}
		};
		logOut.addMouseListener(m);
		chart.addMouseListener(m);
		gameManeger.addMouseListener(m);
		setFrame("관리자", 700, 350);
	}
	
	private void setbool(int i, boolean b) {
		bool[i] = b;
		revalidate();
		repaint();
	}
	private void setting() {
		gameManeger.add(new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(new ImageIcon("datafiles/게임아이콘.png").getImage(), 0+40, 0 + 20, getWidth()-80, getHeight() - 40, null);
			}
		});
		gameManeger.add(new JLabel("게임 관리", JLabel.CENTER), BorderLayout.SOUTH);
		
		chart.add(new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(new ImageIcon("datafiles/차트.png").getImage(), 0+40, 0 + 20, getWidth()-80, getHeight() - 40, null);
			}
		});
		chart.add(new JLabel("차트", JLabel.CENTER), BorderLayout.SOUTH);
		
		logOut.add(new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(new ImageIcon("datafiles/로그아웃.png").getImage(), 0 + 40, 0+20, getWidth()-80, getHeight()-40, null);
			}
		});
		logOut.add(new JLabel("로그아웃", JLabel.CENTER), BorderLayout.SOUTH);
		
	}

	public static void main(String[] args) {
		new AdminMain();
	}
}
