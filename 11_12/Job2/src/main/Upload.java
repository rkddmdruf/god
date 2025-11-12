package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import utils.*;
import utils.sp.cb;
import utils.sp.cp;
import utils.sp.cl;

public class Upload extends BaseFrame{
	int selectBut = 0;
	Polygon pol;
	JButton[] but = new JButton[2];
	
	JComboBox<String> category = new JComboBox<String>() {{
		addItem("카테고리"); 
		for(Row row : Query.selectText("select cname from category")) addItem(row.getString(0));
	}};
	List<Row> brandNumber = new ArrayList<>();
	JComboBox<String> brand = new JComboBox<String>() {{ addItem("브랜드"); setEnabled(false); setPreferredSize(new Dimension(140, 27));}};
	JComboBox<String> jwork = new JComboBox<String>("고용 형태,정규직,계약직".split(","));
	JComboBox<String> jgrade = new JComboBox<String>("지원 자격,무관,고졸,대학".split(","));
	
	String[] tf_em = "급여(월 단위),모집 인원,근무 요일,근무 시간".split(",");
	JTextField[] tf = new JTextField[4];
	
	JLabel brandImgLabel = new sp.cl("브랜드를 선택해주세요.").fontColor(Color.LIGHT_GRAY).font(sp.font(1, 12)).setHo(JLabel.CENTER);
	JTextArea title = new sp.cta("") {
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if(!this.getText().isEmpty()) return;
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("제목", 0, 13);
		}
	{setBorder(sp.line); size(275, 55);}};
	JTextArea advertise = new sp.cta("") {
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if(!this.getText().isEmpty()) return;
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("내용", 0, 23);
		}
	{setBorder(sp.com(sp.em(10, 0, 0, 0), sp.line)); size(275, 200);}};
	
	
	JPanel northPanel = new cp(new BorderLayout(), sp.em(10, 10, 5, 10),null);
	JPanel southPanel = new sp.cp(new FlowLayout(FlowLayout.LEFT, 20, 0), sp.em(10, -15, 10, 10), null) {{
		this.add(category); this.add(brand);
	}};
	JPanel mainPanel = new cp(new BorderLayout(), sp.em(0, 10, 10, 10), null);
	JPanel westPanel = new cp(new BorderLayout(), null, null);
	JPanel eastPanel = new cp(new BorderLayout(), null, null);
	///////////////////////////////////////////////////////////////////////////// 이거 아래는 광고에서만 사용
	public Upload() {
		setFrame("등록하기", 600, 375, ()->{});
	}

	@Override
	protected void desing() {
		for(int i = 0; i < 4; i++) {final int index = i;
			tf[i] = new JTextField() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					g.setColor(Color.LIGHT_GRAY);
					if(this.getText().isEmpty()) {
						g.drawString(tf_em[index], 5, 20);
					}
				}
			};
			tf[i].setPreferredSize(new Dimension(100,30));
		}
		for(int i = 0; i < 2; i++) but[i] = new sp.cb(i == 0 ? "알바" : "광고").setBorders(sp.line(sp.color)).font(sp.font(0, 14)).size(100, 30);
		
		setBut();
		northPanel.add(new sp.cp(new FlowLayout(FlowLayout.LEFT), null, null) {{ add(but[0]);	add(but[1]); }}, sp.w);
		northPanel.add(new sp.cp(new BorderLayout(), null, null) {
			@Override
			public void paintComponent(Graphics g) {
				g.setColor(Color.red);
				int x_plus = 190;
				int[] x = {0 + x_plus, 75 + x_plus, 150 + x_plus};
				int[] y = {40, 5, 40};
				pol = new Polygon(x, y, 3);
				g.fillPolygon(pol);
				g.setColor(Color.white);
				g.drawString("업로드", x_plus+ 57, 30);
			}
		});
		setjobPanel();
		
		add(northPanel, sp.n);
		mainPanel.add(westPanel, sp.w);
		mainPanel.add(eastPanel, sp.e);
		add(mainPanel);
	}

	
	private void setjobPanel() {
		westPanel.removeAll();
		eastPanel.removeAll();
		mainPanel.remove(southPanel);
		title.setPreferredSize(new Dimension(275, 55));
		eastPanel.add(title, sp.n);
		westPanel.add(new sp.cp(new BorderLayout(), sp.com(sp.em(0, 5, 0, 0), sp.line), null) {{
			brandImgLabel.setText(selectBut == 0 ? "브랜드를 선택해주세요." : "");
			add(brandImgLabel);
			size(275,10);
		}});
		if(selectBut == 0) {
			mainPanel.add(southPanel, sp.s);
			eastPanel.add(new sp.cp(new GridLayout(0, 2), null, null) {{
				add(new cl("근무조건").font(sp.font(1, 16)));
				add(new cl("모집 조건").font(sp.font(1, 16)));
			}});
			eastPanel.add(new sp.cp(new GridLayout(0, 2,10, 10), null, null) {{
				int j = 0;
				for(int i = 0; i < 6; i++) {
					if(i == 3) add(jwork);
					else if(i == 5) add(jgrade);
					else add(tf[j++]);
				}
			}}, sp.s);
		}else {
			eastPanel.add(advertise, sp.c);
		}
		
		
		
		
		repaints();
	}
	@Override
	protected void action() {
		JPanel p = (JPanel) getContentPane().getComponent(0);
		p.getComponent(1).addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(pol.contains(e.getPoint())) {
					if(title.getText().isEmpty()) {sp.ErrMes("빈칸이 존재합니다."); return;}
					if(selectBut == 1) {
						Query.update("insert into advertise values(0, ? , ?);", title.getText(), advertise.getText());
					}// 
					if(selectBut == 0) {
						if(category.getSelectedIndex() == 0) 			 {sp.ErrMes("빈칸이 존재합니다."); return;}
						if(jwork.getSelectedIndex() == 0) 				 {sp.ErrMes("빈칸이 존재합니다."); return;}
						if(jgrade.getSelectedIndex() == 0) 				 {sp.ErrMes("빈칸이 존재합니다."); return;}
						for(JTextField t : tf) if(t.getText().isEmpty()) {sp.ErrMes("빈칸이 존재합니다."); return;}
						Query.update("insert into job values(0, ?, ?, ?, ?, ?, ?, ? ,?);"
						, title.getText(), brandNumber.get(brand.getSelectedIndex()).get(0), tf[0].getText(), tf[1].getText(), tf[2].getText(), tf[3].getText()
						, jwork.getSelectedIndex(), jgrade.getSelectedIndex());
					}
					sp.InforMes("업로드가 완료되었습니다.");
				}
			}
		});
		
		category.addItemListener(e->{
			if(e.getStateChange() != ItemEvent.SELECTED) return;
			brand.removeAllItems();
			if(category.getSelectedIndex() == 0) {
				brand.addItem("브랜드");
				brandImgLabel.setIcon(null);
				brandImgLabel.setText("브랜드를 선택해주세요.");
				brand.setEnabled(false);
				return;
			}
			brandNumber = Query.selectText("select * from brand where cno = ?", category.getSelectedIndex());
			for(Row row : brandNumber) brand.addItem(row.getString(1));
			brand.setEnabled(true);
			setImages();
		});
		brand.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				setImages();
			}
		});
		for(int i = 0; i < 2; i++) { final int index = i;
			but[i].addActionListener(e->{
				selectBut = index;
				brandImgLabel.setIcon(null);
				brandImgLabel.setText("브랜드를 선택해주세요.");
				category.setSelectedIndex(0);
				title.setText("");
				advertise.setText("");
				for(JTextField t : tf) t.setText("");
				jwork.setSelectedIndex(0);
				jgrade.setSelectedIndex(0);
				setjobPanel();
				setBut();
			});
		}
	}
	private void setBut() {
		for(JButton b : but) {
			b.setBackground((b == but[selectBut] ? sp.color : Color.white));
			b.setForeground((b == but[selectBut] ? Color.white : sp.color));
		}
	}
	private void setImages() {
		brandImgLabel.setIcon(sp.getImg("brand/" + brandNumber.get(brand.getSelectedIndex()).getInt(0) + ".png", 280, 225));
	}
	public static void main(String[] args) {
		new Upload();
	}
}
