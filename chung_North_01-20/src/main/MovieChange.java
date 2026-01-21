package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import static javax.swing.BorderFactory.*;

public class MovieChange extends JFrame{

	
	JPanel borderPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,10,10,10));
	}};
	
	JPanel mainPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
	}};
	JTextField name = new JTextField() {{
		setBorder(createLineBorder(Color.black));
		setPreferredSize(new Dimension(0, 30));
	}};
	JTextArea infor = new JTextArea() {{
		setBorder(createLineBorder(Color.black));
	}};
	TitledBorder ti = new TitledBorder(createCompoundBorder(createLineBorder(Color.black), createEmptyBorder(0, 0,0,0)), "설명", TitledBorder.LEFT, TitledBorder.TOP);
	JComboBox<Object> genre = new JComboBox<Object>() {{
		for(Data d : Connections.select("select g_name from genre")) addItem(d.get(0));
	}};
	JComboBox<Object> ageLimit = new JComboBox<Object>("All            ,12,15,19".split(","));
	
	JButton change = new CustumButton("수정") {{
		setPreferredSize(new Dimension(80, 25));
	}};
	int m_no;
	Data movie;
	
	public MovieChange(int m_no) {
		this.m_no = m_no;
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		JLabel movieImage = new JLabel(getter.getImageIcon("datafiles/movies/" + m_no + ".jpg", 175, 250));
		movieImage.setVerticalAlignment(JLabel.BOTTOM);
		
		name.setText(movie.get(1).toString());
		infor.setText(movie.get(4).toString());
		genre.setSelectedIndex(movie.getInt(5) - 1);
		ageLimit.setSelectedIndex(movie.getInt(2) - 1);
		
		setMainPanel();
		borderPanel.add(mainPanel);
		borderPanel.add(movieImage, BorderLayout.WEST);
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5)) {{
			setBackground(Color.white);
			add(change);
		}}, BorderLayout.SOUTH);
		add(borderPanel);
		setButAction();
		setFrame.setframe(this, "영화수정", 600, 350);
	}
	
	private void setMainPanel() {
		JPanel inforPanel = new JPanel(new BorderLayout());
		inforPanel.setBorder(ti);
		inforPanel.setBackground(Color.white);
		inforPanel.add(new JScrollPane(infor));
		
		mainPanel.add(name, BorderLayout.NORTH);
		mainPanel.add(inforPanel);
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 3,3)) {{
			setBackground(Color.white);
			add(genre);
			add(ageLimit);
		}}, BorderLayout.SOUTH);
	}
	
	private void setButAction(){
		change.addActionListener(e->{
			String n = name.getText();
			String i = infor.getText();
			int g = genre.getSelectedIndex() + 1;
			int a = ageLimit.getSelectedIndex() + 1;
			
			if(n.isEmpty() || i.isEmpty()) {
				getter.mg("빈칸이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(n.equals(movie.get(1).toString()) && i.equals(movie.get(4).toString()) && g == movie.getInt(5) && a == movie.getInt(1)) {
				getter.mg("수정된 부분이 없습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String[] tiString = "시발,개새끼,존나,병신".split(",");
			for(String s : tiString) {
				if(i.contains(s)) {
					getter.mg("욕설을 포함하고 있습니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			getter.mg("정보가 수정되었습니다", JOptionPane.INFORMATION_MESSAGE);
			Connections.update("update movie set m_name = ?, m_plot = ?, g_no = ?, l_no = ? where m_no = ?;", n, i, g, a, m_no);
			new MovieSerch();
			dispose();
		});
	}
	public static void main(String[] args) {
		new MovieChange(1);
	}
}
