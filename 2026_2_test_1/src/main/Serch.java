package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import utils.*;

public class Serch extends CFrame{
	
	JPanel northPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
	}};
	List<JButton> categories = new ArrayList<>();
	int cgno;
	String text;
	
	JPanel mainPanel = new JPanel(new GridLayout(0, 1, 10, 10)) {{
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(mainPanel) {{
		setBackground(Color.white);
	}};
	public Serch(int cgno, String text){
		this.text = text;
		this.cgno = cgno;
		NorthPanel.vg = JLabel.CENTER;
		northPanel.add(new NorthPanel(), BorderLayout.NORTH);
		borderPanel.add(northPanel, BorderLayout.NORTH);
		setCategoryPanel();
		setMainPanel();
		borderPanel.add(sc);
		setFrame("자격증 목록", 800, 550);
	}

	private void setCategoryPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 7, 5, 5));
		panel.setBackground(Color.white);
		panel.setBorder(createEmptyBorder(0, 275, 0, 30));
		String[] cgName = "추천과정,IT,요리,봉사,운동,의학,항공".split(",");
		for(int i = 0; i < 7; i++) {
			final int index = i;
			JButton b = new RoundedButton(cgName[i]);
			b.setBackground(Color.white);
			b.setForeground(Color.black);
			panel.add(b);
			if(i == cgno) {
				b.setBackground(Color.blue);
				b.setForeground(Color.white);
			}
			b.addActionListener(e ->{
				if(cgno == -1) cgno = 0;
				setColor(cgno, Color.white);
				setColor(index, Color.blue);
				cgno = index;
				setMainPanel();
			});
			categories.add(b);
		}
		
		northPanel.add(panel);
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		List<Data> list = new ArrayList<>();
		if(cgno == 0) {
			list = Connections.select("SELECT certi.*, count(certi.cno) as c FROM lecture.course_registration\r\n"
					+ "join certi on certi.cno = course_registration.cno\r\n"
					+ "group by certi.cno order by c desc;");
		}else if(cgno > 0) {
			list = Connections.select("SELECT * FROM lecture.certi where cgno like ?;", "%" + cgno + "%");
		}else {
			List<Data> certis = Connections.select("select * from certi");
			for(Data d : certis) {
				String s = d.getString(1) + " " + d.getString(6) + "급";
				if(s.contains(text)) {
					list.add(d);
				}
			}
		}
		
		String[] tName = "담당교수,응시조건,주무부처,발급기관".split(",");
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			Data data = list.get(index);
			JPanel p = new JPanel(new BorderLayout(15, 15));
			p.setBackground(Color.white);
			p.setBorder(createCompoundBorder(createLineBorder(Color.black), createEmptyBorder(20, 20, 20, 20)));
			p.add(new JLabel(getter.getImage("datafiles/certification/" + data.getInt(0) + ".png", 180, 140)) {{
				setBorder(createLineBorder(Color.black));
			}}, BorderLayout.WEST);
			
			JPanel inforPanel = new JPanel(new BorderLayout(2, 2));
			inforPanel.setPreferredSize(new Dimension(0, 125));
			inforPanel.setBackground(Color.white);
			inforPanel.add(new JLabel(data.getString(1) + " " + data.getString(6) + "급") {{
				setFont(new Font("맑은 고딕", 1, 20));
			}}, BorderLayout.NORTH);
			inforPanel.add(new JLabel("<html><font color='blue'>· </font color><font color='gray'> 수강혜택</font color><font color='red'> 교안무료+시험예상기출문제 제공</font color></html>"), BorderLayout.SOUTH);
			inforPanel.setBorder(createEmptyBorder(35, 0, 35, 0));
			
			JPanel gPanel = new JPanel(new GridLayout(2, 2, 2, 2));
			gPanel.setBackground(Color.white);
			
			for(int j = 0; j < 4; j++) {
				String str1 = tName[j];
				String str2 = "";
				switch (j) {
					case 0 : {
						str2 = Connections.select("select tname from teacher where tno = ?", data.get(11)).get(0).getString(0) + " 교수"; break;
					}
					case 1 : {
						str2 = data.getString(3) + "/" + data.getString(4); break;
					}
					case 2 : {
						str2 = data.getString(7); break;
					}
					case 3 : {
						str2 = data.getString(9); break;
					}
				}
				String html = "<html>"
						+ "<font color='blue'>· </font color>"
						+ "<font color='gray'> " + str1 + " </font color>"
						+ str2
						+ "</html>";
				gPanel.add(new JLabel(html));
			}
			
			inforPanel.add(gPanel);
			p.add(inforPanel);
			
			JPanel butPanel = new JPanel(new GridLayout(2, 1, 10, 10));
			butPanel.setBackground(Color.white);
			butPanel.add(new RoundedButton("과목선택하기") {{
				setBackground(Color.blue);
				setForeground(Color.white);
			}});
			butPanel.add(new RoundedButton("기출문제 맛보기") {{
				setBackground(new Color(0, 0, 150));
				setForeground(Color.white);
			}});
			butPanel.setBorder(createEmptyBorder(50, 0, 30, 0));
			p.add(butPanel, BorderLayout.EAST);
			mainPanel.add(p);

		}
		SwingUtilities.invokeLater(() -> {
			sc.getVerticalScrollBar().setValue(0);
		});
		revalidate();
		repaint();
	}

	private void setColor(int index, Color backColor) {
		categories.get(index).setBackground(backColor);
		categories.get(index).setForeground(backColor == Color.white ? Color.black : Color.white);
	}
	public static void main(String[] args) {
		new Serch(-1, "생");
	}
}
