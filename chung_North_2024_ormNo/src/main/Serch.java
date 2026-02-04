package main;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

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
import java.util.stream.Collectors;

import utils.*;

public class Serch extends CFrame {
	Font font = new Font("맑은 고딕", 1, 30);
	JComboBox<Object> category = new JComboBox<Object>() {{
		for (Data data : Connections.select("SELECT ca_name FROM category;"))
			addItem(data.get(0));
	}};
	JComboBox<Object> order = new JComboBox<Object>("오름차순 / 내림차순".split(" / "));

	JTextField serch = new JTextField() {{
		setPreferredSize(new Dimension(175, 27));
	}};
	JLabel serchLabel = new JLabel(getter.getImage("검색.png", 25, 25));
	
	JPanel mainPanel = new JPanel(new BorderLayout(5, 5)) {{
		setBackground(Color.white);
	}};
	JPanel gamePanel = new JPanel(new GridLayout(0, 1, 3, 3)) {{
		setBackground(Color.white);
	}};

	List<Boolean> bool = new ArrayList<>();
	List<JPanel> imgList = new ArrayList<>();
	boolean mouseIn = false;
	public Serch() {
		add(new JLabel("검색", JLabel.CENTER) {
			{
				setFont(font);
				setBorder(createEmptyBorder(5, 0, 5, 0));
			}
		}, BorderLayout.NORTH);

		UIManager.put("Label.font", font.deriveFont(15f));
		setMainPanel();
		
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.setBackground(Color.white);

		southPanel.add(order);
		add(southPanel, BorderLayout.SOUTH);
		setAction();
		setFrame("검색", 475, 400);
	}

	private void setMainPanel() {
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBorder(createEmptyBorder(0, 5, 0, 5));
		northPanel.setBackground(Color.white);
		
		JPanel wPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		wPanel.setBackground(Color.white);
		wPanel.add(new JLabel("카테고리 :"));
		wPanel.add(category);
		
		JPanel ePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ePanel.setBackground(Color.white);
		ePanel.add(new JLabel("검색 :") {{
			setFont(font.deriveFont(14f));
		}});
		ePanel.add(serch);
		ePanel.add(serchLabel);
		
		setGamePanel();
		
		northPanel.add(wPanel, BorderLayout.WEST);
		northPanel.add(ePanel, BorderLayout.EAST);
		
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(gamePanel) {{
			getVerticalScrollBar().setUnitIncrement(20);
		}});
		
		add(mainPanel);
	}

	private void setGamePanel() {
		gamePanel.removeAll();
		bool.clear();
		imgList.clear();
		String query = "SELECT gameinformation.*, avg(c_star) as z, category.ca_name FROM game_site.gameinformation \r\n"
				+ "right join comments on comments.g_no = gameinformation.g_no right join category on category.ca_no = gameinformation.ca_no\r\n"
				+ "where category.ca_no between ? and ? and lower(g_name) like ? group by gameinformation.g_no order by g_no " + (order.getSelectedIndex() == 0 ? "asc" : "desc");
		String serchText = "%" + serch.getText() + "%";
		List<Data> list = Connections.select(query, 1, 7, serchText);
		gamePanel.setLayout(new GridLayout(list.size() == 1 ? 2 : list.size(), 1, 3, 3));
		
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			bool.add(false);
			Data data = list.get(i);
			
			JPanel p = new JPanel(new BorderLayout(10,10));
			p.setBackground(Color.white);
			p.setBorder(createLineBorder(Color.black));
			p.setPreferredSize(new Dimension(0, 150));
			
			JPanel imgPanel = new JPanel(null);
			imgPanel.setBackground(Color.white);
			imgPanel.setPreferredSize(new Dimension(150, 150));
			
			JLabel img = new JLabel("") {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/gameimage/" + data.getInt(0) + ".jpg").getImage(), 0, 0, 150, 150, null);
					if(data.getInt(5) == 1) {
						g.drawImage(new ImageIcon("datafiles/19세 마크.png").getImage(), getWidth() - 30, 5, 25, 25, null);
					}
					if(bool.get(index)) {
						g.drawImage(new ImageIcon("datafiles/담기.png").getImage(), 0, 0, 32, 32, null);
					}
				}
			};
			img.setBounds(0, 0, 150, 150);
			
			imgPanel.add(img);
			imgList.add(imgPanel);
			
			JPanel inforPanel = new JPanel(new GridLayout(0, 1, 5 ,5));
			inforPanel.setBackground(Color.white);
			inforPanel.setBorder(createEmptyBorder(0, 0, 50, 0));
			
			inforPanel.add(new JLabel("게임명 : " + data.get(1)));
			inforPanel.add(new JLabel("카테고리 : " + data.get(data.size() - 1)));
			inforPanel.add(new JLabel("포인트 : " + data.get(2)));
			inforPanel.add(new JLabel("평점 : " + data.get(data.size() - 2).toString().substring(0, 3)));
			
			p.add(inforPanel);
			p.add(imgPanel, BorderLayout.WEST);
			gamePanel.add(p);
		}
		
		for(int i = 0; i < imgList.size(); i++) {
			final int index = i;
			Data data = list.get(index);
			imgList.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					setBool(index, true);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					setBool(index, false);
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					if(isInField(e.getPoint())) {
						if(data.getInt(5) == 1) {
							getter.mg("19세 게임은 검색폼에서 담을 수 없습니다.", JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						int uno = UserU.getUser().getInt(0), gno = data.getInt(0);
						List<Data> inBasket = Connections.select("SELECT * FROM game_site.shoppingbasket\r\n"
								+ " where u_no = ? and g_no = ?;", uno, gno);
						if(!inBasket.isEmpty()) {
							getter.mg("장바구니에 있는 게임입니다.", JOptionPane.ERROR_MESSAGE);
							return;
						}
						List<Data> buyGame = Connections.select("SELECT * FROM game_site.purchasegame \r\n"
								+ "where u_no = ? and g_no = ?;", uno, gno);
						if(!buyGame.isEmpty()) {
							getter.mg("이미 구입한 게임입니다.", JOptionPane.ERROR_MESSAGE);
							return;
						}
						getter.mg("장바구니에 담겼습니다.", JOptionPane.INFORMATION_MESSAGE);
						Connections.update("insert into shoppingbasket values(0, ?, ?)", gno, uno);
					}
					dispose();
					new GameInfor(data.getInt(0));
				}
			});
		}
		
		revalidate();
		repaint();
	}

	private void setAction() {
		serchLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setGamePanel();
				if(gamePanel.getComponentCount() == 0) {
					getter.mg("검색결과가 없습니다.", JOptionPane.ERROR_MESSAGE);
					serch.setText("");
				}
			}
		});
	}
	
	private boolean isInField(Point p) {
		return p.x >= 0 && p.x <= 32 && p.y >= 0 && p.y <= 32;
	}
	
	private void setBool(int index, boolean bools) {
		bool.set(index, bools);
		revalidate();
		repaint();
	}
	public static void main(String[] args) {
		new Serch();
	}
}
