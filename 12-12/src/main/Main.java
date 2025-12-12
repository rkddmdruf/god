package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class Main extends BaseFrame{

	JPanel northPanel = new cp(new BorderLayout(), null, null) {{
		add(new cp(new FlowLayout(FlowLayout.LEFT), null, null) {{
			add(new cl(sp.getImg("datafiles/icon/cat.png", 70, 35)));
			add(new cl("알바캣").fontColor(sp.color).font(sp.font(1, 24)));
		}}, sp.n);
	}};
	String[] butString = "채용, 브랜드, 찾기, 마이페이지".split(",");
	JButton[] but = new cb[4];
	
	JLabel advertise = new cl("").size(200, 125).setBorders(sp.line);
	JPanel mainPanel = new cp(new BorderLayout(5,5), sp.em(5, 5, 5, 5), null);
	int n = 0;
	Main(){
		setFrame("메인", 625, 575, ()->{});
	}
	@Override
	protected void desing() {
		JPanel butPanel = new cp(new GridLayout(0,4), null, sp.color);
		for(int i = 0; i < 4; i++) {
			but[i] = new cb(butString[i]) {{
				setFocusable(false);
				setContentAreaFilled(false);
				setBorderPainted(false);
			}}.backColor(sp.color).fontColor(Color.white).setHo(JLabel.LEFT).font(sp.font(0, 16)).size(0, 50);
			butPanel.add(but[i]);
		}
		northPanel.add(butPanel);
		add(northPanel, sp.n);
		
		JPanel mainNorthPanel = new cp(new BorderLayout(5,5), null ,null);
		mainNorthPanel.add(new cp(new BorderLayout(5,5), sp.com(sp.line, sp.em(10, 5, 10, 7)), sp.gray) {{
			add(new cl("인기 브랜드 TOP 5").setHo(JLabel.LEFT), sp.n);
			JPanel p = new cp(new FlowLayout(FlowLayout.LEFT), null, sp.gray);
			for(Row row : Query.Main_top5.select()) {
				p.add(new cl(sp.getImg("datafiles/brand/" + row.getInt(1) + ".png", 65, 65)).setBorders(sp.line));
			}
			add(p);
		}});
		Timer t = new Timer(0, e-> { setImg(); });
		t.start();
		t.setDelay(2000);
		WindowAdapter a = new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				t.stop();
				removeWindowListener(this);
			}
		};
		addWindowListener(a);
		mainNorthPanel.add(advertise, sp.e);
		mainPanel.add(mainNorthPanel, sp.n);
		
		
		JPanel mainCenterPanel = new cp(new BorderLayout(), sp.com(sp.line(sp.color), sp.em(10, 5, 5, 5)), sp.gray);
		mainCenterPanel.add(new cl("추천 알바").setHo(JLabel.LEFT), sp.n);
		mainCenterPanel.add(new cp(new GridLayout(0, 3, 20, 15), sp.em(10, 30, 3, 30), sp.gray) {{
			List<Row> list = Query.Main_get6Job.select(Query.Main_getCno.select(sp.user.get(0)).get(0).getInt(0));
			for(int i = 0; i < 6; i++) {
				int n = (int) (Math.random() * list.size());
				JPanel p = new cp(new BorderLayout(), sp.com(sp.line(sp.color), sp.em(10, 0, 10, 0)), null);
				p.add(new ca(list.get(n).getString(1)).setting().font(sp.font(1, 12)));
				p.add(new ca("[주 " + list.get(n).getString(5) + "일, " + list.get(n).getString(6) + "시간, " + (list.get(n).getInt(7) == 0 ? "정규직" : "계약직")
					+ "]\n시급: " +list.get(n).getInt(3) + "원").setting().font(sp.font(0, 11)), sp.s);
				add(p);
			}
			
		}});
		mainPanel.add(mainCenterPanel);
		add(mainPanel);
	}

	private void setImg() {
		n = (int) (Math.random() * 200 + 1);
		advertise.setIcon(sp.getImg("datafiles/advertise/" + n + "-1.jpg", 200, 125));
		rp();
	}
	@Override
	protected void action() {
		advertise.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new advertise(n);
				dispose();
			}
		});
	}
	public static void main(String[] args) {
		new Main();
	}
}
