package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import utils.*;

public class kiosc extends CFrame{
	JPanel mainPanel = new JPanel(new GridLayout(0, 3, 5, 5)) {{
		setBackground(Color.white);
	}};
	
	List<JLabel> labels = new ArrayList<>();
	List<Data> list = new ArrayList<>();
	JComboBox<Object> cb = new JComboBox<Object>("전체,세트O,세트X".split(","));
	public kiosc(){
		UIManager.put("Label.font", new Font("맑은 고딕", 1, 14));
		JLabel l = new JLabel(getter.getImage("datafiles/로고2.jpg", 400, 175)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setFont(getFont().deriveFont(1, 26));
				g.setColor(Color.black);
				g.drawString("키오스크", 150, 100);
			}
		};
		l.setBorder(createEmptyBorder(0,0,10,0));
		add(l, BorderLayout.NORTH);
		
		setMainPanel();
		add(new JScrollPane(mainPanel));
		add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(cb);
		}}, BorderLayout.SOUTH);
		setAction();
		setFrame("키오스크", 400, 600);
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		labels.clear();
		list = Connections.select("SELECT * FROM moviedb.food where f_set != ?", cb.getSelectedIndex() - 1);
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			JPanel p = new JPanel(new BorderLayout());
			p.setPreferredSize(new Dimension(0, 170));
			p.setBackground(Color.white);
			
			JPanel gridPanel = new JPanel(new GridLayout(0, 1));
			gridPanel.setBackground(Color.white);
			
			gridPanel.add(new JLabel(data.get(1).toString(), JLabel.CENTER));
			gridPanel.add(new JLabel(data.get(3).toString(), JLabel.CENTER));
			gridPanel.add(new JLabel(data.get(2).toString(), JLabel.CENTER));
			
			JLabel img = new JLabel(getter.getImage("datafiles/foods/" + data.getInt(0) + ".jpg", 90, 120));
			
			labels.add(img);
			p.add(img);
			p.add(gridPanel, BorderLayout.SOUTH);
			mainPanel.add(p);
		}
		
		for(int i = 0; i < labels.size(); i++) {
			final int index = i;
			labels.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new FoodInfor(list.get(index).getInt(0));
				}
			});
		}
		revalidate();
		repaint();
	}
	
	private void setAction() {
		cb.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED)
				setMainPanel();
		});
	}

	public static void main(String[] args) {
		new kiosc();
	}
}
