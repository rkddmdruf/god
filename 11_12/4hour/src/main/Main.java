package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import utils.*;
import utils.sp.*;

public class Main extends BaseFrame{
	JPanel northPanel = new cp(new FlowLayout(FlowLayout.LEFT), sp.em(5, 10, 0, 0), null);
	JPanel butPanel = new cp(new GridLayout(0, 4), null, sp.color);
	String[] butString = "채용,브랜드,찾기,마이페이지".split(",");
	JButton[] but = new JButton[4];
	
	JPanel mainPanel = new cp(new BorderLayout(10,10), sp.em(5, 5, 5, 5), null);
	
	JPanel brand5Panel = new cp(new BorderLayout(), sp.com(sp.line, sp.em(5, 5, 5, 5)), sp.gray);
	JPanel advertisePanel = new cp(new BorderLayout(), sp.line, sp.gray);
	JPanel bestJobPanel = new cp(new BorderLayout(), sp.com(sp.line(sp.color), sp.em(10, 10, 10, 10)), sp.gray).size(300, 300);
	MouseAdapter m;
	Main(){
		setFrame("메인", 600, 570, ()->{});
	}
	@Override
	protected void desing() {
		for(int i = 0; i < 4; i++) {
			but[i] = new cb(butString[i]).BackColor(sp.color).font(sp.font(0, 18)).fontColor(Color.white).setHo(JLabel.LEFT).setting();
			butPanel.add(but[i]);
		}
		northPanel.add(new cl(sp.getImg("datafiles/icon/cat.png", 70, 40)));
		northPanel.add(new cl("알바캣").fontColor(sp.color).font(sp.font(1, 18)));
		
		
		brand5Panel.add(new cl("인기 브랜드 TOP5").setHo(JLabel.LEFT), sp.n);
		brand5Panel.add(new cp(new GridLayout(0, 5, 10,10), sp.em(10, 5, 5, 5), sp.gray) {{
			for(Row row : Query.Main_brand5.select()) {
				add(new cl(sp.getImg("datafiles/brand/" +  row.getInt(0) + ".png", 70,60)).setBorders(sp.line));
			}
		}});
		advertisePanel.add(new sp.cl(sp.getImg("datafiles/advertise/1-1.jpg", 175, 105)));
		mainPanel.add(new cp(new BorderLayout(5,5), null, null) {{
			add(brand5Panel);
			add(advertisePanel, sp.e);
		}});
		
		adver();
		Timer t = new Timer(2000, e->{
			adver();
		});
		t.start();
		int cno = Query.Main_category.select(sp.user.get(0)).get(0).getInt(0);
		List<Row> list = Query.Main_job6.select(cno);
		System.out.println(list);
		bestJobPanel.add(new sp.cl("추천 알바").setHo(JLabel.LEFT), sp.n);
		bestJobPanel.add(new sp.cp(new GridLayout(2, 3, 20, 15), sp.em(10, 30, 0, 30), sp.gray) {{
			
			for(Row row : list) {
				m = new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						new JobInfor(row.getInt(0));
					}
				};
				add(new sp.cp(new BorderLayout(10, 10) ,sp.com(sp.line(sp.color), sp.em(15, 0, 15, 0)), null) {{
					add(new sp.cta(row.getString(1)) {{addMouseListener(m);}}.setting().font(sp.font(1, 11)),sp.n);
					add(new sp.cta("[주 " + row.getInt(5) + ", " + row.getInt(6) + "시간, " + (row.getInt(7) == 0 ? "정규직" : "계약직") + "]\n"
							+ "시급: " + row.getString(3) + "원") {{addMouseListener(m);}}.setting().font(sp.font(0, 10)));
				}});
			}
		}});
		mainPanel.add(bestJobPanel, sp.s);
		add(butPanel);
		add(mainPanel, sp.s);
		add(northPanel, sp.n);
	}
	
	private void adver() {
		int number = (int) (Math.random() * 200 + 1);
		advertisePanel.removeAll();
		advertisePanel.add(new cl(sp.getImg("datafiles/advertise/" + number + "-1.jpg", 175, 105)) {{
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new AdvertiseForm(number);
				}
			});
		}});
		repaints();
	}
	@Override
	protected void action() {
		
	}
	public static void main(String[] args) {
		new Main();
	}
}
