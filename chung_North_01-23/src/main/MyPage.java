package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

public class MyPage extends JFrame{

	Font font = new Font("맑은 고딕", 1, 20);
	JPanel borderPanel = new JPanel(new BorderLayout(3,3)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(5,5,5,5));
	}};
	
	JPanel southPanel = new JPanel(new GridLayout(1, 2, 4, 4)) {{
		setBorder(createLineBorder(Color.black));
		setBackground(Color.white);
	}};
	
	List<Data> food = Connections.select("SELECT food.f_no, f_name FROM moviedb.fb \r\n"
			+ "join food on food.f_no = fb.f_no\r\n"
			+ "where u_no = ?;", User.getUno());
	List<Data> movie = Connections.select("SELECT movie.m_no, m_name FROM moviedb.reservation\r\n"
			+ "join movie on movie.m_no = reservation.m_no\r\n"
			+ " where u_no = ? group by movie.m_no;", User.getUno());
	List<JLabel> labels = new ArrayList<>(); 
	MyPage(){
		setNorthPanel();
		setWPanel();
		setEPanel();
		setAction();
		borderPanel.add(southPanel);
		add(borderPanel);
		setFrame.setframe(this, "내 정보", 600, 400);
	}
	
	private void setAction() {
		for(int i = 0; i < labels.size() ;i++) {
			final int index = i;
			labels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new MovieInfor(movie.get(index).getInt(0));
					dispose();
				}
			});
		}
	}
	
	private void setWPanel() {
		JPanel gridPanel = new JPanel(new GridLayout(0, 2));
		gridPanel.setBackground(Color.white);
		for (int i = 0; i < food.size(); i++) {
			JPanel p = new JPanel(new BorderLayout(7,7));
			p.setBackground(Color.white);
			
			p.add(new JLabel(food.get(i).get(1).toString(), JLabel.CENTER), BorderLayout.SOUTH);
			p.add(new JLabel(getter.getImageIcon("datafiles/foods/" + food.get(i).getInt(0) + ".jpg", 80, 80)));
			gridPanel.add(p);
		}
		for (int i = 0; i < 6 - food.size(); i++) {
			gridPanel.add(new JLabel(""));
		}
		
		southPanel.add(new JScrollPane(gridPanel));
	}
	
	private void setEPanel() {
		JPanel gridPanel = new JPanel(new GridLayout(0, 2));
		gridPanel.setBackground(Color.white);
		for(int i = 0; i < movie.size();i++) {
			JPanel p = new JPanel(new BorderLayout(5,5));
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(90, 140));
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			panel.setBackground(Color.white);
			
			JLabel img = new JLabel(getter.getImageIcon("datafiles/movies/" + movie.get(i).getInt(0) + ".jpg", 80, 120));
			labels.add(img);
			panel.add(img);
			
			p.add(panel);
			p.add(new JLabel(movie.get(i).get(1).toString(), JLabel.CENTER), BorderLayout.SOUTH);
			
			gridPanel.add(p);
		}
		southPanel.add(new JScrollPane(gridPanel));
	}
	
	private void setNorthPanel(){
		JPanel northPanel = new JPanel(new BorderLayout(5,5));
		northPanel.setBackground(Color.white);
		
		JPanel p = new JPanel(new GridLayout(2, 1, 10, 10));
		p.setBorder(createEmptyBorder(5,0,50,0));
		p.setBackground(Color.white);
		
		p.add(new JLabel(User.getUser().get(2).toString()) {{
			setFont(font);
		}});
		p.add(new JLabel(User.getUser().get(1).toString()) {{
			setFont(font);
		}});
		
		northPanel.add(new JLabel(getter.getImageIcon("datafiles/user/" + User.getUno() + ".jpg", 120,120)) {{
			setBorder(createLineBorder(Color.black));
		}}, BorderLayout.WEST);
		northPanel.add(p);
		
		borderPanel.add(northPanel, BorderLayout.NORTH);
	}
	public static void main(String[] args) {
		new MyPage();
	}
}
