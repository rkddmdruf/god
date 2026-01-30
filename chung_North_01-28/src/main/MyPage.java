package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import utils.CFrame;
import utils.Connections;
import utils.Data;
import utils.User;
import utils.getter;

public class MyPage extends CFrame{
	JPanel borderPanel = new JPanel(new BorderLayout(3,3)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(5,5,5,5));
	}};
	
	List<Data> movies;
	List<JLabel> labels = new ArrayList<>();
	Data user = User.getUser();
	public MyPage() {
		setUserPanel();
		setMainPanel();
		add(borderPanel);
		for(int i = 0; i < labels.size(); i++) {
			final int index = i;
			labels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new MovieInfor(movies.get(index).getInt(0));
					dispose();
					return;
				}
			});
		}
		setFrame("내 정보", 700, 400);
	}
	
	private void setMainPanel() {
		JPanel southPanel = new JPanel(new GridLayout(0, 2, 5, 5));
		southPanel.setBackground(Color.white);
		southPanel.setBorder(createLineBorder(Color.black));
		
		JPanel wPanel = new JPanel(new GridLayout(0, 2, 5, 5));
		wPanel.setBackground(Color.white);
		
		List<Data> listF = Connections.select("SELECT fb.f_no, f_name FROM moviedb.fb\r\n"
				+ "join food on food.f_no = fb.f_no where u_no = ?;", user.get(0));
		for(int i = 0; i < listF.size(); i++) {
			JPanel p = new JPanel(new BorderLayout(5,5));
			p.setBackground(Color.white);
			
			p.add(new JLabel(getter.getImage("datafiles/foods/" + listF.get(i).getInt(0) + ".jpg", 80, 70), JLabel.CENTER));
			p.add(new JLabel(listF.get(i).get(1).toString(), JLabel.CENTER){{
				setFont(getFont().deriveFont(1, 14));
			}}, BorderLayout.SOUTH);
			
			wPanel.add(p);
		}
		
		for(int i = 0; i < 6 - listF.size(); i++) {
			wPanel.add(new JLabel(""));
		}
		
		
		
		
		JPanel ePanel = new JPanel(new GridLayout(0, 2, 5, 5));
		ePanel.setBackground(Color.white);
		
		movies = Connections.select("SELECT movie.m_no, m_name FROM moviedb.reservation\r\n"
				+ "join movie on movie.m_no = reservation.m_no\r\n"
				+ " where u_no = ?;", user.get(0));
		
		for(int i = 0; i < movies.size(); i++) {
			JPanel p = new JPanel(new BorderLayout(5,5));
			p.setPreferredSize(new Dimension(0, 150));
			p.setBackground(Color.white);
			
			JLabel l = new JLabel(getter.getImage("datafiles/movies/" + movies.get(i).getInt(0) + ".jpg", 90, 125), JLabel.CENTER);
			
			labels.add(l);
			p.add(l);
			p.add(new JLabel(movies.get(i).get(1).toString(), JLabel.CENTER){{
				setFont(getFont().deriveFont(1, 14));
			}}, BorderLayout.SOUTH);
			
			ePanel.add(p);
		}
		southPanel.add(new JScrollPane(wPanel));
		southPanel.add(new JScrollPane(ePanel));
		borderPanel.add(southPanel);
	}
	
	private void setUserPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		
		panel.add(new JLabel(getter.getImage("datafiles/user/" + user.getInt(0) + ".jpg", 125,125)) {{
			setBorder(createLineBorder(Color.black));
		}}, BorderLayout.WEST);
		
		JPanel labelPanel = new JPanel(new BorderLayout(10,10));
		labelPanel.setBackground(Color.white);
		labelPanel.setBorder(createEmptyBorder(0,10,60,0));
		
		labelPanel.add(new JLabel(user.get(2).toString()) {{
			setFont(getFont().deriveFont(1,16));
		}}, BorderLayout.NORTH);
		labelPanel.add(new JLabel(user.get(1).toString()) {{
			setFont(getFont().deriveFont(1,16));
		}});
		
		panel.add(labelPanel);
		borderPanel.add(panel, BorderLayout.NORTH);
	}
	public static void main(String[] args) {
		new MyPage();
	}
}
