package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import static javax.swing.BorderFactory.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class MovieChange extends JFrame{

	JPanel borderPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(1,10,10,10));
	}};
	
	JButton but = new CustumButton("수정");
	
	JTextField name = new JTextField() {{
		setPreferredSize(new Dimension(0, 30));
		setBorder(createLineBorder(Color.black));
	}};
	JTextArea infor = new JTextArea() {{
		setLineWrap(true);
		setBorder(createLineBorder(Color.black));
	}};
	TitledBorder ti = new TitledBorder(createLineBorder(Color.black), "설명", TitledBorder.LEFT, TitledBorder.TOP);
	JComboBox<Object> genre = new JComboBox<Object>() {{
		for(Data d : Connections.select("select g_name from genre")) addItem(d.get(0));
	}};
	JComboBox<Object> ageLimit = new JComboBox<Object>("ALL,12,15,19".split(","));
	JPanel mainPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
	}};
	
	int m_no;
	Data movie;
	MovieChange(int m_no){
		this.m_no = m_no;
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		name.setText(movie.get(1).toString());
		infor.setText(movie.get(4).toString());
		genre.setSelectedIndex(movie.getInt(5) - 1);
		ageLimit.setSelectedIndex(movie.getInt(2) - 1);
		
		setMainPanel();
		JLabel img = new JLabel(getter.getImageIcon("datafiles/movies/" + m_no + ".jpg", 150, 225));
		img.setVerticalAlignment(JLabel.BOTTOM);
		
		borderPanel.add(img, BorderLayout.WEST);
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(but);
		}}, BorderLayout.SOUTH);
		borderPanel.add(mainPanel);
		add(borderPanel);
		
		but.addActionListener(e->{
			String n = name.getText();
			String i = infor.getText();
			int g = genre.getSelectedIndex() + 1;
			int a = ageLimit.getSelectedIndex() + 1;
			if(n.isEmpty() || i.isEmpty()) {
				getter.mg("빈칸이 있습니다", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(n.equals(movie.get(1).toString()) && i.equals(movie.get(4).toString()) && g == movie.getInt(5) && a == movie.getInt(2)) {
				getter.mg("수정된 부분이 없습니다", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String[] tiString = "시발,개새끼,존나,병신".split(",");
			for(int j = 0; j < 4; j++) {
				if(i.contains(tiString[j])) {
					getter.mg("욕설을 포함하고 있습니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			getter.mg("정보가 수정되었습니다", JOptionPane.INFORMATION_MESSAGE);
			Connections.select("update movie set m_name = ?, l_no = ?, m_plot = ?, g_no = ? where m_no = ?;", n,a,i,g,m_no);
			new MovieSerch();
			dispose();
		});
		SetFrame.setFrame(this, "영화 수정", 600, 275);
	}
	
	private void setMainPanel() {
		mainPanel.add(name, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(infor) {{
			setBackground(Color.white);
			setBorder(ti);
		}});
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 3,3)) {{
			setBackground(Color.white);
			add(genre);
			add(ageLimit);
		}}, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		new MovieChange(1);
	}
}
