package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.*;

import utils.*;

public class HospitalIfor extends BaseFrame{
	Row row;
	JLabel doctor = new JLabel("의사 소개 >");
	List<Row> list = Query.medisn.select();
	JPanel borderPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}};
	
	JPanel centerInJPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(5,10,0,10));
	}};
	JPanel centerJPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		add(centerInJPanel, sp.c);
	}};
	
	JPanel southPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
	}};
	HospitalIfor(Row row) {
		this.row = row;
		setFrame("병원 정보", 600, 500, ()->{});
	}
	@Override
	protected void desing() {
		System.out.println(row);
		borderPanel.add(new JLabel(new ImageIcon(new ImageIcon("src/hospital/" + row.getInt(0) + ".png").getImage().getScaledInstance(600 - 20-20, 200, Image.SCALE_SMOOTH))) {{
			setBorder(sp.line);
		}}, sp.n);
		
		
		
		centerJPanel.add(new JLabel(row.getString(1)) {{
			setFont(sp.fontM(1, 20));
		}}, sp.n);
		centerInJPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBackground(Color.white);
			add(new JLabel(row.getString(4)));
		}},sp.n);
		centerInJPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBackground(Color.white);
			add(doctor);
		}},sp.c);
		
		southPanel.add(new JPanel(new BorderLayout()) {{
			setBackground(Color.white);
			add(new JLabel("위치"), sp.n);
			add(new JPanel(null) {{
				setPreferredSize(new Dimension(250, 125));
				add(new points() {{
					setBounds(0,0,250,125);
				}});
				add(new JLabel(new ImageIcon(new ImageIcon("src/map.png").getImage().getScaledInstance(250,  125, Image.SCALE_SMOOTH))) {{
					setBorder(sp.line);
					setBounds(0,0,250, 125);
				}});
				
			}});
			
		}}, sp.w);
		southPanel.add(new JPanel(new BorderLayout()) {{
			setBackground(Color.white);
			add(new JLabel("주변 약국"), sp.n);
			add(new JPanel(null) {{
				setPreferredSize(new Dimension(250, 125));
				add(new 약국() {{
					setBounds(0, 0, 250, 125);
				}});
				add(new JLabel(new ImageIcon(new ImageIcon("src/map.png").getImage().getScaledInstance(250,  125, Image.SCALE_SMOOTH))) {{
					setBorder(sp.line);
					setBounds(0,0, 250, 125);
				}});
			}});
			
		}}, sp.e);
		borderPanel.add(southPanel, sp.s);
		borderPanel.add(centerJPanel);
		add(borderPanel);
	}
	
	@Override
	protected void action() {
		//((Container) centerInJPanel.getComponent(1).getComponent(0)) <- 이거로 해도 가능, 하지만 내 생각엔 가독성↓↓
		JPanel doctorCompo = (JPanel) centerInJPanel.getComponent(1);
		doctorCompo.getComponent(0).addMouseListener(new MouseAdapter() {
			 @Override
				public void mouseClicked(MouseEvent e) {
					new Doctor(row);
				}
		});
		JPanel 약국Compo = (JPanel) southPanel.getComponent(1);
		약국Compo.getComponent(1).addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Map(row);
			}
		});
	}
	
	class points extends JPanel{
		@Override
		public void paint(Graphics g) {
			System.out.println("sdfs");
			g.setColor(Color.blue);
			g.fillOval((row.getInt(2) / 2) - 8, (row.getInt(3) / 4) - 8, 8, 8);
		}
	}

	class 약국 extends JPanel{
		@Override
		public void paint(Graphics g) {
			g.setColor(Color.blue);
			for(Row row : list) {
				g.fillOval((row.getInt(2) / 2), (row.getInt(3) / 4), 8, 8);
			}
		}
	}
	public static void main(String[] args) {
		new HospitalIfor(Query.hospitalUserPoint.select(1).get(0));
	}
}
