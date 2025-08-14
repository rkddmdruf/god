package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import utils.*;

public class Main extends BaseFrame{

	List<Row> list = Query.symptom.select();
	List<Row> category = Query.category.select();
	List<JButton> but = new ArrayList<JButton>();
	String[] str = "마이홈,고객센터,분석,지도".split(",");
	JButton[] northBut = new Cb[4];
	JPanel borderPanel = new JPanel(new BorderLayout()) {{
		setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
	}};
	
	JPanel northPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
	}};
	
	JPanel mainPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
	}};
	
	JPanel categoryPanel = new JPanel(new GridLayout(0, 4)) {{
		setBackground(Color.white);
	}};
	
	JPanel southPanel = new JPanel(new BorderLayout());
	JPanel symptomPanel = new JPanel(new FlowLayout(0, 30, 10)) {{
		for(Row row : list) 
			this.add(new JLabel(row.getString(1)) {{
				setForeground(new Color(150, 200, 255));
				setFont(sp.fontM(1, 15));
			}});
	}};
	Main(){
		setFrame("메인", 500, 800, ()->{});
	}
	@Override
	protected void desing() {
		getContentPane().setBackground(Color.white);
		borderPanel.add(northPanel, sp.n);
		northPanel.add(new JLabel("Medinow", JLabel.CENTER) {{setFont(sp.fontM(1, 24));}}, sp.n);
		for(int i = 0; i < northBut.length; i++) {
			northBut[i] = new Cb(str[i]);
		}
		northPanel.add(new JPanel(new BorderLayout()) {{
			add(new JTextField() {{
				setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.red));
			}});
			add(new JButton("검색"), sp.e);
		}});
		northPanel.add(new JPanel(new GridLayout(0, 4)) {{
			setBackground(Color.white);
			for(JButton b : northBut) {
				add(b);
			}
		}}, sp.s);
		
		
		borderPanel.add(mainPanel, sp.c);
		mainPanel.add(new JLabel("진료 과목") {{
			setFont(sp.fontM(1, 17));
		}}, sp.n);
		setcategory();
		mainPanel.add(new JScrollPane(categoryPanel) {{
			this.getVerticalScrollBar().setUnitIncrement(25);
		}});
		
		
		
		borderPanel.add(southPanel, sp.s);
		southPanel.add(new JLabel("증상") {{setFont(sp.fontM(1, 17));}}, sp.n);
		southPanel.add(symptomPanel);
		add(borderPanel);
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
				System.out.println(str[index]);
			});
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
