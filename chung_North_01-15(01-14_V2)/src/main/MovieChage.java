package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import static javax.swing.BorderFactory.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class MovieChage extends JFrame{
	JPanel borderPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(7,7,7,7));
	}};
	
	JButton movieChageBut = new CustumButton("수정");
	Data movie;
	
	JTextField name = new JTextField() {{
		setBorder(createLineBorder(Color.black));
		setPreferredSize(new Dimension(0, 30));
	}};
	JTextArea infor = new JTextArea() {{
		setBorder(createLineBorder(Color.black));
	}};
	JScrollPane sc = new JScrollPane(infor) {{
		setBorder(createLineBorder(Color.black));
		setBackground(Color.white);
	}};
	TitledBorder tb = createTitledBorder(createCompoundBorder(createLineBorder(Color.black), createEmptyBorder(3, 1, 1, 1)), "설명", TitledBorder.LEFT, TitledBorder.TOP);
	JComboBox<String> genre = new JComboBox<>() {{
		for(Data d : Connections.select("select g_name from genre")) addItem(d.get(0).toString());
	}};
	JComboBox<String> ageLimit = new JComboBox<String>("ALL,12,15,19".split(","));
	
	int m_no;
	public MovieChage(int m_no) {
		this.m_no = m_no;
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);

		name.setText(movie.get(1).toString());
		infor.setText(movie.get(4).toString());
		genre.setSelectedIndex(Integer.parseInt(movie.get(5).toString()) - 1);
		ageLimit.setSelectedIndex(Integer.parseInt(movie.get(2).toString()) - 1);
		
		
		
		JPanel mainPanel = new JPanel(new BorderLayout(10,10));
		mainPanel.setBackground(Color.white);
		mainPanel.add(new JLabel(getter.getImageIcon("datafiles/movies/" + m_no + ".jpg", 175, 250)), BorderLayout.WEST);
		
		JPanel inforPanel = new JPanel(new BorderLayout(15,15));
		inforPanel.setBackground(Color.white);
		inforPanel.add(name, BorderLayout.NORTH);
		sc.setBorder(tb);
		inforPanel.add(sc);
		inforPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
			setBackground(Color.white);
			add(genre);
			add(ageLimit);
		}}, BorderLayout.SOUTH);
		mainPanel.add(inforPanel);
		
		borderPanel.add(mainPanel);
		
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(movieChageBut);
		}}, BorderLayout.SOUTH);
		add(borderPanel);
		
		movieChageBut.addActionListener(e->{
			String name = this.name.getText();
			String inf = this.infor.getText();
			if(name.isEmpty() || inf.isEmpty()) {
				JOptionPane.showMessageDialog(null, "빈칸이 있습니다", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(name.equals(movie.get(1).toString()) && inf.equals(movie.get(4).toString())
					&& (genre.getSelectedIndex() + 1) == Integer.parseInt(movie.get(5).toString()) && (ageLimit.getSelectedIndex() + 1) == Integer.parseInt(movie.get(2).toString()) ) {
				JOptionPane.showMessageDialog(null, "수정된 부분이 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String[] ti = "시발,개새끼,존나,병신".split(",");
			for(int i = 0; i < 4; i++) {
				if(inf.contains(ti[i])) {
					JOptionPane.showMessageDialog(null, "욕설을 포함하고 있습니다.", "경고", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			JOptionPane.showMessageDialog(null, "정보가 수정되었습니다", "정보", JOptionPane.INFORMATION_MESSAGE);
			Connections.update("update movie set m_name = ?, l_no = ?, m_plot = ?, g_no = ? where m_no = ?;", name, ageLimit.getSelectedIndex() + 1, inf, genre.getSelectedIndex() + 1, movie.get(0));
			new MovieSerch(false);
			dispose();
			return;
		});
		A_setFrame.setting(this, "영화수정", 700, 350);
	}
	
	public static void main(String[] args) {
		new MovieChage(1);
	}
}
