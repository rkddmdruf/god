package main;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class B_Tmain extends JComponent{
	JFrame f = new JFrame();
	
	JPanel noQlist = new JPanel(new GridBagLayout());
	JPanel publicP = new JPanel();
	Color white = Color.white;
	Color black = Color.black;
	
	JButton Question = new JButton("질문 목록");
	JButton stats = new JButton("통계");
	JButton logo = new JButton();
	JButton imgg = new JButton();
	
	Color BC = new Color(30, 120, 30);
	JLabel noQlistl = new JLabel("답변하지 않은 질문");
	JLabel myQnA = new JLabel("내 답변률 : ");
	
	List<JPanel> list = new ArrayList<>();
	
	B_Tmain(int number, String tname){
		f.setTitle("선생님 메인");
		
		noQlist.setLayout(null);
		noQlist.setBackground(Color.cyan);
		noQlist.setBounds(240, 100, 420, 330);
		noQlist.setBorder(BorderFactory.createLineBorder(black));
		
		Image img = new ImageIcon("imgs/icon/logo.png").getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
		logo.setBorder(BorderFactory.createLineBorder(white));
		logo.setIcon(new ImageIcon(img));
		logo.setBackground(white);
		logo.addActionListener(new logoAction());
		logo.setBounds(0, 20, 70, 70);
		
		Question.setBackground(BC);
		Question.setForeground(white);
		Question.addActionListener(new QuestionAction());
		Question.setBounds(50, 150, 120, 40);
		stats.setBackground(BC);
		stats.setForeground(white);
		stats.addActionListener(new StatsAction());
		stats.setBounds(50, 250, 120, 40);
		
		noQlistl.setFont(new Font("", Font.BOLD, 18));
		noQlistl.setBounds(240, 20, 200, 70);
		myQnA.setFont(new Font("", Font.BOLD, 18));
		myQnA.setBounds(40, 410, 150, 50);
		//.setBounds(140, 410, 200, 50);
		try {
			double ALLQ = 0;
			double NullQ = 0;
			Connection c = DriverManager.getConnection(Z_UUP.url(), Z_UUP.username(), Z_UUP.password());
			Statement al = c.createStatement();
			ResultSet res = al.executeQuery("select * from catalog where tno = "+number);
			while(res.next()) {
				ALLQ++;
			}
			ResultSet re = al.executeQuery("select * from catalog where tno = " + number + " and explan is null");
			int i = 0, j = 10;
			int r = 0, g = 0, b = 0;
			while(re.next()) {
				NullQ++;
				list.add(i, new JPanel(new BorderLayout()));
				JLabel test = new JLabel(re.getString("title"));
				list.get(i).add(test);
				list.get(i).setBackground(white);
				list.get(i).setBounds(10, j, 380, 150);
				noQlist.add(list.get(i));
				i += 1;j += 160;
			}
			double sum = ALLQ - NullQ;
			int ui = (int)((sum/ALLQ) * 100);
			myQnA.setText("내 답변률 : " + (""+ui) + "%");
			re.close();
			al.close();
			c.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		JScrollPane scll = new JScrollPane(noQlist);
		scll.setBounds(240, 100, 410, 300);
		scll.setViewportView(noQlist);
		publicP.setBackground(Color.white);
		publicP.add(scll);
		//add(noQlist);
		publicP.add(logo);
		publicP.add(Question);
		publicP.add(stats);
		publicP.add(noQlistl);
		publicP.add(myQnA);
		publicP.setLayout(null);
		f.add(publicP);
		f.setBackground(white);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		f.setBounds(610, 340, 700, 500);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				f.setVisible(false);
				new A_Login();
			}
		});
		f.setVisible(true);
		
	}
	
class logoAction implements ActionListener{
		
	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(f, "정보 : 로그아웃 되었습니다.");
		setVisible(false);
		new A_Login();
	}
}
class QuestionAction implements ActionListener{
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
class StatsAction implements ActionListener{
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
	public static void main(String[] args) {
		new B_Tmain(1, "박지우");
	}
}
