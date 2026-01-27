package main;

import utils.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import static javax.swing.BorderFactory.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class MovieChange extends JFrame{

	JPanel borderPanel  = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,10,10,10));
	}};
	
	JPanel mainPanel = new JPanel(new BorderLayout(10,10));
	JTextField name = new JTextField() {{
		setBorder(createLineBorder(Color.black));
		setPreferredSize(new Dimension(0, 30));
	}};
	JTextArea infor = new JTextArea() {{
		setBorder(createLineBorder(Color.black));
	}};
	TitledBorder ti = new TitledBorder(createLineBorder(Color.black), "설명", TitledBorder.LEFT, TitledBorder.TOP);
	JComboBox<Object> genre = new JComboBox<Object>() {{
		setBackground(Color.white);
		for (Data d : Connections.select("select g_name from genre")) addItem(d.get(0));
	}};
	JComboBox<Object> ageLimit = new JComboBox<Object>("ALL, 12, 15, 19".split(", ")) {{
		setBackground(Color.white);
	}};
	
	JButton change = new CustumButton("수정");
	int m_no;
	String n, i;
	int g, a;
	Data d;
	public MovieChange(int m_no) {
		this.m_no = m_no;
		d = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		setMainPanel();
		JLabel img = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/movies/" + m_no + ".jpg").getImage(), 0, 20, getWidth(), getHeight()-20, null);
			}
		};
		img.setPreferredSize(new Dimension(150, 100));
		img.setVerticalAlignment(JLabel.BOTTOM);
		
		borderPanel.add(mainPanel);
		borderPanel.add(img, BorderLayout.WEST);
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(change);
		}}, BorderLayout.SOUTH);
		add(borderPanel);
		
		change.addActionListener(e->{
			String n = name.getText();
			String i = infor.getText();
			int g = genre.getSelectedIndex() + 1;
			int a = ageLimit.getSelectedIndex() + 1;
			
			if(n.isEmpty() || i.isEmpty()) {
				getter.mg("빈칸이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(this.n.equals(n) && this.i.equals(i) && this.g == g && this.a == a) {
				getter.mg("수정된 부분이 없습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String[] tiString = "시발,개새끼,존나,병신".split(",");
			for(int s = 0; s < 4; s++) {
				if(i.contains(tiString[s])) {
					getter.mg("욕설을 포함하고 있습니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			getter.mg("정보가 수정되었습니다.", JOptionPane.INFORMATION_MESSAGE);
			Connections.update("update movie set m_name = ?, l_no = ?, m_plot = ?, g_no = ? where m_no = ?", n, a, i, g, m_no);
			new MovieSerch();
			dispose();
			return;
		});
		
		setFrame.setframe(this, "영화 수정", 600, 275);
	}
	
	private void setMainPanel() {
		mainPanel.setBackground(Color.white);
		name.setText(n = d.get(1).toString());
		infor.setText(i = d.get(4).toString());
		genre.setSelectedIndex(g = d.getInt(5) - 1);
		genre.setPreferredSize(new Dimension(80, 25));
		ageLimit.setSelectedIndex(a = d.getInt(2) - 1);
		ageLimit.setPreferredSize(new Dimension(80, 25));
		
		mainPanel.add(name, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(infor) {{
			setBackground(Color.white);
			setBorder(ti);;
		}});
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3)) {{
			setBackground(Color.white);
			add(genre);
			add(ageLimit);
		}}, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		new MovieChange(1);
	}
}
