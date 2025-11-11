package main;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import utils.*;
import utils.sp.cb;
import utils.sp.cl;
import utils.sp.cp;

public class PullOut extends BaseFrame{
	boolean tfCheck = true;
	List<Integer> gwoungGo = new ArrayList<>();
	List<JLabel> gwoungGoLabel = new ArrayList<JLabel>();
	List<Row> list = new ArrayList<>();
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(0, 10, 10, 10), null);
	JPanel scrollPanel = new sp.cp(new GridLayout(0,1, 10, 10), sp.com(sp.line(sp.color), sp.em(10, 10, 10, 10)), null);
	JTextField tf = new JTextField() {
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.setColor(Color.LIGHT_GRAY);
			g.setFont(sp.font(1, 17));
			if(this.getText().isEmpty()) {
				g.drawString("검색", 0, 22);
			}
		}
	};
	JComboBox<String> category = new JComboBox<>() {{
		addItem("전체");
		for(Row row : Query.selectText("select cname from category")) addItem(row.getString(0));
	}};
	JComboBox<String> order_by = new JComboBox<>() {{
		setPreferredSize(new Dimension(70,20));
		addItem("기본순"); addItem("인기순"); addItem("급여높은순");
	}};
	JLabel serch = new JLabel(sp.getImg("icon/search.png", 50,50));
	public PullOut() {
		setFrame("채용", 550, 600, ()->{});
	}
	@Override
	protected void desing() {
		tf.setBorder(sp.line(sp.color));
		tf.setPreferredSize(new Dimension(300, 30));
		borderPanel.add(new sp.cp(new FlowLayout(FlowLayout.LEFT, 10, 10), null, null) {{
			add(new sp.cl(sp.getImg("icon/cat.png", 100, 50)));
			add(tf);
			add(serch);
		}}, sp.n);
		borderPanel.add(new sp.cp(new BorderLayout(), sp.em(0, 0, 5, 8),null) {{
			add(category, sp.w);
			add(order_by, sp.e);
		}});
		
		borderPanel.add( new JScrollPane(scrollPanel) {{
			setPreferredSize(new Dimension(100, 450));
			this.getVerticalScrollBar().setUnitIncrement(20);
			this.setBorder(sp.line(sp.color));
		}}, sp.s);
		setPanel();
		add(borderPanel);
	}

	@Override
	protected void action() {
		category.addItemListener(e -> {
			if(e.getStateChange() == ItemEvent.SELECTED && tfCheck) setPanel();
		});
		order_by.addItemListener(e -> {
			if(e.getStateChange() == ItemEvent.SELECTED && tfCheck) setPanel();
		});
		serch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(tf.getText().isEmpty()) {
					tfCheck = false;
					gwoungGo.clear();
					gwoungGoLabel.clear();
					category.setSelectedIndex(0);
					order_by.setSelectedIndex(0);
					tfCheck = true;
					setPanel();
					return;
				}
				setPanel();
			}
		});
		for(int i = 0; i < gwoungGo.size(); i++) { final int index = i;
			gwoungGoLabel.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new GwoungGo(gwoungGo.get(index));
				}
			});
		}
	}
	
	public void setPanel() {
		scrollPanel.removeAll();
		String string = "%" + tf.getText() +"%";
		int in = category.getSelectedIndex();
		int[] cno = {in == 0 ? 1 : in, in == 0 ? 10 : in};
		switch (order_by.getSelectedIndex()) {
			case 0: {list = Query.PullOut_nomal.select(cno[0], cno[1], string); break;}
			case 1: {list = Query.PullOut_best .select(cno[0], cno[1], string); break;}
			case 2: {list = Query.PullOut_money.select(cno[0], cno[1], string); break;}
		}
		for(Row row : list) {
			JPanel p = new sp.cp(new BorderLayout(), sp.line, null).size(10, 70);
			JTextPane textPane = new JTextPane();
			textPane.setEditable(false);
			textPane.setFocusable(false);
			textPane.setFont(sp.font(1, 16));
			textPane.setText(row.getString(1));
			StyledDocument doc = textPane.getStyledDocument();
			SimpleAttributeSet center = new SimpleAttributeSet();
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
			doc.setParagraphAttributes(0, doc.getLength(), center, false);
			textPane.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new JobInfor(row.getInt(0));
				}
			});
			p.add(textPane);
			if((int) (Math.random() * 10) % 3 == 0) {
				gwoungGo.add((int) (Math.random() * 200) + 1);
				int number = gwoungGo.get(gwoungGo.size()-1);
				gwoungGoLabel.add(new sp.cl(sp.getImg("advertise/" + number + "-" + (number > 5 ? 1 : 2) + ".jpg", 120, 70)).size(120, 70));
				p.add(gwoungGoLabel.get(gwoungGoLabel.size()-1), sp.w);
			}
			scrollPanel.add(p);
		}
		
		repaints();
		SwingUtilities.invokeLater(()->{
			((JScrollPane) borderPanel.getComponent(2)).getVerticalScrollBar().setValue(0);
		});
		
	}
	public static void main(String[] args) {
		new PullOut();
	}
}
