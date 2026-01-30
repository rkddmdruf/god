package main;

import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.JobAttributes;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import utils.*;

public class MovieChange extends CFrame{
	JTextField name = new JTextField() {{
		setBorder(createLineBorder(Color.black));
		setPreferredSize(new Dimension(0, 30));
	}};
	JTextArea infor = new JTextArea() {{
		setBorder(createLineBorder(Color.black));
	}};
	TitledBorder tiBorder = new TitledBorder(createLineBorder(Color.black), "설명", TitledBorder.LEFT, TitledBorder.TOP);
	JComboBox<Object> ageLimit = new JComboBox<Object>("ALL,12,15,19".split(",")) {{
		setPreferredSize(new Dimension(90, 25));
		setBackground(Color.white);
	}};
	JComboBox<Object> genre = new JComboBox<Object>() {{
		setPreferredSize(new Dimension(90, 25));
		setBackground(Color.white);
		for(Data data : Connections.select("select g_name from genre")) addItem(data.get(0));
	}};
	
	JButton but = new CustumButton("수정") {{
		setPreferredSize(new Dimension(100, 25));
	}};
	
	JPanel borderPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(5,10,10,10));
	}};
	int m_no;
	Data data;
	public MovieChange(int m_no) {
		this.m_no = m_no;
		data = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		setting();
		JLabel l = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/movies/" + data.getInt(0) + ".jpg").getImage(), 0, 10, getWidth(), getHeight()-15, null);
			}
		};
		l.setPreferredSize(new Dimension(150, 0));
		
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		p.setBackground(Color.white);
		p.add(but);
		
		setMainPanel();
		borderPanel.add(l, BorderLayout.WEST);
		borderPanel.add(p, BorderLayout.SOUTH);
		
		add(borderPanel);
		setAction();
		setFrame("영화 수정", 575, 275);
	}
	
	private void setAction() {
		but.addActionListener(e->{
			String n = name.getText();
			String i = infor.getText();
			int g = genre.getSelectedIndex() + 1;
			int a = ageLimit.getSelectedIndex() + 1;
			
			if(n.isEmpty() || i.isEmpty()) {
				getter.mg("빈칸이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(n.equals(data.get(1).toString()) && i.equals(data.get(4).toString()) && g == data.getInt(5) && a == data.getInt(2)) {
				getter.mg("수정된 부분이 없습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String[] ti = "시발, 개새끼, 존나, 병신".split(",");
			for(int si = 0; si < 4; si++) {
				if(i.contains(ti[si])) {
					getter.mg("욕설을 포함하고 있습니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			getter.mg("정보가 수정되었습니다.", JOptionPane.INFORMATION_MESSAGE);
			Connections.update("update movie set m_name = ?, m_plot = ?, g_no = ?, l_no = ? where m_no = ?", n, i, g, a, m_no);
			new MovieSerch();
			dispose();
		});
	}
	
	private void setting() {
		name.setText(data.get(1).toString());
		infor.setText(data.get(4).toString());
		genre.setSelectedIndex(data.getInt(5) - 1);
		ageLimit.setSelectedIndex(data.getInt(2) - 1);
	}

	private void setMainPanel() {
		JPanel p = new JPanel(new BorderLayout(10,10));
		p.setBackground(Color.white);
		
		p.add(name, BorderLayout.NORTH);
		p.add(new JScrollPane(infor) {{
			setBackground(Color.white);
			setBorder(tiBorder);
		}});
		p.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3)) {{
			setBackground(Color.white);
			add(genre);
			add(ageLimit);
		}}, BorderLayout.SOUTH);
		
		borderPanel.add(p);
	}

	public static void main(String[] args) {
		new MovieChange(1);
	}
}
