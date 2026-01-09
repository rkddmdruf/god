package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
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

public class MovieChage extends JFrame{

	JPanel borderPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(10,10,10,10));
	}};
	JTextArea infor = new JTextArea();
	JTextField name = new JTextField();
	JButton chage = new CustumButton("수정");
	JComboBox<String> genre = new JComboBox<String>() {{
		for(Data d : Connections.select("select * from genre")) addItem(d.get(1).toString());
	}};
	JComboBox<String> age = new JComboBox<String>() {{		
		for(Data d : Connections.select("select * from movie_limit")) addItem(d.get(1).toString());
	}};
	Data data;
	MovieChage(int m_no){
		data = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		genre.setSelectedIndex(Integer.parseInt(data.get(5).toString()) - 1);
		age.setSelectedIndex(Integer.parseInt(data.get(2).toString()) - 1);
		name.setText(data.get(1).toString());
		infor.setText(data.get(4).toString());
		
		borderPanel.add(new JLabel(getImage("datafiles/movies/" + data.get(0) + ".jpg", 180, 250)) {{
		}}, BorderLayout.WEST);
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
			setBackground(Color.white);
			add(chage);
		}}, BorderLayout.SOUTH);
		
		JPanel p = new JPanel(new BorderLayout(10, 10));
		p.setBackground(Color.white);
		p.add(name, BorderLayout.NORTH);
		JPanel panel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setFont(new Font("맑은 고딕", 0, 12));
				g.drawString("설명", 5, 7);
			}
		};
		panel.setBackground(Color.white);
		panel.setBorder(createCompoundBorder(createLineBorder(Color.black), createEmptyBorder(7, 2, 2,2)));
		panel.add(new JScrollPane(infor));
		p.add(panel);
		p.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
			setBackground(Color.white);
			add(genre);
			add(age);
		}}, BorderLayout.SOUTH);
		
		borderPanel.add(p);
		add(borderPanel);
		new A_setFrame(this, "영화 수정", 650, 300);
		
		chage.addActionListener(e->{
			if(name.getText().equals(data.get(1).toString())
					&& infor.getText().equals(data.get(4).toString())
					&& age.getSelectedIndex() == Integer.parseInt(data.get(2).toString()) - 1 
					&& genre.getSelectedIndex() == Integer.parseInt(data.get(5).toString()) - 1) {
				JOptionPane.showMessageDialog(null, "수정된 부분이 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(name.getText().isEmpty() || infor.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "빈칸이 있습니다", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			List<String> check = Arrays.asList("시발,개새끼,존나,병신".split(","));
			for(int i = 0; i < 4; i++) {
				if(name.getText().contains(check.get(i)) || infor.getText().contains(check.get(i))) {
					JOptionPane.showMessageDialog(null, "욕설을 포함하고 있습니다.", "경고", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			JOptionPane.showMessageDialog(null, "정보가 수정되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
			Connections.update("update movie set m_name = ?, l_no = ?, m_plot = ?, g_no = ? where m_no = ?;",
					name.getText(), age.getSelectedIndex() + 1, infor.getText(), genre.getSelectedIndex() + 1, m_no);
			new MovieSerch(-1);
			dispose();
		});
	}
	
	private ImageIcon getImage(String s, int w, int h) {
		return new ImageIcon(new ImageIcon(s).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	
	public static void main(String[] args) {
		new MovieChage(1);
	}
}
