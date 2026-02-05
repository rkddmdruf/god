package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


import static javax.swing.BorderFactory.*;

import utils.*;

public class Calendar extends CFrame{
	Font font = new Font("맑은 고딕", 1, 30);
	LocalDate nows = LocalDate.now();
	JComboBox<Object> year = new JComboBox<Object>() {{
		for(int i = 1990; i <= nows.getYear(); i++) {
			addItem(i);
		}
	}};
	
	Data user = UserU.getUser();
	List<Data> list = Connections.select("SELECT gameinformation.g_no, g_name, p_birth FROM game_site.purchasegame \r\n"
			+ "join gameinformation on gameinformation.g_no = purchasegame.g_no\r\n"
			+ "where u_no = ? order by p_birth desc;", user.get(0));
	
	
	int Moment = LocalDate.parse(list.get(0).get(2).toString()).getMonthValue();
	
	LocalDate setDate = null;
	
	JLabel MomentLabel = new JLabel("sdfsdf") {{
		setFont(font);
		setPreferredSize(new Dimension(100, 50));
		setBackground(Color.red);
		setOpaque(true);
		setForeground(Color.white);
	}};
	JTextArea year_MomentName = new JTextArea() {{
		setFont(font.deriveFont(17f));
		setForeground(Color.red);
		setFocusable(false);
		setCursor(Cursor.getDefaultCursor());
	}};
	
	JLabel left = new JLabel("<") {{
		setFont(font);
	}};
	JLabel right = new JLabel(">") {{
		setFont(font);
	}};
	JPanel mainPanel = new JPanel(new GridLayout(7, 7)) {{
		setBackground(Color.white);
	}};
	public Calendar() {
		year.setSelectedIndex(LocalDate.parse(list.get(0).get(2).toString()).getYear() - 1990);
		setDate = LocalDate.of(year.getSelectedIndex() + 1990, Moment, 1);
		UIManager.put("Label.font", font.deriveFont(17f));
		
		ToolTipManager m = ToolTipManager.sharedInstance();
		m.setEnabled(true);
		m.setInitialDelay(0);
		setNorthPanel();
		setMainPanel();
		setAction();
		setFrame("달력", 525, 375);
	}
	
	private void setMainPanel() {
		JPanel p = new JPanel(new BorderLayout(10, 10));
		p.setBorder(createEmptyBorder(10, 0, 30, 0));
		p.setBackground(Color.white);
		
		p.add(new JPanel() {{
			setBorder(createEmptyBorder(125, 0, 0, 0));
			setBackground(Color.white);
			add(left);
		}}, BorderLayout.WEST);
		p.add(new JPanel() {{
			setBorder(createEmptyBorder(125, 0, 0, 0));
			setBackground(Color.white);
			add(right);
		}}, BorderLayout.EAST);
		p.add(mainPanel);
		add(p);
	}

	private void setAction() {
		year.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				Moment = 1;
				setYM();
			}
		});
		
		MouseAdapter m = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getSource() == left) {
					if(Moment == 1) return;
					Moment -= 1;
				}
				
				if(e.getSource() == right) {
					if(Moment == 12) return; 
					Moment += 1;
				}
				setYM();
			}
		};
		
		left.addMouseListener(m);
		right.addMouseListener(m);
	}
	
	List<JLabel> days = new ArrayList<>();
	private void setYM() {
		days.clear();
		mainPanel.removeAll();
		setDate = LocalDate.of(year.getSelectedIndex() + 1990, Moment, 1);
		MomentLabel.setText(" " + (Moment <= 9 ? "0" + Moment : Moment));
		year_MomentName.setText(year.getSelectedItem().toString() + "\n" + setDate.getMonth().name());
		
		for(String str : "일월화수목금토".split("")) {
			JLabel l = new JLabel(str, JLabel.CENTER);
			if(str.equals("일")) l.setForeground(Color.red);
			if(str.equals("토")) l.setForeground(Color.blue);
			mainPanel.add(l);
		}
		
		int test = setDate.getDayOfWeek().getValue();
		if(test != 7) {
			for(int i = 0; i < test; i++) {
				mainPanel.add(new JLabel(""));
			}
		}
		for(int i = 1; i <= setDate.lengthOfMonth(); i++) {
			LocalDate date = LocalDate.of(setDate.getYear(), setDate.getMonthValue(), i);
			JLabel l = new JLabel(i + "", JLabel.CENTER);
			if(date.getDayOfWeek().getValue() == 6) l.setForeground(Color.blue);
			if(date.getDayOfWeek().getValue() == 7) l.setForeground(Color.red);
			for(int s = 0; s < list.size(); s++) {
				if(date.isEqual(LocalDate.parse(list.get(s).get(2).toString()))) {
					l = new JLabel(i + "", JLabel.CENTER) {
						@Override
						protected void paintComponent(Graphics g) {
							g.setColor(Color.red);
							g.fillOval(0 + 10, 0, getWidth() - 20, getHeight());
							super.paintComponent(g);
						}
					};
					l.setToolTipText(list.get(i).get(1).toString());
					l.setForeground(Color.white);
				}
			}
			JLabel label = l;
			l.addMouseListener(new MouseAdapter() {
				LocalDate dates = date;
				@Override
				public void mouseClicked(MouseEvent e) {
					if(label.getForeground() != Color.white) {
						UIManager.put("OptionPane.messageFont", font.deriveFont(13f));
						getter.mg("해당 날짜에는 구매한 게임이 없습니다.", JOptionPane.ERROR_MESSAGE);
						return;
					}
					for(int i = 0; i < list.size(); i++) {
						if(LocalDate.parse(list.get(i).get(2).toString()).isEqual(date)) {
							new BuyGameFrom(list.get(i).getInt(0));
							dispose();
							return;
						}
					}
				}
			});
			mainPanel.add(label);
		}
		
		for(int i = 0; i < 36 - setDate.lengthOfMonth(); i ++) {
			mainPanel.add(new JLabel(""));
		}
		revalidate();
		repaint();
	}
	
	private void setNorthPanel() {
		setYM();
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.white);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		
		panel.add(MomentLabel);
		panel.add(year_MomentName, BorderLayout.EAST);
		p.add(panel, BorderLayout.WEST);
		
		p.add(new JPanel(new BorderLayout()) {{
			setBackground(Color.white);
			year.setPreferredSize(new Dimension(100, 40));
			add(year, BorderLayout.SOUTH);
		}}, BorderLayout.EAST);
		add(p, BorderLayout.NORTH);
	}

	public static void main(String[] args) {
		new Calendar();
	}
}
