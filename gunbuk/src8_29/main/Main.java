package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import utils.*;

public class Main extends BaseFrame{
	JFrame f = new JFrame();
	List<Row> list = Query.symptom.select();
	List<Row> category = Query.category.select();
	List<Row> hospitalList = Query.hospital.select();
	List<JButton> but = new ArrayList<JButton>();
	List<JButton> hospitalBut = new ArrayList<JButton>();
	String[] str = "마이홈,고객센터,분석,지도".split(",");
	JButton[] northBut = new Cb[4];
	Color NomalColor = new Color(150, 200, 220);
	int X = 0;
	int XX = 0;
	int x = 0;
	JTextField tf = new JTextField() {{
		setPreferredSize(new Dimension(300, 40));
		setBorder(BorderFactory.createMatteBorder(0, 0, 1,0 , NomalColor));
	}};
	JButton serch = new JButton("검색") {{
		setForeground(Color.white);
		setBackground(NomalColor);
		setPreferredSize(new Dimension(80, 40));
	}};
	
	JPanel borderPanel = new JPanel(new BorderLayout()) {{
		setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
	}};
	
	JPanel northPanel = sp.Cjp(new BorderLayout());
	JPanel northInPanel = sp.Cjp(new BorderLayout());
	
	
	JPanel categoryPanel = sp.Cjp(new GridLayout(0,4));
	JPanel mainPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		add(new JScrollPane(categoryPanel) {{
			this.setBorder(null);
			this.getVerticalScrollBar().setUnitIncrement(25);
		}});
	}};
	
	JPanel southPanel = sp.Cjp(new BorderLayout());
	JPanel symptomPanel = new JPanel(null) {{
		setPreferredSize(new Dimension(300000000, 50));
		setBackground(Color.white);
		for(int i = 0; i < list.size(); i++) {final int index = i;
			this.add(new JLabel() {{
				setText(list.get(index).getString(1));
				setBackground(Color.blue);
				int size = list.get(index).getString(1).toCharArray().length*15 + 40;
				setBounds(X, 0, size, 30);
				setForeground(NomalColor);
				setFont(sp.fontM(1, 16));
			}});
			
			X = X + 100 + list.get(index).getString(1).toCharArray().length * 10;
		}
	}};
	
	JPanel hospital = new JPanel(new FlowLayout()) {{
		setBackground(Color.white);
	}};
	Main(){
		setFrame("메인", 500, 800, ()->{});
	}
	@Override
	protected void desing() {
		f.setTitle("메인");
		f.setIconImage(new ImageIcon("src/logo.png").getImage());
		f.setSize(500, 200);
		getContentPane().setBackground(Color.white);
		
		northInPanel.add(new JLabel("Medinow", JLabel.CENTER) {{setFont(sp.fontM(1, 24));}}, sp.n);
		for(int i = 0; i < northBut.length; i++) {
			northBut[i] = new Cb(str[i]);
			northBut[i].setFont(sp.fontM(1, 17));
		}
		northInPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
			setBackground(Color.white);
			add(tf);
			add(serch);
		}});
		northInPanel.add(new JPanel(new GridLayout(0, 4)) {{
			setBackground(Color.white);
			for(JButton b : northBut) {
				add(b);
			}
		}}, sp.s);
		northPanel.add(northInPanel);
		northPanel.add(new JPanel(new BorderLayout()) {{
			setBackground(Color.white);
			add(new JLabel("병원") {{setFont(sp.fontM(1, 17));}}, sp.n);
			add(hospital, sp.c);
		}}, sp.s);
		mainPanel.add(new JLabel("진료 과목") {{
			setFont(sp.fontM(1, 17));
		}}, sp.n);
		setHospital(hospitalList);
		setcategory();
		
		
		borderPanel.add(northPanel, sp.n);
		borderPanel.add(mainPanel, sp.c);
		borderPanel.add(southPanel, sp.s);
		southPanel.add(new JLabel("증상") {{setFont(sp.fontM(1, 17));}}, sp.n);
		southPanel.add(symptomPanel, sp.c);
		borderPanel.setBackground(Color.white);
		add(borderPanel);revalidate();
	}

	@Override
	protected void action() {
		for(int i = 0; i < but.size(); i++) {final int index = i;
			but.get(i).addActionListener(e->{
				System.out.print(category.get(index).getString(1));
			});
		}
		for(int i = 0; i < northBut.length; i++) {final int index = i;
			northBut[i].addActionListener(e->{
				switch (index) {
					case 0: {new myHome();}
					//case 1: {new userCenter();}
					//case 2: {new chat();}
					//case 3: {new map();}
					dispose();
					break;
				}
			});
		}
		getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				if(e.getX() < 0 || e.getX() >= 484 || e.getY() < 0 || e.getY() >= 761);
			}
		});
		
	}
	
	private void setHospital(List<Row> list) {
		hospital.removeAll();
		for(Row row : list) {
			hospital.add(new JPanel(new BorderLayout()){{
				setBackground(Color.white);
				hospitalBut.add(new Cb(new ImageIcon(new ImageIcon("src/hospital/" + row.getInt(0) + ".png").getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH))) {{
					setPreferredSize(new Dimension(100, 50));
				}});
				add(hospitalBut.get(hospitalBut.size()-1));
				add(new JLabel(row.getString(1)), sp.s);
			}});
		}
	}
	private void setcategory() {
		but.clear();
		categoryPanel.removeAll();
		for(int i = 0; i < category.size(); i++) {final int index = i;
			but.add(new Cb() {{
				setPreferredSize(new Dimension(90, 110));
				add(new JPanel(new BorderLayout()) {{
					setBackground(Color.white);
					add(new JLabel(new ImageIcon(new ImageIcon("src/category/" + (index+1) + ".png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH))), sp.n);
					add(new JLabel(category.get(index).getString(1)) {{setHorizontalAlignment(JLabel.CENTER);}});
				}});
			}});
			categoryPanel.add(but.get(index));
		}
	}
	public static void main(String[] args) {
		new Main();
	}
}
