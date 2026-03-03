package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import utils.CFrame;
import utils.getter;

public class pay extends CFrame{
	JPanel inforPanel = new JPanel(new GridLayout(3, 1, 20, 20)) {{
		setBackground(Color.white);
		setBorder(createMatteBorder(0, 0, 1, 0, Color.black));
	}};
	String cardNumber = "";
	String number = "";
	String password = "";
	List<JTextField> cardText = new ArrayList<>();
	pay(){
		setCardNumber();
		setNumber();
		setpassword();
		
		borderPanel.setBorder(createEmptyBorder(5,10,5,10));
		borderPanel.add(inforPanel);
		borderPanel.add(new JPanel() {{
			setPreferredSize(new Dimension(0, 50));
		}}, BorderLayout.SOUTH);
		setFrame("결제하기", 500, 200);
	}

	private void setNumber() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.white);
		setPanel_Label(p, "주민등록번호");
		
		JTextField birth = new JTextField();
		birth.setPreferredSize(new Dimension(200, 25));
		JTextField mm = new JTextField();
		mm.setPreferredSize(new Dimension(25, 25));
		
		mm.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String s = e.getKeyChar() + "";
				List<String> list = Arrays.asList("1,2,3,4".split(","));
				if(!mm.getText().isEmpty() && !list.contains(s)) {
					getter.mg("성별을 확인하세요.", JOptionPane.ERROR_MESSAGE);
					mm.setText("");
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				if(!mm.getText().isEmpty()) {
					e.consume();
				}
			}
		});
		birth.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(birth.getText().length() == 6) {
					mm.requestFocusInWindow();
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
				if(birth.getText().length() == 6) {
					mm.requestFocusInWindow();
				}
			}
		});
		
		
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		panel.setBorder(createEmptyBorder(0, 0, 0, 60));
		panel.add(birth, BorderLayout.WEST);
		panel.add(new JLabel(" - "));
		panel.add(new JPanel(new BorderLayout()) {{
			setBackground(Color.white);
			add(mm, BorderLayout.WEST);
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
		panel.add(new JTextField() {{
			addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if(getText().length() == 2) {
						e.consume();
					}
				};
			});
			setPreferredSize(new Dimension(125, 25));
		}}, BorderLayout.WEST);
		
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
			JTextField t = new JTextField();
			t.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if(t.getText().length() == 4) {
						cardText.get(cardText.indexOf(t) + 1).requestFocusInWindow();
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
					if(t.getText().length() == 4) {
						cardText.get(cardText.indexOf(t) + 1).requestFocusInWindow();
					}
				}
			});
			cardText.add(t);
			panel.add(t);
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
		new pay();
	}
}
