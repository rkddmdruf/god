package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import utils.*;
import utils.sp.cl;
import utils.sp.cp;

public class BrandInf extends BaseFrame{
	List<Row> list;
	List<Row> job;
	List<JPanel> jobPanel = new ArrayList<JPanel>();
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(10, 10, 10, 10), Color.white);
	JPanel northPanel = new sp.cp(new BorderLayout(), null, Color.white);
	JPanel centerPanel = new sp.cp(new BorderLayout(), null, Color.white);
	JPanel southPanel = new sp.cp(new BorderLayout(), sp.em(7, 10, 7, 10), Color.black);
	
	JPanel joblist = new sp.cp(new GridLayout(0, 1), null, Color.white);
	
	
	int brand, number = ((int) (Math.random() * 200) + 1);
	int clickJob = -1, doubleClick = 0;
	public BrandInf(int brand) {
		this.brand = brand;
		setFrame("브랜드 정보", 520, 520, ()->{
			new Main();
		});
	}
	@Override
	protected void desing() {
		list = Query.BrandInf.select(brand);
		job = Query.BrandInfJob.select(brand);
		northPanel.add(new sp.cp(new BorderLayout(), null, Color.white) {{
			add(new sp.cl(sp.getImg("brand/" + brand + ".png", 100, 100)).setBorders(sp.line(sp.color)), sp.w);
			add(new sp.cl("  [" + list.get(0).getString(5) + "] ") {{
				setVerticalAlignment(JLabel.TOP);
			}}.font(sp.font(1, 14)));
		}}, sp.w);
		northPanel.add(new sp.cl("     " + list.get(0).getString(1)).font(sp.font(1, 24)).setHo(JLabel.LEFT));
		borderPanel.add(northPanel, sp.n);
		
		JLabel label = new sp.cl(sp.getImg("지도.png", 300, 275)) {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.red);
				g.fillOval((int) (list.get(0).getInt(3) * (300.0 / 500.0)), (int) (list.get(0).getInt(4) * (275.0 / 500.0)), 7,7);
			}
		};
		label.setBounds(0 ,20, 300, 275);
		centerPanel.add(new sp.cp(null, null, null) {{
			add(label);
			
		}}, sp.c);
		
		job.forEach(row -> {
			jobPanel.add(new sp.cp(new BorderLayout(), sp.line, Color.white) {{
				add(new JTextArea(row.getString(1)) {{
					setLineWrap(true);
					setEditable(false);
					setFocusable(false);
				}});
			}}.size(100, 50));
			joblist.add(jobPanel.get(jobPanel.size()-1));
		});
		
		centerPanel.add(new sp.cp(new BorderLayout(), null, Color.white) {{
			add(new sp.cl("총 " + job.size() + "건의 알바가 있어요!").font(sp.font(1, 16)).fontColor(sp.color), sp.n);
			add(new JPanel(new BorderLayout()) {{
				JScrollPane sc = new JScrollPane(joblist);
				sc.getVerticalScrollBar().setUnitIncrement(20);
				sc.getVerticalScrollBar().setValue(0);
				sc.setColumnHeaderView(new sp.cp(new BorderLayout(), null, sp.color) {{
					add(new sp.cl(list.get(0).getString(1) + " 알바 목록").setHo(JLabel.CENTER).fontColor(Color.white));
				}});
				add(sc);
			}});
		}}, sp.e);
		
		borderPanel.add(centerPanel);
		add(borderPanel);
		
		setadvertise();
		Timer timer = new Timer(2000, e->{
			number = ((int) (Math.random() * 200) + 1);
			setadvertise();
			System.out.println("sdf");
		});
		timer.start();
		addWindowListener(new WindowAdapter() { @Override public void windowClosed(WindowEvent e) { timer.stop(); } });// 타이버 스탑
		add(southPanel, sp.s);
	}

	@Override
	protected void action() {
		for(int j = 0; j < job.size(); j++) {final int i = j;
			jobPanel.get(i).getComponent(0).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(i == clickJob) {
						new JobInfor(job.get(i).getInt(0));
						dispose();
					}
					if(clickJob < 0) clickJob = 0;// 처음 클릭에 넘어가는거 막는거 -> -1
					jobPanel.get(clickJob).getComponent(0).setBackground(Color.white);
					jobPanel.get(i).getComponent(0).setBackground(Color.yellow);
					clickJob = i;
				}
			});
		}
	}
	private void setadvertise() {
		southPanel.removeAll();
		List<Row> gg = Query.Gwoung_go.select(number);
		southPanel.add(new sp.cl(sp.getImg("advertise/" + number +"-1.jpg", 70,45)) {{
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new Gwoung_go(number);
				}
			});
		}}.setBorders(sp.line), sp.w);
		southPanel.add(new sp.cl("  " + gg.get(0).getString(1)).font(sp.font(Font.ITALIC, 12)).fontColor(Color.white));
		RePaint();
	}
	public static void main(String[] args) {
		new BrandInf(1);
	}
}
