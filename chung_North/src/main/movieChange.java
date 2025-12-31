package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import utils.*;
import utils.sp.*;

public class movieChange extends BaseFrame{

	JPanel borderPanel = new cp(new BorderLayout(), sp.em(10, 10, 10, 10), null);
	JPanel inforPanel = new cp(new BorderLayout(10,10), null, null);
	JButton change = new cb("수정").BackColor(sp.color).fontColor(Color.white).size(100, 30);
	JComboBox<String> category = new JComboBox<String>() {{
		for(Row row : Query.select("SELECT * FROM moviedb.genre;")) {
			addItem(row.getString(1));
		}
		setPreferredSize(new Dimension(100, 25));
	}};
	Row movie;
	JComboBox<String> age = new JComboBox<String>("ALL, 12, 15, 19".split(", ")) {{
		setPreferredSize(new Dimension(100, 25));
	}};	
	int mno = 0;
	JTextField name = new JTextField();
	TitledBorder titledBorder = BorderFactory.createTitledBorder(
            sp.line,              // 사용할 테두리
            "설명",  // 캡션 텍스트
            TitledBorder.LEFT,   // 제목 정렬 (왼쪽)
            TitledBorder.TOP     // 제목 위치 (상단)
        );
	JTextArea infor = new ca(""){{setBorder(sp.line);}}.setting();
	JScrollPane scroll = new JScrollPane(infor) {{
		setBorder(sp.com(titledBorder, sp.em(7, 3, 2, 3)));
		setBackground(Color.white);
	}};
	
	JPanel comboBoxPanel = new cp(new FlowLayout(FlowLayout.RIGHT), null, null) {{
		add(category);
		add(age);
	}};
	movieChange(int mno){
		this.mno = mno;
		movie = Query.select("SELECT * FROM moviedb.movie where m_no = ?;", mno).get(0);
		infor.setText(movie.getString(4));
		setFrame("영화수정", 600 + 16, 300 + 39, ()->{});
	}
	
	@Override
	protected void desing() {
		category.setSelectedIndex(movie.getInt(5) - 1);
		age.setSelectedIndex(movie.getInt(2) - 1);
		
		borderPanel.add(new cp(new FlowLayout(), sp.em(3, 0, 0, 0), null) {{
			add(new cl(sp.getImg("datafiles/movies/" + mno + ".jpg", 170, 230)) {{
				setVerticalAlignment(JLabel.TOP);
			}}.setBorders(sp.line));
		}}, sp.w);
		
		borderPanel.add(inforPanel);
		name.setPreferredSize(new Dimension(name.getX(), 35));
		name.setBorder(sp.line);
		name.setText(movie.getString(1));
		infor.setFocusable(true);
		inforPanel.add(name, sp.n);
		inforPanel.add(scroll);
		inforPanel.add(comboBoxPanel, sp.s);
		JPanel southP = new cp(new BorderLayout(), null, null);
		southP.add(change, sp.e);
		borderPanel.add(southP, sp.s);
		add(borderPanel);
	}

	@Override
	protected void action() {
		change.addActionListener(e->{
			String name = this.name.getText();
			String infor = this.infor.getText();
			if(name.isEmpty() || infor.isEmpty()) {
				sp.Infor("빈칸이 있습니다.");
				return;
			}
			if(name.equals(movie.getString(1))
					&& infor.equals(movie.getString(4)) 
					&& category.getSelectedIndex() == movie.getInt(5)-1 
					&& age.getSelectedIndex() == movie.getInt(2)-1) 
			{
				sp.Infor("수정된 부분이 없습니다.");
				return;
			}
			String[] str = "시발,개새끼,존나,병신".split(",");
			for(int i = 0; i < 4; i ++) {
				if(infor.contains(str[i])) {
					sp.err("욕설을 포함하고 있습니다.");
					return;
				}
			}
			sp.Infor("정보가 수정되었습니다.");
			Query.update("update movie set m_name = ?, l_no = ?, m_plot = ?, g_no = ?", name, age.getSelectedIndex()+1, infor, category.getSelectedIndex()+1);
			new AdminSerch();
			dispose();
			
		});
	}
	public static void main(String[] args) {
		new movieChange(1);
	}
}
