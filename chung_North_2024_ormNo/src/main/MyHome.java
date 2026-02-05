package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import utils.*;

public class MyHome extends CFrame{
	Font font = new Font("맑은 고딕", 1, 30);
	JButton inforChange = new JButtonC("정보수정") {{
		setPreferredSize(new Dimension(100, 50));
	}};
	JButton pointBuy = new JButtonC("포인트 충전!") {{
		setPreferredSize(new Dimension(125, 35));
		setBackground(Color.red);
	}};
	JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
	}};
	
	List<JPanel> buyGames = new ArrayList<>();
	Data data;
	int uno;
	public MyHome(int uno){
		this.uno = uno;
		setNorthPanel();
		UIManager.put("Label.font", font.deriveFont(20f));
		setUserInforPanel();//여기서 data setting
		if(data.getInt(7) == 0) {
			UIManager.put("Label.font", font.deriveFont(14f));
			setSouthPanel();
		}else {
			System.out.println("비공개");
		}
		add(mainPanel);
		setAction();
		setFrame("마이홈", 700, 500);
	}
	
	private void setAction() {
		inforChange.addActionListener(e->{
			new InforChange();
			dispose();
		});
		pointBuy.addActionListener(e->{
			dispose();
		});
	}
	private void setSouthPanel() {
		JPanel southPanel = new JPanel(new GridLayout(0, 2, 5, 5));
		southPanel.setBackground(Color.white);
		
		setWPanel(southPanel);
		setEPanel(southPanel);
		
		mainPanel.add(southPanel);
	}
	
	private void setWPanel(JPanel southPanel) {
		JPanel panel = new JPanel(new BorderLayout(5,5));
		panel.setBackground(Color.white);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		gridPanel.setBackground(Color.white);
		
		List<Data> list = Connections.select("SELECT gameinformation.g_no, g_name, g_price, ca_name FROM game_site.purchasegame \r\n"
				+ "join gameinformation on gameinformation.g_no = purchasegame.g_no join category on category.ca_no = gameinformation.ca_no\r\n"
				+ "where u_no = ?", data.get(0));
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			JPanel p = new JPanel(new BorderLayout(10, 10));
			p.setBackground(Color.white);
			p.setBorder(createLineBorder(Color.black));
			p.setPreferredSize(new Dimension(0, 100));
			
			p.add(new JLabel(getter.getImage("gameimage/" + data.getInt(0) + ".jpg", 100, 100)), BorderLayout.WEST);
			
			JPanel inforPanel = new JPanel(new GridLayout(3, 1));
			inforPanel.setBackground(Color.white);
			inforPanel.setBorder(createEmptyBorder(0, 0, 40, 0));
			
			inforPanel.add(new JLabel("이름 : " + data.get(1)));
			inforPanel.add(new JLabel("포인트 : " + getter.df.format(data.getInt(2)) + "포인트") {{ setForeground(Color.red); }});
			inforPanel.add(new JLabel("카테고리 : " + data.get(3)));
			
			p.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					dispose();
					new GameInfor(data.getInt(0));
				}
			});
			
			p.add(inforPanel);
			gridPanel.add(p);
		}
		
		panel.add(new JLabel("보유한 게임") {{
			setFont(font.deriveFont(18f));
		}}, BorderLayout.NORTH);
		panel.add(new JScrollPane(gridPanel));
		southPanel.add(panel);
	}

	List<JPanel> panels = new ArrayList<>();
	List<Boolean> bool = new ArrayList<>();
	private void setEPanel(JPanel southPanel) {
		if(southPanel.getComponentCount() == 2) {
			southPanel.remove(1);
		}
		ToolTipManager m = ToolTipManager.sharedInstance();
		m.setEnabled(true);
		m.setInitialDelay(100);
		
		//SELECT c_no, c_content FROM game_site.comments where u_no = 1;
		JPanel panel = new JPanel(new BorderLayout(5,5));
		panel.setBackground(Color.white);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		gridPanel.setBackground(Color.white);
		
		List<Data> list = Connections.select("SELECT c_no, c_content, g_no FROM game_site.comments where u_no = ?;", data.get(0));
		for(int i = 0; i < list.size(); i++) {
			bool.add(false);
			Data data = list.get(i);
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(10, 10));
			p.setToolTipText(data.get(1).toString());
			p.setBackground(Color.white);
			p.setBorder(createLineBorder(Color.black));
			p.setPreferredSize(new Dimension(0, 80));
			
			JPanel imgPanel = new JPanel(null);
			imgPanel.setBackground(Color.white);
			imgPanel.setPreferredSize(new Dimension(80, 80));
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.clearRect(0, 0, getWidth(), getHeight());
					g.drawImage(new ImageIcon("datafiles/gameimage/" + data.getInt(2) + ".jpg").getImage(), 0, 0, 80, 80, null);
					if(bool.get(index)) {
						g.drawImage(new ImageIcon("datafiles/X.png").getImage(),	0, 0, 15, 15, null);
					}
					
				}
			};
			img.setBounds(0, 0, 80, 80);
			imgPanel.add(img);
			p.add(imgPanel, BorderLayout.WEST);
			
			JPanel inforPanel = new JPanel(new GridLayout(3, 1));
			inforPanel.setBackground(Color.white);
			inforPanel.setBorder(createEmptyBorder(0, 0, 20, 0));
			
			String str = (data.get(1).toString().length() >= 15 ?  data.get(1).toString().substring(0, 15) + "..." : data.get(1).toString());
			
			inforPanel.add(new JLabel("댓글 : "));
			inforPanel.add(new JLabel("-> " + str) {{
				setForeground(Color.red);
			}});
			inforPanel.add(new JLabel("작성자 : " + this.data.get(2)));
			
			p.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(isInPoint(e.getPoint())) {
						Connections.update("delete from comments where c_no = ?", data.get(0));
						setEPanel(southPanel);
						return;
					}
					dispose();
					new GameInfor(data.getInt(2));
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					setBool(index, true);
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					setBool(index, false);
				}
			});
			
			p.add(inforPanel);
			gridPanel.add(p);
		}
		
		panel.add(new JLabel("댓글") {{
			setFont(font.deriveFont(18f));
		}}, BorderLayout.NORTH);
		panel.add(new JScrollPane(gridPanel));
		
		southPanel.add(panel);
		revalidate();
		repaint();
	}

	private boolean isInPoint(Point p) {
		if(data.getInt(0) != UserU.getUser().getInt(0)) return false;
		return p.x >= 0 && p.x <= 15 && p.y >= 0 && p.y <= 15;
	}
	private void setBool(int i, boolean b) {
		if(data.getInt(0) != UserU.getUser().getInt(0)) return;
		bool.set(i, b);
		revalidate();
		repaint();
	}
	private void setUserInforPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(Color.white);
		panel.setBorder(createLineBorder(Color.black));
		
		panel.add(new JLabel(getter.getImage("userimage/" + data.get(0) + ".jpg", 200, 200)), BorderLayout.WEST);
		
		JPanel gridPanel = new JPanel(new GridLayout(3, 1));
		gridPanel.setBackground(Color.white);
		gridPanel.setBorder(createEmptyBorder(0,0,90, 0));
		gridPanel.setPreferredSize(new Dimension(200, 200));
		
		JPanel pointPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pointPanel.setBackground(Color.white);
		pointPanel.add(new JLabel("포인트 : " + data.get(6)) {{
			setPreferredSize(new Dimension(200, 40));
		}});
		if(data.getInt(0) == UserU.getUser().getInt(0)) {
			pointPanel.add(pointBuy);
		}
		
		gridPanel.add(new JLabel("이름 : " + data.get(1)));
		gridPanel.add(new JLabel("생년월일 : " + data.get(5)));
		gridPanel.add(pointPanel);
		
		
		panel.add(gridPanel);
		mainPanel.add(panel, BorderLayout.NORTH);
	}

	private void setNorthPanel() {
		JPanel northPanel = new JPanel(new BorderLayout(0, 0));
		northPanel.setBackground(Color.white);
		northPanel.setBorder(createEmptyBorder(5, 0, 10, 0));
		
		JLabel l = new JLabel("", JLabel.CENTER);
		l.setFont(font);
		if(uno != UserU.getUser().getInt(0)) {
			data = Connections.select("select * from user where u_no = ?", uno).get(0);
			l.setText(data.get(2).toString() + "홈");
		}else {
			l.setText("마이홈");
			data = UserU.getUser();
			northPanel.add(inforChange, BorderLayout.EAST);
		}
		
		northPanel.add(l);
		
		add(northPanel, BorderLayout.NORTH);		
	}

	public static void main(String[] args) {
		new MyHome(1);
	}
}
