package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class MovieSerch extends BaseFrame{
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(10, 10, 10, 10), null);
	JPanel northPanel = new sp.cp(new BorderLayout(), null, null);
	JTextField serch = new JTextField() {{
		setPreferredSize(new Dimension(175, 25));
	}};
	JButton serchBut = new cb("검색").BackColor(sp.color).fontColor(Color.white);
	JComboBox<String> cb1 = new JComboBox<String>("전체,예매순,평점순".split(","));
	JComboBox<String> cb2 = new JComboBox<String>() {{
		addItem("전체");
		for(Row row : Query.select("SELECT * FROM moviedb.genre;"))
			addItem(row.getString(1));
	}};
	boolean serchDouble = true;
	
	JPanel mainPanel = new sp.cp(new GridLayout(0, 4, 10, 10), null, null);
	public MovieSerch(){
		setFrame("영화 검색", 900, 400, ()->{});
	}
	@Override
	protected void desing() {
		northPanel.add(new cp(new BorderLayout(), null, null) {{
			add(logo, sp.w);
			add(new cp(new GridLayout(0, 2, 5, 5), null, null) {{
				add(loginAll); add(movieSerch);
			}}, sp.e);
		}}, sp.n);
		northPanel.add(new cp(new FlowLayout(FlowLayout.LEFT), null, null) {{
			add(new cl("검색창").font(sp.font(1, 20)));
			add(serch);
			add(serchBut);
			add(cb1);
			add(cb2);
		}});
		setMainPanel();
		add(borderPanel);
	}

	private void setMainPanel() {
		borderPanel.removeAll();
		mainPanel.removeAll();
		int[] gno = {cb2.getSelectedIndex() == 0 ? 0 : cb2.getSelectedIndex()
				, cb2.getSelectedIndex() == 0 ? 21 : cb2.getSelectedIndex()}; 
		List<Row> list = (cb1.getSelectedIndex() == 0 ? Query.MovieSerch_All : 
			(cb1.getSelectedIndex() == 1 ? Query.MovieSerch_예매순 : Query.MovieSerch_평점순)).select(gno[0], gno[1], "%" + serch.getText() + "%");
		System.out.println(list.size());
		
		for(int i = 0; i < list.size(); i++) {
			JPanel panel = new cp(new BorderLayout(5,5), null, null).size(0, 225);
			if(cb1.getSelectedIndex() == 1 && i < 10) {
				setNumbering(panel, i+1);
			}else if(cb1.getSelectedIndex() == 2 && i < 5) {
				setNumbering(panel, i+1);
			}
			panel.add(new cl(sp.getImg("datafiles/limits/" + list.get(i).getInt(2) + ".png", 35, 35)) {{
				setBorder(sp.em(0, 5, 0, 0));
				setVerticalAlignment(JLabel.TOP);
			}}, sp.w);
			panel.add(new cl(sp.getImg("datafiles/movies/" + list.get(i).getInt(0) + ".jpg", 125, 175)).setHo(JLabel.LEFT));
			panel.add(new JTextArea(list.get(i).getString(1) + "\n3.2% | 개봉일: " + list.get(i).getString(6)) {{
				setFont(sp.font(1, 13));
				setLineWrap(true);
			}}, sp.s);
			mainPanel.add(panel);
		}
		System.out.println(mainPanel.getComponentCount());
		borderPanel.add(northPanel, sp.n);
		borderPanel.add(new JScrollPane(mainPanel) {{setBorder(sp.em(0, 0, 0, 0)); getVerticalScrollBar().setUnitIncrement(20);}});
		RePaint();
	}
	
	private void setNumbering(JComponent c, int n) {
		c.add(new cl("No." + n) {{
			setOpaque(true);
			setBackground(new Color(255, 0, 0, 100));
		}}.fontColor(Color.white).setHo(JLabel.CENTER), sp.n);
	}
	@Override
	protected void action() {
		cb1.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED && serchDouble) {
				serch.setText("");
				setMainPanel();
			}
		});
		cb2.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED && serchDouble) {
				serch.setText("");
				setMainPanel();
			}
		});
		serchBut.addActionListener(e->{
			if(serch.getText().isEmpty()) {
				serchDouble = false;
				cb1.setSelectedIndex(0);
				cb2.setSelectedIndex(0);
				serchDouble = true;
			}else {
				setMainPanel();
			}
		});
	}
	public static void main(String[] args) {
		new MovieSerch();
	}
}
