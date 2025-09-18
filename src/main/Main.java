package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import utils.*;
public class Main extends BaseFrame{
	int number = ((int) (Math.random() * 200) + 1);
	List<Row> jobList = Query.CnoJob.select(Query.userJobCno.select(1).get(0).get(6));
	List<Row> job6List = new ArrayList<>();
	List<Row> brand = Query.Top5.select();
	String[] butName = "채용,브랜드,찾기,마이페이지".split(",");
	JButton[] but = new JButton[4];
	JPanel mainPanel = new JPanel(new BorderLayout()) {{
		setPreferredSize(new Dimension(0, 425));
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		setBackground(Color.white);
	}};
	JPanel NorthPanel = new JPanel(new GridLayout(0, 4)) {{
		setPreferredSize(new Dimension(0, 50));
		setBackground(sp.color);
	}};
	JPanel mNorthPanel = new JPanel(new BorderLayout());
	JPanel mNorthCenterPanel = new JPanel(new BorderLayout()) {{
		setBorder(sp.line);
	}};
	JPanel 광고 = new JPanel(new BorderLayout()) {{
		setPreferredSize(new Dimension(200, 100));
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
	}};
	
	JPanel jobPanel = new JPanel(new BorderLayout()) {{
		setBorder(BorderFactory.createLineBorder(sp.color));
	}};
	JPanel 알바Panel = new JPanel(new GridLayout(0, 3, 24, 12)) {{
		setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
	}};
	Main(){
		setFrame("메인", 600, 600, ()->{sp.user = new Row();});
	}
	//D 알바 없을떄 안함
	@Override
	protected void desing() {
		while(job6List.size() < 6) {
			for(Row row : jobList) {
				if(row.getInt(1) == number) {
					job6List.add(row);
				}
			}
			number = ((int) (Math.random() * 200) + 1);
		}
		//추천 알바 랜덤
		add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBackground(Color.white);
			add(new JLabel(new ImageIcon(new ImageIcon("src/icon/cat.png").getImage().getScaledInstance(100, 70, Image.SCALE_SMOOTH))));
			add(new JLabel("알바캣") {{
				setFont(sp.font(1, 20));
				setForeground(sp.color);
			}});
		}}, sp.n);
		
		
		for(int i = 0; i < 4; i++) {
			but[i] = sp.Cb(butName[i]);
			but[i].setForeground(Color.white);
			but[i].setFont(sp.font(1, 17));
			but[i].setHorizontalAlignment(JButton.LEFT);
			NorthPanel.add(but[i]);
		}
		
		
		mNorthCenterPanel.add(new JLabel("   인기 브랜드 TOP 5"), sp.c);
		mNorthCenterPanel.add(new JPanel(new FlowLayout(1,7,7)) {{
			for(int i = 0; i < 5; i ++) add(new JLabel(new ImageIcon(new ImageIcon("src/brand/" + brand.get(i).getInt(1) + ".png")
					.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH))) {{
						setBorder(sp.line);
					}});
		}}, sp.s);
		mNorthPanel.add(mNorthCenterPanel);
		
		
		setadvertise();
		Timer timer = new Timer(2000, e->{
			number = ((int) (Math.random() * 200) + 1);
			setadvertise();
		});
		timer.start();
		mNorthPanel.add(광고, sp.e);
		
		
		mainPanel.add(mNorthPanel,sp.n);
		
		
		jobPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			add(new JLabel("추천 알바"));
		}},sp.n);
		setjobsPanel(알바Panel);
		jobPanel.add(알바Panel);
		mainPanel.add(new JPanel(new BorderLayout()) {{
			setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
			setBackground(Color.white);
			add(jobPanel, sp.c);
		}});
		add(mainPanel, sp.s);
		add(NorthPanel, sp.c);
	}
	
	@Override
	protected void action() {
		for(int i = 0; i < 4; i++) {final int index = i;
			but[i].addActionListener(e->{
				switch (index) {
					case 0 :{new ceyung();	dispose();	break;}
				}
			});
		}
	}
	private void setadvertise() {
		광고.removeAll();
		광고.add(new JLabel(new ImageIcon(new ImageIcon("src/advertise/" + number + "-1.jpg")
		.getImage().getScaledInstance(190, 100, Image.SCALE_SMOOTH))) {{
			setBorder(sp.line);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new Gwoung_go(number);
				}
			});
		}});
		RePaint();
	}
	
	private void setjobsPanel(JPanel panel) {
		for(Row row : job6List) {
			panel.add(new JPanel(new BorderLayout()) {{
				setBorder(BorderFactory.createLineBorder(sp.color));
				setBackground(Color.white);
				add(new JTextArea() {{
					setjob(this, 1, 12, true, row);
				}}, sp.c);
				add(new JTextArea() {{
					setjob(this, 1, 10, false, row);
				}}, sp.s);
			}});
		}
	}
	private void setjob(JTextArea ta, int font, int size, boolean frist, Row row) {
		ta.setFont(sp.font(font, size));
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
						new JobInfor(job6List.get(i));
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
