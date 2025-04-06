package main;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Z_SqlConnection {
	Connection c;
	Statement s;
	ResultSet re;
	PreparedStatement pre;
	List<Object> list = new ArrayList<>();
	
	public ResultSet selectQ(String title, String where) {
		try {
			int i = 0;
			c = DriverManager.getConnection(Z_UUP.url(),Z_UUP.username(), Z_UUP.password());
			s = c.createStatement();
			re = s.executeQuery("select * from question." + title + " " + where);
			
			s.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return re;
		
	}
	public ResultSet selectTlist(String title) {
		try {
			private void tryT() {
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
							
							list[j].add(imgSize, new JPanel(null));
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
			class
			
			int i = 0;
			c = DriverManager.getConnection(Z_UUP.url(),Z_UUP.username(), Z_UUP.password());
			s = c.createStatement();
			s.executeQuery("USE question");
			re = s.executeQuery(title);
			String[][] str;
			while(re.next()) {i++;}
			re.beforeFirst();
			str = new String[i][8];
			for(int j = 0; j < i; j++) {
				while(re.next()) {
					str[j][i]= "";
				}
				re.beforeFirst();
			}
			
			s.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return re;
	}
	public void updateQ(int uno, int tno, int types, String titles, String dates, String questionexplans) {
		try {
			int i = 0;
			c = DriverManager.getConnection(Z_UUP.url(),Z_UUP.username(), Z_UUP.password());
			s = c.createStatement();
			s.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
			pre = c.prepareStatement("insert into question.catalog values (?, ?, ?, ?, ?, ?, ?, ?)");
			
			re = s.executeQuery("select cno from question.catalog order by cno asc");
			while(re.next()) {i = re.getInt("cno");}
			i++;
			pre.setInt(1, i);pre.setInt(2, uno);pre.setInt(3, tno);
			pre.setInt(4, types);pre.setString(5, titles);pre.setString(6, dates);
			pre.setString(7, questionexplans);pre.setString(8, null);
			
			pre.executeUpdate();
			
			pre.close();
			re.close();
			s.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
