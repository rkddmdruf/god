package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.*;
import utils.sp.*;

public class Main extends BaseFrame{
	JPanel borderPanel = new cp(new BorderLayout(10, 10), sp.em(0, 10, 10, 10), null);
	JPanel southPanel = new cp(new GridLayout(0, 2, 5, 5), sp.em(10, 0, 0, 0), null);
	JPanel topPanel = new cp(new BorderLayout(), null, null);
	//JButton login = new cb(sp.user.isEmpty() ? "로그인" : "내 정보").BackColor(Color.blue).fontColor(Color.white).size(100,  50);
	//JButton movieSerch = new cb("영화 검색").BackColor(Color.blue).fontColor(Color.white);
	JButton movieAllShow = new cb("영화 전체보기").BackColor(Color.blue).font(sp.font(1, 10)).fontColor(Color.white);
	JButton foodOrder = new cb("먹거리 키오스크").BackColor(Color.blue).font(sp.font(1, 10)).fontColor(Color.white);
	JLabel mainImg = new cl(sp.getImg("datafiles/advertising/1.jpg", 625, 200)).size(625, 200);
	List<Row> movie10 = Query.select("SELECT reservation.m_no, count(reservation.m_no) AS co, movie.m_name  FROM moviedb.reservation \r\n"
			+ "left join movie on movie.m_no = reservation.m_no\r\n"
			+ "group by reservation.m_no order by co desc, reservation.m_no limit 10;");
	List<Row> likeMovie = Query.select("SELECT movie.m_name, review.*, avg(review.re_star) as d FROM moviedb.review\r\n"
			+ "left join movie on movie.m_no = review.m_no\r\n"
			+ " group by review.m_no order by d desc, review.m_no limit 5;");
	Main(){
		setFrame("메인", 650, 580, ()->{});
	}
	@Override
	protected void desing() {
		System.out.println(movie10);
		topPanel.add(new cp(new GridLayout(0, 2, 5, 5), null, null) {{
			add(loginAll); add(movieSerch);
		}}, sp.e);
		topPanel.add(logo, sp.w);
		
		borderPanel.add(mainImg);
		
		
		JPanel southWestPanel = new cp(new BorderLayout(), sp.com(sp.line, sp.em(5, 5, 5, 5)), null);
		southWestPanel.add(new cp(new FlowLayout(FlowLayout.LEFT), null, null) {{
			add(movieAllShow);
			add(foodOrder);
		}}, sp.n);
		southWestPanel.add(new cp(null, null, null) {{
			setPreferredSize(new Dimension(0, 200));
			for(int i = 0; i < 10; i++) {final int j = i;
				add(new cp(null, null, null) {{
					add(new cl(j+1) {{
						setBounds(10,5, 20, 40);
					}}.fontColor(Color.white).font(sp.font(1, 30)));
					add(new cl(sp.getImg("datafiles/movies/" + movie10.get(j).getInt(0) + ".jpg", 110, 140)) {{
						setBounds(0, 0, 110, 140);
					}});
					setBounds(j * 120, 20, 110, 140);
				}});
				add(new cl(movie10.get(j).getString(2)) {{
					setBounds(j*125, 155, 100, 30);
				}}.font(sp.font(1, 11)));
			}
		}});
		southPanel.add(southWestPanel, sp.w);
		
		JPanel southEastPanel = new cp(new BorderLayout(), sp.com(sp.line, sp.em(15, 5, 10, 5)), null);
		JPanel p = new cp(null, null, null);
		southEastPanel.add(p);
		for(int i = 0; i < 5; i++) {final int j = i;
			JPanel panel = new cp(new BorderLayout(), null, null);
			panel.setBounds(j * 130 + 1, 1, 120, 200);
			panel.add(new cp(new FlowLayout(FlowLayout.LEFT), null, null) {
				@Override
				public void paintComponent(Graphics g) {
					g.setColor(Color.black);
					g.setFont(sp.font(1, 30));
					g.drawString(likeMovie.get(j).getString(7).substring(0, 3), 70, 30);
				}
			}.size(120, 50), sp.n);
			panel.add(new cl(sp.getImg("datafiles/movies/" + likeMovie.get(j).getInt(3) + ".jpg", 120, 140)));
			panel.add(new cl(likeMovie.get(j).getString(0)).font(sp.font(1, 12)), sp.s);
			p.add(panel);
		}
		southPanel.add(southEastPanel);
		borderPanel.add(southPanel, sp.s);
		borderPanel.add(topPanel, sp.n);
		add(borderPanel);
	}

	@Override
	protected void action() {
		
	}
	public static void main(String[] args) {
		new Main();
	}
}
