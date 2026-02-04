package main;

import javax.swing.*;


import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import utils.*;

public class GameInfor extends CFrame{
	Font font = new Font("맑은 고딕", 1, 15);
	JPanel borderPanel = new JPanel(new BorderLayout(90,90)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0, 5, 5, 5));
	}};
	int g_no;
	Data game;
	
	JButton all = new JButtonC("전체");
	JButton like = new JButtonC("긍정적 댓글");
	JButton die = new JButtonC("부정적 댓글");
	JButton basket = new JButtonC("장바구니");
	JButton buy = new JButtonC("구매");
	
	JPanel chagePanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
	}};
	JPanel reviewPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
	}};
	JPanel reviewGridPanel = new JPanel(new GridLayout(0, 1, 2, 2)) {{
		setBackground(Color.white);
	}};
	String but = "--";
	
	JButton insert = new JButtonC("등록");
	JTextArea reviewTextArea = new JTextArea() {{
		setBorder(createLineBorder(Color.black));
		setFont(font.deriveFont(20f));
	}};
	int star = -1;
	List<JLabel> starList = new ArrayList<>();
	Data user = UserU.getUser();
	public GameInfor(int g_no) {
		if(LocalDate.parse(user.get(5).toString()).isAfter(LocalDate.now().minusYears(19))) {
			getter.mg("이 게임은 19세 미만은 구매할 수 없습니다.", JOptionPane.ERROR_MESSAGE);
			new Serch();
			dispose();
			return;
		}
		
		ToolTipManager m = ToolTipManager.sharedInstance();
		m.setEnabled(true);
		m.setInitialDelay(100);
		
		this.g_no = g_no;
		game = Connections.select("SELECT gameinformation.*, avg(c_star) as z, category.ca_name FROM game_site.gameinformation \r\n"
				+ "right join comments on comments.g_no = gameinformation.g_no right join category on category.ca_no = gameinformation.ca_no\r\n"
				+ "where gameinformation.g_no = ? group by gameinformation.g_no", g_no).get(0);
		setReviewPanel();
		setReviews();
		UIManager.put("Label.font", font);
		setSouthInfor();
		setGamePanel();
		add(borderPanel);
		setAction();
		setFrame("게임 정보", 800, 510);
	}

	private void setAction() {
		ActionListener ac = e-> {
			if(e.getSource() == all) but = "--";
			if(e.getSource() == like) but = ">=";
			if(e.getSource() == die) but = "<";
			setReviews();
		};
		all.addActionListener(ac);
		like.addActionListener(ac);
		die.addActionListener(ac);
		
		buy.addActionListener(e->{
			List<Data> isBuyGame = Connections.select("SELECT * FROM game_site.purchasegame where u_no = ? and g_no = ?;", user.get(0), game.get(0));
			if(isBuyGame.isEmpty()) {
				getter.mg("구매가 완료되었습니다.", JOptionPane.INFORMATION_MESSAGE);
				user.set(6, user.getInt(6) - game.getInt(2));
				Connections.update("update user set u_price = ? where u_no = ?;", user.get(6), user.get(0));
				Connections.update("insert into purchasegame values(0, ?, ?, ?)", user.get(0), game.get(0), LocalDate.now());
				UserU.setUser(user);
				
			}else {
				getter.mg("이미 구매한 게임입니다.", JOptionPane.ERROR_MESSAGE);
			}
			if(user.getInt(6) < game.getInt(2) && isBuyGame.isEmpty()) {
				int result = JOptionPane.showConfirmDialog(null, "포인트가 부족합니다. 충전하시겠습니다?.", "충전 필요", JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					//new 충전하기
					dispose();
				}
				return;
			}
			setReviewInsertPanel();
			buy.setEnabled(false);
			basket.setEnabled(false);
			revalidate();
			repaint();
		});
		
		basket.addActionListener(e->{
			List<Data> list = Connections.select("SELECT * FROM game_site.shoppingbasket where u_no = ? and g_no = ?;", user.get(0), game.get(0));
			if(list.isEmpty()) {
				getter.mg("장바구니에 추가되었습니다.", JOptionPane.INFORMATION_MESSAGE);
				Connections.update("insert into shoppingbasket values(0, ?, ?)", game.get(0), user.get(0));
				return;
			}else {
				getter.mg("이미 장바구니에 있는 게임입니다.", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
	
	private void setReviews() {
		reviewGridPanel.removeAll();
		String qurey = "SELECT user.u_no, u_id, c_content, c_no FROM game_site.comments\r\n"
				+ " join user on user.u_no = comments.u_no\r\n"
				+ " where g_no = ? and c_star " + but + " 3";
		List<JPanel> 댓list = new ArrayList<>();
		List<Data> list = Connections.select(qurey, game.get(0));
		reviewGridPanel.setLayout(new GridLayout(list.size() <= 1 ? 2 : list.size(), 1, 3, 3));
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			JPanel panel = new JPanel(new BorderLayout(5,5));
			panel.setBackground(Color.white);
			panel.setBorder(createLineBorder(Color.black));
			panel.setPreferredSize(new Dimension(0, 85));
			panel.add(new JLabel(getter.getImage("userimage/" + data.getInt(0) + ".jpg", 85, 85)) {{
				setPreferredSize(new Dimension(85, 85));
			}}, BorderLayout.WEST);
			
			JPanel textPanel = new JPanel(new GridLayout(0, 1, 4, 4));
			textPanel.setBackground(Color.white);
			textPanel.setBorder(createEmptyBorder(0, 0, 50, 0));
			
			String text = data.get(2).toString();
			if(data.get(2).toString().length() > 9) {
				text = text.substring(0, 9) + "...";
			}
			textPanel.add(new JLabel("구매 아이디 : " + data.get(1)) {{
				if(data.getInt(0) == user.getInt(0))
					setForeground(Color.red);
				setFont(font.deriveFont(1, 14));
			}});
			textPanel.add(new JLabel("댓글 : " + text) {{
				setFont(font.deriveFont(1, 14));
			}});
			
			댓list.add(textPanel);
			panel.add(textPanel);
			panel.setToolTipText(data.get(2).toString());
			reviewGridPanel.add(panel);
		}
		
		for(int i = 0; i < 댓list.size(); i++) {
			Data data = list.get(i);
			댓list.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(data.getInt(0) == user.getInt(0)) {
						int re = JOptionPane.showConfirmDialog(null, "댓글을 삭제하시겠습니까?", "댓글 삭제", JOptionPane.YES_NO_OPTION);
						if(re == JOptionPane.YES_OPTION) {
							getter.mg("삭제되었습니다.", JOptionPane.INFORMATION_MESSAGE);
							Connections.update("delete from comments where c_no = ?", data.get(3));
							setReviews();
						}
						return;
					}
					dispose();
					new MyHome(data.getInt(0));
				}
			});
		}
		revalidate();
		repaint();
	}
	
	private void setReviewPanel() {
		JPanel butPanel = new JPanel(new GridLayout(0, 3, 2, 2));
		butPanel.setBackground(Color.white);
		butPanel.add(all);
		butPanel.add(like);
		butPanel.add(die);
		
		reviewPanel.add(new JScrollPane(reviewGridPanel) {{
			setBackground(Color.white);
			setPreferredSize(new Dimension(300, 150));
		}});
		reviewPanel.add(butPanel, BorderLayout.SOUTH);
		
		borderPanel.add(reviewPanel, BorderLayout.EAST);
	}

	private void setGamePanel() {
		JPanel p = new JPanel(new BorderLayout(10, 10));
		p.setBackground(Color.white);
		p.setBorder(createEmptyBorder(0, 0, 10, 0));
		
		JPanel mainPanel = new JPanel(new BorderLayout(7,7));
		mainPanel.setBackground(Color.white);
		
		JLabel image = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/gameimage/" + game.getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				if(game.getInt(5) == 1) {
					g.drawImage(new ImageIcon("datafiles/19세 마크.png").getImage(), 2, 2, 25, 25, null);
				}
			}
		};
		image.setBorder(createLineBorder(Color.black));
		mainPanel.add(image);
		mainPanel.add(new JPanel(new GridLayout(0, 2, 3, 3)) {{
			setBackground(Color.white);
			add(basket);
			add(buy);
		}}, BorderLayout.SOUTH);
		
		JPanel inforPanel = new JPanel(new GridLayout(6, 1, 5, 5));
		inforPanel.setBorder(createEmptyBorder(0,0,10,0));
		inforPanel.setBackground(Color.white);
		
		inforPanel.add(new JLabel("게임이름 : " + game.get(1).toString()));
		inforPanel.add(new JLabel("게임 포인트 : " + game.getInt(2)));
		inforPanel.add(new JLabel("출시일 : " + game.get(3)));
		inforPanel.add(new JLabel("카테고리 : " + game.get(game.size() - 1)));
		inforPanel.add(new JLabel("게임 등급 : " + (game.getInt(5) == 1 ? "19세" : "전체")));
		inforPanel.add(new JLabel("평점 : " + game.get(game.size() - 2).toString().substring(0, 3)));
		
		p.add(inforPanel, BorderLayout.EAST);
		p.add(mainPanel);
		borderPanel.add(p);
	}

	private void setSouthInfor() {
		JTextArea infor = new JTextArea(game.get(6).toString());
		infor.setFocusable(false);
		infor.setBorder(createLineBorder(Color.black));
		infor.setPreferredSize(new Dimension(0, 200));
		infor.setLineWrap(true);
		infor.setFont(font);
		
		chagePanel.add(infor);
		borderPanel.add(chagePanel, BorderLayout.SOUTH);
	}

	private void setReviewInsertPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(Color.white);
		panel.setPreferredSize(new Dimension(250, 0));
		
		JPanel starPanel = new JPanel(new GridLayout(0, 5, 10, 10));
		starPanel.setBackground(Color.white);
		starPanel.setPreferredSize(new Dimension(0, 30));
		
		for(int i = 0; i < 5; i++) {
			JLabel starImage = new JLabel(getter.getImage("별공백.png", 25, 25));
			starList.add(starImage);
			starPanel.add(starImage);
		}
		
		panel.add(starPanel, BorderLayout.NORTH);
		panel.add(reviewTextArea);
		panel.add(insert, BorderLayout.SOUTH);
		
		setReviewAction();
		
		chagePanel.add(panel, BorderLayout.WEST);
	}
	private void setReviewAction() {
		for (int i = 0; i < starList.size(); i++) {
			final int index = i;
			JLabel l = starList.get(i);
			l.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					star = index;
					for(int i = index; i < starList.size(); i++)
						starList.get(i).setIcon(getter.getImage("별공백.png", 25, 25));
					for(int i = 0; i <= index; i++) 
						starList.get(i).setIcon(getter.getImage("별선택.png", 25, 25));
				}
			});
		}
		
		insert.addActionListener(e->{
			if(reviewTextArea.getText().isEmpty() || star == -1) {
				getter.mg("빈칸이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getter.mg("댓글이 등록되었습니다.", JOptionPane.INFORMATION_MESSAGE);
			Connections.update("insert into comments values(0, ?, ?, ?, ?);", user.get(0), game.get(0), star + 1, reviewTextArea.getText());
			star = -1;
			for(int i = 0; i < starList.size(); i++)
				starList.get(i).setIcon(getter.getImage("별공백.png", 25, 25));
			reviewTextArea.setText("");
			setReviews();
		});
	}
	
	public static void main(String[] args) {
		new GameInfor(5);
	}
}
