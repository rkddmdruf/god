package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;

import utils.*;
import utils.sp.cl;
import utils.sp.cp;
import utils.sp.cta;

public class Pass extends BaseFrame{
	Row apply;
	Row job;
	Row user;
	JPanel borderPanel = new cp(new BorderLayout(), sp.em(10, 10, 10, 0), null);
	JPanel westPanel   = new cp(new BorderLayout(10, 10), sp.em(0, 0, 4, 0)   , null);
	JPanel eastPanel   = new cp(new BorderLayout(), sp.em(0, 0, 0, 0)   , null);
	
	JButton no = new sp.cb("거절하기").BackColor(Color.white).fontColor(sp.color).setBorders(sp.line(sp.color));
	JButton yes = new sp.cb("승인하기").BackColor(sp.color).fontColor(Color.white).setBorders(sp.line(sp.color));
	Pass(int apno){
		apply = Query.selectText("select * from apply where apno = ?", apno).get(0);
		user = Query.selectText("SELECT * FROM parttimecat.user where uno = ?;", apply.get(3)).get(0);
		job = Query.selectText("select * from job where jno = ?", apply.get(1)).get(0);
		setFrame("승인하기", 600, 305, ()->{});
	}
	@Override
	protected void desing() {
		System.out.println(apply);
		System.out.println(user);
		System.out.println(job);
		westPanel.add(new sp.cl(sp.getImg("user/" + user.getInt(0) + ".jpg", 120, 155)).size(120, 0).setBorders(sp.line), sp.w);
		westPanel.add(new cp(new BorderLayout(), sp.em(10, 10, 10, 0),null) {{
			add(new sp.cta("[지원번호: " + apply.getInt(0) + "]\n" + "지원 날짜: " + apply.getString(2)).font(sp.font(1, 14)).setting(), sp.n);
			add(new sp.cta("\n성명: "  + user.getString(1) + "\n"
						 + "아이디: "  + user.getString(2) + "\n"
						 + "성별: "  + (user.getInt(5) == 1 ? "남자" : "여자") + "\n"
						 + "생년월일: "  + user.getString(6) + "\n"
						 + "학력: "  + user.getString(7) + "\n").setting());
			
		}});
		westPanel.add(new sp.cta(user.getString(8)) {{
			setPreferredSize(new Dimension(350, 75));
			setBorder(sp.line);
		}}.setting(), sp.s);
		borderPanel.add(westPanel, sp.w);
		
		
		eastPanel.add(new sp.cp(new BorderLayout(), sp.em(10, 0, 10,0), Color.black) {{
			setPreferredSize(new Dimension(200, 10));
			add(new sp.cta(job.getString(1) + "\n\n"
					+ "브랜드: " + Query.Pass.select(job.get(2)).get(0).getString(1) + "\n"
					+ "급여: " + sp.df.format(job.get(3)) + "원\n"
					+ "근무요일: 주 " + job.getInt(5) + "일\n"
					+ "근무시간: " + job.getInt(6) + "시간\n"
					+ "고용형태" + job.get(7) + "\n\n"
					+ "지원자격: " + (job.getInt(8) == 0 ? "정규지" : "계약직") + "\n"
					+ "모집인원: " + job.getInt(4)) {{ setBackground(Color.black); }}
			.setting().fontColor(sp.color));
		}});
		eastPanel.add(new sp.cp(new GridLayout(0, 2, 10, 10), sp.em(10, 10, 10, 10), null) {{
			add(no);
			add(yes);
		}}, sp.s);
		borderPanel.add(eastPanel, sp.e);
		add(borderPanel);
	}

	@Override
	protected void action() {
		no.addActionListener(e->{ new AdminMain(); });
		yes.addActionListener(e->{
			Query.update("update apply set apok = 1 where uno = ? and jno = ?;", user.get(0), job.get(0));
			sp.InforMes("승인되었습니다");
			new AdminMain();
			dispose();
		});
	}
	public static void main(String[] args) {
		new Pass(1);
	}
}
