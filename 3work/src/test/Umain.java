package test;

import javax.swing.*;
import javax.swing.border.Border;

import main.Z_SqlConnection;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
public class Umain {
	JLabel[] l1 = new JLabel[3];
	JPanel slidP = new JPanel(new GridLayout(1, 3));
	JPanel p2 = new JPanel(new FlowLayout(10, 85, 10));
	JPanel p3 = new JPanel(new FlowLayout());
	
    JButton nextButton = new JButton(">");
    JButton nonextButton = new JButton("<");
    JButton witeq = new JButton("대기중인 질문");
    JButton[] manu = {new JButton("질문등록"),new JButton("내 질문"),new JButton("문제퀴즈"),new JButton("오답 노트")};
	JFrame f = new JFrame();
	Timer timer;
	Font manuf = new Font("맑은 고딕", Font.BOLD, 15);
	private int i;
	private int xOffset;
	private int sliderC;
	
	Z_SqlConnection al = new Z_SqlConnection();
	Umain(int number){
		
		
		witeq.setBackground(Color.white);
		witeq.setBorder(BorderFactory.createLineBorder(Color.white));
		witeq.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		for(int i = 0; i < 4; i++) {
			manu[i].setBorder(BorderFactory.createLineBorder(Color.white));
			manu[i].setBackground(Color.white);
			manu[i].setFont(manuf);
			p2.add(manu[i]);
		}
		slidP.setBounds(0,0,2100,350);
		p2.setBackground(Color.white);
		for(int i = 0; i <= 2; i++) {
			Image img = new ImageIcon("imgs/main/" + (i + 1) + ".png").getImage().getScaledInstance(700, 350, Image.SCALE_SMOOTH);
			l1[i] = new JLabel(new ImageIcon(img));
			slidP.add(l1[i]);
		}
		/*
		 * f.addWindowListener(new WindowAdapter() {
		 * 
		 * @Override public void windowClosing(WindowEvent e) {
		 * JOptionPane.showMessageDialog(f,"정보 : 로그아웃 되었습니다."); new Login();
		 * f.setVisible(false);
		 * 
		 * } }); 
		 */
		nextButton.addActionListener(new MyAction());
        nonextButton.addActionListener(new MyAction());
        
        try {
			ResultSet re = al.selectQ("catalog", "where uno = " + number + " and explan is null");
			while(re.next()) {
				i++;
			}
			re.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
        
        p2.setBackground(Color.white);
        p2.setBounds(0, 400, 700, 100);

        p3.setBackground(Color.white);
        p3.setBounds(0,350, 700, 50);
        p3.add(witeq);
        
        witeq.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//질문 폼 이동
			}
		});
        
		String str = ": " + i + "개";
		JLabel set = new JLabel(str);
		set.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		p3.add(set);
		
		//추가
        f.add(p3);
        f.add(p2);
        f.add(slidP);
        
        nextButton.setBounds(0, 100, 100, 100);
        
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setFocusPainted(false);
        nextButton.setOpaque(false);
        
        nextButton.setFont(new Font("", Font.BOLD, 50));
        nextButton.setForeground(Color.red);
        
        f.add(nonextButton);
        
        //프레임 세팅
        f.setTitle("학생 메인");
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(610, 340, 700, 500);
        f.add(nextButton);
        f.setVisible(true);
	}
	
	class MyAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == nextButton) {nextSlide();}
			else if(e.getSource() == nonextButton) {nonextSlide();}
		}
	}
	private void nonextSlide() {
		timer = new Timer(10, new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
                if (xOffset < 0) {
                    xOffset += 10;
                    slidP.setLocation(xOffset, 0);
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();
	}
    private void nextSlide() {
        timer = new Timer(10, new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
	                if (xOffset > -1400) {
	                    xOffset -= 10;
	                    slidP.setLocation(xOffset, 0);
	                } else {
	                    timer.stop();
	                }
	            }
			
        });
        timer.start();
    }
	public static void main(String[] args) {
		new Umain(1);
	}
}


