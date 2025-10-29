package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.cl;
import utils.sp.cp;

public class UpLoad extends BaseFrame{
	List<Row> list = new ArrayList<>();
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(10, 10, 10,10), null);
	JPanel northPanel = new sp.cp(new BorderLayout(), null, null);
	JPanel WestPanel = new sp.cp(new BorderLayout(), null, null);
	JPanel EastPanel = new sp.cp(new BorderLayout(), null, null);
	JPanel SouthPanel = new sp.cp(new FlowLayout(0, 10,0), sp.em(15, -10, 0, 0), null);
	Polygon pol = new Polygon();
	JButton[] but = {new sp.cb("알바").setting().setBorders(sp.line(sp.color)).fontColor(Color.white).BackColor(sp.color).font(sp.font(0, 15)),
			new sp.cb("광고").setting().setBorders(sp.line(sp.color)).fontColor(sp.color).BackColor(Color.white).font(sp.font(0, 15))};
	
	boolean emptyCheck = false;
	JPanel upLoding = new sp.cp(null,null,null);
	JLabel brandLabel = new sp.cl("브랜드를 선택해주세요").fontColor(Color.LIGHT_GRAY).setBorders(sp.line).setHo(JLabel.CENTER);
	JLabel ggImage = new sp.cl("").setBorders(sp.line);
	JTextArea title = new JTextArea() {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(getText().isEmpty()) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawString("제목", 0, 15);
			}
		}
	};
	JTextArea 내용 = new JTextArea() {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(getText().isEmpty()) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawString("내용", 0, 15);
			}
		}
	};
	
	
	String[] tfs = "급여(월 단위),모집 인원,근무요일,근무시간".split(",");
	JTextField[] tf = new JTextField[4];
	JComboBox<String> category = new JComboBox<String>() {{
		addItem("카테고리");
		for(Row row : Query.category.select()) addItem(row.getString(1));
		setPreferredSize(new Dimension(100, 28));
	}};
	JComboBox<String> brand = new JComboBox<String>() {{
		addItem("브랜드");
		setEnabled(false);
		setPreferredSize(new Dimension(200, 30));
	}};
	JComboBox<String> jwork = new JComboBox<String>("고용형태,정규직,계약직".split(","));
	JComboBox<String> jgrade = new JComboBox<String>("지원 자격,무관,대학,고등".split(","));
	UpLoad(){
		setFrame("등록하기", 700, 440, ()->{new AdminMain();});
	}
	@Override
	protected void desing() {
		northPanel.add(new sp.cp(new FlowLayout(FlowLayout.LEFT), sp.em(0, -5, 0, 0), null) {{
			for(JButton b : but) {
				b.setContentAreaFilled(true);
				b.setPreferredSize(new Dimension(100, 30));
				add(b);
			}
		}}, sp.w);
		upLoding.add(new up() {{
			setBounds(0,0,700,50);
		}});
		northPanel.add(upLoding, sp.c);
		
		ggImage.setPreferredSize(new Dimension(350, 70));
		brandLabel.setPreferredSize(new Dimension(350, 70));
		title.setPreferredSize(new Dimension(300, 80));
		title.setBorder(sp.line);
		title.setLineWrap(true);
		내용.setBorder(sp.line);
		내용.setLineWrap(true);
		
		setCenterPanel(0);
		borderPanel.add(northPanel, sp.n);
		borderPanel.add(WestPanel,sp.w);
		borderPanel.add(EastPanel,sp.e);
		borderPanel.add(SouthPanel, sp.s);
		
		add(borderPanel);
	}
	
	public void setCenterPanel(int number) {
		if(number == 0) {
			setjopPanel();
			return;
		}
		set광고Panel();
	}
	private void set광고Panel() {
		WestPanel.removeAll();
		EastPanel.removeAll();
		SouthPanel.removeAll();
		title.setText("");
		category.setSelectedIndex(0);
		for(int i = 0; i < 4; i++) {
			tf[i].setText("");
		}
		jgrade.setSelectedIndex(0);
		jwork.setSelectedIndex(0);
		
		WestPanel.add(ggImage);
		EastPanel.add(new sp.cp(new BorderLayout(10,10), null, null) {{
			add(title, sp.n);
			add(내용);
		}});
		
		
		
		RePaint();
	}
	private void setjopPanel() {
		WestPanel.removeAll();
		EastPanel.removeAll();
		WestPanel.add(brandLabel);
		
		
		EastPanel.add(title, sp.n);
		EastPanel.add(new sp.cp(new FlowLayout(0,60,0), sp.em(20, -60, 0, 0), null) {{
			Font font = sp.font(1, 17);
			add(new sp.cl("근무 조건").font(font));
			add(new sp.cl("모집 조건").font(font));
		}});
		EastPanel.add(new sp.cp(new GridLayout(0,2, 15,10), null, null) {{
			setPreferredSize(new Dimension(40, 160));
			for(int i = 0; i < 4; i++) {final int index = i;
				tf[i] = new JTextField() {
					@Override
					public void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.setColor(Color.LIGHT_GRAY);
						g.setFont(sp.font(0, 16));
						if(getText().isEmpty()) g.drawString(tfs[index], 0, 30);
					}
				};
				tf[i].setFont(sp.font(0, 16));
			}
			
			add(tf[0]);
			add(tf[1]);
			add(tf[2]);
			add(jwork);
			add(tf[3]);
			add(jgrade);
			
		}}, sp.s);
		
		SouthPanel.add(category);
		SouthPanel.add(brand);
		RePaint();
	}
	
	@Override
	protected void action() {
		upLoding.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(pol.contains(e.getX(), e.getY())) {
					if(but[0].getBackground() == sp.color) {
						for(JTextField tf : tf) {
							if(tf.getText().isEmpty()) emptyCheck = true;
						}
						if(title.getText().isEmpty()) emptyCheck = true;
						if(category.getSelectedItem().toString().equals("카테고리")) emptyCheck = true;
						if(jgrade.getSelectedIndex() == 0 || jwork.getSelectedIndex() == 0) emptyCheck = true;
						if(emptyCheck) {
							sp.ErrMes("빈칸이 존재합니다");
							emptyCheck = false;
							return;
						}
						Query.UpLoadjob.update(title.getText(), list.get(brand.getSelectedIndex()).getInt(0),
								tf[0].getText(), tf[1].getText(),tf[2].getText(),tf[3].getText(),
								jwork.getSelectedIndex(), jgrade.getSelectedIndex());
						sp.InforMes("업로드가 완료되었습니다");
					}
					if(but[1].getBackground() == sp.color) {
						//if(ggImage.getIcon() == null) return;// 이거 해야함
						if(title.getText().isEmpty()) emptyCheck = true;
						if(내용.getText().isEmpty()) emptyCheck = true;
						if(emptyCheck) {
							sp.ErrMes("빈칸이 존재합니다");
							emptyCheck = false;
							return;
						}
						Query.UpLoad광고.update(title.getText(), 내용.getText());
						sp.InforMes("업로드가 완료되었습니다");
					}
				}
			}
		});
		for(int j = 0; j < 2; j ++) {final int i = j;
			but[j].addActionListener(e->{
				if(but[i].getBackground() == Color.white) {
					but[i].setBackground(sp.color);
					but[i].setForeground(Color.white);
					but[i == 0 ? 1 : 0].setBackground(Color.white);
					but[i == 0 ? 1 : 0].setForeground(sp.color);
					setCenterPanel(i);
				}
			});
		}
		
		category.addActionListener(e->{
			if(category.getSelectedIndex() == 0) {
				brandLabel.setIcon(null);
				brandLabel.setText("브랜드를 선택해주세요.");
				brand.removeAllItems();
				brand.addItem("브랜드");
				brand.setEnabled(false);
				return;
			}
			list =  Query.UpLoad.select(category.getSelectedIndex());
			brand.removeAllItems();
			
			for(Row row : list) {
				brand.setEnabled(true);
				brand.addItem(row.getString(1));
			}
		});
		brand.addActionListener(e->{
			if(category.getSelectedItem() == "카테고리") return;
			if(brand.getSelectedIndex() != -1)
			brandLabel.setIcon(sp.getImg("src/brand/" + list.get(brand.getSelectedIndex()).getInt(0) + ".png", 360, 300));
		});
	}
	class up extends JPanel{
		@Override
		public void paintComponent(Graphics g) {
			int[] x = {380, 310 , 450};
			int[] y = {0, 35, 35};
			pol = new Polygon(x,y,3);
			g.setColor(Color.red);
			g.fillPolygon(x, y, 3);
			g.setColor(Color.white);
			g.drawString("업로드", 363, 25);
		}
	}
	public static void main(String[] args) {
		new UpLoad();
	}
}
