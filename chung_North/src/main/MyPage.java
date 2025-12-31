package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class MyPage extends BaseFrame{
	JPanel scPanel1 = new cp(new GridLayout(0, 2, 10, 10), null, null);
	JPanel scPanel2 = new cp(new GridLayout(0, 2, 0,0), null, null);
	JScrollPane sc1 = new JScrollPane(scPanel1);
	JScrollPane sc2 = new JScrollPane(scPanel2);
	JPanel northPanel = new cp(new BorderLayout(7,7) , null, null);
	JPanel mainPanel = new cp(new GridLayout(0, 2, 5, 5), sp.line, null) {{
		add(sc1); add(sc2);
	}};
	JPanel borderPanel = new cp(new BorderLayout(5, 5), sp.em(0, 10, 10, 10), null) {{
		add(northPanel, sp.n);
		add(mainPanel);
	}};
	List<Row> food = Query.select("SELECT food.* FROM moviedb.fb "
			+ "join food on food.f_no = fb.f_no "
			+ "where u_no = ? order by fb.fb_no;", sp.user.get(0));
	List<Row> movie = Query.select("SELECT movie.* FROM moviedb.reservation "
			+ "join movie on movie.m_no = reservation.m_no "
			+ "where u_no = ?;", sp.user.get(0));
	List<JLabel> movies = new ArrayList<>();
	public MyPage() {
		setFrame("내 정보",  703, 425, ()->{});
	}
	@Override
	protected void desing() {
		northPanel.add(new cl(sp.getImg("datafiles/user/" + sp.user.getInt(0) + ".jpg", 120, 120)).setBorders(sp.line), sp.w);
		northPanel.add(new cp(new BorderLayout(0, -60), null, null) {{
			add(new cl(sp.user.getString(2)).font(sp.font(1, 18)), sp.n);
			add(new cl(sp.user.getString(1)).font(sp.font(1, 18)));
		}});
		for(Row row : food) {
			scPanel1.add(new cp(new BorderLayout(5,5), null, null) {{
				add(new cl(sp.getImg("datafiles/foods/" + row.getInt(0) + ".jpg", 77, 77)).size(77, 77));
				add(new cl(row.get(1)).setHo(JLabel.CENTER).font(sp.font(1, 13)), sp.s);
			}});
		}
		for(Row row : movie) {
			scPanel2.add(new cp(new BorderLayout(5,5), sp.em(0,0,5,0) ,null) {{
				JLabel l = new cl(sp.getImg("datafiles/movies/" + row.getInt(0) + ".jpg", 77, 99)).size(77, 99);
				movies.add(l);
				add(movies.get(movies.size()-1));
				add(new cl(row.getString(1)).setHo(JLabel.CENTER).font(sp.font(1, 12)), sp.s);
			}});
		}
		add(borderPanel);
	}

	@Override
	protected void action() {
		for(int i = 0; i < movies.size(); i++) {final int index = i;
			movies.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new MovieInfor(movie.get(index).getInt(0));
					dispose();
				}
			});
		}
	}
	public static void main(String[] args) {
		new MyPage();
	}
}
