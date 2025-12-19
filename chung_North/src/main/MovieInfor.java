package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import utils.*;
import utils.sp.*;

public class MovieInfor extends BaseFrame{

	JPanel borderPanel = new sp.cp(new BorderLayout(10, 10), sp.em(5, 5, 5, 5), null);
	JPanel northPanel = new cp(new BorderLayout(), null, null);
	JPanel mainPanel = new cp(new GridLayout(0,1, 10, 10), sp.em(10, 5, 5, 5), null);
	JPanel one = new cp(new BorderLayout(), null, null).size(0, 250);
	JPanel two = new cp(new BorderLayout(), null, null).size(0, 250);
	JPanel three = new cp(new BorderLayout(), sp.line, null).size(0, 250);
	JButton 예매 = new cb("예매하기").BackColor(sp.color).fontColor(Color.white);
	JTextArea Infor = new JTextArea() {{
		setFont(sp.font(1, 24));
		setFocusable(false);
		setLineWrap(true);
		setEditable(false);
	}};
	int m_no = 0;
	List<Row> list = new ArrayList<>();
	public MovieInfor(int m_no) {
		this.m_no = m_no;
		list = Query.select("SELECT movie.*, genre.g_name FROM moviedb.movie \r\n"
				+ "join genre on movie.g_no = genre.g_no\r\n"
				+ "where m_no = ?;", m_no);
		setFrame("영화 정보", 750, 450, ()->{});
	}
	@Override
	protected void desing() {
		northPanel.add(logo, sp.w);
		northPanel.add(new cp(new GridLayout(0, 2, 5, 5), null, null) {{
			add(loginAll); add(movieSerch);
		}}, sp.e);
		one.add(new cp(null,null,null) {{
			add(new cl(sp.getImg("datafiles/limits/" + list.get(0).getInt(2) + ".png", 35, 35)) {{
				setBounds(10, 0, 35, 35);
			}});
			add(new cl(sp.getImg("datafiles/movies/" + list.get(0).getInt(0) + ".jpg", 140, 240)) {{
				setBounds(0, 0, 150, 250);
			}});
		}}.size(150, 0), sp.w);
		one.add(new cp(new BorderLayout(), null,null) {{
			
			add(new cp(new GridLayout(0, 1), null, null) {{
				add(new cl("제목: " + list.get(0).getString(1)).font(sp.font(1, 26)));
				add(new cl("감독: " + list.get(0).getString(3)).font(sp.font(1, 14)));
				add(new cl("장르: " + list.get(0).getString(9)).font(sp.font(1, 14)));
				add(new cl("개봉일: " + list.get(0).getString(6)).font(sp.font(1, 14)));
			}});
			add(new cp(new FlowLayout(FlowLayout.LEFT), sp.em(30, 5, 40, 0), null) {{
				add(예매, sp.s);
			}}, sp.s);
		}}, sp.c);
		
		Infor.setText(list.get(0).getString(4));
		two.add(new JScrollPane(Infor));
		
		mainPanel.add(one); mainPanel.add(two); mainPanel.add(three);
		borderPanel.add(new JScrollPane(mainPanel) {{
			getVerticalScrollBar().setUnitIncrement(20);
		}});
		three.add(new cl("") {
			@Override
			public void paintComponent(Graphics g) {
				
			}
		});
		borderPanel.add(northPanel, sp.n);
		add(borderPanel);
	}

	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new MovieInfor(1);
	}
}
