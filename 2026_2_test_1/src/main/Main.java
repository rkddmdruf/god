package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import static javax.swing.BorderFactory.*;

import utils.*;

public class Main extends CFrame{

	LocalDate now = LocalDate.now();
	JPanel mainPanel = new JPanel(new BorderLayout(10 ,10)) {{
		setBorder(createEmptyBorder(0, 10, 10, 10));
		setBackground(Color.white);
	}};
	JPanel southPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setPreferredSize(new Dimension(0, 200));
	}};
	JButton login = new RoundedButton("로그인") {{
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
	Thread thread;
	MouseEvent me;
	String[] toolName = "IT,요리,봉사,운동,항공,의학".split(",");
	List<JLabel> iconLabelList = new ArrayList<>();
	public Main() {
		JPanel northPanel = new NorthPanel() {
			@Override
			protected void setSerchAction() {
				super.setSerchAction();
				serch.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(!tf.getText().isBlank() && certi.isEmpty()) {
							getter.err("해당하는 자격증이 존재하지 않습니다");
							return;
						}
						else {
							new Serch(-1, tf.getText());
							dispose();
						}
					}
				});
			}
		};
		northPanel.setBorder(createEmptyBorder(10, 10, 10, 10));
		borderPanel.add(northPanel, BorderLayout.NORTH);
		setAdverPanel();
		setLoginPanel();
		setSouthPanel();
		borderPanel.add(mainPanel);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				thread.interrupt();
			}
		});
		setAction();
		setFrame("자격증 메인 화면", 800, 550);
	}
	
	private void setAction() {
		login.addActionListener(e -> {
			if(login.getText().equals("로그인")) {
				new Login();
				dispose();
			}else {
				User.setUser(null);
				new Main();
				dispose();
			}
		});
		for(int i = 0; i < iconLabelList.size(); i++) {
			final int index = i;
			iconLabelList.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(User.getUser() == null) {
						getter.err("로그인하세요");
						return;
					}
					System.out.println(toolName[index]);
				}
			});
		}
	}

	int selectIndex;
	private void setSouthPanel() {
		ToolTipManager.sharedInstance().setInitialDelay(0);
		
		southPanel.add(new JLabel("자격증을 선택해주세요.", getter.getImage("datafiles/icon/medel.png", 45, 40), JLabel.LEFT) {{
			setBorder(createEmptyBorder(10, 40, 20, 0));
		}}, BorderLayout.NORTH);
		JPanel panel = new JPanel(new GridLayout(0, 6)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.black);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawRoundRect(0, 0, getWidth() - 1,500, 80, 80);
			}
		};
		String[] imgName = Arrays.asList("it,cooking,volunteer,health,aviation,hospital".split(","))
				.stream()
				.map(c -> "datafiles/icon/" + c + ".png")
				.toArray(String[]::new);
		
		
		Timer timer = new Timer(1000, e -> {
			ToolTipManager.sharedInstance().setEnabled(true);
			ToolTipManager.sharedInstance().mouseMoved(me);
			iconLabelList.get(selectIndex).setIcon(getter.getImage(imgName[selectIndex], 60, 60));
		});
		
		for(int i = 0 ; i <6; i++) {
			JLabel l = new JLabel(getter.getImage(imgName[i], 50, 50));
			final int index = i;
			l.setToolTipText(toolName[i]);
			l.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					selectIndex = index;
					me = e;
					timer.start();
				};
				public void mouseExited(MouseEvent e) {
					ToolTipManager.sharedInstance().setEnabled(false);
					l.setIcon(getter.getImage(imgName[index], 50, 50));
					timer.stop();
				};
			});
			panel.add(l);
			iconLabelList.add(l);
		}
		panel.setBorder(createEmptyBorder(0, 125, 30, 125));
		panel.setBackground(Color.white);
		southPanel.add(panel);
	}

	private void setLoginPanel() {
		login.setText(User.getUser() == null ? "로그인" : "로그아웃");
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(Color.white);
		String loginLabel = "로그인이 필요합니다.";
		
		JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 15, 15));
		buttonPanel.setBackground(Color.white);
		buttonPanel.add(login);
		if(User.getUser() != null) {
			buttonPanel.add(new RoundedButton("내 정보") {{
				addActionListener(e ->{
					new MyInfor();
					dispose();
				});
			}}.set테두리(Color.black));
			loginLabel = User.getUser().get(1) + "님, 환영합니다.";
		}
		
		JPanel buttonLabelPanel = new JPanel(new BorderLayout(10, 10));
		buttonLabelPanel.setBackground(Color.white);
		buttonLabelPanel.add(new JLabel(loginLabel, getter.getImage("datafiles/icon/check.png", 25, 25), JLabel.LEFT), BorderLayout.NORTH);
		buttonLabelPanel.add(buttonPanel);
		
		if(User.getUser() == null) {
			JPanel roundPnael = new JPanel(new BorderLayout()) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(Color.black);
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
				}
			};
			roundPnael.setBackground(Color.white);
			roundPnael.add(new JLabel("<html><font color='red'>로그인이 필요합니다.</font color><br>1. 유요한 사용자 정보를 입력하세요.<br>2. 인증절차를 완료하세요.<br>3.로그인 후 이용가능합니다.<br>4.오류가 지속되면 관리자에게 문의하세요."));
			panel.add(roundPnael);
		}else {
			setLoginPanel(panel);
		}
		panel.add(buttonLabelPanel, BorderLayout.NORTH);
		mainPanel.add(panel);
	}

	private void setLoginPanel(JPanel p) {
		List<Data> list = Connections.select("SELECT * FROM lecture.course_registration\r\n"
				+ "join certi on certi.cno = course_registration.cno\r\n"
				+ "where uno = ?", User.getUser().get(0));
		JPanel panel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.black);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
			}
		};
		panel.setBackground(Color.white);
		
		JPanel mainPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		mainPanel.setBackground(Color.white);
		
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			LocalDate date = LocalDate.parse(data.getString(3));
			LocalDate date7 = date.plusDays(Integer.parseInt(data.getString(10).substring(0, 1)) * 7);
			JPanel pa = new JPanel(new BorderLayout()) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(Color.black);
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
				}
			};
			pa.setBackground(Color.white);
			pa.setBorder(createEmptyBorder(3,3,3,6));
			JPanel nPanel = new JPanel(new BorderLayout());
			nPanel.setBackground(Color.white);
			
			JLabel dispose = new JLabel(!now.isAfter(date7) ? "학습중" : "종료");
			dispose.setBackground(Color.orange);
			dispose.setForeground(Color.white);
			dispose.setOpaque(true);
			
			nPanel.add(dispose, BorderLayout.EAST);
			pa.add(nPanel, BorderLayout.NORTH);
			
			pa.add(new JLabel("<html>" + data.getString(6) + " " + data.getString(11) + "급<br>수강신청 : " + date + "~" + date7 + "</html>"));
			pa.add(new JLabel("<html><br><font color='blue'><u>강의실 가기</u></font color></html>", JLabel.RIGHT) {{
				addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if(dispose.getText().equals("종료")) {
							getter.infor("학습기간이 종료되었습니다.");
							return;
						}else {
							new 강의(data.getInt(1));
							dispose();
						}
					};
				});
			}}, BorderLayout.SOUTH);
			
			mainPanel.add(pa);
		}
		panel.add(new JScrollPane(mainPanel) {{
			setBorder(null);
		}});
		panel.setBorder(createEmptyBorder(7,7,7,7));
		p.add(panel);
	}
	private void setAdverPanel() {
		int w = 500, h = 215;
		JPanel adverPanel = new JPanel(null);
		adverPanel.setPreferredSize(new Dimension(500, 0));
		adverPanel.setBackground(Color.white);
		List<JLabel> labels = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			JLabel img = new JLabel(getter.getImage("datafiles/main/" + (i + 1) + ".png", w, h));
			img.setBounds(i * w, 0, w, h);
			adverPanel.add(img);
			labels.add(img);
		}
		
		thread = new Thread(() -> {
			try {
				while(!Thread.interrupted()) {
					for(int i = 0; i <= w; i++) {
						labels.get(0).setBounds(- i, 0, w, h);
						labels.get(1).setBounds(w - i, 0, w, h);
						Thread.sleep(5);
					}
					labels.add(labels.get(0));
					labels.remove(0);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}finally {
				thread.interrupt();
			}
		});
		thread.start();
		
		mainPanel.add(adverPanel,BorderLayout.WEST);
	}

	public static void main(String[] args) {
		new Main();
	}
}
