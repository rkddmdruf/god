package main;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.util.ArrayList;
import java.util.List;
import utils.*;

public class ceyung extends BaseFrame{
	JPanel northPanel = new JPanel(new FlowLayout()) {{
		setBackground(Color.white);
	}};
	JPanel centerPanel = new JPanel(new BorderLayout()) {{
		setBorder(BorderFactory.createEmptyBorder(5,0,5,10));
		setBackground(Color.white);
	}};
	JPanel southPanel = new JPanel(new GridLayout(0, 1, 50,10)) {{
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setBackground(Color.white);
	}};
	JPanel BorderPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}};
	JTextField tf = new JTextField() {{
		setPreferredSize(new Dimension(330, 30));
		setBorder(BorderFactory.createLineBorder(sp.color));
	}};
	JButton serch = new sp.Cb() {{
		setPreferredSize(new Dimension(60,50));
		setHorizontalAlignment(SwingConstants.RIGHT);
		setIcon(new ImageIcon(new ImageIcon("src/icon/search.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
	}};
	List<Row> jobList = Query.job.select();
	JComboBox<String> category = new JComboBox<String>() {{
		addItem("전체");
		for(Row row : Query.category.select()) addItem(row.getString(1));
	}};
	JComboBox<String> order_by = new JComboBox<String>("기본순,인기순,급여높은순".split(","));
	
	JScrollPane sc = new JScrollPane(southPanel) {{
		setBorder(BorderFactory.createLineBorder(sp.color));
		setPreferredSize(new Dimension(500, 444));
		this.getVerticalScrollBar().setUnitIncrement(20);
	}};
	List<JButton> 광고 = new ArrayList<>();
	List<Integer> 광고_number = new ArrayList<Integer>();
	List<JTextPane> tp = new ArrayList<JTextPane>();
	boolean fristAction = true;
	ceyung(){
		setFrame("채용", 550, 600, ()->{new Main();});
	}
	@Override
	protected void desing() {
		northPanel.add(new JLabel(new ImageIcon(new ImageIcon("src/icon/cat.png").getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH))),sp.w);
		northPanel.add(tf);
		northPanel.add(serch);
		
		centerPanel.add(category, sp.w);
		centerPanel.add(order_by, sp.e);
		
		setSouthPanel();
		BorderPanel.add(northPanel, sp.n);
		BorderPanel.add(centerPanel, sp.c);
		BorderPanel.add(sc, sp.s);
		
		
		
		
		add(BorderPanel);
	}
	
	
	@Override
	protected void action() {
		category.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) setSouthPanel();
		});
		order_by.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) setSouthPanel();
		});
		serch.addActionListener(e->{
			if(tf.getText().equals("")) {
				category.setSelectedIndex(0);
				order_by.setSelectedIndex(0);
			}
		});
		for(int i = 0; i < (광고_number.size() > tp.size() ? 광고_number.size(): tp.size()); i++) {final int index = i;
			if(i < tp.size()) {
				tp.get(index).addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						new JobInfor(jobList.get(index));
					}
				});
			}
			if(i < 광고_number.size()) {
				광고.get(index).addActionListener(e->{
					new Gwoung_go(광고_number.get(index));
				});
			}
		}
	}
	
	public void setSouthPanel(){
		int[] cno = {category.getSelectedIndex() == 0 ? 1 : category.getSelectedIndex(),
				category.getSelectedIndex() == 0 ? 10 : category.getSelectedIndex()};
		
		if(order_by.getSelectedIndex() == 0) {
			jobList = Query.ceyung_asc.select(cno[0], cno[1]);
		}else if(order_by.getSelectedIndex() == 1) {
			jobList = Query.ceyung_ingi.select(cno[0], cno[1]);
		}else if(order_by.getSelectedIndex() == 2) {
			jobList = Query.ceyung_money.select(cno[0], cno[1]);
		}
		southPanel.removeAll();
		광고.clear();
		광고_number.clear();
		tp.clear();
		for(Row row : jobList) {
			JPanel panel = new JPanel(new BorderLayout()) {{
				setPreferredSize(new Dimension(100, 70));
				setBorder(sp.line);
			}};
			if((int) (Math.random() * 2) == 1) {
				int randomN = (int) (Math.random() * 200 +1);
				광고.add(sp.Cb(new ImageIcon(new ImageIcon("src/advertise/" + randomN + "-1.JPG")
						.getImage().getScaledInstance(100, 70, Image.SCALE_SMOOTH))));
				광고.get(광고.size()-1).setPreferredSize(new Dimension(100,70));
				광고_number.add(randomN);
				panel.add(광고.get(광고.size()-1), sp.w);
			}
			tp.add(new JTextPane());
			tp.get(tp.size() - 1);
			tp.get(tp.size() - 1).setEditable(false);
			tp.get(tp.size() - 1).setFont(sp.font(1, 16));
			tp.get(tp.size() - 1).setText(row.getString(2));
			
			StyledDocument doc = tp.get(tp.size() - 1).getStyledDocument();
			SimpleAttributeSet center = new SimpleAttributeSet();
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
			doc.setParagraphAttributes(0, doc.getLength(), center, false);
			//이거 더 알아봐야 할듯
			panel.add(tp.get(tp.size() - 1),sp.c);
			southPanel.add(panel);
		}
		RePaint();
		SwingUtilities.invokeLater(() -> {
			sc.getVerticalScrollBar().setValue(sc.getVerticalScrollBar().getMinimum());
		});
		if(!fristAction) {
			action();
		}
		fristAction = false;
	}
	public static void main(String[] args) {
		new ceyung();
	}
}
