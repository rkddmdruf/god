package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import main.TList.MyMouse;

public class TList extends JComponent{
	JFrame f = new JFrame();
	
	CardLayout card = new CardLayout();
	JPanel publicP = new JPanel();
	JPanel[] listP = {new JPanel(),new JPanel(), new JPanel()};
	JPanel cardP = new JPanel(card);
	
	
	List<JPanel>[] list = new ArrayList[3];
	JLabel TL = new JLabel("선생님을 선택하세요!");
	JLabel[] 정보 = new JLabel[4];
	JLabel penL = new JLabel();
	JButton[][] Timg = new JButton[3][8];
	JButton logo = new JButton();
	JComboBox<String> cb;
	String Data[] = {"전체", "답변률순", "문제풀이가 많은 순"};
	Font fonts = new Font("맑은 고딕", Font.BOLD, 20);
	
	int number;
	int imgnumber;
	int pomcheck;
	String stitle;
	String s질문내용;
	
	int MC =0;
	int Qcheck = 0;
	double sum = 0;
	int X;
	int x;
	String order;
	int cblist = 0;
	int[][] Tarry = new int[3][8];
	ResultSet re;
	ResultSet nullre;
	ResultSet re2;
	TList(int number, String stitle, String s질문내용, int imgnumber, int pomcheck){
		this.pomcheck = pomcheck;
		System.out.println(pomcheck);
		for (int i = 0; i < 3;i++) {
			listP[i].setBounds(0, 0, 3350, 250);
			listP[i].setLayout(null);
			listP[i].setBackground(Color.white);
		}
		cardP.setBounds(0,100,3500, 250);
		this.number = number;
		this.stitle = stitle;
		this.s질문내용 = s질문내용;
		this.imgnumber = imgnumber;
		cb = new JComboBox<String>(Data);
		cb.setBounds(620, 15, 250, 40);
		cb.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		order = cb.getSelectedItem().toString();
		cb.addActionListener(new MyAction());
		
		Image img = new ImageIcon("imgs/icon/logo.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		logo.setIcon(new ImageIcon(img));
		logo.setBounds(0, 0, 80, 80);
		logo.setBackground(Color.white);
		logo.setBorder(BorderFactory.createLineBorder(Color.white));
		logo.addActionListener(new MyAction());
		
		TL.setFont(fonts);
		TL.setBounds(370, 20, 200, 30);
		for (int i = 0; i < 3; i++) {
		    for (int j = 0; j < 8; j++) {
		        Timg[i][j] = new JButton();
		    }
		}
		tryT();
		card.show(cardP, "C0");
		
		publicP.add(cardP);
		publicP.add(cb);
		publicP.add(TL);
		publicP.add(logo);
		publicP.setLayout(null);
		publicP.setBackground(Color.white);
		f.add(publicP);
		f.setDefaultCloseOperation(f.DO_NOTHING_ON_CLOSE);
		f.addWindowListener(new WindowAdapter() {
			@Override 
				public void windowClosing(WindowEvent e) {
				if(pomcheck != 0) {new WiteQuestion(number, 0, 0);}
				else { new QuestionUp(number,0, stitle, s질문내용, imgnumber);}
				f.setVisible(false);
			} 
		}); 
		f.setVisible(true);
		f.setSize(900, 500);
	}
	
	class MyAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == cb) {
				order = cb.getSelectedItem().toString();
				if(order.equals("전체")) {card.show(cardP, "C0");cblist = 0;}
				if(order.equals("답변률순")) {card.show(cardP, "C1");cblist = 1;}
				if(order.equals("문제풀이가 많은 순")) {card.show(cardP, "C2");cblist = 2;}
			}else if(e.getSource() == logo) {
				if(pomcheck != 0) {new WiteQuestion(number, 0, 0);}
				else {new QuestionUp(number, 0, stitle, s질문내용,imgnumber);}
				f.setVisible(false);
			}
		}
		
	}
	private void tryrT() {
		try {
			System.out.println("try진입");
			Connection c= DriverManager.getConnection(Z_UUP.url(),Z_UUP.username(), Z_UUP.password());
			Statement cs = c.createStatement();
			re = cs.executeQuery("SELECT * FROM question.teacher");
			for(int s = 0; s < 3; s++) {
				list[s] = new ArrayList<>();
			}
			for(int j = 0; j < 3 ; j++) {
				re = cs.executeQuery("select * from question.teacher");
				
				if(j==2) {
					re = cs.executeQuery("SELECT catalog.tno, teacher.*, COUNT(explan) AS explan FROM catalog join teacher on teacher.tno = catalog.tno WHERE explan IS NOT NULL "
					+ "GROUP BY catalog.tno ORDER BY explan DESC;");
				}
				if(j==1) {
					re = cs.executeQuery("SELECT catalog.tno, teacher.*, COUNT(explan) AS explan, "
					+ "COUNT(explan) / COUNT(*) AS answer_rate FROM catalog "
					+ "join teacher on teacher.tno = catalog.tno GROUP BY catalog.tno "
					+ "ORDER BY answer_rate DESC;");
				}
				int imgSize = 0;
				while(re.next()) {
					int i = re.getInt("tno");
					System.out.println(i);
					JLabel penL = new JLabel();
					정보[0] = new JLabel("이름 : " + re.getString("name"));
					정보[1] = new JLabel("학력 : " + re.getString("uni"));
					nullre = cs.executeQuery("select * from question.catalog where !(explan is null) and tno = " + (i)); 
					while(nullre.next()) {Qcheck += 1;}
					re2 = cs.executeQuery("select * from question.catalog where tno = " + (i));
					while(re2.next()) {sum += 1;}
					
					정보[2] = new JLabel("총 문제풀이한 개수 : " + Qcheck + "개");
					정보[3] = new JLabel("답변률 : " + ((int) (((double) Qcheck / sum) * 100)) + "%");
					nullre.close();
					
					for(int s = 0; s < 4;s++) {
						정보[s].setFont(new Font("맑은 고딕", Font.BOLD, 18));
					}
					
					정보[0].setBounds(200, 20, 200, 30);
					정보[1].setBounds(180, 50, 250, 50);
					정보[2].setBounds(115, 150, 300, 30);
					정보[3].setForeground(Color.red);
					정보[3].setBounds(115, 200, 300, 30);// 답변률 바꿔
					String str = "imgs/teacher/" + (i) + ".jpg";
					Image imgs = new ImageIcon(str).getImage().getScaledInstance(110, 125, Image.SCALE_SMOOTH);
					Image penImg= new ImageIcon("imgs/icon/pencil.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
					
					list[j].add(imgSize , new JPanel(null));
					list[j].get(imgSize).setBackground(Color.white);
					list[j].get(imgSize).setBorder(BorderFactory.createLineBorder(Color.black));
					list[j].get(imgSize).setBounds(10 +(imgSize * 420),0, 400, 250);
					for(int s = 0; s<4; s++) {
						list[j].get(imgSize).add(정보[s]);
					}
					Timg[j][imgSize].setBounds(10, 10, 120, 125);
					Timg[j][imgSize].setIcon(new ImageIcon(imgs));
					Timg[j][imgSize].setBackground(Color.white);
					Timg[j][imgSize].setBorder(BorderFactory.createLineBorder(Color.black));
					Timg[j][imgSize].addMouseListener(new MyMouseAction());
					penL.setIcon(new ImageIcon(penImg));
					penL.setBounds(15, 145, 80, 80);
					penL.setBackground(Color.white);
					penL.setBorder(BorderFactory.createLineBorder(Color.white));
					
					list[j].get(imgSize).add(Timg[j][imgSize]);
					list[j].get(imgSize).add(penL);
					listP[j].add(list[j].get(imgSize));
					
					publicP.addMouseListener(new MyMouse());
					publicP.addMouseMotionListener(new MyMouse());
					imgSize++;
					Qcheck=0;
					sum=0;
					
				}
				
				imgSize = 0;
				String name = "C"+j;
				cardP.add(listP[j],name);
				
			}
			
			
			re.close();
			cs.close();
			c.close();
		} catch (Exception e) {
			
		}
		System.out.println("try탈출");
	}
	private void tryT() {
    try {
        System.out.println("try진입");
        Connection c = DriverManager.getConnection(Z_UUP.url(), Z_UUP.username(), Z_UUP.password());
        Statement cs = c.createStatement();
        for(int s = 0; s < 3; s++) {
        re = cs.executeQuery("SELECT * FROM question.teacher");
        
		if(s==2) {

			re = cs.executeQuery("SELECT catalog.tno, teacher.*, COUNT(explan) AS explan FROM catalog join teacher on teacher.tno = catalog.tno WHERE explan IS NOT NULL "
			+ "GROUP BY catalog.tno ORDER BY explan DESC;");
		}
		if(s==1) {

			re = cs.executeQuery("SELECT catalog.tno, teacher.*, COUNT(explan) AS explan, "
			+ "COUNT(explan) / COUNT(*) AS answer_rate FROM question.catalog "
			+ "join teacher on teacher.tno = catalog.tno GROUP BY catalog.tno "
			+ "ORDER BY answer_rate DESC;");
		}
        List<Integer> teacherIds = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> unis = new ArrayList<>();

        // 데이터를 리스트에 저장
        int Tn = 0;
        while (re.next()) {
            teacherIds.add(re.getInt("tno"));
            names.add(re.getString("name"));
            unis.add(re.getString("uni"));
            Tarry[s][Tn] = re.getInt("tno");
            Tn++;
        }
        re.close();

            list[s] = new ArrayList<>();

            for (int i = 0; i < teacherIds.size(); i++) {
                int tno = teacherIds.get(i);
                String name = names.get(i);
                String uni = unis.get(i);

                JLabel[] 정보 = new JLabel[4];
                정보[0] = new JLabel("이름 : " + name);
                정보[1] = new JLabel("학력 : " + uni);

                ResultSet nullre = cs.executeQuery("SELECT * FROM question.catalog WHERE !(explan IS NULL) AND tno = " + tno);
                int Qcheck = 0;
                while (nullre.next()) {
                    Qcheck++;
                }
                nullre.close();

                nullre = cs.executeQuery("SELECT * FROM question.catalog WHERE tno = " + tno);
                int sum = 0;
                while (nullre.next()) {
                    sum++;
                }
                nullre.close();

                정보[2] = new JLabel("총 문제풀이한 개수 : " + Qcheck + "개");
                정보[3] = new JLabel("답변률 : " + (int) (((double) Qcheck / sum) * 100) + "%");
                
                정보[0].setBounds(200, 20, 200, 30);
				정보[1].setBounds(180, 50, 250, 50);
				정보[2].setBounds(115, 150, 300, 30);
				정보[3].setForeground(Color.red);
				정보[3].setBounds(115, 200, 300, 30);
				
                list[s].add(new JPanel(null));
                list[s].get(i).setBackground(Color.white);
                list[s].get(i).setBorder(BorderFactory.createLineBorder(Color.black));
                list[s].get(i).setBounds(10 + (i * 420), 0, 400, 250);

                for (int s2 = 0; s2 < 4; s2++) {
                	정보[s2].setFont(new Font("맑은 고딕", Font.BOLD, 18));
                    list[s].get(i).add(정보[s2]);
                }

                // 버튼 및 이미지 설정
                String imgPath = "imgs/teacher/" + tno + ".jpg";
                Image img = new ImageIcon(imgPath).getImage().getScaledInstance(110, 125, Image.SCALE_SMOOTH);
                Image penImg= new ImageIcon("imgs/icon/pencil.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                Timg[s][i] = new JButton(new ImageIcon(img));
                Timg[s][i].setBounds(10, 10, 120, 125);
                Timg[s][i].setBackground(Color.white);
                Timg[s][i].setBorder(BorderFactory.createLineBorder(Color.black));
                Timg[s][i].addMouseListener(new MyMouseAction());
                
                penL.setIcon(new ImageIcon(penImg));
				penL.setBounds(15, 145, 80, 80);
				penL.setBackground(Color.white);
				penL.setBorder(BorderFactory.createLineBorder(Color.white));
				
                list[s].get(i).add(Timg[s][i]);
                list[s].get(i).add(penL);
                listP[s].add(list[s].get(i));
				publicP.addMouseListener(new MyMouse());
				publicP.addMouseMotionListener(new MyMouse());
            }

            String name = "C" + s;
            cardP.add(listP[s], name);
        }
        
        re.close();
        cs.close();
        c.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    System.out.println("try탈출");
}
	class MyMouse extends MouseAdapter implements MouseMotionListener{
		@Override
		public void mousePressed(MouseEvent e) {
			X = e.getX();
			System.out.println("dfdsf" + e.getX());
		}
		@Override
		public void mouseDragged(MouseEvent e) {
				x = e.getX() - X;
				if(x>=0) {
					x = 0;
				}
				if(x<=-2500) {x=-2500;};
				System.out.println(x);
				listP[cblist].setLocation(x, 0);
		}

	}
class MyMouseAction implements MouseListener{
	@Override
	public void mousePressed(MouseEvent e) {
		MC ++;
		System.out.println("마우스클릭");
		for(int i = 0; i < 8; i++) {
			if(e.getSource() == Timg[cblist][i] && MC == 2) {
				System.out.println("선생님 찾음");
				if(pomcheck != 0) {
					System.out.println("바꿀 문제 찾기");
					try {
						Connection c = DriverManager.getConnection(Z_UUP.url(),Z_UUP.username(), Z_UUP.password());
						
						System.out.println( Tarry[cblist][i]);
						System.out.println(pomcheck);
						String str = "update catalog set tno = ? where cno = ?";
						PreparedStatement pre = c.prepareStatement(str);
						pre.setInt(1, Tarry[cblist][i]);
						pre.setInt(2, pomcheck);
						
						pre.close();
						c.close();
						
					}catch (Exception e2) {
						System.out.println(e2.getMessage());
					}
					new WiteQuestion(number, Tarry[cblist][i], 0);
					f.setVisible(false);
				}else if(pomcheck == 0) {
					new QuestionUp(number, Tarry[cblist][i], stitle, s질문내용, imgnumber);
					f.setVisible(false);
				}
				
			}
		}
	}
	@Override public void mouseExited(MouseEvent e) {MC = 0;}
	@Override public void mouseClicked(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
}
public static void main(String[] args) {
	new TList(0, "", "", 0, 0);
}
}
