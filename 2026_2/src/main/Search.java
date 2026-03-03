package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;


import static javax.swing.BorderFactory.*;

import utils.CFrame;
import utils.Connections;
import utils.Data;
import utils.NorthPanel;
import utils.RoundedButton;
import utils.getter;

public class Search extends CFrame{
	JPanel categoryPanel = new JPanel(new GridLayout(1, 0, 5, 5)) {{
		setBorder(createEmptyBorder(0, 250, 0, 0));
		setBackground(Color.white);
	}};
	List<Data> list = new ArrayList<>();
	JPanel mainPanel = new JPanel(new BorderLayout(5, 5)) {{
		add(categoryPanel, BorderLayout.NORTH);
		setBackground(Color.white);
	}};
	
	JPanel scPanel = new JPanel(new GridLayout(0, 1, 10, 10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0, 0, 0, 10));
	}};
	JScrollPane sc = new JScrollPane(scPanel);
	
	List<JButton> butList = new ArrayList<>();
	int selectCategory;
	String name;
	public Search(int selectCategory, String name) {
		this.selectCategory = selectCategory;
		this.name = name;
		NorthPanel.vera = JLabel.CENTER;
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		setCategoryPanel();
		mainPanel.add(sc);
		borderPanel.add(mainPanel);
		setButColor();
		setAction();
		setFrame("자격증 목록", 750, 525);
	}
	
	private void setAction() {
		for(int i = 0; i < butList.size(); i++) {
			final int index = i;
			butList.get(index).addActionListener(e -> {
				selectCategory = index;
				setButColor();
			});
		}
	}
	
	int[] butId = {5,2,1,4,3,6};
	String[] butName = "IT, 요리, 봉사, 운동, 의학, 항공".split(", ");
	private void setCategoryPanel() {
		UIManager.put("Button.font", new Font("맑은 고딕", 1, 12));
		JButton best = new RoundedButton("추천과정") {{
			setBackground(Color.white);
			setForeground(Color.black);
		}};
		butList.add(best);
		categoryPanel.add(best);
		
		for(int i = 0; i < 6; i++) {
			JButton but = new RoundedButton(butName[i]);
			but.setBackground(Color.white);
			but.setForeground(Color.black);
			butList.add(but);
			categoryPanel.add(but);
		}
	}
	
	private void setButColor() {
		scPanel.removeAll();
		if(selectCategory != -1) {
			System.out.println("sd");
			butList.forEach(e -> {
				e.setBackground(e == butList.get(selectCategory) ? Color.blue : Color.white);
				e.setForeground(e  == butList.get(selectCategory) ? Color.white : Color.black);
			});
		}
		
		System.out.println(selectCategory);
		if(selectCategory > 0) {
			System.out.println(butId[selectCategory -1 ]);
			list = Connections.select("SELECT * FROM lecture.certi")
					.stream()
					.filter(e -> Arrays.asList(e.getString(12).split(","))
							.contains(butId[selectCategory - 1] + ""))
					.collect(Collectors.toList());
		}else if(selectCategory == 0) {
			list = Connections.select("SELECT certi.*, count(crno) as c FROM lecture.course_registration\r\n"
					+ "join certi on certi.cno = course_registration.cno\r\n"
					+ "group by certi.cno order by c desc;");
		}else if(selectCategory == -1) {
			list = Connections.select("SELECT * FROM lecture.certi where cname like ?;", "%" + name + "%");
		}
		Font font = new Font("맑은 고딕", 1, 10);
		
		int[] listIndex = {1, 7, 9};
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(5, 5));
			p.setBackground(Color.white);
			p.setBorder(createLineBorder(Color.black));
			
			JLabel img = new JLabel(getter.getImageIcon("datafiles/certification/" + list.get(index).getString(0) + ".png", 200, 150));
			img.setBorder(createCompoundBorder(createEmptyBorder(18, 21, 18, 0), createLineBorder(Color.black)));
			
			JPanel inforPanel = new JPanel(new BorderLayout());
			inforPanel.setBackground(Color.white);
			inforPanel.setBorder(createEmptyBorder(50, 0, 60, 0));
			inforPanel.add(new JLabel(list.get(i).getString(1) + " " + list.get(i).getInt(6) + "급") {{
				setFont(new Font("굴림", 1, 18));
			}}, BorderLayout.NORTH);
			
			JPanel infor = new JPanel(new GridLayout(2, 2));
			infor.setBackground(Color.white);
			
			Data data = Connections.select("select * from teacher where tno = ?", list.get(index).get(11)).get(0);
			for(int j = 0; j < 4; j++) {
				JLabel l = new JLabel(setString(j == 0 ? data : list.get(i), j));
				l.setFont(font);
				infor.add(l);
			};
			inforPanel.add(new JLabel( "<html><font color='gray'>· 수강혜택</font><font color='red'> 교안무료+시험예상기출문제 제공</font></html>") {{
				setFont(font);
			}}, BorderLayout.SOUTH);
			inforPanel.add(infor);
			
			JPanel butPanel = new JPanel(new GridLayout(2, 1, 10, 10));
			butPanel.setBorder(createEmptyBorder(65, 0, 65, 0));
			butPanel.setBackground(Color.white);
			butPanel.add(new RoundedButton("과목 선택하기") {{
				setForeground(Color.white);
				setBackground(Color.blue);
			}});
			butPanel.add(new RoundedButton("기출문제 맛보기") {{
				setForeground(Color.white);
				setBackground(new Color(0, 0, 139));
				addActionListener(e ->{
					try {
						Desktop.getDesktop().open(new File("datafiles/question/" + list.get(index).getString(1) + "/1.pdf"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				});
			}});
			
			p.add(butPanel, BorderLayout.EAST);
			p.add(img, BorderLayout.WEST);
			p.add(inforPanel);
			scPanel.add(p);
		}
		revalidate();
		repaint();
	}
	
	Object[] inforTitle = Arrays.asList("담당교수,응시조건,주무부처,발급기관".split(",")).stream().map(e -> "· " + e).toArray();
	private String setString(Data data, int i) {
		String str = "";
		switch (i) {
			case 0: { str = data.getString(1); break;}
			case 1: { str = data.getString(3) + "/" + data.getString(4); break;}
			case 2: { str = data.getString(7); break;}
			case 3: { str = data.getString(10); break;}
		}
		String s = "<html><font color='gray'>" + inforTitle[i] +"</font><font color='black'> " + str + (i == 0 ? " 교수" : "") + "</font></html>";
		return s;
	}
	public static void main(String[] args) {
		new Search(-1, "코");
	}
}
