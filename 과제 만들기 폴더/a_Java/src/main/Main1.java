package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

import builder.SimpleJarBuilder;
import utils.CFrame;
import utils.Connections;
import utils.Data;
import utils.User;
import utils.getter;

public class Main1 extends CFrame{
	int w, h;
	int w2, h2;
	int fromNumber = 0;
	
	JPanel loginInfor = new JPanel(new BorderLayout(3, 3)) {{
		setBackground(getter.color);
	}};
	JPanel TitlePanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(getter.color);
	}};
	
	JTextField tf = new JTextField();
	JButton serch = new JButton(getter.getImage("datafiles/serch.png", 20, 20)) {{
		setBackground(new Color(100, 125, 200));
		setPreferredSize(new Dimension(30, 25));
	}};
	JButton shoppingBasket = new JButton("장바구니") {{
		setBackground(Color.cyan);
		setForeground(Color.black);
	}};
	JLabel login = new JLabel("로그인") {{
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if(e.getClickCount() == 2) {
					getter.err("더블 클릭");
				}
				if(User.getUser() != null) {
					Connections.update("delete from loginuser");
					getter.infor("로그아웃 되었습니다.");
					User.setUser(null);
					login.setText("로그인");
					login.setIcon(null);
					setMainPanel();
					return;
				}
				if(fromNumber != 1)
					setLoginPanel();
			};
		});
		setForeground(Color.white);
	}};
	
	JLabel before = new JLabel("<") {{
		setEnabled(false);
		setFont(new Font("맑은 고딕", 1, 20));
		setForeground(Color.white);
		setPreferredSize(new Dimension(50, 30));
	}};
	
	JPanel mainPanel = new JPanel(new BorderLayout()) {{
		setBackground(getter.color);
	}};
	Stack<Integer> froms = new Stack<>();
	List<JLabel> bestGame = new ArrayList<>();
	
	Thread thread, thread2;
	public Main1() {
		loginInfor.add(login);
		setIconImage(new ImageIcon("datafiles/logo.jpg").getImage());
		setResizable(false);
		setTitlePanel();
		borderPanel.setBackground(getter.color);
		
		setMainPanel();
		borderPanel.add(TitlePanel, BorderLayout.NORTH);
		borderPanel.add(mainPanel);
		setFrameCd("Nexus", 1200, 800, () -> {
			if(thread != null) 
				thread.interrupt();
		});
	}

	private void setLoginPanel() {
		froms.push(fromNumber);
		fromNumber = 1;// 1200, 680 -> 600, 350
		before.setEnabled(true);
		mainPanel.removeAll();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(165, 300, 165, 300));
		
		Color color = getter.color.darker();
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(color);
		
		
		LoginPanel loginPanel = new LoginPanel(this, null);
		
		panel.add(loginPanel);
		mainPanel.add(panel);
		revalidate();
		repaint();
	}
	
	void setMainPanel() {
		if(loginInfor.getComponentCount() > 1) {
			loginInfor.remove(1);
		}
		if(User.getUser() != null) {
			login.setText(User.getUser().getString(1));
			loginInfor.add(new JLabel(getter.getImage("datafiles/user/" + User.getUser().getInt(4) + ".png", 20, 20)), BorderLayout.EAST);
		}
		fromNumber = 0;
		froms.clear();
		before.setEnabled(false);
		mainPanel.removeAll();
		mainPanel.setBorder(null);
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(getter.color);
		
		JMenuBar mb = new JMenuBar();
		mb.setBackground(new Color(75, 75, 225));
		
		JMenu m1 = new JMenu("카테고리");
		m1.setForeground(Color.white);
		mb.add(m1);
		
		JMenu m2 = new JMenu("가격");
		m2.setForeground(Color.white);
		mb.add(m2);
		
		JButton news = new JButton("뉴스");
		news.setBackground(Color.white);
		mb.add(news);
		
		List<Data> list = Connections.select("select * from category");
		for(Data data : list) {
			JMenuItem mi = new JMenuItem(data.getString(1));
			mi.addActionListener(e -> {
				System.out.println(data.getInt(0));
			});
			m1.add(mi);
		}
		//메뉴바에 메뉴 추가
		
		mb.add(new JLabel() {{
			setPreferredSize(new Dimension(User.getUser() == null ? 275 : 225, 10));
		}});
		mb.add(tf);
		mb.add(serch);
		if(User.getUser() != null)
			mb.add(shoppingBasket);
		
		
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 200));
		northPanel.setBackground(getter.color);
		northPanel.add(mb);
		
		
		JPanel gamePanel = new JPanel(new BorderLayout(10, 10));
		gamePanel.setBackground(Color.white);
		
		setGamePanel(gamePanel);
		
		panel.add(northPanel, BorderLayout.NORTH);
		panel.add(gamePanel);
		mainPanel.add(panel);
		revalidate();
		repaint();
	}

	int scValue = 0, scValuez = 1;
	int scPM = 1;
	private void setGamePanel(JPanel gamePanel) {
		Font font = new Font("맑은 고딕", 1, 25);
		JPanel panel = new JPanel(new BorderLayout());
		
		JPanel bestTop6Game = new JPanel(new BorderLayout(0, 10));
		bestTop6Game.setBackground(getter.color);
		bestTop6Game.setBorder(BorderFactory.createEmptyBorder(10, 180, 10, 180));
		
		JLabel left = new JLabel("<");
		left.setPreferredSize(new Dimension(20, 0));
		left.setFont(font);
		left.setForeground(Color.white);
		JLabel right = new JLabel(">");
		right.setPreferredSize(new Dimension(20, 0));
		right.setFont(font);
		right.setForeground(Color.white);
		
		
		bestTop6Game.add(new JLabel("인기 게임") {{
			setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
			setForeground(Color.white);
		}}, BorderLayout.NORTH);
		bestTop6Game.add(left, BorderLayout.WEST);
		bestTop6Game.add(right, BorderLayout.EAST);
		
		JPanel gameInforPanel = new JPanel(new GridLayout());
		gameInforPanel.setBackground(getter.color);
		
		List<Data> list = Connections.select("SELECT *, count(gno) as c FROM game.buygame\r\n"
				+ "group by gno order by c desc, gno limit 6");
		List<Data> categorys = Connections.select("select * from category");//.stream().map(c -> c.getString(0)).toArray(String[]::new);
		Map<Integer, String> map = new HashMap<>();
		for(Data data : categorys) {
			map.put(data.getInt(0), data.getString(1));
		}
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			Data game = Connections.select("select * from game where gno = ?", data.get(1)).get(0);
			
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(getter.color);
			
			JLabel image = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/games/" + data.getInt(1) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			image.setPreferredSize(new Dimension(600, 250));
			
			String result = categorys.stream()
	                .filter(c -> Arrays.asList(game.getString(2).split(",")).contains(String.valueOf(c.get(0))))
	                .map(c -> (String) c.get(1))
	                .collect(Collectors.joining(", "));
			JLabel text = new JLabel("<html>" + game.getString(1) + "<br><br>카테고리 : " + result + "</html>", JLabel.CENTER);
			text.setForeground(Color.white);
			text.setPreferredSize(new Dimension(200, 0));
			
			p.add(image);
			p.add(text, BorderLayout.EAST);
			gameInforPanel.add(p);
		}
		
		JScrollPane sc1 = new JScrollPane(gameInforPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			setBorder(null);
			setPreferredSize(new Dimension(0, 250));
		}};
		bestTop6Game.add(sc1);
		
		Runnable run2 = () -> {
			try {
				int saveSCValue = 0;
				JScrollBar sb = sc1.getHorizontalScrollBar();
				while(!thread2.interrupted()) {
					Thread.sleep(1000);
					System.out.println("sdfds");
					for(int i = 0; i < 4000; i++) {
						if(i != 0 && i % 800 == 0) {
							scValue += 800 * scValuez;
							Thread.sleep(1000);
						}
						saveSCValue = sb.getValue();
						sb.setValue(sb.getValue() + scValuez);
						if(saveSCValue == sb.getValue()) {
							break;
						}
						Thread.sleep(2);
					}
					scValuez *= -1;
				}
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			} finally {
				thread2.interrupt();
			}
		};
		
		MouseAdapter mc = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(thread != null && thread.isAlive())
					return;
				if(e.getSource() == right)
					scValue += 800;
				else if(e.getSource() == left)
					scValue -= 800;
				if(scValue > 4800)
					scValue = 4800;
				if(scValue < 0)
					scValue = 0;
				thread = new Thread(() -> {
					try {
						JScrollBar sb = sc1.getHorizontalScrollBar();
						while(sb.getValue() != scValue) {
							sb.setValue(sb.getValue() + (e.getSource() == right ? 1 : -1));
							Thread.sleep(1);
						}
					} catch (Exception e2) {
						System.out.println(e2.getMessage());
					} finally {
						Thread.currentThread().interrupt();
					}
				});
				thread.start();
				System.out.println(scValue);
			}
		};
		
		right.addMouseListener(mc);
		left.addMouseListener(mc);
		
		JPanel gameIndex = new JPanel(new GridLayout(1, 6, 15, 15));
		gameIndex.setBackground(getter.color);
		gameIndex.setPreferredSize(new Dimension(0, 25));
		gameIndex.setBorder(BorderFactory.createEmptyBorder(5, 300, 5, 300));
		for(int i  = 0; i < 6; i++) {
			int index = i;
			JButton b = new JButton();
			b.addActionListener(e -> {
				scValue = index * 800;
				sc1.getHorizontalScrollBar().setValue(scValue);
			});
			gameIndex.add(b);
		}
		bestTop6Game.add(gameIndex, BorderLayout.SOUTH);
		panel.add(bestTop6Game, BorderLayout.NORTH);
		//인기 게임 판넬 
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBackground(getter.color);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 200));
		
		if(User.getUser() != null) {
			String[] categoryz = Connections.select("SELECT cno FROM game.buygame \r\n"
					+ "join game on game.gno = buygame.gno\r\n"
					+ "where uno = 1 order by date desc limit 1;").get(0).getString(0).split(",");
			List<Data> userBuyGame = new ArrayList<>();
			for(String s : categoryz) {
				 userBuyGame.addAll(Connections.select("SELECT game.*, count(game.gno) as c FROM game.buygame\r\n"
						+ "join game on game.gno = buygame.gno\r\n"
						+ "where find_in_set(?, cno)\r\n"
						+ "group by game.gno order by c desc, game.gno limit 5", s));
			}
			userBuyGame.sort((a, b) -> ((Integer) a.getInt(0)).compareTo((Integer) b.getInt(0)));
			if(userBuyGame.size() > 5) {
				userBuyGame.subList(5, userBuyGame.size()).clear();
			}
			
			JPanel userRecommendGame = new JPanel(new BorderLayout(10, 10));
			userRecommendGame.add(new JLabel(User.getUser().getString(1) + "님의 추천 게임") {{
				setForeground(Color.white);
			}}, BorderLayout.NORTH);
			userRecommendGame.setBackground(getter.color);
			
			JPanel userRecommendGames = new JPanel(new GridLayout(0, 5, 10, 10));
			userRecommendGames.setBackground(getter.color);
			userRecommendGames.setPreferredSize(new Dimension(0, 75));
			for(int i = 0; i < userBuyGame.size(); i++) {
				Data data = userBuyGame.get(i);
				JLabel img = new JLabel() {
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.drawImage(new ImageIcon("datafiles/games/" + data.getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
					};
				};
				JLabel gameName = new JLabel(data.getString(1));
				gameName.setForeground(Color.white);
				
				userRecommendGames.add(new JPanel(new BorderLayout()) {{
					add(img);
					add(gameName, BorderLayout.SOUTH);
					setBackground(Color.black);
					setFont(new Font("맑은 고딕", 0, 10));
					userRecommendGames.setPreferredSize(new Dimension(0, 75 + getFontMetrics(getFont()).getHeight()));
				}});
				//System.out.println((800 - 40) / 5); 이거로 하나의 크기를 알아내서 1/2 한게 높이
			}
			
			userRecommendGame.add(userRecommendGames);
			mainPanel.add(userRecommendGame, BorderLayout.NORTH);
		}
		
		JPanel gsPanel = new JPanel(new BorderLayout(10, 10));
		gsPanel.setBackground(getter.color);
		
		
		JButton m1 = new JButton("인기 게임");
		m1.setFocusPainted(false);
		m1.setBackground(Color.white);
		JButton m2 = new JButton("무료 게임");
		m2.setBackground(Color.white);
		m2.setFocusPainted(false);
		
		JMenuBar mb = new JMenuBar() {{
			setBackground(getter.color);
			setBorder(null);//JMenuBar 기본 Border는 밑에 줄있음
			add(m1);
			add(m2);
		}};
		gsPanel.add(mb, BorderLayout.NORTH);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 1, 10, 10));
		gridPanel.setBackground(Color.white);
		
		
		mainPanel.add(gsPanel);
		panel.add(mainPanel);
		
		JScrollPane sc2 = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sc2.addMouseWheelListener(e -> {
			sc2.getVerticalScrollBar().setValue(sc2.getVerticalScrollBar().getValue() + (e.getWheelRotation() * 20));
		});
		sc2.setBorder(null);
		gamePanel.add(sc2);
		revalidate();
		repaint();
		
		
	}
	
	public void setTitlePanel() {
		TitlePanel.add(new JLabel(getter.getImage("datafiles/logo.jpg", 200, 100)) {{
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					getter.infor("메인 화면으로 이동하겠습니다.");
					setMainPanel();
				};
			});
		}}, BorderLayout.WEST);// 2 : 1
		TitlePanel.setPreferredSize(new Dimension(0, 110));
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.setBackground(getter.color);
		
		panel.add(new JLabel("app install", JLabel.CENTER) {{
			setPreferredSize(new Dimension(80, getFontMetrics(getFont()).getHeight() + 6));
			setOpaque(true);
			setBackground(new Color(0, 150, 0));
			setForeground(Color.white);
			setVerticalAlignment(JLabel.CENTER);
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					new SimpleJarBuilder();
				};
			});
		}});
		panel.add(loginInfor);
		
		TitlePanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {{
			setBackground(getter.color);
			setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
			add(before);
		}}, BorderLayout.SOUTH);
		before.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!before.isEnabled()) return;
				if(froms.pop() == 0) {
					setMainPanel();
				}
			}
		});
		TitlePanel.add(panel, BorderLayout.EAST);
	}
	
	public static void main(String[] args) {
		new Main1();
	}
}
