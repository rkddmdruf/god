package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import utils.*;

public class Main extends CFrame{
	Font font = new Font("맑은 고딕", 1, 24);
	JPanel mainPanel = new JPanel(new GridLayout(2, 2)) {{
		setBackground(Color.white);
	}};
	
	JButton login = new JButton(User.getUser() == null ? "로그인" : "로그아웃") {{
		setFont(font.deriveFont(0, 18));
		setForeground(Color.white);
		setBackground(getter.COLOR);
	}};
	
	List<JLabel> myIcon = new ArrayList<>();
	List<JLabel> reviewGame = new ArrayList<>();
	List<JLabel> firstGame = new ArrayList<>();
	Thread thread;
	public Main() {
		UIManager.put("Label.font", new Font("맑은 고딕", 1, 12));
		add(new JLabel("게임 스토어") {{
			setPreferredSize(new Dimension(0, 50));
			setBorder(createEmptyBorder(10,10,10,10));
			setBackground(Color.black);
			setOpaque(true);
			setForeground(Color.white);
			setFont(font);
		}}, BorderLayout.NORTH);
		setTopLeft();
		setTopRight();
		setBottomLeft();
		setBottomRight();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				thread.interrupt();
			}
		});
		add(mainPanel);
		
		setAction();
		setFrame("메인", 650, 400);
	}
	
	
	private void setAction() {
		login.addActionListener(e->{
			if(User.getUser() == null) {
				new Login();
				dispose();
				return;
			}
			getter.mg("로그아웃 되었습니다.", JOptionPane.INFORMATION_MESSAGE);
			User.setUser(null);
			login.setText("로그인");
			revalidate();
			repaint();
		});
	}


	private void setTopLeft() {
		List<JPanel> panels = new ArrayList<>();
		JPanel nullPanel = new JPanel(null);
		nullPanel.setBorder(createLineBorder(Color.black));
		nullPanel.setBackground(Color.white);
		
		List<Data> list = Connections.select("SELECT gameinformation.*, count(purchasegame.g_no) as c FROM game_site.purchasegame\r\n"
				+ "join gameinformation on gameinformation.g_no = purchasegame.g_no\r\n"
				+ " group by purchasegame.g_no order by c desc, purchasegame.g_no limit 5;");
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			
			JPanel p = new JPanel(new BorderLayout(0, 0));
			p.setBackground(Color.white);
			
			p.add(new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/gameimage/" + data.getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			});
			JLabel name = new JLabel(" " + data.get(1).toString()) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					if(data.getInt(5) == 1)
						g.drawImage(new ImageIcon("datafiles/19세 마크.png").getImage(), getWidth() - 40, (getHeight()-25) / 2, 25, 25, null);
				}
			};
			name.setFont(font);
			p.add(name, BorderLayout.SOUTH);
			
			p.setBorder(createLineBorder(Color.black));
			p.setBounds(i * 325, 0, 325, 350 / 2);
			nullPanel.add(p);
			panels.add(p);
		}

		thread = new Thread(()->{
			try {
				while(! Thread.interrupted()) {
					Thread.sleep(2000);
					for(int i = 0; i <= 325; i++) {
						panels.get(0).setBounds(-i, 0, 325, 175);
						panels.get(1).setBounds(325 - i, 0, 325, 175);
						Thread.sleep(1);
					}
					panels.add(panels.get(0));
					panels.remove(0);
					
					revalidate();
					repaint();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
		thread.start();
		mainPanel.add(nullPanel);
	}
	
	private void setTopRight() {
		JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
		panel.setBackground(Color.white);
		panel.setBorder(createEmptyBorder(3,3,3,3));
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 4, 5,5));
		gridPanel.setBackground(Color.white);
		
		String[] string = "검색,마이홈,장바구니,달력".split(",");
		for(int i = 0; i < 4; i++) {
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			
			JLabel img = new JLabel(getter.getImage("datafiles/" + string[i] + ".png", 25, 25));
			p.add(img);
			myIcon.add(img);
			
			p.add(new JLabel(string[i], JLabel.CENTER) {{
				setFont(font.deriveFont(1, 13));
			}}, BorderLayout.SOUTH);
			gridPanel.add(p);
		}
		panel.add(gridPanel);
		panel.add(login);
		
		for(int i = 0; i < myIcon.size(); i++) {
			final int index = i;
			myIcon.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(User.getUser() == null) {
						getter.mg("로그인이 되어있지 않습니다.", JOptionPane.ERROR_MESSAGE);
						dispose();
						new Login();
						return;
					}
				}
			});
		}
		
		mainPanel.add(panel);
	}
	
	private void setBottomLeft() {
		JPanel panel = new JPanel(new BorderLayout(7,7));
		panel.setBackground(Color.white);
		
		
		List<Data> list = Connections.select("SELECT gameinformation.*, avg(c_star) as a FROM game_site.comments \r\n"
				+ "join gameinformation on gameinformation.g_no = comments.g_no\r\n"
				+ "group by gameinformation.g_no order by a desc, gameinformation.g_no limit 5;");
		JPanel gridPanel = new JPanel(new GridLayout(0, 2, 3, 3));
		gridPanel.setBackground(Color.white);
		
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			p.setBorder(createLineBorder(Color.black));
			
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/gameImage/" + data.getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
					if(data.getInt(5) != 1) return;
					g.drawImage(new ImageIcon("datafiles/19세 마크.png").getImage(), 5, 5, 20, 20, null);
				}
			};
			img.setPreferredSize(new Dimension(0, 80));
			JPanel inforPanel = new JPanel(new GridLayout(3, 1, 5, 5));
			inforPanel.setBackground(Color.white);
			
			inforPanel.setBorder(createEmptyBorder(7,0,7,0));
			inforPanel.add(new JLabel("포인트 : " + data.getInt(2)));
			inforPanel.add(new JLabel("별점 : " + data.get(data.size() - 1).toString().substring(0, 3)));
			String can = Connections.select("SELECT ca_name FROM game_site.category where ca_no = ?;", data.get(4)).get(0).get(0).toString();
			inforPanel.add(new JLabel("카테고리 : " + can));
			
			reviewGame.add(img);
			p.add(img, BorderLayout.NORTH);
			p.add(inforPanel);
			gridPanel.add(p);
		}
		
		panel.add(new JLabel("별점 TOP 5") {{
			setFont(font);
		}}, BorderLayout.NORTH);
		panel.add(new JScrollPane(gridPanel));
		mainPanel.add(panel);
	}
	
	private void setBottomRight() {
		JPanel panel = new JPanel(new BorderLayout(10,10));
		panel.setBackground(Color.white);
		
		panel.add(new JLabel("최신게임 5", JLabel.RIGHT) {{
			setFont(font.deriveFont(22f));
		}}, BorderLayout.NORTH);
		
		List<Data> list = Connections.select("SELECT * FROM game_site.gameinformation order by g_birth desc, g_no limit 5;");
		JPanel gridPanel = new JPanel(new GridLayout(0, 2, 3, 3));
		gridPanel.setBackground(Color.white);
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			p.setBorder(createLineBorder(Color.black));
			
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/gameImage/" + data.getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
					if(data.getInt(5) != 1) return;
					g.drawImage(new ImageIcon("datafiles/19세 마크.png").getImage(), 5, 5, 20, 20, null);
				}
			};
			img.setPreferredSize(new Dimension(0, 80));
			
			JPanel inforPanel = new JPanel(new GridLayout(3, 1, 5, 5));
			inforPanel.setBackground(Color.white);
			
			inforPanel.setBorder(createEmptyBorder(7,0,-7,0));
			inforPanel.add(new JLabel("포인트 : " + data.getInt(2)));
			String can = Connections.select("SELECT ca_name FROM game_site.category where ca_no = ?;", data.get(4)).get(0).get(0).toString();
			inforPanel.add(new JLabel("카테고리 : " + can));
			
			firstGame.add(img);
			p.add(img, BorderLayout.NORTH);
			p.add(inforPanel);
			gridPanel.add(p);
		}
		panel.add(new JScrollPane(gridPanel));
		mainPanel.add(panel);
	}
	public static void main(String[] args) {
		new Main();
	}
}
