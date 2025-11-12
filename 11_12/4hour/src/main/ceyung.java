package main;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.text.*;

import utils.*;
import utils.sp.*;
public class ceyung extends BaseFrame{
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(10,10,10,10), null);
	JTextField tf = new JTextField() {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setFont(sp.font(1, 16));
			g.setColor(Color.LIGHT_GRAY);
			if(this.getText().isEmpty()) {
				g.drawString("검색", 2, 20);
			}
		}
	{
		setPreferredSize(new Dimension(250, 30));
		setBorder(sp.line(sp.color));
	}};
	JButton serch = new cb(sp.getImg("datafiles/icon/search.png", 40, 40)).setting().size(40, 40).BackColor(Color.white);
	JPanel southPanel = new sp.cp(new BorderLayout(8, 8), sp.em(0, 0, 15, 15), null) {{
		add(new cl(sp.getImg("datafiles/icon/cat.png", 70, 40)), sp.w);
		add(new cp(new FlowLayout(), sp.em(7, 0, 0, 0),null) {{
			add(tf);
		}});
		add(serch, sp.e);
	}};
	
	JComboBox<String> category = new JComboBox<String>() {{
		addItem("전체");
		for(Row row : Query.selectText("select cname from category")) {
			addItem(row.getString(0));
		}
	}};
	JComboBox<String> orderBy = new JComboBox<String>("기본순, 인기순, 급여높은순".split(","));
	
	JPanel mainPanel = new cp(new GridLayout(0, 1, 10, 10), sp.com(sp.line(sp.color), sp.em(7,7,7,7)), null);
	JScrollPane sc = new JScrollPane(mainPanel) {{
		getVerticalScrollBar().setUnitIncrement(20);
		setPreferredSize(new Dimension(100,350));
		setBorder(sp.line(sp.color));
	}};
	boolean reset = true;
	public ceyung() {
		setFrame("채용", 450, 510, ()->{});
	}
	@Override
	protected void desing() {
		setmainPanel();
		borderPanel.add(new cp(new BorderLayout(), sp.em(0, 0, 10, 5), null) {{
			add(category, sp.w);
			add(orderBy, sp.e);
		}});
		borderPanel.add(sc, sp.s);
		borderPanel.add(southPanel, sp.n);
		add(borderPanel);
	}
	@Override
	protected void action() {
		category.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED && reset) setmainPanel(); 
		});
		orderBy.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED && reset) setmainPanel(); 
		});
		serch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!tf.getText().isEmpty()) {
					setmainPanel();
					return;
				}
				reset = false;
				category.setSelectedIndex(0);
				orderBy.setSelectedIndex(0);
				setmainPanel();
				reset = true;
			}
		});
	}
	private void setmainPanel() {
		mainPanel.removeAll();
		int[] cno = { category.getSelectedIndex() == 0 ? 1 : category.getSelectedIndex(), category.getSelectedIndex() == 0 ? 10 : category.getSelectedIndex()};
		String string = "" + "%" + tf.getText() + "%";
		List<Row> list = null;
		if(orderBy.getSelectedIndex() == 0) list =  Query.ceyung_nomal.select(cno[0], cno[1], string);
		if(orderBy.getSelectedIndex() == 1) list =  Query.ceyung_best.select(cno[0], cno[1], string);
		if(orderBy.getSelectedIndex() == 2) list =  Query.ceyung_money.select(cno[0], cno[1], string);
		for(Row row : list) {
			mainPanel.add(new cp(new BorderLayout(), sp.line, null) {{
				int truefalse = (int) (Math.random() * 2) + 1;
				System.out.println(truefalse);
				if(truefalse == 1) {
					int r = (int) ((Math.random() * 200) + 1);
					add(new sp.cl(sp.getImg("datafiles/advertise/" + r + "-" + ((int) (Math.random() * 2) + 1) + ".jpg", 80, 70)) {{
						addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								new AdvertiseForm(r);
							}
						});
					}}, sp.w);
				}
				JTextPane tpName = new JTextPane();
				tpName.setFont(sp.font(1, 16));
				tpName.setEditable(false);
				tpName.setText(row.getString(1));
				StyledDocument doc = tpName.getStyledDocument();
				SimpleAttributeSet center = new SimpleAttributeSet();
				StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
				doc.setParagraphAttributes(0, doc.getLength(), center, false);
				tpName.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						new JobInfor(row.getInt(0));
					}
				});
				add(tpName);
			}}.size(300, 70));
		}
		SwingUtilities.invokeLater(()->{
			sc.getVerticalScrollBar().setValue(0);
		});
		repaints();
	}
	public static void main(String[] args) {
		new ceyung();
	}
}
