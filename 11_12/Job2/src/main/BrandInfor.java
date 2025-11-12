package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.util.List;
import utils.*;
import utils.sp.cl;
import utils.sp.cp;

public class BrandInfor extends BaseFrame{
	int w, h, r;
	Row brand;
	List<Row> jobs;
	Timer timer;
	JLabel label = new sp.cl("").font(sp.font(Font.ITALIC, 12)).fontColor(Color.white);
	JPanel borderPanel = new sp.cp(new BorderLayout(10, 10), sp.em(5, 5, 5, 5), null);
	JPanel southPanel = new sp.cp(new BorderLayout(), sp.em(5, 7, 5, 5), Color.black) {{
		timer = new Timer(0, e->{
			r = (int) ((Math.random() * Query.selectText("select count(ano) from advertise").get(0).getInt(0)) + 1);
			label.setIcon(sp.getImg("advertise/" + r + "-1.jpg", 80, 50));
			label.setText("   " + Query.selectText("select * from advertise where ano = ?", r).get(0).getString(1));
			repaints();
			System.out.println("sdf");
		});
		timer.start();
		timer.setDelay(2000);
		add(label, sp.w);
	}};
	JPanel northPanel = new sp.cp(new BorderLayout(), null, null);
	JPanel mainPanel = new sp.cp(new BorderLayout(), null, null);
	JPanel scrollPanel = new sp.cp(new GridLayout(0,1), null, null);
	JScrollPane sc;
	
	public BrandInfor(int bno) {
		brand = Query.BrandInfor_getBrand.select(bno).get(0);
		jobs = Query.BrandInfor_getJobs.select(bno);
		w = (brand.getInt(3) / 2) -4;
		h = (int) (brand.getInt(4) * (275.0 / 500.0)) -4;
		setFrame("브랜드 정보", 525, 550, ()->{});
	}

	@Override
	protected void desing() {
		int n = 120;
		northPanel.add(new sp.cl(sp.getImg("brand/" + brand.getInt(0) + ".png", n, n)).size(n, n).setBorders(sp.line(sp.color)), sp.w);
		northPanel.add(new sp.cl("  [" + brand.getString(5) + "]") {{ setVerticalAlignment(JLabel.TOP); }}.font(sp.font(1, 14)));
		northPanel.add(new sp.cl(brand.getString(1) + "                  ").setHo(JLabel.LEFT).font(sp.font(1, 25)), sp.e);
		borderPanel.add(northPanel, sp.n);
		
		mainPanel.add(new sp.cp(null, sp.em(0, 0, 0, 8), null) {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(sp.getImg("지도.png", 300, 275).getImage(), 0, 0, 300, 275, null);
				g.setColor(Color.red);
				g.fillOval(w, h, 8, 8);
				
			}
		});
		for(int i = 0; i < jobs.size(); i++) 
			scrollPanel.add(new sp.cta(jobs.get(i).getString(1)) {{ setBorder(sp.line); }}.setting().size(0, 50));
		mainPanel.add(sc = new JScrollPane(scrollPanel) {{
			setPreferredSize(new Dimension(180, 30));
			getVerticalScrollBar().setUnitIncrement(20);
			setColumnHeaderView(new sp.cp(new FlowLayout(), sp.line, sp.color) {{
				add(new sp.cl(brand.getString(1) + "알바 목록").font(sp.font(1, 13)).fontColor(Color.white));
			}});
		}}, sp.e);
		mainPanel.add(new sp.cl("총 " + jobs.size() + "건의 알바가 있어요!").font(sp.font(1, 16)).fontColor(sp.color).setHo(JLabel.RIGHT), sp.n);
		borderPanel.add(mainPanel);
		
		add(borderPanel);
		add(southPanel, sp.s);
	}

	@Override
	protected void action() {
		southPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new GwoungGo(r);
				dispose();
			}
		});
		int counts = scrollPanel.getComponentCount();
		for(int i = 0; i < counts; i++) { final int index = i;
			scrollPanel.getComponent(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					for(int j = 0; j < counts; j++)  scrollPanel.getComponent(j).setBackground(j == index ? Color.yellow : Color.white);
					if(e.getClickCount() == 2) {
						new JobInfor(jobs.get(index).getInt(0));
						dispose();
					}
				}
			});
		}
	}
	public static void main(String[] args) {
		new BrandInfor(1);
	}
}
