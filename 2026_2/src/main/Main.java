package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import utils.CFrame;
import utils.NorthPanel;
import utils.getter;

public class Main extends CFrame{

	Thread thread;
	public Main() {
		borderPanel.add(new NorthPanel(), BorderLayout.NORTH);
		setMainPanel();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(thread != null) thread.interrupt();
			}
		});
		setFrame("자격증 메인 화면", 700, 500);
	}
	
	private void setMainPanel() {
		List<JLabel> list = new ArrayList<>();
		JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
		mainPanel.setBackground(Color.white);
		
		/////////////////////////////////////////////////////
		JPanel adverPanel = new JPanel(null);
		adverPanel.setBackground(Color.white);
		adverPanel.setPreferredSize(new Dimension(400, 175));
		
		for(int i = 1; i <= 5; i++) {
			JLabel l = new JLabel(getter.getImageIcon("datafiles/main/" + i + ".png", 400, 175));
			l.setBounds((i - 1) * 400, 0, 400, 175);
			adverPanel.add(l);
			list.add(l);
		}
		
		thread = new Thread(()->{
			try {
				while(!Thread.interrupted()) {
					for(int i = 0; i <= 400; i ++) {
						list.get(0).setBounds(-i, 0, 400, 175);
						list.get(1).setBounds(400-i, 0, 400, 175);
						Thread.sleep(10);
					}
					list.add(list.get(0));
					list.remove(0);
				}
			} catch (Exception e) {
				Thread.interrupted();
			}
		});
		thread.start();
		mainPanel.add(adverPanel, BorderLayout.WEST);
		/////////////////////////////////////////
		
		JPanel loginPanel = new JPanel(new BorderLayout());
		loginPanel.setBackground(Color.white);
		
		mainPanel.add(loginPanel);
		borderPanel.add(mainPanel);
	}

	public static void main(String[] args) {
		new Main();
	}
}
