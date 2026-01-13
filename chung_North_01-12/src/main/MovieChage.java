package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.swing.BorderFactory.*;

import javax.swing.ImageIcon;
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


public class MovieChage extends JFrame{

	JPanel borderPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(7,7,7,7));
	}};
	
	JButton chage = new CustumButton("수정") {{
		setPreferredSize(new Dimension(100, 25));
	}};
	JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
		setBackground(Color.white);
		add(chage);
	}};
	
	JPanel inforPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
	}};
	JTextField movieName = new JTextField() {{
		setPreferredSize(new Dimension(0, 30));
		setBorder(createLineBorder(Color.black));
	}};
	JTextArea movieInfor = new JTextArea();
	JComboBox<String> genre = new JComboBox<String>() {{
		for(Data d : Connections.select("select g_name from genre")) addItem(d.get(0).toString());
	}};
	JComboBox<String> ageLimit = new JComboBox<String>("ALL,12,15,19".split(",")) {{
		setPreferredSize(new Dimension(80, 25));
	}};
	
	Data data;
	public MovieChage(int u_no, int m_no){
		
		data = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		genre.setSelectedIndex(Integer.parseInt(data.get(5).toString()) - 1);
		ageLimit.setSelectedIndex(Integer.parseInt(data.get(2).toString()) - 1);
		movieName.setText(data.get(1).toString());
		movieInfor.setText(data.get(4).toString());
		
		borderPanel.add(new JLabel(getImage("datafiles/movies/" + data.get(0) + ".jpg", 150, 225)), BorderLayout.WEST);		
		
		JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		flowPanel.setBackground(Color.white);
		flowPanel.add(genre);
		flowPanel.add(ageLimit);
		
		TitledBorder tb = new TitledBorder(createCompoundBorder(createLineBorder(Color.black), createEmptyBorder(5,1,1,1)), "설명", TitledBorder.LEFT, TitledBorder.TOP);
		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBackground(Color.white);
		titlePanel.setBorder(tb);
		titlePanel.add(new JScrollPane(movieInfor));
		
		inforPanel.add(titlePanel);
		inforPanel.add(movieName, BorderLayout.NORTH);
		inforPanel.add(flowPanel, BorderLayout.SOUTH);
		
		borderPanel.add(butPanel, BorderLayout.SOUTH);
		borderPanel.add(inforPanel);
		add(borderPanel);
		
		chage.addActionListener(e -> {
			String n = movieName.getText();
			String i = movieInfor.getText();
			int g = genre.getSelectedIndex();
			int age = ageLimit.getSelectedIndex();
			if(n.isEmpty() || i.isEmpty()) {
				JOptionPane.showMessageDialog(null, "빈칸이 있습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(n.equals(data.get(1).toString()) && i.equals(data.get(4).toString()) && g == Integer.parseInt(data.get(5).toString()) - 1 && age == Integer.parseInt(data.get(2).toString()) - 1) {
				JOptionPane.showMessageDialog(null, "수정된 부분인 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String[] ti = "시발,개새끼,존나,병신".split(",");
			for(String s : ti) {
				if(i.contains(s)) {
					JOptionPane.showMessageDialog(null, "욕설을 포함하고 있습니다.", "경고", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			JOptionPane.showMessageDialog(null, "정보가 수정되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
			Connections.update("update movie set m_name = ?, m_plot = ?, g_no = ?, l_no = ? where m_no = ?", n, i, g + 1, age + 1, m_no);
			new MovieSerch(-1);
			dispose();
		});
		
		new A_setFrame(this, "영화수정", 600, 310);
	}
	
	private ImageIcon getImage(String file, int w, int h) {
		return new ImageIcon(new ImageIcon(file).getImage().getScaledInstance(w, h, 4));
	}
	
	public static void main(String[] args) {
		new MovieChage(-1, 1);
	}
}
