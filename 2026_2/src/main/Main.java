package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javax.swing.BorderFactory. *;
import javax.swing.*;

import utils.*;

public class Main extends CFrame{

	JButton login = new RoundedButton("로그아웃") {{
		setForeground(Color.white);
		setBackground(Color.blue);
		setPreferredSize(new Dimension(100, 25));
	}};
	JButton myInfor = new RoundedButton("내 정보") {{
		setPreferredSize(new Dimension(90, 25));
		
	}}.set테두리(Color.black);
	Thread thread;
	public Main() { 
		NorthPanel.vera = JLabel.BOTTOM;
		ToolTipManager.sharedInstance().setInitialDelay(1000);
		borderPanel.add(new NorthPanel(), BorderLayout.NORTH);
		setMainPanel();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(thread != null) thread.interrupt();
			}
		});
		setSouthPanel();
		setFrame("자격증 메인 화면", 700, 525);
	}
	
	int selectIcon = -1;
	List<JLabel> iconLabels = new ArrayList<>();
	
	private void setSouthPanel() {
		String[] imgName = "it,cooking,volunteer,health,hospital,aviation".split(",");
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		panel.setPreferredSize(new Dimension(100, 200));
		
		panel.add(new JLabel("자격증을 선택해주세요", getter.getImageIcon("datafiles/icon/medel.png", 50, 40),JLabel.LEFT) {{
			setBorder(createEmptyBorder(20, 50, 20, 0));
		}}, BorderLayout.NORTH);
		
		JPanel gridPanel = new JPanel(new GridLayout(0,6, 5, 5)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.black);
				if(User.getUser() == null)
					g2.drawRoundRect(0, 0, 800, 300, 70, 70);
				else
					g2.drawRoundRect(0, 0, getWidth(), getHeight() + 100, 70, 70);
			}
		};
		gridPanel.setBackground(Color.white);
		gridPanel.setBorder(createEmptyBorder(0, 100, 30, 100));
		
		Timer t = new Timer(1000, e ->{
			iconLabels.get(selectIcon).setIcon(getter.getImageIcon("datafiles/icon/" + imgName[selectIcon] + ".png", 60, 60));
		});
		List<Data> list = Connections.select("SELECT cgname FROM lecture.category;");
		for(int i = 0; i < 6; i++) {
			final int index = i;
			JLabel l = new JLabel(getter.getImageIcon("datafiles/icon/" + imgName[i] + ".png", 50, 50));
			iconLabels.add(l);
			l.setToolTipText(list.get(index).getString(0));
			l.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					t.start();
					selectIcon = index;
				}
				@Override
				public void mouseExited(MouseEvent e) {
					t.stop(); selectIcon = -1;
					l.setIcon(getter.getImageIcon("datafiles/icon/" + imgName[index] + ".png", 50, 50));
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					if(User.getUser() == null) {
						getter.mg("로그인하세요", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			});
			gridPanel.add(l);
		}
		panel.add(gridPanel);
		borderPanel.add(panel, BorderLayout.SOUTH);
	}


	private void setMainPanel() {
		final int h = 200;
		List<JLabel> list = new ArrayList<>();
		JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
		mainPanel.setBorder(createEmptyBorder(25, 0, 0, 0));
		mainPanel.setBackground(Color.white);
		
		/////////////////////////////////////////////////////
		JPanel adverPanel = new JPanel(null);
		adverPanel.setBackground(Color.white);
		adverPanel.setPreferredSize(new Dimension(400, h));
		
		for(int i = 1; i <= 5; i++) {
			JLabel l = new JLabel(getter.getImageIcon("datafiles/main/" + i + ".png", 400, h));
			l.setBounds((i - 1) * 400, 0, 400, h);
			adverPanel.add(l);
			list.add(l);
		}
		
		thread = new Thread(()->{
			try {
				while(!Thread.interrupted()) {
					for(int i = 0; i <= 400; i ++) {
						list.get(0).setBounds(-i, 0, 400, h);
						list.get(1).setBounds(400-i, 0, 400, h);
						Thread.sleep(10);
					}
					list.add(list.get(0));
					list.remove(0);
				}
			} catch (Exception e) {
				Thread.interrupted();
			}
		});
		thread.start();
		mainPanel.add(adverPanel, BorderLayout.WEST);
		/////////////////////////////////////////
		
		JPanel loginPanel = new JPanel(new BorderLayout(5, 5));
		loginPanel.setBackground(Color.white);
		loginPanel.setBorder(createEmptyBorder(5, 5, 5, 5));
		
		JPanel p = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.black);
				g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-2, 20, 20);
			}
		};
		p.setBackground(Color.white);
		
		loginPanel.add(new JLabel( User.getUser() == null ? "로그인이 필요합니다." : User.getUser().getString(1) + "님, 환영합니다."
				, getter.getImageIcon("datafiles/icon/check.png", 35, 28), JLabel.LEFT), BorderLayout.NORTH);
		loginPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBackground(Color.white);
			add(login);
			if(User.getUser() == null) {
				setNoLoginPanel(p);
			}else {
				setLoginPanel(p);
				add(myInfor);
			}
		}});
		loginPanel.add(p, BorderLayout.SOUTH);
		
		mainPanel.add(loginPanel);
		borderPanel.add(mainPanel);
	}

	private void setLoginPanel(JPanel panel) {
		List<Data> list = Connections.select("SELECT crno, cname, ratring, start_date, days FROM course_registration\r\n"
				+ "join certi on certi.cno = course_registration.cno\r\n"
				+ "where uno = ?;", User.getUser().get(0));
		panel.setBorder(createEmptyBorder(5,5,5,5));
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		gridPanel.setBackground(Color.white);
		LocalDate now = LocalDate.now();
		
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			System.out.println(data);
			JPanel p = new JPanel(new BorderLayout()) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(Color.black);
					g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-2, 20, 20);
				}
			};
			p.setBorder(createEmptyBorder(4,4,4,4));
			p.setPreferredSize(new Dimension(100,100));
			p.setBackground(Color.white);
			
			LocalDate start = LocalDate.parse(data.getString(3));
			LocalDate end = start.plusDays(Integer.parseInt(data.getString(4).substring(0, 1)) * 7);
			p.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
				setBackground(Color.white);
				JLabel l = new JLabel(now.isBefore(end) ? "종료" : "학습중", JLabel.CENTER);
				l.setBackground(Color.orange);
				l.setForeground(Color.white);
				l.setPreferredSize(new Dimension(40, 20));
				l.setOpaque(true);
				add(l);
			}}, BorderLayout.NORTH);
			p.add(new JTextArea(data.getString(1) + data.getInt(2) + "\n수강신청 : " + start + "~" + end) {{
				setFocusable(false);
			}});
			p.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
				setBackground(Color.white);
				add(new JLabel("강의실 가기") {{
					Font font = new Font("맑은 고딕", 1, 12);
					Map attributes = font.getAttributes();
					attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

					setForeground(Color.blue);
					setFont(font.deriveFont(attributes));
				}});
			}}, BorderLayout.SOUTH);
			
			gridPanel.add(p);
		}
		
		panel.add(new JScrollPane(gridPanel) {{
			setBorder(null);
			setPreferredSize(new Dimension(100, 115));
		}});
		
	}
	private void setNoLoginPanel(JPanel p) {
		login.setText("로그인");
		
		p.setBorder(createEmptyBorder(20, 0, 20, 0));
		p.add(new JLabel("로그인이 필요합니다.") {{
			setForeground(Color.red);
		}}, BorderLayout.NORTH);
		
		UIManager.put("TextArea.font", new Font("맑은 고딕", 1, 11));
		p.add(new JTextArea("1. 유요한 사용자 정보를 입력하세요.\n2. 인증 절차를 완료하세요\n3. 로그인 후 이용 가능합니다.\n4. 오류가 지속되면 관리자에게 문의하세요.") {{
			setFocusable(false);
			setBorder(createMatteBorder(0, 1, 0, 1, Color.black));
		}});
	}


	public static void main(String[] args) {
		new Main();
	}
}
