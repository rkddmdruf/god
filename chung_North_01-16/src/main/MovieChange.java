package main;

import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class MovieChange extends JFrame{
	Data movie;
	JPanel borderPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(10,10,10,10));
	}};
	
	JPanel inforPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
		setPreferredSize(new Dimension(500, 0));
	}};
	
	JButton change = new CustumButton("수정") {{
		setPreferredSize(new Dimension(100, 25));
	}};
	
	TitledBorder borderT = new TitledBorder(createCompoundBorder(createLineBorder(Color.black), createEmptyBorder(3,1,1,1)), "설명", TitledBorder.LEFT, TitledBorder.TOP);
	JTextField name = new JTextField() {{
		setPreferredSize(new Dimension(0, 30));
		setBorder(createLineBorder(Color.black));
	}};
	JTextArea infor = new JTextArea();
	JScrollPane sc = new JScrollPane(infor) {{
		setBorder(createLineBorder(Color.black));
		setBackground(Color.white);
	}};
	JComboBox<String> genre = new JComboBox<String>() {{
		setBackground(Color.white);
		setPreferredSize(new Dimension(80, 25));
		for(Data d : Connections.select("select g_name from genre")) {
			addItem(d.get(0).toString());
		}
	}};
	JComboBox<String> ageLimit = new JComboBox<String>("ALL,12,15,19".split(",")) {{
		setPreferredSize(new Dimension(80, 25));
		setBackground(Color.white);
	}};
	
	public MovieChange(int m_no) {
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		
		name.setText(movie.get(1).toString());
		infor.setText(movie.get(4).toString());
		genre.setSelectedIndex(movie.getInt(5) - 1);
		ageLimit.setSelectedIndex(movie.getInt(2) - 1);
		
		setInforPanel();
		borderPanel.add(inforPanel, BorderLayout.EAST);
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0)) {{
			setBackground(Color.white);
			add(change);
		}}, BorderLayout.SOUTH);
		borderPanel.add(new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/movies/" + m_no + ".jpg").getImage(), 0, 4, getWidth(), getHeight() - 5, null);
			}
		});
		
		change.addActionListener(e->{
			String name = this.name.getText();
			String infor = this.infor.getText();
			if(name.isEmpty() || infor.isEmpty()) {
				mg("빈칸이 있습니다", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(name.equals(movie.get(1).toString()) && infor.equals(movie.get(4).toString())
					&& genre.getSelectedIndex() == movie.getInt(5) - 1 && ageLimit.getSelectedIndex() == movie.getInt(2) - 1) {
				mg("수정된 부분이 없습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String[] ti = "시발,개새끼,존나,병신".split(",");
			for(int i = 0; i < 4; i++) {
				if(infor.contains(ti[i])) {
					mg("욕설을 포함하고 있습니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			mg("정보가 수정되었습니다.", JOptionPane.INFORMATION_MESSAGE);
			Connections.update("update movie set m_name = ?, l_no = ?, m_plot = ?, g_no = ? where m_no = ?", name, ageLimit.getSelectedIndex() + 1, infor, genre.getSelectedIndex() + 1, movie.get(0));
			new MovieSerch();
			dispose();
			return;
		});
		add(borderPanel);
		SetFrames.setframe(this, "영화수정", 700, 325);
	}
	
	private void mg(String s, int type) {
		JOptionPane.showMessageDialog(null, s, type == 1 ? "정보" : "경고", type);
	}
	private void setInforPanel() {
		inforPanel.add(name, BorderLayout.NORTH);
		inforPanel.add(new JPanel(new BorderLayout()) {{
			add(sc);
			setBackground(Color.white);
			setBorder(borderT);
		}});
		inforPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)) {{
			setBackground(Color.white);
			add(genre);
			add(ageLimit);
		}}, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		new MovieChange(1);
	}
}
