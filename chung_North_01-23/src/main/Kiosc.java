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

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Kiosc extends JFrame{
	Font font = new Font("맑은 고딕", 1, 25);
	JComboBox<String> order = new JComboBox<String>("전체/세트O/세트X".split("/"));
	JPanel mainPanel = new JPanel(new GridLayout(0, 3, 10,10)) {{
		setBackground(Color.white);
	}};
	
	List<Data> list = new ArrayList<>();
	List<JLabel> imgs = new ArrayList<>();
	String[] query = {
			"SELECT * FROM moviedb.food;",
			
			"SELECT * FROM moviedb.food where f_set = 1;",
			
			"SELECT * FROM moviedb.food where f_set = 0;"
	};
	Kiosc(){
		JLabel logo = new JLabel(getter.getImageIcon("datafiles/로고2.jpg", 425, 175)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.black);
				g.setFont(font);
				g.drawString("키오스크", 160, 100);
			}
		};
		logo.setVerticalAlignment(JLabel.TOP);
		logo.setPreferredSize(new Dimension(425, 180));
		
		setMainPanel();
		add(logo, BorderLayout.NORTH);
		add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(order);
		}}, BorderLayout.SOUTH);
		add(new JScrollPane(mainPanel) {{
			setBackground(Color.white);
		}});
		order.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED)
				setMainPanel();
		});
		setFrame.setframe(this, "키오스크", 425, 625);
	}
	
	private void setFoodAction() {
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			imgs.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new FoodInfor(list.get(index));
					dispose();
					return;
				}
			});
		}
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		imgs.clear();
		
		list = Connections.select(query[order.getSelectedIndex()]);
		for(int i = 0; i < list.size(); i++) {
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(90, 165));
			JPanel imgPanel = new JPanel(new FlowLayout(1,0,0));
			imgPanel.setBackground(Color.white);
			
			JLabel img = new JLabel(getter.getImageIcon("datafiles/foods/" + list.get(i).getInt(0) + ".jpg", 80, 110));
			imgPanel.add(img);
			imgs.add(img);
			
			JPanel inforPanel = new JPanel(new GridLayout(3, 1, 3, 3));
			inforPanel.setBackground(Color.white);
			
			Font font = new Font("맑은 고딕", 1, 12);
			inforPanel.add(new JLabel(list.get(i).get(1).toString(), JLabel.CENTER) {{
				setFont(font);
			}});
			inforPanel.add(new JLabel(list.get(i).get(3).toString(), JLabel.CENTER) {{
				setFont(font);
			}});
			inforPanel.add(new JLabel(list.get(i).get(2).toString(), JLabel.CENTER) {{
				setFont(font);
			}});
			
			p.add(inforPanel);
			p.add(imgPanel, BorderLayout.NORTH);
			mainPanel.add(p);
		}
		setFoodAction();
		revalidate();
		repaint();
	}
	
	public static void main(String[] args) {
		new Kiosc();
	}
}
