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

import javax.swing.*;


import static javax.swing.BorderFactory.*;

import utils.Connections;
import utils.Data;
import utils.User;
import utils.getter;
import utils.setFrame;

public class MyPage extends JFrame{
	Font font = new Font("맑은 고딕", 1, 20);
	JPanel borderPanel = new JPanel(new BorderLayout(2, 2)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,7,7,7));
	}};
	
	JPanel mainPanel = new JPanel(new GridLayout(0, 2, 5, 5)) {{
		setBackground(Color.white);
		setBorder(createLineBorder(Color.gray));
	}};
	List<Data> movie = Connections.select("SELECT movie.m_no, m_name FROM moviedb.reservation\r\n"
			+ "join movie on movie.m_no = reservation.m_no\r\n"
			+ " where u_no = ?;", User.getUser().get(0));
	
	List<Data> food = Connections.select("SELECT food.f_no, f_name FROM moviedb.fb \r\n"
			+ "join food on food.f_no = fb.f_no\r\n"
			+ "where u_no = ?;", User.getUser().get(0));
	
	List<JLabel> foods = new ArrayList<>();
	List<JLabel> movies = new ArrayList<>();
	public MyPage(){
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBackground(Color.white);
		
		panel.add(new JLabel(getter.getImage("datafiles/user/" + User.getUser().getInt(0) + ".jpg", 130, 130)) {{
			setBorder(createLineBorder(Color.black));
		}}, BorderLayout.WEST);
		
		JLabel user = new JLabel("user" + User.getUser().get(0));
		user.setFont(font);
		
		JLabel name = new JLabel(User.getUser().get(1).toString());
		name.setFont(font);
		
		panel.add(new JPanel(new GridLayout(2, 1, 15, 15)) {{
			setBackground(Color.white);
			setBorder(createEmptyBorder(5, 5, 55, 5));
			add(user);
			add(name);
		}});
		setMainPanel();
		setMainPanel2();
		borderPanel.add(mainPanel);
		borderPanel.add(panel, BorderLayout.NORTH);
		add(borderPanel);
		setAction();
		setFrame.setframe(this, "내 정보", 700, 400);
	}
	
	private void setAction() {
		for(int i = 0; i < movies.size(); i++) {
			final int index = i;
			movies.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new MovieInfor(movie.get(index).getInt(0));
					dispose();
				}
			});
		}
	}
	
	private void setMainPanel2() {
		JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
		panel.setBackground(Color.white);
		
		List<Data> movie = Connections.select("SELECT movie.m_no, m_name FROM moviedb.reservation\r\n"
				+ "join movie on movie.m_no = reservation.m_no\r\n"
				+ " where u_no = ?;", User.getUser().get(0));
		for(int i = 0; i < movie.size(); i++) {
			JPanel p = new JPanel(new BorderLayout(5,5));
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(0, 140));
			
			JPanel imgPanel = new JPanel(new FlowLayout(1, 0, 0));
			imgPanel.setBackground(Color.white);
			
			JLabel img = new JLabel(getter.getImage("datafiles/movies/" + movie.get(i).getInt(0) + ".jpg", 90, 120));
			
			movies.add(img);
			imgPanel.add(img);
			
			p.add(imgPanel);
			p.add(new JLabel(movie.get(i).get(1).toString(), JLabel.CENTER), BorderLayout.SOUTH);
			
			panel.add(p);
		}
		
		mainPanel.add(new JScrollPane(panel));
	}
	private void setMainPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
		panel.setBackground(Color.white);
		
		List<Data> food = Connections.select("SELECT food.f_no, f_name FROM moviedb.fb \r\n"
				+ "join food on food.f_no = fb.f_no\r\n"
				+ "where u_no = ?;", User.getUser().get(0));
		for(int i = 0; i < food.size(); i++) {
			JPanel p = new JPanel(new BorderLayout(5,5));
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(0, 110));
			
			JPanel imgPanel = new JPanel(new FlowLayout());
			imgPanel.setBackground(Color.white);
			
			JLabel img = new JLabel(getter.getImage("datafiles/foods/" + food.get(i).getInt(0) + ".jpg", 75, 80));
			
			foods.add(img);
			imgPanel.add(img);
			
			p.add(imgPanel);
			p.add(new JLabel(food.get(i).get(1).toString(), JLabel.CENTER), BorderLayout.SOUTH);
			
			panel.add(p);
		}
		
		mainPanel.add(new JScrollPane(panel));
	}
	
	public static void main(String[] args) {
		new MyPage();
	}
}
