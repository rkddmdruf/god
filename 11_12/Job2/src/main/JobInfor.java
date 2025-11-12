package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Date;

import javax.swing.*;

import utils.*;
import utils.sp.cl;
import utils.sp.cp;
import utils.sp.cta;

public class JobInfor extends BaseFrame{
	Row job;
	Row brand;
	
	JPanel borderPanel  = new sp.cp(new BorderLayout(), sp.em(10, 10, 10, 13), null)
		  ,scrollPanel  = new sp.cp(new GridLayout(0,1,20,20), sp.em(10, 10, 10, 10), null)
		  ,scNorthPanel = new sp.cp(new BorderLayout(), null, null)
		  ,scSouthPanel = new sp.cp(null, null, null)
		  ,southPanel   = new sp.cp(new BorderLayout(), sp.em(10, 0, 0, 0), null);
	JScrollPane sc;
	JLabel map;
	JLabel hart = new sp.cl(sp.getImg("icon/하트1.png", 40,40));
	JButton apply = new sp.cb("지원하기 →").BackColor(sp.color).fontColor(Color.white).size(140, 40).font(sp.font(0, 14));
	int hartColor = 1, jno;
	
	public JobInfor(int jno) {
		this.jno = jno;
		job = Query.JobInfor_job.select(jno).get(0);
		brand = Query.JobInfor_brand.select(jno).get(0);
		hartColor = (!Query.JobInfor_like_YN.select(sp.user.get(0), jno).isEmpty() ? 1 : 2);
		hart = new sp.cl(sp.getImg("icon/하트" + (!Query.JobInfor_like_YN.select(sp.user.get(0), jno).isEmpty() ? 1 : 2) + ".png", 40,40));
		setFrame("알바 정보", 410, 500, ()->{});
	}

	@Override
	protected void desing() {
		borderPanel.add(new sp.cta(job.getString(1)).setting().font(sp.font(1, 20)), sp.n);
		
		scNorthPanel.add(new sp.cp(new BorderLayout(), null, null) {{
			String string = "[근무조건]\n"
					+ "급여: " + job.getInt(3) + "원\n"
					+ "근무요일: 주 " + job.getInt(5) + "회\n"
					+ "근무시간: " + job.getInt(6) + "시간\n"
					+ "교용형태: " + (job.getInt(7) == 0 ? "정규직" : "계약직") + "\n\n"
					+ "[모집조건]\n"
					+ "지원자격: " + (job.getInt(8) == 0 ? "무관" : job.getInt(8) == 1 ? "고졸" : "대학") + "\n"
					+ "모집인원: " + job.getInt(4) + "명\n\n"
					+ "[기업정보]\n"
					+ brand.getString(1);
			add(new sp.cta(string).setting().font(sp.font(0, 16)));
			JPanel p = new sp.cp(new FlowLayout(FlowLayout.LEFT), sp.em(-5, -7, 0, 0), null);
			p.add(new sp.cl("▽위치보기").font(sp.font(0, 16)));
			add(p,sp.s);
		}});
		scrollPanel.add(scNorthPanel);
		scSouthPanel.add(map = new JLabel(sp.getImg("지도.png", 300, 275)) {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.setColor(Color.red);
				int w = brand.getInt(3) / 2;
				int h = (int) (((double) brand.getInt(4)) * (275.0 / 500.0));
				g.fillOval(w-4, h-4, 8, 8);
			}
		});
		map.setBounds(0, 0, 300, 275);
		scrollPanel.add(scSouthPanel);
		borderPanel.add(sc = new JScrollPane(scrollPanel) {{
			this.getVerticalScrollBar().setUnitIncrement(400);
			setBackground(Color.white);
			setBorder(sp.com(sp.em(20, 0, 0, 0), sp.com(BorderFactory.createMatteBorder(5, 0, 0, 0, sp.color), sp.line(Color.gray))));
		}});
		
		
		southPanel.add(hart, sp.w);
		southPanel.add(apply, sp.e);
		borderPanel.add(southPanel, sp.s);
		add(borderPanel);
	}

	@Override
	protected void action() {
		hart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				hartColor = hartColor == 1 ? 2 : 1;
				if(hartColor == 1) {
					Query.update("insert into likes values(0,?,?,?);", jno, LocalDate.now().plusDays(1), sp.user.get(0));
				}else {
					Query.update("delete from likes where uno = ? and jno = ?;", sp.user.get(0), jno);
				}
				hart.setIcon(sp.getImg("icon/하트" + hartColor + ".png", 40, 40));
				repaints();
			}
		});
		JPanel panel = (JPanel) scNorthPanel.getComponent(0);
		((JPanel) panel.getComponent(1)).getComponent(0).addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sc.getVerticalScrollBar().setValue(sc.getVerticalScrollBar().getMaximum());
			}
		});
		
		apply.addActionListener(e->{
			if(!Query.JobInfor_apply_YN.select(jno, sp.user.get(0)).isEmpty()) {
				sp.ErrMes("이미 지원한 적 있는 알바입니다");
				return;
			}
			if(!sp.user.getString(7).contains("대학교")) 
				if(job.getInt(8) == 1 && sp.user.getString(7).contains("고등학교")) 
					return;
			sp.InforMes("지원이 완료되었습니다.");
			Query.update("insert into apply values(0, ?,?,?,?);", jno, LocalDate.now().plusDays(1), sp.user.get(0), 0);
			dispose();
		});
		map.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = brand.getInt(3) / 2;
				int y =(int) (brand.getInt(4) * (275.0 / 500.0));
				if(e.getX() > x - 4 && e.getX() < x + 4) {
					if(e.getY() > y - 4 && e.getY() < y + 4) {
						System.out.println("sdfsd");
					}
				}
			}
		});
	}
	private void setext() {
		
	}
	public static void main(String[] args) {
		new JobInfor(1);
	}
}
