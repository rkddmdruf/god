package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;

import utils.*;

public class JobInfor extends BaseFrame{
	Row list = new Row();
	List<Row> like = Query.userLike.select(sp.user.get(0));
	boolean likesCheck = false;
	JPanel BorderPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}};
	JPanel southPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
	}};
	JLabel hart = new JLabel();
	int hartNumber = 1;
	JButton but = new JButton("지원하기 →") {{
		setPreferredSize(new Dimension(125, 45));
		setFont(sp.font(1, 16));
		setBackground(sp.color);
		setForeground(Color.white);
	}};
	
	////
	JPanel mapPanel = new JPanel(null) {{
		setPreferredSize(new Dimension(300,300));
		add(new brandPoint() {{
			setBounds(10,10,300, 275);
		}});
		add(new JLabel(new ImageIcon(new ImageIcon("src/지도.PNG").getImage().getScaledInstance(300,  275, Image.SCALE_SMOOTH))) {{
			setBounds(10,10, 300, 275);
		}});
		
		setBackground(Color.white);
	}};
	JPanel jobInforPanel = new JPanel(new BorderLayout(0,20)) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	}};
	JPanel scrollPanel = new JPanel(new GridLayout(2,1)) {{
		add(jobInforPanel);
		add(mapPanel);
	}};
	JPanel mainBorderPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		Border bo = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(20,0,5,0),BorderFactory.createMatteBorder(5, 0, 0, 0, sp.color));
		setBorder(bo);
		add(new JScrollPane(scrollPanel) {{
			getVerticalScrollBar().setUnitIncrement(400);
		}});
	}};
	// 스크롤할떄쓰는 패널
	Row row;
	JobInfor(Row row){
		this.row  = row;
		setFrame("알바 정보", 375, 500, ()->{});
	}
	
	@Override
	protected void desing() {
		System.out.println(row);
		BorderPanel.add(new JTextArea(row.getString(2)) {{
			setFont(sp.font(1, 20));
			setLineWrap(true);
			setEditable(false);
		}}, sp.n);
		
		
		
		jobInforPanel.add(new whitePanel() {{
			setBackground(Color.white);
			add(jobs("[근무조건]"));
			add(jobs("급여: " + new DecimalFormat("###,###").format(row.getInt(4))+ "원"));
			add(jobs("근무요일: 주 " + row.getInt(6) + "회"));
			add(jobs("근무시간: " + row.getInt(7) + "시간"));
			add(jobs("고용형태: " + (row.getInt(8) == 0 ? "정규직" : "계약직")));
		}}, sp.n);
		
		jobInforPanel.add(new whitePanel() {{
			setBackground(Color.white);
			add(jobs("[모집조건]"));
			add(jobs("지원자격: " + (row.getInt(9) == 0 ? "무관" : (row.getInt(9)== 1 ? "대학" : "고등"))));
			add(jobs("모집인원: " + row.getInt(5) + "명"));
		}}, sp.c);
		
		jobInforPanel.add(new whitePanel() {{
			setBackground(Color.white);
			add(jobs("[기업정보]"));
			add(jobs(row.getString(0)));
			add(jobs("▽위치보기"));
		}}, sp.s);
		for(int i = 0; i < like.size(); i++) {
			if(row.getInt(1) == like.get(i).getInt(1)) {
				likesCheck = true;
				break;
			}
		}
		setHart(likesCheck ? 1 : 2);
		southPanel.add(hart, sp.w);
		southPanel.add(but, sp.e);
		BorderPanel.add(southPanel, sp.s);
		
		BorderPanel.add(mainBorderPanel);
		add(BorderPanel);
	}
	private class whitePanel extends JPanel{
		public whitePanel() {
			setBackground(Color.white);
			setLayout(new GridLayout(0,1));
		}
	}
	private JLabel jobs(String string) {
		JLabel label = new JLabel(string);
		label.setFont(sp.font(0, 16));
		return label;
	}
	private void setHart(int i) {
		hartNumber = i;
		hart.setIcon(new ImageIcon(new ImageIcon("src/icon/하트" + i +".PNG").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
		RePaint();
	}
	
	@Override
	protected void action() {
		hart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setHart(hartNumber == 1 ? 2 : 1);
				(hartNumber == 2 ? Query.likeDel : Query.likeInsert).update(row.get(1), hartNumber == 1 ? LocalDate.now() : null, sp.user.get(0));
			}
		});
		mapPanel.getComponent(1).addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getX() >= (list.getInt(0)/2) && e.getX() <= (list.getInt(0)/2) + 10) {
					int listy = list.getInt(1)/ 2 + ((list.getInt(1)/2)/10);
					if(e.getY() >= listy && e.getY() <= listy + 10){
						//브랜드 정보 이동 7-1
					}
				}
			}
		});
		but.addActionListener(e->{
			for(Row row : Query.applyYes_NO.select(sp.user.get(0))) {
				if(row.get(1) == this.row.get(1)) {
					sp.ErrMes("이미 지원한 적 있는 알바입니다.");
					return;
				}
			}
			sp.InforMes("지원이 완료되었습니다.");
			Query.applyInsert.update(row.get(1), LocalDate.now(), sp.user.get(0));
			new Main();
			dispose();
		});
	}
	class brandPoint extends JPanel{
		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(Color.red);
			Row lists = Query.brandXY.select(row.get(3)).get(0);
			list = lists;
			g.fillOval(lists.getInt(0) / 2, lists.getInt(1)/ 2 + ((lists.getInt(1)/2)/10), 10, 10);
		}
	}
}
