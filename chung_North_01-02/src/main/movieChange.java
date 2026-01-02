package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class movieChange extends JFrame{
	JPanel borderPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}};
	JComboBox<String> category = new JComboBox<String>() {{
		for(List<Object> list : setList("select * from genre")) addItem(list.get(1).toString());
	}};
	JComboBox<String> age = new JComboBox<String>() {{
		for(List<Object> list : setList("select * from movie_limit")) addItem(list.get(1).toString());
	}};
	JTextField name = new JTextField();
	JTextArea infor = new JTextArea() {{
		setBorder(BorderFactory.createLineBorder(Color.black));
	}};
	TitledBorder inforBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black), "설명", TitledBorder.LEFT,TitledBorder.TOP);
	JScrollPane sc = new JScrollPane(infor) {{
		setBorder(BorderFactory.createCompoundBorder(inforBorder, BorderFactory.createEmptyBorder(5,2,2,2)));
	}};
	JButton change = new JButton("수정") {{
		setPreferredSize(new Dimension(100, 30));
		setForeground(Color.white);
		setBackground(Color.blue);
	}};
	List<List<Object>> list = new ArrayList<>();
	int m_no = 0;
	movieChange(int m_no){
		this.m_no = m_no;
		list = setList("select * from movie where m_no = ?", m_no);
		System.out.println(list);
		age.setSelectedIndex(Integer.parseInt(list.get(0).get(2).toString())-1);
		category.setSelectedIndex(Integer.parseInt(list.get(0).get(5).toString())-1);
		name.setText(list.get(0).get(1).toString());
		infor.setText(list.get(0).get(4).toString());
		
		borderPanel.add(new JLabel(new ImageIcon(new ImageIcon("datafiles/movies/" + m_no + ".jpg").getImage().getScaledInstance(200, 275, Image.SCALE_SMOOTH))) {{
			setVerticalAlignment(JLabel.TOP);
			setPreferredSize(new Dimension(200, 275));
		}},BorderLayout.WEST);
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.add(name, BorderLayout.NORTH);
		panel.add(sc);
		panel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
			add(category);add(age);
		}}, BorderLayout.SOUTH);
		borderPanel.add(panel);
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
			add(change);
		}}, BorderLayout.SOUTH);
		setFrame();
		add(borderPanel);
	}
	
	private void setFrame() {
		setAction();
		setTitle("영화 수정");
		setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		getContentPane().setBackground(Color.white);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		int w = 700 + 16; int h = 350 + 39;
		setBounds(960 - (w / 2), 540 - (h / 2), w, h);
		setVisible(true);
	}
	
	private void setAction() {
		change.addActionListener(e->{
			if(name.getText() == list.get(0).get(1).toString() && infor.getText() == list.get(0).get(4).toString() 
					&& category.getSelectedIndex() == Integer.parseInt(list.get(0).get(5).toString()) && age.getSelectedIndex() == Integer.parseInt(list.get(0).get(2).toString())) {
				JOptionPane.showMessageDialog(null, "수정된 부분이 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(name.getText().isEmpty() || infor.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "빈칸이 있습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String[] str = "시발,개새끼,존나,병신".split(",");
			for(int i = 0; i < 4; i++) {
				if(infor.getText().contains(str[i])) {
					JOptionPane.showMessageDialog(null, "욕설을 포함하고 있습니다.", "경고", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			try {
				Connection c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=Asia/Seoul&allowLoadLocalInfile=true", "root", "1234");
				PreparedStatement ps = c.prepareStatement("update movie set m_name = ? , l_no = ? , m_plot = ? , g_no = ? where m_no = ?;");
				//name.getText(), age.getSelectedIndex(), infor.getText(), category.getSelectedIndex(), m_no
				
				ps.setString(1, name.getText().toString());
				ps.setObject(2, age.getSelectedIndex()+1);
				ps.setObject(3, infor.getText());
				ps.setObject(4, category.getSelectedIndex()+1);
				ps.setObject(5, m_no);
				JOptionPane.showMessageDialog(null, "정보가 수정되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
				ps.executeUpdate();
				new movieSerch(-1);
				dispose();
				return;
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});
	}
	private List<List<Object>> setList(String string, Object...val) {
		List<List<Object>> list = new ArrayList<>();
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=Asia/Seoul&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement st = c.prepareStatement(string);
			for(int i = 0; i < val.length; i++) {
				st.setObject(i+1, val[i]);
			}
			ResultSet re = st.executeQuery();
			while(re.next()) {
				List<Object> l = new ArrayList<>();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++)
					l.add(re.getObject(i+1));
				list.add(l);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] args) {
		new movieChange(1);
	}
}
