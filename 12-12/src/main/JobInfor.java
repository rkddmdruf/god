package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.*;

import utils.sp.*;
import utils.*;

public class JobInfor extends BaseFrame{

	JPanel borderPanel = new cp(new BorderLayout(15,15), sp.em(10, 10, 10, 10), null);
	JLabel hart = null;
	int hartNumber = 0;
	JButton apply = new cb("지원하기→").backColor(sp.color).fontColor(Color.white).size(125, 50).font(sp.font(0, 16));
	
	JPanel mainPanel = new cp(new BorderLayout(15,15), sp.com(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black), sp.em(10, 10, 10, 10)), null);
	
	Row list = new Row();
	int jno = 0;
	JScrollPane scroll = new JScrollPane(mainPanel);
	public JobInfor(int jno) {
		this.jno = jno;
		list = Query.selectText("select job.*, brand.bno, brand.bname, brand.bxx, brand.byy"
				+ " from job join brand on brand.bno = job.bno where jno = ?" , jno).get(0);
		System.out.println(list);
		if(Query.selectText("select * from apply where uno = ? and jno = ?", sp.user.get(0), jno).isEmpty()) {
			hart = new cl(sp.getImg("datafiles/icon/하트2.png", 50, 50));
			hartNumber = 2;
		}else {
			hart = new cl(sp.getImg("datafiles/icon/하트1.png", 50, 50));
			hartNumber = 1;
		}
		setFrame("알바 정보", 400, 500, ()->{});
	}
	@Override
	protected void desing() {
		borderPanel.add(new ca(list.getString(1)).setting().font(sp.font(1, 20)), sp.n);
		
		
		mainPanel.add(new cp(new BorderLayout(), null, null) {{
			add(new ca("[근무조건]\n"
				+ "급여: " + sp.form.format(list.getInt(3)) + "원\n"
				+ "근무요일: 주 " + list.getInt(5) + "회\n"
				+ "근무시간: " + list.getInt(6) + "시간\n"
				+ "고용형태: " + (list.getInt(7) == 0 ? "정규직" : "계약직") + "\n\n"
				+ "[모집조건]\n"
				+ "지원자격: " + (list.getInt(7) == 0 ? "무관" : (list.getInt(7) == 1 ? "대학" : "고등")) + "\n"
				+ "모집인원: " + list.getInt(4) + "\n\n"
				+ "[기업정보]\n"
				+ list.getString(10)					
				).setting().font(sp.font(1, 16)), sp.n);
			add(new cp(new FlowLayout(FlowLayout.LEFT), sp.em(-5, -5, 0, 0), null) {{
				add(new cl("위치보기▽") {{
					addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
						}
					});
				}}.font(sp.font(1, 16)).setHo(JLabel.LEFT));
			}});
		}},sp.n);
		System.out.println(list.get(12));
		mainPanel.add(new cp(null, null, null) {{
			setPreferredSize(new Dimension(300, 275));
			JLabel l = new cl(sp.getImg("datafiles/지도.png", 300, 275)) {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.red);
					g.fillOval((list.getInt(11) / 2) - 4,(list.getInt(12) / 2) - 4 , 8, 8);
				}
			};
			l.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int x = list.getInt(11) / 2 - 4;
					int y = list.getInt(12) / 2 - 4;
					if(x < e.getX() && x + 8 > e.getX()) {
						if(y < e.getY() && y + 8 > e.getY()) {
							System.out.println("df");
						}
					}
				}
			});
			l.setBounds(0,0,300, 275);
			add(l);
		}});
		
		scroll.setBorder(sp.com(sp.line, BorderFactory.createMatteBorder(5, 0, 0, 0, sp.color)));
		scroll.getVerticalScrollBar().setUnitIncrement(400);
		borderPanel.add(scroll);
		borderPanel.add(new cp(new BorderLayout(), null, null) {{
			add(hart, sp.w);
			add(apply, sp.e);
		}}, sp.s);
		
		add(borderPanel);
	}

	@Override
	protected void action() {
		hart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Sdf");
				if(hartNumber == 1) {
					hartNumber = 2;
					hart.setIcon(sp.getImg("datafiles/icon/하트2.png", 50, 50));
					Query.update("delete from likes where uno = ? and jno = ?;", sp.user.get(0), jno);
				}else if(hartNumber == 2) {
					hartNumber = 1;
					hart.setIcon(sp.getImg("datafiles/icon/하트1.png", 50, 50));
					Query.update("insert into likes values(0, ?, ?, ?);", jno, LocalDate.now(), sp.user.get(0));
				}
				rp();
			}
		});
		
	}
	public static void main(String[] args) {
		new JobInfor(1);
	}
}
