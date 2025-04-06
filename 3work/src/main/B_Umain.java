package main;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class B_Umain {
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
	private int fcheck= 0;
	B_Umain(int number){
		
		witeq.setBackground(Color.white);
		witeq.setBorder(BorderFactory.createLineBorder(Color.white));
		witeq.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		for(int i = 0; i < 4; i++) {
			manu[i].setBorder(BorderFactory.createLineBorder(Color.white));
			manu[i].setBackground(Color.white);
			manu[i].setFont(manuf);
			p2.add(manu[i]);
		}
		slidP.setBounds(0,0,(700 * 3),350);
		p2.setBackground(Color.white);
		for(int i = 0; i <= 2; i++) {
			Image img = new ImageIcon("imgs/main/" + (i + 1) + ".png").getImage();
			Image imgs = img.getScaledInstance(685, 350, Image.SCALE_SMOOTH);
			l1[i] = new JLabel();
			l1[i].setSize(685, 350);
			l1[i].setIcon(new ImageIcon(imgs));
			slidP.add(l1[i]);
		}
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.addWindowListener(new WindowAdapter() {
			@Override 
				public void windowClosing(WindowEvent e) {
				JOptionPane.showMessageDialog(f,"정보 : 로그아웃 되었습니다."); new A_Login();
				f.setVisible(false);
			} 
		}); 
		 
		nextButton.addActionListener(new MyAction());
        nonextButton.addActionListener(new MyAction());
        
        try {
        	Connection c = DriverManager.getConnection(Z_UUP.url(),Z_UUP.username(),Z_UUP.password());
        	Statement cs = c.createStatement();
			ResultSet re = cs.executeQuery("select * from question.catalog where uno = " + number + " and explan is null");
			while(re.next()) {
				i++;
			}
			re.close();
			cs.close();
			c.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
        
        p2.setBackground(Color.white);
        p2.setBounds(0, 400, 685, 100);

        p3.setBackground(Color.white);
        p3.setBounds(0,350, 685, 50);
        p3.add(witeq);
        
        witeq.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new WiteQuestion(number,0, 0);
				f.setVisible(false);
			}
		});
        for(int i = 0; i < 4; i++) {
        	manu[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(e.getSource() == manu[0]) {
						f.setVisible(false);
						new QuestionUp(number,0, "","", 0);
					}else if(e.getSource() == manu[1]) {
						f.setVisible(false);
						new WiteQuestion(number,0, 1);
					}
				}
			});;
        }
		String str = "<html>: <span style='color:red;'>"+i+"</span>개</html>";;
		JLabel set = new JLabel(str);
		set.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		p3.add(set);
		
		//추가
        f.add(p3);
        f.add(p2);
        f.add(slidP);
        
        nextButton.setBounds(600, 100, 100, 100);
        
        nextButton.setBorderPainted(false);
        //nextButton.setContentAreaFilled(false);
        nextButton.setFocusPainted(false);
        //nextButton.setOpaque(false);
        
        nextButton.setFont(new Font("", Font.BOLD, 50));
        nextButton.setForeground(Color.red);
        
        f.add(nextButton);
        //f.add(nonextButton);
        
        //프레임 세팅
        f.setTitle("학생 메인");
        f.setLayout(null);
        f.setSize(700, 500);
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
				if(fcheck == 0) {
	                if (xOffset > -685) {
	                    xOffset -= 10;
	                    slidP.setLocation(xOffset, 0);
	                } else {
	                	fcheck = 1;
	                    timer.stop();
	                }
				}
				if(fcheck == 1) {
	                if (xOffset > -1400) {
	                    xOffset -= 10;
	                    slidP.setLocation(xOffset, 0);
	                } else {
	                	fcheck = 2;
	                    timer.stop();
	                }
				}
	            }
			
        });
        timer.start();
    }
	public static void main(String[] args) {
		new B_Umain(1);
	}
}


