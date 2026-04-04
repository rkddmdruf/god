package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.PopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.LocalDate;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.CFrame;
import utils.Connections;
import utils.Data;

public class Schedule extends CFrame{

	LocalDate date = LocalDate.of(2026, 5, 1);
	int cno;
	Data	 data;
	JSlider js = new JSlider(1, 31) {{
		setBackground(Color.white);
	}};
	JPanel southPanel = new JPanel(new GridLayout(0, 3, 20, 20)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(10, 80, 10, 80));
	}};
	public Schedule(int scno, int cno) {
		data = Connections.select("select * from certi where cno = ?", cno).get(0);
		this.cno = cno;
		borderPanel.setBorder(createEmptyBorder(10,10,10,10));
		borderPanel.add(new JLabel(data.getString(1) + " " +  data.getString(6) + "급") {{
			setFont(new Font("맑은 고딕", 1, 16));
		}}, BorderLayout.NORTH);
		List<Data> list = Connections.select("SELECT * FROM lecture.schedule where cno = ?;", cno);
		js.setValue(LocalDate.parse(list.get(0).getString(3)).getDayOfMonth());
		setSouthPanel();
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		setDayPanel();
		setFrame("시험스케줄", 400, 175);
	}
	
	private void setSouthPanel() {
		southPanel.removeAll();
		LocalDate date = LocalDate.of(this.date.getYear(), this.date.getMonthValue(), js.getValue());
		List<Data> list = Connections.select("SELECT * FROM lecture.schedule where cno = ? and exam_date = ?;", cno, date);
		System.out.println(list);
		System.out.println(date);
		if(list.isEmpty()) {
			southPanel.setLayout(new BorderLayout());
			southPanel.add(new JLabel("시험이 존재하지 않습니다.", JLabel.CENTER));
		}else {
			String[] strs = list.get(0).getString(4).split(", ");
			for(String s : strs) {
				southPanel.setLayout(new GridLayout(0, 3, 20, 20));
				southPanel.add(new JButton(s) {{
					setBackground(Color.white);
					setBorder(createLineBorder(Color.black));
					setFont(new Font("맑은 고딕", 1, 14));
				}});
			}
		}
		revalidate();
		repaint();
	}

	private void setDayPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(createEmptyBorder(10,0, 10, 0));
		p.setBackground(Color.white);
		p.add(js, BorderLayout.SOUTH);
		p.add(new JLabel(date.getYear() + "-" + date.getMonthValue(), JLabel.CENTER), BorderLayout.NORTH);
		p.add(new JLabel("1"), BorderLayout.WEST);
		p.add(new JLabel("31"), BorderLayout.EAST);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		js.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				ToolTipManager.sharedInstance().setEnabled(true);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				ToolTipManager.sharedInstance().setEnabled(false);
			}
		});
		js.addMouseMotionListener(new MouseMotionAdapter() {
		    @Override
		    public void mouseDragged(MouseEvent e) {
		        js.setToolTipText(js.getValue() + "");
		        ToolTipManager.sharedInstance().mouseMoved(e);
		    }
		});
		js.addChangeListener(e->{
			setSouthPanel();
		});
		borderPanel.add(p);
		
	}

	public static void main(String[] args) {
		new Schedule(1, 1);
	}
}
