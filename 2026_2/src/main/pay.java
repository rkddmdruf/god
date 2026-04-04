package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import utils.CFrame;
import utils.RoundedButton;
import utils.User;
import utils.getter;

public class pay extends CFrame{
	JPanel inforPanel = new JPanel(new GridLayout(3, 1, 20, 20)) {{
		setBackground(Color.white);
		setBorder(createMatteBorder(0, 0, 1, 0, Color.black));
	}};
	JTextField[] cardNumberTf = new JTextField[4];
	String cardNumber = "";
	JTextField Number6 = new JTextField();
	JTextField Number2 = new JTextField();
	String number = "";
	JTextField passwordTf = new JTextField();
	String password = "";
	List<JTextField> cardText = new ArrayList<>();
	
	JButton payMent = new RoundedButton("결제하기").set테두리(Color.black);
	int cno;
	pay(int cno){
		this.cno = cno;
		setCardNumber();
		setNumber();
		setpassword();
		
		borderPanel.setBorder(createEmptyBorder(5,10,5,10));
		borderPanel.add(inforPanel);
		borderPanel.add(new JPanel(new GridLayout(2, 1)) {{
			setPreferredSize(new Dimension(0, 55));
			setBackground(Color.white);
			add(new JLabel("원가 " +  1));
			add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
				setBackground(Color.white);
				add(payMent);
			}});
		}}, BorderLayout.SOUTH);
		
		
		
		
		payMent.addActionListener(e->{
			for(JTextField t : cardNumberTf) {
				if(t.getText().isEmpty()) {
					getter.mg("빈칸이 있습니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			if(passwordTf.getText().isEmpty()) {
				getter.mg("빈칸이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(Number2.getText().isEmpty() || Number6.getText().isEmpty()) {
				getter.mg("빈칸이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String cardNumbers = String.join("-",
			Arrays.asList(cardNumberTf).stream().map(t -> ((JTextField) t).getText()).collect(Collectors.toList()));
			if(!User.getUser().getString(5).equals(cardNumbers)) {
			getter.mg("카드번호가 올바르지 않습니다.", JOptionPane.ERROR_MESSAGE); return; }
			 
			LocalDate userD = LocalDate.parse(User.getUser().getString(6));
			String s = (userD.getYear()+"").substring(2, 4) + "" + (userD.getMonthValue() < 10 ? "0" : "") + userD.getMonthValue() + "" + userD.getDayOfMonth();
			String two19 = (userD.getYear() + "").substring(0, 2);
			boolean two = two19.equals("20"); 
			System.out.println(two19);
			if(!s.equals(Number6.getText())) {
				getter.mg("주민번호가 올바르지 않습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(User.getUser().getString(7).equals("W")) {
				System.out.println("sd");
				System.out.println(two);
				if(!two && !Number2.getText().equals("2")) {
					getter.mg("주민번호가 올바르지 않습니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(two && !Number2.getText().equals("4")) {
					getter.mg("주민번호가 올바르지 않습니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			if(User.getUser().getString(7).equals("M")) {
				if(!two && !Number2.getText().equals("1")) {
					getter.mg("주민번호가 올바르지 않습니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(two && !Number2.getText().equals("3")) {
					getter.mg("주민번호가 올바르지 않습니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			if(!(s.substring(3,4) + s.substring(5, 6)).equals(passwordTf.getText())) {
				getter.mg("비밀번호를 확인해주세요.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			getter.mg("결제가 완료되었습니다.", JOptionPane.INFORMATION_MESSAGE);
			new MyForm();
			dispose();
		});
		
		
		
		
		setFrame("결제하기", 500, 200);
	}

	private void setNumber() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.white);
		setPanel_Label(p, "주민등록번호");
		
		Number6.setPreferredSize(new Dimension(200, 25));
		Number2.setPreferredSize(new Dimension(25, 25));
		
		Number2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String s = e.getKeyChar() + "";
				List<String> list = Arrays.asList("1,2,3,4".split(","));
				if(!Number2.getText().isEmpty() && !list.contains(s)) {
					getter.mg("성별을 확인하세요.", JOptionPane.ERROR_MESSAGE);
					Number2.setText("");
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				if(!Number2.getText().isEmpty()) {
					e.consume();
				}
			}
		});
		Number6.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(Number6.getText().length() == 6) {
					Number2.requestFocusInWindow();
				}
			}
			
			@Override
			public void keyTyped(KeyEvent e) {
				String s = e.getKeyChar() + "";
				try {
					Integer intz = Integer.parseInt(s);
				} catch (Exception e2) {
					e.consume();
				}
				if(Number6.getText().length() == 6) {
					e.consume();
					Number2.requestFocusInWindow();
				}
			}
		});
		
		
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		panel.setBorder(createEmptyBorder(0, 0, 0, 60));
		panel.add(Number6, BorderLayout.WEST);
		panel.add(new JLabel(" - "));
		panel.add(new JPanel(new BorderLayout()) {{
			setBackground(Color.white);
			add(Number2, BorderLayout.WEST);
			add(new JLabel("●●●●●●"));
		}}, BorderLayout.EAST);
		p.add(panel);
		inforPanel.add(p);
	}

	private void setpassword() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.white);
		setPanel_Label(p, "비밀번호");
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(createEmptyBorder(0,0,5,0));
		panel.setBackground(Color.white);
		passwordTf.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				try {
					Integer intz = Integer.parseInt(e.getKeyChar() + "");
				} catch (Exception e2) {
					e.consume();
				}
				if(passwordTf.getText().length() >= 2) {
					e.consume();
				}
			};
		});
		passwordTf.setPreferredSize(new Dimension(125, 25));
		panel.add(passwordTf, BorderLayout.WEST);
		
		panel.add(new JLabel(" ●●"));
		p.add(panel);
		inforPanel.add(p);
	}

	private void setCardNumber() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.white);
		setPanel_Label(p, "카드번호");
		
		JPanel panel = new JPanel(new GridLayout(0, 4, 5, 5));
		panel.setBackground(Color.white);
		for(int i = 0; i < 4; i++) {
			final int index = i;
			cardNumberTf[i] = new JTextField();
			cardNumberTf[i].addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if(cardNumberTf[index].getText().length() == 4) {
						if(cardText.indexOf(cardNumberTf[index]) + 1 < 4)
							cardText.get(cardText.indexOf(cardNumberTf[index]) + 1).requestFocusInWindow();
					}
				}
				
				@Override
				public void keyTyped(KeyEvent e) {
					String s = e.getKeyChar() + "";
					try {
						Integer intz = Integer.parseInt(s);
					} catch (Exception e2) {
						e.consume();
					}
					if(cardNumberTf[index].getText().length() == 4) {
						e.consume();
					}
				}
			});
			cardText.add(cardNumberTf[i]);
			panel.add(cardNumberTf[i]);
		}
		
		p.add(panel);
		inforPanel.add(p);
	}
	
	private void setPanel_Label(JPanel p, String s) {
		p.add(new JLabel(s) {{
			setPreferredSize(new Dimension(100, 25));
		}}, BorderLayout.WEST);
	}
	public static void main(String[] args) {
		new pay(1);
	}
}
