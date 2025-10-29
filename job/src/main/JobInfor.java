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
import utils.sp.cl;
import utils.sp.cp;

public class JobInfor extends BaseFrame{
	Row list = new Row();
	boolean likesCheck = false;
	JPanel BorderPanel = new sp.cp(new BorderLayout(), sp.em(10, 10, 10, 10), null);
	JPanel southPanel = new sp.cp(new BorderLayout(), null, null);
	JLabel hart = new JLabel();
	int hartNumber = 1;
	JButton but = new sp.cb("지원하기 →").font(sp.font(1, 16)).BackColor(sp.color).fontColor(Color.white).size(125, 45);
	
	////밑에 스크롤관련 패널
	
	JPanel mapPanel = new sp.cp(null, null, null) {{
		this.size(300, 300);
		add(new brandPoint() {{ setBounds(10,10,300, 275); }});
		add(new sp.cl(sp.getImg("지도.PNG",300,  275)) {{
			setBounds(10,10, 300, 275);
		}});
	}};
	JPanel jobInforPanel = new sp.cp(new BorderLayout(0,20), sp.em(5, 5, 5, 5), null);
	JPanel scrollPanel = new JPanel(new GridLayout(2,1)) {{
		add(jobInforPanel);
		add(mapPanel);
	}};
	Border bo = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(20,0,5,0),BorderFactory.createMatteBorder(5, 0, 0, 0, sp.color));
	JPanel mainBorderPanel = new sp.cp(new BorderLayout(), bo, null) {{
		add(new JScrollPane(scrollPanel) {{ getVerticalScrollBar().setUnitIncrement(400); }});
	}};
	// 스크롤할떄쓰는 패널
	Row row;
	JobInfor(int jno){
		row = Query.JobInfor.select(jno).get(0);
		list = Query.brand_name_XY.select(row.get(2)).get(0);
		setFrame("알바 정보", 375, 500, ()->{new Main();});
	}
	
	@Override
	protected void desing() {
		System.out.println(row);
		BorderPanel.add(new JTextArea(row.getString(1)) {{
			setFont(sp.font(1, 20));
			setLineWrap(true);
			setEditable(false);
		}}, sp.n);
		
		
		
		jobInforPanel.add(new sp.cp(new GridLayout(0,1), null, null) {{
			add(jobs("[근무조건]"));
			add(jobs("급여: " + sp.df.format(row.getInt(3))+ "원"));
			add(jobs("근무요일: 주 " + row.getInt(5) + "회"));
			add(jobs("근무시간: " + row.getInt(6) + "시간"));
			add(jobs("고용형태: " + (row.getInt(7) == 0 ? "정규직" : "계약직")));
		}}, sp.n);
		
		jobInforPanel.add(new sp.cp(new GridLayout(0,1), null, null) {{
			add(jobs("[모집조건]"));
			add(jobs("지원자격: " + (row.getInt(8) == 0 ? "무관" : (row.getInt(8)== 1 ? "대학" : "고등"))));
			add(jobs("모집인원: " + row.getInt(4) + "명"));
		}}, sp.c);
		
		jobInforPanel.add(new sp.cp(new GridLayout(0,1), null, null) {{
			add(jobs("[기업정보]"));
			add(jobs(list.getString(2)));
			JLabel label = jobs("▽위치보기");
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println("sdf");
					JScrollPane sc = (JScrollPane) mainBorderPanel.getComponent(0);
					sc.getVerticalScrollBar().setValue(400);
				}
			});
			add(label);
		}}, sp.s);
		
		if(!Query.userLike.select(sp.user.get(0), row.getInt(0)).isEmpty()) likesCheck = true;
		
		hartNumber = likesCheck ? 1 : 2;
		setHart();
		southPanel.add(hart, sp.w);
		southPanel.add(but, sp.e);
		BorderPanel.add(southPanel, sp.s);
		
		BorderPanel.add(mainBorderPanel);
		add(BorderPanel);
	}
	private JLabel jobs(String string) {
		return new sp.cl(string).font(sp.font(0, 16));
	}
	private void setHart() {
		hart.setIcon(sp.getImg("icon/하트" + hartNumber +".PNG",50, 50));
		RePaint();
	}
	
	@Override
	protected void action() {
		hart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				hartNumber = hartNumber == 1 ? 2 : 1;
				setHart();
				(hartNumber == 2 ? Query.likeDel : Query.likeInsert).update(row.get(0), hartNumber == 1 ? LocalDate.now() : null, sp.user.get(0));
			}
		});
		mapPanel.getComponent(1).addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				long x = Math.round(list.getInt(0) / 2.0);
				long y = Math.round(list.getInt(1) * (275.0 / 500.0));
				if(e.getX() > x-5 && e.getX() < x + 5) {
					if(e.getY() > y-5 && e.getY() < y+5) {
						new BrandInf(row.getInt(2));
						dispose();
					}
				}
			}
		});
		but.addActionListener(e->{
			if(!Query.applyYes_NO.select(sp.user.get(0), row.get(0)).isEmpty()) {
				sp.ErrMes("이미 지원한 적 있는 알바입니다.");
				return;
			}
			
			sp.InforMes("지원이 완료되었습니다.");
			Query.applyInsert.update(row.get(0), LocalDate.now().plusDays(1), sp.user.get(0));
			new Main();
			dispose();
		});
	}
	class brandPoint extends JPanel{
		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(Color.red);
			g.fillOval((list.getInt(0) / 2) - 5, (list.getInt(1)/ 2 + ((list.getInt(1)/2)/10)) - 5, 10, 10);
		}
	}
}
