package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.*;
public class Main extends BaseFrame{
	int number = ((int) (Math.random() * 200) + 1);
	List<Row> jobList = Query.CnoJob.select(Query.userJobCno.select(1).get(0).get(6));// job6List 넣을때만 필요
	List<Row> job6List = new ArrayList<>();// job6개 랜덤 저장
	List<Row> brand = Query.Top5.select();// brand 5개
	
	String[] butName = "채용,브랜드,찾기,마이페이지".split(",");
	JButton[] but = new JButton[4];
	
	JPanel mainPanel = new sp.cp(new BorderLayout(), sp.em(5, 5, 5, 5), null).size(0, 425);
	JPanel NorthPanel = new sp.cp(new FlowLayout(FlowLayout.LEFT), null, null);
	JPanel CenterPanel = new sp.cp(new GridLayout(0, 4), null, sp.color).size(0, 50);
	JPanel mNorthPanel = new sp.cp(new BorderLayout(), null, sp.gray);
	JPanel mNorthCenterPanel = new sp.cp(new BorderLayout(), sp.line, sp.gray);
	JPanel 광고 = new sp.cp(new BorderLayout(), sp.em(0, 10, 0, 0), null).size(200, 100);
	
	JPanel jobPanel = new sp.cp(new BorderLayout(), sp.line(sp.color), sp.gray);
	JPanel 알바Panel = new sp.cp(new GridLayout(0, 3, 24, 12), sp.em(10, 30, 10, 30), sp.gray);
	
	Timer timer;
	
	Main(){
		setFrame("메인", 600, 600, ()->{sp.user = new Row(); new Login();});
	}
	//D 알바 없을떄 안함
	@Override
	protected void desing() {
		while(job6List.size() < 6) {
			jobList.forEach(row -> { if(row.getInt(1) == number) job6List.add(row); });
			number = ((int) (Math.random() * 200) + 1);
		}
		//추천 알바 랜덤
		
		NorthPanel.add(new sp.cl(sp.getImg("icon/cat.png", 100, 70)));
		NorthPanel.add(new sp.cl("알바캣").font(sp.font(1, 20)).fontColor(sp.color));
		
		for(int i = 0; i < 4; i++) {
			but[i] = new sp.cb(butName[i]).fontColor(Color.white).font(sp.font(1, 17)).setting();
			but[i].setBorderPainted(false);
			but[i].setHorizontalAlignment(JButton.LEFT);
			CenterPanel.add(but[i]);
		}
		
		mNorthCenterPanel.add(new sp.cl("   인기 브랜드 TOP 5"), sp.c);
		mNorthCenterPanel.add(new sp.cp(new FlowLayout(1,7,7), null, sp.gray) {{
			brand.forEach(row -> {	add(new sp.cl(sp.getImg("brand/" + row.getInt(1) + ".png", 60, 60)).setBorders(sp.line));	});	
		}}, sp.s);
		mNorthPanel.add(mNorthCenterPanel);
		
		setadvertise();
		timer = new Timer(2000, e->{ setadvertise(); });
		timer.start();
		mNorthPanel.add(광고, sp.e);
		
		mainPanel.add(mNorthPanel,sp.n);
		
		
		jobPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			add(new JLabel("추천 알바"));
		}},sp.n);
		setjobsPanel();
		jobPanel.add(알바Panel);
		
		mainPanel.add(new sp.cp(new BorderLayout(), sp.em(5, 0, 0, 0), null) {{
			add(jobPanel, sp.c);
		}});
		
		add(NorthPanel, sp.n);
		add(CenterPanel, sp.c);
		add(mainPanel, sp.s);
	}
	
	@Override
	protected void action() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				timer.stop();
			}
		});
		for(int i = 0; i < 4; i++) {final int index = i;
			but[i].addActionListener(e->{
				switch (index) {
					case 0 :{new ceyung();	dispose();	break;}
					case 1 :{new Brand();	dispose();	break;}
					case 2 :{new Serch();	dispose();	break;}
					case 3 :{new MyPage2();	dispose();	break;}
				}
			});
		}
	}
	private void setadvertise() {
		number = ((int) (Math.random() * 200) + 1);
		광고.removeAll();
		광고.add(new sp.cl(sp.getImg("advertise/" + number + "-1.jpg", 190, 100)).setBorders(sp.line));
		광고.getComponent(0).addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Gwoung_go(number);
				dispose();
			}
		});
		RePaint();
	}
	
	private void setjobsPanel() {
		job6List.forEach(row -> {
			알바Panel.add(new sp.cp(new BorderLayout(), sp.line(sp.color), null) {{
				add(new JTextArea() {{ setjob(this, sp.font(1, 12), true, row); }}, sp.c);
				add(new JTextArea() {{ setjob(this, sp.font(1, 12), false, row); }}, sp.s);
			}});
		});
	}
	private void setjob(JTextArea ta, Font font, boolean frist, Row row) {
		ta.setFont(font);
		ta.setText(frist ? "\n" + row.getString(2) :
			("\n"+"[주 " + row.getInt(6) + "일, " + row.getInt(7) + "시간, " + (row.getInt(8) == 0 ? "정규직" : "계약직") + "]\n시급: " + row.getInt(4) + "원\n"));
		ta.setLineWrap(true);
		ta.setEditable(false);
		ta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for(int i = 0; i < 6; i++) {
					JPanel panel = ((JPanel) 알바Panel.getComponent(i));
					if(panel.getComponent(0) == e.getSource() || panel.getComponent(1) == e.getSource()) {
						new JobInfor(row.getInt(1));
						dispose();
					}
				}
			}
		});
	}
	public static void main(String[] args) {
		new Main();
	}
}
