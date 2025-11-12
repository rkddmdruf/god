package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import utils.*;
import utils.sp.*;

public class JobInfor extends BaseFrame{
	Row job;
	JPanel borderPanel = new cp(new BorderLayout(), sp.em(10, 10, 10, 10), null);
	JPanel scPanel = new cp(new GridLayout(0,1, 10, 10), sp.em(10, 10, 10, 10), null);
	JPanel southPanel = new cp(new BorderLayout(), sp.em(10, 0, 0, 0), null);
	JButton but = new cb("지원하기→").size(125, 30).font(sp.font(0, 13)).BackColor(sp.color).fontColor(Color.white);
	JButton likes = new cb(sp.getImg("datafiles/icon/하트1.png", 40, 40)).setting().size(40, 40);
	int likesNumber = 1;
	JScrollPane sc = new JScrollPane(scPanel) {{
		this.getVerticalScrollBar().setUnitIncrement(500);
		setBackground(Color.white);
		setBorder(sp.com(sp.em(15, 0, 0, 0), sp.com(BorderFactory.createMatteBorder(5, 0, 0, 0, sp.color), sp.line)));
	}};
	JobInfor(int jno){
		job = Query.selectText("select job.*, brand.* from job left join brand on brand.bno = job.bno where jno = ?", jno).get(0);
		System.out.println(job);
		setFrame("알바 정보", 400, 500, ()->{});
	}
	@Override
	protected void desing() {
		if(Query.selectText("select * from likes where jno = ? and uno = ?", job.get(0), sp.user.get(0)).isEmpty()) {
			likes.setIcon(sp.getImg("datafiles/icon/하트2.png", 40, 40));
			likesNumber = 2;
		}
		borderPanel.add(new cta(job.getString(1)).setting().font(sp.font(1, 20)), sp.n);
		borderPanel.add(sc);
		scPanel.add(new cta("급여: " +   sp.df.format(job.getInt(3)) + "원\n"
				+ "근무요일: 주 " + job.getString(5) + "회\n"
				+ "근무시간: " + job.getString(6) + "시간\n"
				+ "고용형태: " + (job.getInt(7) == 1 ? "계약직" : "정규직")  + "\n\n"
				+ "[모집조건]\n"
				+ "지원자격: " + (job.getInt(8) == 0 ? "무관" : (job.getInt(8) == 1 ? "대학" : "고등")) + "\n"
				+ "모집인원: " + job.getString(4) + "명\n\n"
				+ "[기업정보]\n"
				+ job.getString(10) +"\n"
				+ "▽위치보기" ).setting().font(sp.font(0, 18)));
		scPanel.add(new cp(null, null, null) {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.red);
				g.drawImage(sp.getImg("datafiles/지도.png", 300, 275).getImage(), 0, 0, 300, 275, null);
				g.fillOval(job.getInt(12) / 2, (int) (job.getInt(13) * (275.0 / 500.0)), 8, 8);
			}
		});
		
		southPanel.add(likes, sp.w);
		southPanel.add(but, sp.e);
		borderPanel.add(southPanel, sp.s);
		add(borderPanel);
	}

	@Override
	protected void action() {
		likes.addActionListener(e->{
			setLikes();
			System.out.println(likesNumber);
		});
		but.addActionListener(e->{
			if(!Query.selectText("select * from apply where jno = ? and uno = ?", job.get(0), sp.user.get(0)).isEmpty()) {
				sp.ErrMes("이미 지원한 적 있는 알바입니다");
				return;
			}
			if(job.getInt(8) <= sp.user.getInt(5)) {
				Query.update("insert into apply values(0, ?, ?, ?, 0)", job.get(0), LocalDate.now().plusDays(1), sp.user.get(0));
				sp.InforMes("지원이 완료되었습니다.");
				dispose();
			}
		});
		scPanel.getComponent(1).addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = job.getInt(12) / 2;
				int y = (int) (job.getInt(13) * (275.0 / 500.0));
				if(e.getX() > x && e.getX() > x - 8)
					if(e.getY() > y && e.getY() > y - 8) {
						System.out.println("Dsf");
					}
			}
		});
	}
	private void setLikes() {
		if(likesNumber == 1) {
			likes.setIcon(sp.getImg("datafiles/icon/하트2.png", 40, 40));
			Query.update("delete from likes where jno = ? and uno = ?;", job.getInt(0), sp.user.get(0));
			likesNumber = 2;
		}else {
			likes.setIcon(sp.getImg("datafiles/icon/하트1.png", 40, 40));
			Query.update("insert into likes values(0, ?, ?, ?);", job.getInt(0), LocalDate.now().plusDays(1), sp.user.get(0));
			likesNumber = 1;
		}
		repaints();
	}
	public static void main(String[] args) {
		new JobInfor(3);
	}
}
