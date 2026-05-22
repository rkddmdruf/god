package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import utils.*;

public class gamePanel extends JPanel{
	Main1 main1;
	Data data;
	public gamePanel(Main1 main1, Data data) {
		this.main1 = main1;
		this.data = data;
		System.out.println(data);
		setBackground(getter.color);
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 200));
		
		JPanel game_ImageName = new JPanel(new BorderLayout(10, 10));
		game_ImageName.setBackground(getter.color);
		game_ImageName.add(new JLabel(getter.getImage("datafiles/games/" + data.getInt(0) + ".jpg", 500, 250), JLabel.LEFT), BorderLayout.WEST);
		
		JPanel gameInforPanel = new JPanel(new BorderLayout());
		gameInforPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		gameInforPanel.setBackground(getter.color);
		gameInforPanel.add(new JLabel(data.getString(1)) {{
			setForeground(Color.white);
			setFont(new Font("맑은 고딕", 1, 24));
			while(getFontMetrics(getFont()).stringWidth(getText()) > 290) 
				setFont(getFont().deriveFont((float) getFont().getSize() - 1));
		}}, BorderLayout.NORTH);
		List<String> strs = new ArrayList<>();
		for(Data d : Connections.select("select * from category")) {
			if(Arrays.asList(data.getString(2).split(",")).contains(d.getString(0))) {
				strs.add(d.getString(1));
			}
		}
		gameInforPanel.add(new JLabel("<html>"
				+ "카테고리 : " + String.join(", ", strs)
				+ "<br><br>"
				+ "가격 : " + (data.getInt(4) == 0 ? "무료" : new DecimalFormat("###,###").format(data.getInt(4))) +
				"</html>") {{
			setFont(new Font("맑은 고딕", 1, 20));
			setVerticalAlignment(JLabel.BOTTOM);
			setForeground(Color.white);
		}});
		
		game_ImageName.add(gameInforPanel);
		add(game_ImageName, BorderLayout.NORTH);
		
		setGcCpuPanel();
		
		addMouseWheelListener(e -> {
			e.getWheelRotation();
		});
	}
	private void setGcCpuPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBackground(getter.color);
		
		JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
		panel.setBackground(getter.color);
		Data cg = Connections.select("SELECT gno, Cm.cpuname, Gm.gcname, minRam, Cn.cpuname, Gn.gcname, nomalRam  FROM game.cpugc \r\n"
				+ "join gc Gm on Gm.gcno = cpugc.minGc\r\n"
				+ "join gc Gn on Gn.gcno = cpugc.nomalGc\r\n"
				+ "join cpu Cm on Cm.cpuno = cpugc.minCpu\r\n"
				+ "join cpu Cn on Cn.cpuno = cpugc.nomalCpu\r\n"
				+ "where gno = ?", data.getInt(0)).get(0);
		
		JPanel minPanel = new JPanel(new BorderLayout(10, 10));
		minPanel.setBackground(getter.color);
		minPanel.add(new JLabel("최소 사양") {{
			setForeground(Color.white);
		}}, BorderLayout.NORTH);
		minPanel.add(new JLabel(new Html()
				.add("CPU : " + cg.getString(1))
				.nextLine()
				.nextLine()
				.add("GPU : " + cg.getString(2))
				.nextLine()
				.nextLine()
				.add("RAM : " + cg.getString(3) + "GB")
				.getString()) {{setForeground(Color.white); setVerticalAlignment(JLabel.TOP);}}
				);
		JPanel nomalPanel = new JPanel(new BorderLayout(10, 10));
		nomalPanel.setBackground(getter.color);
		nomalPanel.add(new JLabel("권장 사양"){{
			setForeground(Color.white);
		}}, BorderLayout.NORTH);
		nomalPanel.add(new JLabel(new Html()
				.add("CPU : " + cg.getString(4))
				.nextLine()
				.nextLine()
				.add("GPU : " + cg.getString(5))
				.nextLine()
				.nextLine()
				.add("RAM : " + cg.getString(6) + "GB")
				.getString()) {{setForeground(Color.white); setVerticalAlignment(JLabel.TOP);}}
				);
		
		panel.add(minPanel);
		panel.add(nomalPanel);
		
		JPanel eastPanel = new JPanel(new BorderLayout(10, 10));
		eastPanel.setBackground(getter.color);
		eastPanel.setPreferredSize(new Dimension(300, 0));
		
		eastPanel.add(new JTextArea("게임 설명 : " + data.getString(5)) {{
			setBackground(getter.color);
			setForeground(Color.white);
			setFocusable(false);
			setCursor(getCursor().getDefaultCursor());
			setLineWrap(true);
		}});
		
		JPanel butPanel = new JPanel(new GridLayout(0, 2, 10, 10));
		butPanel.setBackground(getter.color);
		
		JButton buyButton = new JButton("구매") {{
			setForeground(Color.white);
			setBackground(new Color(0, 150, 0));
			addActionListener(e -> {
				Data user = User.getUser();
				if(user == null) {
					getter.err("로그인을 먼저 해주세요.");
					return;
				}
				boolean buyGame = false;
				if(!Connections.select("SELECT * FROM game.buygame where uno = ? and gno = ?", user.get(0), data.getInt(0)).isEmpty()) {
					getter.infor("이미 구매한 게임입니다.");
					return;
				}
				if(data.getInt(4) == 0) {
					buyGame = true;
				}else {
					if(JOptionPane.showConfirmDialog(null
							, "게임을 구매하시겠습니까?"
							+ "\n보유 잔액 : " + getter.df.format(user.getInt(5)) + "원"
							+ "\n가격 : " + getter.df.format(data.getInt(4)) + "원"
							, data.getString(1) + " 구매", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						int price = user.getInt(5) - data.getInt(4);
						if(price < 0) {
							getter.err("보유 잔액이 부족합니다.");
							return;
						}
						Connections.update("update user set price = ? where uno = ?;", price, User.getUser().getInt(0));
						user.set(5, price);
						User.setUser(user);
						buyGame = true;
					}
				}
				if(buyGame) {
					getter.infor("구매 및 라이브러리에 추가 되었습니다.");
					Connections.update("insert into buygame values(0, ?, ?, ?);", data.get(0), user.get(0), LocalDate.now());
					main1.setMainPanel();
				}
			});
		}};
		
		JButton shoppingBasket = new JButton("장바구니추가") {{
			setForeground(Color.white);
			setBackground(new Color(0, 150, 0));
		}};
		butPanel.add(buyButton);
		butPanel.add(shoppingBasket);
		eastPanel.add(butPanel, BorderLayout.NORTH);
		
		
		mainPanel.add(panel);
		mainPanel.add(eastPanel, BorderLayout.EAST);
		mainPanel.add(new JButton("내 컴퓨터 사양 비교하기") {{
			addActionListener(e -> {
				new CpuGpuVsGame(data.getInt(0));
			});
			setFont(new Font("맑은 고딕", 1, 15));
			setBackground(new Color(0, 150, 0));
		}}, BorderLayout.SOUTH);
		add(mainPanel);
	}
}
