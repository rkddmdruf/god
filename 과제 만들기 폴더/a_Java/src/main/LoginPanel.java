package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.Connections;
import utils.Data;
import utils.User;
import utils.getter;

public class LoginPanel extends JPanel{
	
	private JTextField id = new JTextField();
	private JTextField pw = new JTextField();
	private JTextField otp = new JTextField();
	private Data user;
	private JButton login = new JButton("로그인") {{
		setBackground(Color.green.darker());
		setFont(new Font("맑은 고딕", 0, 14));
		setPreferredSize(new Dimension(125, 30));
	}};
	
	Color color = getter.color.darker();
	Font font = new Font("맑은 고딕", 1, 14);
	int labelIndex = 0;
	
	Main1 main1;
	Main2_LoginFrame main2;
	public LoginPanel(Main1 main1, Main2_LoginFrame main2) {
		this.main1 = main1;
		this.main2 = main2;
		setLoginPanel();
		login.addActionListener(e -> {
			String i = getId();
			String p = getPw();
			String str = "";
			for(int n = 0; n < 5; n++) {
				int r = (int) (Math.random() * 2);
				int n2 = 0;
				if(r == 0) {
					n2 = (int) (Math.random() * 10) + 48;
				}else {
					n2 = (int) (Math.random() * 26) + 65;
				}
				str += (char) n2;
			}
			//48 - 0, 57 - 9, 65 - a, 90 - z
			if(i.isBlank() || p.isBlank()) {
				getter.err("입력하지 않은 항목이 있습니다.");
				return;
			}
			List<Data> list = Connections.select("select * from user where id = ? and pw = ?", i, p);
			if(list.isEmpty()) {
				id.setText("");
				pw.setText("");
				getter.err("아이디 또는 비밀번호가 틀렸습니다.");
				return;
			}
			user = list.get(0);
			Connections.update("update user set otp = ? where uno = ?;", str, list.get(0).get(0));
			otpPanel();
		});
	}
	
	private void setLoginPanel() {
		removeAll();
		setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));
		user = null;
		id.setText("");
		pw.setText("");
		setBackground(color);
		setLayout(new GridLayout(3, 1, 20, 20));
		
		JPanel p1 = new JPanel(new BorderLayout(3, 3));
		p1.setBackground(color);
		JPanel p2 = new JPanel(new BorderLayout(3, 3));
		p2.setBackground(color);
		
		p1.add(new JLabel("아이디") {{
			setForeground(Color.white);
			setFont(font);
			setPreferredSize(new Dimension(150, getFontMetrics(font).getHeight()));
		}}, BorderLayout.NORTH);
		p2.add(new JLabel("비밀번호") {{
			setForeground(Color.white);
			setFont(font);
			setPreferredSize(new Dimension(150, getFontMetrics(font).getHeight()));
		}}, BorderLayout.NORTH);
		p1.add(id);
		p2.add(pw);
		
		add(p1);
		add(p2);
		add(new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)) {{
			add(new JLabel("") {{
				setPreferredSize(new Dimension(2000, 20));
			}});
			setBackground(color);
			add(login);
		}});
		revalidate();
		repaint();
	}
	
	private void otpPanel() {
		removeAll();
		setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));
		labelIndex = 0;
		otp.setText("");
		setLayout(new BorderLayout(10, 10));
		
		JPanel panel = new JPanel(new GridLayout(1, 5, 10, 10));
		panel.setBackground(color);
		
		List<JLabel> labels = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			JLabel l = new JLabel();
			l.setForeground(Color.white);
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setVerticalAlignment(JLabel.CENTER);
			l.setFont(new Font("맑은 고딕", 1, 40));
			l.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					otp.requestFocus();
				}
			});
			l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
			labels.add(l);
			panel.add(l);
		}
		
		JPanel beforePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		beforePanel.setBackground(color);
		beforePanel.add(new JButton("로그인 화면으로 돌아가기") {{
			addActionListener(e -> {
				setLoginPanel();
			});
		}});
		
		add(beforePanel, BorderLayout.NORTH);
		add(new JPanel(null) {{
			setBackground(color);
			setPreferredSize(new Dimension(0, 0));
			add(otp);
		}}, BorderLayout.WEST);
		add(panel);
		
		otp.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int c = e.getKeyCode();
				
				if(e.getKeyCode() == 8) {
					if(labelIndex > 0)
						labelIndex--;
					labels.get(labelIndex).setText("");
					otp.setText("");
					revalidate();
					repaint();
					return;
				}
				if((c < 48 || c > 57) && (c < 65 || c > 90)) {
					e.consume(); return;
				}
				
				try {
					labels.get(labelIndex).setText(otp.getText().substring(0, 1).toUpperCase());
				} catch (Exception e2) {
					
				}
				labelIndex++;
				otp.setText("");
				revalidate();
				repaint();

				if(labelIndex >= 5) {
					List<Data> list = Connections.select("select * from user where uno = ?", user.get(0));
					String strs = String.join("", labels.stream().map(s -> s.getText()).toArray(String[]::new));
					if(!strs.equals(list.get(0).getString(6))) {
						getter.err("인증번호가 다릅니다.");
						labelIndex = 0;
						for(JLabel l : labels) {
							l.setText("");
						}
						revalidate();
						repaint();
					}else {
						boolean 한 = false, 영 = false;
						String text = user.getString(1);

						if (text.matches(".*[가-힣].*"))
						    한 = true;
						if (text.matches(".*[a-zA-Z].*")) 
							영 = true;
						if(영) {
							getter.infor(user.getString(1) + "님 환영합니다!");
						}else {
							getter.infor(user.getString(1) + " 환영한다!");
						}
					    User.setUser(user);
						if(main1 != null)
							main1.setMainPanel();
						else {
							Connections.update("insert into loginuser values(?)", user.getInt(0));
							new Main1();
							main2.dispose();
						}
					}
				}
			}
		});
		revalidate();
		repaint();
		otp.requestFocus();
		
	}
	
	public String getId() {
		return id.getText();
	}
	
	public String getPw() {
		return pw.getText();
	}
}
