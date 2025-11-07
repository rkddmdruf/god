package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.*;

import utils.*;
import utils.sp.cl;
import utils.sp.cp;

public class Main extends BaseFrame{
	JPanel northPanel = new sp.cp(new BorderLayout(), null, null);
	String[] butString = "채용,브랜드,찾기,마이페이지".split(",");
	JButton[] but = new JButton[4];
	
	JPanel centerPanel = new sp.cp(new BorderLayout(), sp.em(5, 5, 5, 5), null);
	JPanel brandPanel = new sp.cp(new BorderLayout(), sp.com(sp.line, sp.em(8, 5, 5, 8)), sp.gray);
	JPanel abtPanel = new sp.cp(new BorderLayout(), sp.line, null).size(200, 100);
	MouseAdapter ma = new MouseAdapter() {};
	JPanel jobPanel = new sp.cp(new BorderLayout(), sp.com(sp.line(sp.color), sp.em(8, 5, 8, 15)), sp.gray);
	Main(){
		setFrame("메인", 600, 550, ()->{});
	}
	@Override
	protected void desing() {
		northPanel.add(new sp.cp(new FlowLayout(FlowLayout.LEFT), null, null) {{
			add(new JLabel(sp.getImg("icon/cat.png", 70, 40)));
			add(new sp.cl("알바캣").font(sp.font(1, 20)).fontColor(sp.color));
		}}, sp.n);
		northPanel.add(new sp.cp(new GridLayout(0, 4), null, sp.color) {{
			for(int i = 0; i < 4; i++) {
				but[i] = new sp.cb(butString[i]).BackColor(sp.color).font(sp.font(0, 15)).fontColor(Color.white).setHo(JButton.LEFT).size(100, 50).setting();
				but[i].setBorderPainted(false);
				add(but[i]);
			}
		}}, sp.c);
		add(northPanel, sp.n);
		
		
		
		brandPanel.add(new sp.cl("인기 브랜드 TOP 5").setHo(JLabel.LEFT), sp.n);
		brandPanel.add(new sp.cp(new GridLayout(0, 5, 10,10), sp.em(10, 15, 5, 10), sp.gray) {{
			List<Row> list = Query.Main_brandTOP5.select();
			list.forEach(row -> {
				add(new sp.cl(sp.getImg("brand/" + row.getInt(0) + ".png", 50, 50)).setBorders(sp.line));
			});
		}});
		setabtPanel();
		Timer timer = new Timer(2000, e->{
			setabtPanel();
		});
		timer.start();
		this.addWindowListener(new WindowAdapter() { @Override public void windowClosed(WindowEvent e) { timer.stop(); } });
		centerPanel.add(new sp.cp(new BorderLayout(10,10), sp.em(8, 8, 8, 8), null) {{
			add(brandPanel);
			add(abtPanel,sp.e);
		}}, sp.n);
		
		
		
		
		jobPanel.add(new sp.cl("추천 알바").setHo(JLabel.LEFT), sp.n);
		Integer[] rand = new Integer[6];
		for(int i = 0; i < 6; i++) rand[i] = (int) ((Math.random() * 500) + 1);
		for(int i = 0; i < 5; i++) while(rand[5] == rand[i]) rand[i] = (int) ((Math.random() * 500) + 1);
		List<Row> list = Query.Main_bestJob.select(rand);
		jobPanel.add(new sp.cp(new GridLayout(2, 3, 10, 10), sp.em(10, 30, 10, 30), sp.gray) {{
			list.forEach(row -> {
				add(new sp.cp(new BorderLayout(), sp.com(sp.line(sp.color), sp.em(20, 0, 20, 0)), null) {{
					
				}});
			});
		}});
		
		centerPanel.add(jobPanel, sp.c);
		
		
		
		add(centerPanel);
	}
	public void setabtPanel() {
		abtPanel.removeAll();
		final int number = (int) ((Math.random() * 200) + 1);
		abtPanel.add(new sp.cl(sp.getImg("advertise/" + number + "-1.jpg", 200, 100)));
		abtPanel.removeMouseListener(ma);
		ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		};
		abtPanel.addMouseListener(ma);
		repaints();
	}
	@Override
	protected void action() {
		
	}
	public static void main(String[] args) {
		new Main();
	}
}
