package main;

import java.awt.*;
import java.nio.ByteOrder;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.border.Border;

import java.util.ArrayList;
import java.util.List;

import utils.*;
import utils.sp.cp;

public class Pass_Yes_No extends BaseFrame{
	Row list;
	Row job;
	JPanel borderPanel =  new sp.cp(new BorderLayout(25,25), sp.em(10, 10, 10, 0), null);
	JPanel userInfor = new sp.cp(new BorderLayout(10,10), null, null);
	JPanel jobInfor = new sp.cp(new BorderLayout(), null, null);
	int apno, uno, jno;
	String date;
	
	JButton[] but = {new sp.cb("거절하기").setting().fontColor(sp.color).setBorders(sp.line(sp.color)).BackColor(Color.white)
			, new sp.cb("승인하기") {
				@Override
				public void paintComponent(Graphics g) {
					g.setColor(sp.color);
					g.fillRect(0, 0, 100,100);
					super.paintComponent(g);
				}
			}.setting().fontColor(Color.white).setBorders(sp.line(sp.color)).BackColor(sp.color)};
	
	public Pass_Yes_No(int uno, int apno,String date, int jno) {
		this.uno = uno; this.apno = apno; this.date = date; this.jno = jno;
		setFrame("승인하기", 600, 300, ()->{new AdminMain();});
	}
	
	@Override
	protected void desing() {
		list = Query.Pass_Yes_No_user.select(uno).get(0);
		job = Query.Pass_Yes_No_job.select(jno).get(0);
		System.out.println(job);
		System.out.println(list);
		userInfor.add(new sp.cl(sp.getImg("src/user/" + uno + ".jpg", 125, 145))
				.setBorders(sp.line), sp.w);
		
		userInfor.add(new sp.cp(new BorderLayout(10,10), null, null) {{
			add(new sp.cp(new GridLayout(0,1), null, null) {{
				Font font = sp.font(1, 13);
				add(new JLabel("[지원번호: " + apno + "]"));
				add(new JLabel("지원 날짜: " + date));
			}}, sp.n);
			add(new sp.cp(new GridLayout(0, 1), null, null) {{
				Font font = sp.font(0, 12);
				String[] str = "성명: ,아이디: ,성별: ,생년월일: ,학력: ".split(",");
				int[] index = {1,2,5,6,7};
				for(int i = 0; i < 5; i++) {
					add(new sp.cl(str[i] + list.getString(index[i])).font(font));
				}
			}});
		}});
		
		userInfor.add(new sp.cp(new BorderLayout(), null, null) {{
			add(new JTextArea(list.getString(8)) {{
				setPreferredSize(new Dimension(100, 80));
				setLineWrap(true);
				setBorder(sp.line);
				setFocusable(false);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}});
		}}, sp.s);
		
		
		jobInfor.add(new sp.cp(new BorderLayout(), sp.em(10, 0, 10, 0), Color.black) {{
			setPreferredSize(new Dimension(200, 10));
			Font font = sp.font(0, 13);
			add(new sp.cp(new BorderLayout(), null, Color.black) {{
				add(new JTextArea(job.getString(2)) {{
					setForeground(sp.color);
					setBackground(Color.black);
					setLineWrap(true);
					setFocusable(false);
				}});
			}}, sp.n);
			add(new sp.cp(new GridLayout(0,1),sp.em(15, 0, 15, 0), Color.black) {{
				add(new sp.cl("브랜드: " + job.getString(0))	.fontColor(sp.color).font(font));
				add(new sp.cl("급여: " + new DecimalFormat("##,###")	.format(job.getInt(4))).fontColor(sp.color).font(font));
				add(new sp.cl("근무요일: " + "주" + job.getString(6) + "일")	.fontColor(sp.color).font(font));
				add(new sp.cl("근무시간: " + job.getString(7) + "시간").fontColor(sp.color).font(font));
				add(new sp.cl("고용형태: " + (job.getInt(8) == 0 ? "정규직" : "계약직"))	.fontColor(sp.color).font(font));
			}});
			add(new sp.cp(new GridLayout(0,1),null, Color.black) {{
				add(new sp.cl("지원자격: " + (job.getInt(9) == 0 ? "무관" : (job.getInt(9) == 1 ? "대학" : "고등")))	.fontColor(sp.color).font(font));
				add(new sp.cl("모집인원: " + job.getString(5) + "명").fontColor(sp.color)	.font(font));
			}}, sp.s);
		}});
		
		jobInfor.add(new sp.cp(new GridLayout(0,2, 10, 10), sp.em(8, 8, 0, 8), null) {{
			add(but[0]);
			add(but[1]);
		}},sp.s);
		borderPanel.add(userInfor);
		borderPanel.add(jobInfor,sp.e);
		add(borderPanel);
	}

	@Override
	protected void action() {
		but[0].addActionListener(e->{
			new AdminMain();
			dispose();
		});
		
		but[1].addActionListener(e->{
			sp.InforMes("승인되었습니다.");
			Query.Pass_Yes_No_update_apok.update(apno);
			new AdminMain();
			dispose();
		});
	}
	
	public static void main(String[] args) {
		new Pass_Yes_No(1,5,"sdfds" ,34);
	}
}
