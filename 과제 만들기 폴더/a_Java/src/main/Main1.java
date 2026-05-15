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
import java.text.DecimalFormat;
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
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
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
		setBackground(getter.color);	}};

	
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
	
	JPanel mainPanel = new JPanel(new BorderLayout()) {{		setBackground(getter.color);

	}};
	Stack<Integer> froms = new Stack<>();
	List<JLabel> bestGame = new ArrayList<>();
	Data gameData = new Data();
	Runnable runGamePanel = () -> {
		froms.add(0);
		mainPanel.removeAll();
		mainPanel.add(new gamePanel(this, gameData));
		before.setEnabled(true);
		revalidate();
		repaint();
	};
	
	Thread thread, thread2;
	public Main1() {
		loginInfor.add(login);
		setIconImage(new ImageIcon("datafiles/logo.jpg").getImage());
		setResizable(false);
		setTitlePanel();		borderPanel.setBackground(getter.color);

		
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
		
		LoginPanel loginPanel = new LoginPanel(this, null);
		
		mainPanel.add(loginPanel);
		revalidate();
		repaint();
	}
	
	private void setUserChangePanel() {
		froms.push(fromNumber);
		before.setEnabled(true);
		fromNumber = 2;// 
		mainPanel.removeAll();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 200));
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(getter.color.darker());
		panel.add(new UserChange(this));
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
			loginInfor.add(new JLabel(getter.getImage("datafiles/user/" + User.getUser().getInt(4) + ".png", 20, 20)) {{
				addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						//if(!Connections.select("select * from loginuser").isEmpty()) 
							setUserChangePanel();
					};
				});
			}}, BorderLayout.EAST);
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
		List<Data> list = Connections.select("select * from category");
		list.add(0, new Data() {{
			add(0);
			add("전체");
		}});
		List<JMenuItem> categoryButtons = new ArrayList<>();
		for(Data data : list) {
			JMenuItem mi = new JMenuItem(data.getString(1));
			categoryButtons.add(mi);
			m1.add(mi);
		}
		//메뉴에 메뉴아이템 추가
		
		JMenu m2 = new JMenu("가격");
		m2.setForeground(Color.white);
		
		Data priceHigh = Connections.select("select price from game order by price desc limit 1").get(0);
		
		JLabel l1 = new JLabel("0원");
		JLabel l2 = new JLabel(priceHigh.getInt(0) + "원") {{
			SwingUtilities.invokeLater(() -> {
				setPreferredSize(getPreferredSize());
				l1.setPreferredSize(getPreferredSize());
			});
		}};
		
		JSlider sl1 = new JSlider(0, priceHigh.getInt(0));
		JSlider sl2 = new JSlider(0, priceHigh.getInt(0));
		sl1.setMajorTickSpacing(100);
		sl2.setMajorTickSpacing(100);
		sl1.setSnapToTicks(true);
		sl2.setSnapToTicks(true);
		sl1.addChangeListener(e -> {
		    if (sl1.getValue() > sl2.getValue()) {
		    	sl1.setValue(sl2.getValue());
		    }
		    l1.setText(sl1.getValue() + "원");
		});
		sl2.addChangeListener(e -> {
		    if (sl2.getValue() < sl1.getValue()) {
		    	sl2.setValue(sl1.getValue());
		    }
		    l2.setText(sl2.getValue() + "원");
		});
		sl1.setValue(0);
		sl2.setValue(sl2.getMaximum());
		
		JPanel sl1Panel = new JPanel(new BorderLayout());
		sl1Panel.add(sl1);
		sl1Panel.add(l1, BorderLayout.EAST);
		
		JPanel sl2Panel = new JPanel(new BorderLayout());
		sl2Panel.add(sl2);
		sl2Panel.add(l2, BorderLayout.EAST);
		
		JButton priceSerch = new JButton("이 가격으로 검색") {{
			setBackground(new Color(0, 150, 0));
			setForeground(Color.white);
		}};
		
		m2.add(sl1Panel);
		m2.add(sl2Panel);
		m2.add(priceSerch);
		
		JButton news = new JButton("뉴스");
		news.setBackground(Color.white);
		
		mb.add(m1);
		mb.add(m2);
		mb.add(news);
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
		
		//액션들
		priceSerch.addActionListener(e -> {
			serchPanel(gamePanel
				, Connections.select("select * from game where price >= ? and price <= ?"
				, sl1.getValue(), sl2.getValue())
				, "가격", getter.df.format(sl1.getValue()) + "~" + getter.df.format(sl2.getValue())
			);
		});
		
		for(int i = 0; i < categoryButtons.size(); i++) {
			final int index = i;
			categoryButtons.get(i).addActionListener(e -> {
				List<Data> arrays = new ArrayList<>();
				if(index != 0) arrays = Connections.select("select * from game where find_in_set(?,cno)", index);
				else arrays = Connections.select("select * from game");
				serchPanel(gamePanel, arrays, "카테고리", (index == 0 ? "전체" : Connections.select("select * from category where cno = ?", index).get(0).getString(1)));
			});
		}
	}

	private void serchPanel(JPanel gamePanel, List<Data> list, String title, String infor) {
		gamePanel.removeAll();
		gamePanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 200));
		gamePanel.setBackground(getter.color);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 1, 10, 10));
		gridPanel.setBackground(getter.color);
		
		JScrollPane sc = new JScrollPane(gridPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sc.setBorder(null);
		sc.addMouseWheelListener(e2 -> {
			sc.getVerticalScrollBar().setValue(sc.getVerticalScrollBar().getValue() + (e2.getWheelRotation() * 40));
		});
		sc.setBackground(Color.white);
		setMainGridPanel(gridPanel, sc, list);
		gamePanel.add(new JLabel(title + " : " + infor) {{
			setForeground(Color.white);
			setFont(new Font("맑은 고딕", 1, 20));
		}}, BorderLayout.NORTH);
		gamePanel.add(sc);
		
		for(int j = 0; j <  5 - gridPanel.getComponentCount(); j++) {
			gridPanel.add(new JLabel(""));
		}
		
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
			image.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(game);
					gameData = game;
					runGamePanel.run();
				}
			});
			String result = categorys.stream()
	                .filter(c -> Arrays.asList(game.getString(2).split(",")).contains(String.valueOf(c.get(0))))
	                .map(c -> (String) c.get(1))
	                .collect(Collectors.joining(", "));
			JLabel text = new JLabel("<html>" + game.getString(1) + "<br><br>카테고리 : " + result + "<br><br>가격 : " + (game.getInt(4) == 0 ? "무료" : new DecimalFormat("###,###").format(game.getInt(4))) + "</html>", JLabel.CENTER);
			text.setForeground(Color.white);
			text.setPreferredSize(new Dimension(200, 0));
			
			p.add(image);
			p.add(text, BorderLayout.EAST);
			gameInforPanel.add(p);
		}
		
		JScrollPane sc1 = new JScrollPane(gameInforPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			setBorder(null);
			setPreferredSize(new Dimension(0, 250));
			addMouseWheelListener(null);
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
						Thread.sleep(1);
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
		//인기 게임 판넬 ^^^^^
		
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBackground(getter.color);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 200));
		
		if(User.getUser() != null) {
			String[] categoryz = Connections.select("SELECT cno FROM game.buygame \r\n"
					+ "join game on game.gno = buygame.gno\r\n"
					+ "where uno = ? order by date desc limit 1;", User.getUser().get(0)).get(0).getString(0).split(",");
			List<Data> userBuyGame = new ArrayList<>();
			for(String s : categoryz) {
				System.out.println(s);
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
				img.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						gameData = data;
						runGamePanel.run();
					};
				});
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
		gridPanel.setBackground(getter.color);

		JPanel grid_gsPanel = new JPanel(new BorderLayout(10, 10));
		grid_gsPanel.setBackground(getter.color);
		grid_gsPanel.add(gsPanel, BorderLayout.NORTH);
		grid_gsPanel.add(gridPanel);
		mainPanel.add(grid_gsPanel);
		
		JScrollPane sc2 = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addMouseWheelListener(e -> {
			sc2.getVerticalScrollBar().setValue(sc2.getVerticalScrollBar().getValue() + (e.getWheelRotation() * 45));
		});
		
		setMainGridPanel(gridPanel, sc2, Connections.select("SELECT game.*, count(game.gno) as c FROM game.game \r\n"
				+ "join buygame on buygame.gno = game.gno\r\n"
				+ "group by game.gno order by c desc, game.gno;"));
		m1.addActionListener(e -> {
			setMainGridPanel(gridPanel, sc2, Connections.select("SELECT game.*, count(game.gno) as c FROM game.game \r\n"
					+ "join buygame on buygame.gno = game.gno\r\n"
					+ "group by game.gno order by c desc, game.gno;"));
		});
		m2.addActionListener(e -> {
			setMainGridPanel(gridPanel, sc2, Connections.select("select * from game where price >= ? and price <= ?", 0, 0));
		});
		
		panel.add(mainPanel);
		sc2.setBorder(null);
		gamePanel.add(sc2);
		revalidate();
		repaint();
		
		
	}
	
	private void setMainGridPanel(JPanel gridPanel, JScrollPane sc, List<Data> list) {
		gridPanel.removeAll();
		List<Data> categorys = Connections.select("select * from category");
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			
			JPanel p = new JPanel(new BorderLayout(10, 10));
			p.setPreferredSize(new Dimension(100, 125));
			p.setBackground(getter.color);
			
			p.add(new JLabel(getter.getImage("datafiles/games/" + data.getInt(0) + ".jpg", 250, 125)) {{
				addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						gameData = data;
						runGamePanel.run();
					};
				});
			}}, BorderLayout.WEST);
			
			List<String> s = new ArrayList<>();
			List<String> categoryz = Arrays.asList(data.getString(2).split(","));
			for(Data c : categorys) {
				if(categoryz.contains(c.getString(0))) {
					s.add(c.getString(1));
				}
			}
			p.add(new JLabel("<html>" + data.getString(1) + "<br>"
					+ "카테고리 : " + String.join(", ", s) + "<br>"
					+ "가격 : " + (data.getInt(4) == 0 ? "무료" : new DecimalFormat("###,###").format(data.getInt(4))) + ""
					+ "</html>") {{
						setPreferredSize(new Dimension(250, 125));
				setForeground(Color.white);
			}});
			p.add(new JTextArea("게임 설명 : " + data.getString(5)) {{
				getFontMetrics(getFont()).getHeight();
				setLineWrap(true);
				setFocusable(false);
				setCursor(getCursor().getDefaultCursor());
				setBackground(getter.color);
				setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
				setForeground(Color.white);
				setPreferredSize(new Dimension(300, 125));
				
			}}, BorderLayout.EAST);
			gridPanel.add(p);
		}
		revalidate();
		repaint();
		SwingUtilities.invokeLater(() -> {
			sc.getVerticalScrollBar().setValue(0);
		});
	}

	public void setTitlePanel() {
		TitlePanel.add(new JLabel(getter.getImage("datafiles/logo.jpg", 200, 100)) {{
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					getter.infor("메인 화면으로 이동하겠습니다.");
					setMainPanel();
				};
			});
		}}, BorderLayout.WEST);// 2 : 1
		TitlePanel.setPreferredSize(new Dimension(0, 110));
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.setBackground(getter.color);
		
		//if(Connections.select("select * from loginuser").isEmpty()) {
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
		//}
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
