package main;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
public class Note extends JComponent{
	JFrame f = new JFrame();
	
	JPanel LP = new JPanel();
	JPanel publicP = new JPanel();
	
	JComboBox<Object> cb;
	
	JTextArea ta = new JTextArea(" 정답 : ");
	JButton[] bt = new JButton[3];
	
	List<String> wrong = new ArrayList<String>();
	JLabel question = new JLabel();
	JLabel[] data = new JLabel[2];
	JLabel penA = new JLabel();
	JLabel 오답노트 = new JLabel("오답노트");
	JLabel 문제개수 = new JLabel();
	int pomcheck = 0;
	int wronglist = 0;
	public Note(int number) {
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/question?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
			Statement cs = c.createStatement();
			ResultSet re = cs.executeQuery("select * from wrong where uno = " + number);
			if(re.next() == false) {
				pomcheck=1;
			}else {
				wrong.add(re.getString("que"));
				while(re.next()) {
					wrong.add(re.getString("que"));
				}
				question.setText(wrong.get(wronglist));
			}
			
			re.close();
			cs.close();
			c.close();
		} catch (Exception e) {}
		if(pomcheck == 0) {
			Image img = new ImageIcon("imgs/icon/pencil.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
			Image imgs = new ImageIcon("imgs/icon/eraser.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
			//data.setIcon(new ImageIcon(img));
			data[0] = new JLabel("그리기", new ImageIcon(img), JLabel.CENTER);
			data[1] = new JLabel("지우기", new ImageIcon(imgs), JLabel.CENTER);
			cb = new JComboBox<>(data);
			cb.setBounds(500, 130, 120, 30);
			cb.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			
			문제개수.setBounds(40, 10, 300, 20);
			문제개수.setText("풀어야 할 문제가 00개 남았습니다.");
			문제개수.setFont(new Font("맑은 고딕", 0, 16));
			
	
			question.setBounds(30, 40, 280, 80);
			question.setBorder(BorderFactory.createLineBorder(Color.black));
			question.setHorizontalAlignment(JLabel.CENTER);
			question.setFont(new Font("맑은 고딕", Font.BOLD, 14));
			
			ta.setBounds(340, 40, 280, 80);
			ta.setBorder(BorderFactory.createLineBorder(Color.black));
			ta.setFont(new Font("맑은 고딕", Font.BOLD, 14));
			
			penA.setBounds(30, 180,585, 200);
			penA.setBorder(BorderFactory.createLineBorder(Color.black));
			
			for(int i = 0; i < 3; i++) {
				bt[i] = new JButton();
				bt[i].setBackground(new Color(30, 120, 30));
				bt[i].setForeground(Color.white);
				bt[i].setFont(new Font("맑은 고딕", Font.BOLD, 13));
			}
			bt[0].setText("<");
			bt[0].setBounds(30, 420, 100, 30);
			bt[1].setText("확인하기");
			bt[1].setBounds(220, 420, 200, 30);
			bt[2].setText(">");
			bt[2].setBounds(520, 420, 100, 30);
			bt[2].addActionListener(new MyAction());
			bt[0].addActionListener(new MyAction());
			if(question.getText().equals(wrong.get(0))) {bt[0].setEnabled(false);}
			else {bt[0].setEnabled(true);}
			///////////////////////////////////////////////////////////////////////////////////////////////
			publicP.add(penA);
			for(int i = 0; i < 3; i++) {
				publicP.add(bt[i]);
			}
			publicP.add(cb);
			publicP.add(ta);
			publicP.add(question);
			publicP.add(문제개수);
			
		}else if(pomcheck == 1) {
			question.setText("오답이 없습니다.");
			question.setFont(new Font("맑은 고딕", Font.BOLD, 22));
			question.setForeground(Color.red);
			question.setBounds(250, 150, 200, 50);
			publicP.add(question);
		}
		
		오답노트.setFont(new Font("맑은 고딕", Font.BOLD, 21));
		오답노트.setBounds(290, 20, 90, 40);
		LP.add(오답노트);
		
		LP.setBackground(Color.white);
		LP.setBounds(-10,-10, 660, 60);
		LP.setBorder(BorderFactory.createLineBorder(Color.black));
		publicP.setBackground(Color.white);
		publicP.setBounds(-10, 50, 650, 500);
		publicP.setBorder(BorderFactory.createLineBorder(Color.black));		
		LP.setLayout(null);
		publicP.setLayout(null);
		///////////////////////////////////////
		f.setLayout(null);
		f.add(LP);
		f.add(publicP);
		f.setBounds(700, 200, 650, 550);
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new B_Umain(number);
				f.setVisible(false);
			}
		});
		f.setResizable(false);
		f.setVisible(true);
	}
class MyAction implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bt[2]) {
			wronglist++;
			question.setText(wrong.get(wronglist));
			if(wronglist != 0) {
				bt[0].setEnabled(true);
			}
			if(wronglist == wrong.size()-1) {
				bt[2].setEnabled(false);
			}
		}
		if(e.getSource() == bt[0]) {
			wronglist--;
			question.setText(wrong.get(wronglist));
			if(wronglist == 0) {
				bt[0].setEnabled(false);
			}
			if(wronglist != 0) {
				bt[0].setEnabled(true);
			}
			if(wronglist != wrong.size()-1) {
				bt[2].setEnabled(true);
			}
		}
	}
}
	public static void main(String[] args) {
		new Note(1);
	}
}
