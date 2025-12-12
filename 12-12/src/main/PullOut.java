package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class PullOut extends BaseFrame{
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(10, 10, 10, 0), null);
	JPanel CenterPanel = new cp(new BorderLayout(10,10), sp.em(0, 0, 0, 30), null);
	JPanel eastPanel = new cp(new BorderLayout(), null,null).size(225, 0);
	List<Row> user = null;
	List<Row> job = null;
	List<Row> apply = null;
	JButton out = new cb("거젛하기").backColor(Color.white).setBorders(sp.line(sp.color)).size(100, 30).fontColor(sp.color)
			, pass = new cb("승인하기").backColor(sp.color).setBorders(sp.line(sp.color)).size(100, 30).fontColor(Color.white);
	PullOut(int apno, int jno, int uno){
		user  = Query.selectText("select * from user  where uno  = ?", uno);
		job   = Query.selectText("select * from job   where jno  = ?", jno);
		apply = Query.selectText("select * from apply where apno = ?", apno);
		System.out.println(user);
		System.out.println(job);
		System.out.println(apply);
		setFrame("승인하기", 700, 350, ()->{});
	}
	@Override
	protected void desing() {//user.get(0).getInt(0)
		CenterPanel.add(new cl(sp.getImg("datafiles/user/" + "2" + ".jpg", 145, 180)).setBorders(sp.line), sp.w);
		CenterPanel.add(new ca(user.get(0).getString(8)) {{
			setPreferredSize(new Dimension(0, 100));
			setBorder(sp.line);
		}}.setting().font(sp.font(0, 14)), sp.s);
		CenterPanel.add(new cp(new BorderLayout(30,30), sp.em(0, 5, 10, 0), null) {{
			add(new ca("[지원번호: " + apply.get(0).getString(0) + "]\n지원 날짜: " + apply.get(0).getString(2)).setting().font(sp.font(1, 14)),sp.n);
			add(new ca("성명: " + user.get(0).getString(1) + "\n"
					+ "아이디: " + user.get(0).getString(2) + "\n"
							+ "성별: " + (user.get(0).getInt(5) == 1 ? "남자" : "여자") + "\n"
									+ "생년월일: " + user.get(0).getString(6) + "\n"
											+ "학력: " + user.get(0).getString(7)).setting().font(sp.font(0, 13)));
		}});
		borderPanel.add(CenterPanel);
		eastPanel.add(new cp(new BorderLayout(15,15), sp.em(15, 0, 15, 0), Color.black) {{
			add(new ca(job.get(0).getString(1)) {{
				setBackground(Color.black);
				setForeground(sp.color);
			}}.setting().font(sp.font(0, 15)), sp.n);
			add(new ca("브랜드: " + Query.selectText("select bname from brand where bno = ?", job.get(0).get(2)).get(0).getString(0) + "\n"
					+ "급여: " + sp.form.format(job.get(0).getInt(3)) + "원\n"
					+ "근무요일: 주" + job.get(0).getInt(5) + "일\n"
					+ "근무시간: " + job.get(0).getInt(6) + "시간\n"
					+ "고용형태: " + (job.get(0).getInt(7) == 0 ? "정규직" : "계약직")) {{
				setBackground(Color.black);
				setForeground(sp.color);
			}}.setting().font(sp.font(0, 14)), sp.c);
			add(new ca("지원자격: " + (job.get(0).getInt(8) == 0 ? "무관" : (job.get(0).getInt(8) == 1 ? "대학" : "고등")) + "\n"
					+ "모집인원: " + job.get(0).getInt(4)) {{
				setBackground(Color.black);
				setForeground(sp.color);
			}}.setting().font(sp.font(0, 14)), sp.s);
		}});
		eastPanel.add(new cp(new GridLayout(0,2,10,10), sp.em(10, 10, 10, 10), null) {{
			add(out);
			add(pass);
		}}, sp.s);
		borderPanel.add(eastPanel, sp.e);
		add(borderPanel);
	}

	@Override
	protected void action() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				new AdminMain();
			}
		});
		out.addActionListener(e->{
			dispose();
		});
		pass.addActionListener(e->{
			Query.update("update apply set apok = 1 where uno = ? and jno = ?;", apply.get(0).get(0), job.get(0).get(0));
			sp.InforMes("승인되었습니다.");
			dispose();
		});
	}
	public static void main(String[] args) {
		new PullOut(1, 1, 1);
	}
}
