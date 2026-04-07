package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.Connections;
import utils.Data;
import utils.getter;

public class NorthPanel extends JPanel{

	JTextField tf = new JTextField() {{
		setBorder(null);
	}};
	JLabel serch = new JLabel(getter.getImage("datafiles/icon/search.png", 30, 30));
	JLabel logo = new JLabel(getter.getImage("datafiles/icon/logo.png", 45, 45));
	
	JLabel certiLabel = new JLabel("자격증 목록");
	JLabel testLabel = new JLabel("시험 일정");
	public static int vg = JLabel.BOTTOM;
	public NorthPanel() {
		setLayout(new BorderLayout(10, 10));
		setBackground(Color.white);
		JPanel nPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5,5 ));
		nPanel.setBackground(Color.white);
		nPanel.add(logo);
		nPanel.add(new JLabel("Skills Qualification Association", JLabel.LEFT) {{
			setPreferredSize(new Dimension(180, 45));
			setVerticalAlignment(vg);
		}});
		
		JPanel tfPanel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.cyan);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
			}
		};
		tfPanel.setBackground(Color.white);
		tfPanel.setBorder(BorderFactory.createEmptyBorder(4, 2, 4, 2));
		tfPanel.setPreferredSize(new Dimension(325, 25));
		tfPanel.add(tf);
		nPanel.add(tfPanel);
		nPanel.add(serch);
		
		JPanel labelPanel = new JPanel(new GridLayout(0, 2));
		labelPanel.setBackground(Color.white);
		labelPanel.add(new JPanel(new FlowLayout()) {{
			setBackground(Color.white);
			add(certiLabel);
		}});
		labelPanel.add(new JPanel(new FlowLayout()) {{
			setBackground(Color.white);
			add(testLabel);
		}});
		add(labelPanel);
		setSerchAction();
		add(nPanel, BorderLayout.NORTH);
	}
	List<Data> certi = new ArrayList<>();
	protected void setSerchAction() {
		List<Data> list = Connections.select("select * from certi");
		serch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				certi = new ArrayList<>();
				for(Data d : list) {
					String s = d.getString(1) + " " + d.getString(6) + "급";
					if(s.contains(tf.getText())) {
						certi.add(d);
					}
				}
			}
		});
	}
}
