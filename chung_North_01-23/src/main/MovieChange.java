package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import static javax.swing.BorderFactory.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class MovieChange extends JFrame{

	JPanel borderPanel = new JPanel(new BorderLayout(3,3)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(5,10,10,10));
	}};
	
	JPanel mainPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,5,0,0));
	}};
	
	JTextField name = new JTextField() {{
		setPreferredSize(new Dimension(0, 30));
		setForeground(Color.black);
		setBorder(createLineBorder(Color.black));
	}};
	JTextArea infor = new JTextArea() {{
		setForeground(Color.black);
		setBorder(createLineBorder(Color.black));
	}};
	TitledBorder ti = new TitledBorder(createLineBorder(Color.black), "설명", TitledBorder.LEFT, TitledBorder.TOP);
	JComboBox<Object> genre = new JComboBox<Object>() {{
		for(Data d : Connections.select("select g_name from genre")) addItem(d.get(0));
	}};
	JComboBox<Object> ageLimit = new JComboBox<Object>("ALL           ,12,15,19".split(","));//setPreferredSize 써도됌
	
	JButton but = new CustumButton("수정");
	
	int m_no;
	Data data = new Data();
	MovieChange(int m_no) {
		this.m_no = m_no;
		data = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		setData();
		setMainPanel();
		borderPanel.add(new JLabel(getter.getImageIcon("datafiles/movies/" + m_no + ".jpg", 140, 200)) {{
			setVerticalAlignment(JLabel.BOTTOM);
		}}, BorderLayout.WEST);
		but.setPreferredSize(new Dimension(80, 25));
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(but);
		}}, BorderLayout.SOUTH);
		add(borderPanel);
		
		but.addActionListener(e->{
			String n = name.getText();
			String i = infor.getText();
			int g = genre.getSelectedIndex() + 1;
			int a = ageLimit.getSelectedIndex() + 1;
			if(n.isEmpty() || i.isEmpty()) {
				getter.mg("빈칸이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(n.equals(data.get(1).toString()) && i.equals(data.get(4).toString()) && g == data.getInt(5) && a == data.getInt(2)){
				getter.mg("수정된 부분이 없습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String[] tiString = "시발, 개새끼, 존나, 병신".split(", ");
			for(String s : tiString) {
				if(i.contains(s)) {
					getter.mg("욕설을 포함하고 있습니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			getter.mg("정보가 수정되었습니다.", JOptionPane.INFORMATION_MESSAGE);
			Connections.update("update movie set m_name = ?, m_plot = ?, g_no = ?, l_no = ? where m_no = ?", n,i,g,a,m_no);
			new MovieSerch();
			dispose();
			return;
		});
		setFrame.setframe(this, "영화수정", 550, 250);
	}
	
	private void setData() {
		name.setText(data.get(1).toString());
		infor.setText(data.get(4).toString());
		genre.setSelectedIndex(data.getInt(5) - 1);
		ageLimit.setSelectedIndex(data.getInt(2) - 1);
	}
	
	private void setMainPanel() {
		JScrollPane s = new JScrollPane(infor);
		s.setBackground(Color.white);
		s.setBorder(ti);
		
		mainPanel.add(name, BorderLayout.NORTH);
		mainPanel.add(s);
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3)) {{
			setBackground(Color.white);
			add(genre);
			add(ageLimit);
		}}, BorderLayout.SOUTH);
		borderPanel.add(mainPanel);
	}
	
	public static void main(String[] args) {
		new MovieChange(1);
	}
}
