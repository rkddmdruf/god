package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import utils.BaseFrame;
import utils.Cb;
import utils.Query;
import utils.Row;
import utils.sp;

public class myHome extends BaseFrame{
	boolean mouse = false;
	int X = 0;private Point clickOffset;
	List<Row> pointHos = Query.hospitalUserPoint.select(1);
	JButton[] but = {new Cb("검사 결과"), new Cb("예약 내역")};
	List<Row> lists = new ArrayList<>() {{
		for(Row row : pointHos) {
			if(row.getInt(2) < sp.user.get(0).getInt(4) + 75 && row.getInt(2) > sp.user.get(0).getInt(4) - 75) {
				if(row.getInt(3)/2 < (sp.user.get(0).getInt(5) / 2) + 75  && row.getInt(3)/2 > (sp.user.get(0).getInt(5) / 2) - 75) {
					add(row);
				}
			}
		}
	}};
	List<JPanel> hospital설명 = new ArrayList<JPanel>();
	List<JButton> hospitalBut = new ArrayList<JButton>();
	JPanel borderPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}};
	JPanel northPanel = sp.Cjp(new GridLayout(0, 2));
	JPanel hospitalPanel = new JPanel(null);
	JPanel centerPanel = sp.Cjp(new BorderLayout());
	JPanel mapPanel = sp.Cjp(new BorderLayout());
	JPanel paintHospital = new JPanel();
	myHome(){
		setFrame("마이홈", 550,  500, ()->{ new Main();});
	}
	@Override
	protected void desing() {
		System.out.println(pointHos.get(0));
		getContentPane().setBackground(Color.white);
		northPanel.add(new JLabel(sp.user.get(0).getString(1) + "님", JLabel.LEFT) {{
			setFont(sp.fontM(1, 22));
		}});
		northPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
			setBackground(Color.white);
			for(JButton b : but) {
				b.setHorizontalAlignment(JLabel.RIGHT);
				this.add(b);
			}
		}});
		borderPanel.add(northPanel, sp.n);
		
		centerPanel.add(new JLabel("진료 기록"), sp.n);
		for(int i = 0; i < pointHos.size(); i++) {final int index = i;
			hospitalPanel.add(new JPanel(new BorderLayout()) {{
				setBackground(Color.white);
				setBorder(sp.line);
				hospitalBut.add(new Cb(new ImageIcon(new ImageIcon("src/hospital/" + pointHos.get(index).getInt(0) + ".png").getImage().getScaledInstance(100,  80, Image.SCALE_SMOOTH))));
				hospitalBut.get(index).setBorder(BorderFactory.createEmptyBorder(10,5,10,5));
				
				
				
				hospital설명.add(new JPanel(new GridLayout(4, 0)) {{
					setBackground(Color.white);
				}});
				if(pointHos.get(index).get(1).toString().toCharArray().length >6) {
					String str = pointHos.get(index).getString(1).substring(0, 6);
					hospital설명.get(index).add(new JLabel(str));
					str = pointHos.get(index).getString(1).substring(6, pointHos.get(index).getString(1).toCharArray().length);
					hospital설명.get(index).add(new JLabel(str));
				}else {
					hospital설명.get(index).add(new JLabel(pointHos.get(index).getString(1)));
				}
				
				hospital설명.get(index).add(new JLabel(pointHos.get(index).getString(10) + " 의사"));
				hospital설명.get(index).add(new JLabel(pointHos.get(index).getString(7)));
				
				
				
				this.add(hospital설명.get(index));
				this.add(hospitalBut.get(index), sp.w);
				this.setBounds(index*210, 10, 200, 90);
			}});
		}
		hospitalPanel.setBackground(Color.white);
		centerPanel.add(new JPanel(null) {{
			setBackground(Color.white);
			this.addMouseListener(new MM());
			this.addMouseMotionListener(new MM());
			hospitalPanel.setBounds(0,0, 210 * pointHos.size(), 100);
			add(hospitalPanel);
		}});
		borderPanel.add(centerPanel, sp.c);
		
		mapPanel.add(new JLabel("근처 병원"), sp.n);
		mapPanel.add(new JPanel(null) {{
			setPreferredSize(new Dimension(500, 250));
			setBackground(Color.white);
			
			add(new myPaint() {{
				setBounds(0,0, 500, 250);
			}});
			
			add(paintHospital = new hosPaint(){{
				setBounds(0,0,500,250);
			}});
			add(new reader() {{
				setBounds(0,0,500, 250);
			}});
			paintHospital.setVisible(false);
			Timer timer = new Timer(2000, e->{
					paintHospital.setVisible(true);
			});
			timer.setRepeats(false);
			timer.start();
			add(new JLabel(new ImageIcon(new ImageIcon("src/map.PNG").getImage().getScaledInstance(500, 250, Image.SCALE_SMOOTH))) {{
				setBounds(0,0, 500, 250);
			}});
		}}, sp.c);
		borderPanel.add(mapPanel, sp.s);
		
		
		
		add(borderPanel);
		
	}
	
	@Override
	protected void action() {
		
		for(int i = 0; i < hospitalBut.size(); i++) {final int index = i;
			hospitalBut.get(index).addActionListener(e->{
				new HospitalIfor(pointHos.get(index));
			});
		}
	}
	
	class hosPaint extends JPanel{
		@Override
		public void paint(Graphics g) {
			g.setColor(Color.black);
			for(Row row : lists) {
				g.fillOval(row.getInt(2), row.getInt(3)/2, 8, 8);
			}
			//병원 위치들 원 추가
		}
	}
	class reader extends JPanel{
		@Override
		public void paint(Graphics g) {
			
			g.setColor(new Color(50, 150, 200, 50));
			g.fillOval(sp.user.get(0).getInt(4) - 75, (sp.user.get(0).getInt(5) / 2) - 75, 150, 150);
		}
	}
	class myPaint extends JPanel{
		@Override
		public void paint(Graphics g) {
			g.setColor(Color.blue);
			g.fillOval(sp.user.get(0).getInt(4)-5,(sp.user.get(0).getInt(5) / 2)-5, 10,10);
			g.setColor(Color.black);
			g.drawString("현재위치", sp.user.get(0).getInt(4)-20,(sp.user.get(0).getInt(5) / 2)-10);
		}
	}
	
	class MM extends MouseAdapter implements MouseMotionListener, MouseListener{
		
	    @Override
	    public void mousePressed(MouseEvent e) {
	        clickOffset = e.getPoint();
	        Point panelLocation = getLocationOnScreen();
	        clickOffset.x -= panelLocation.x;
	        clickOffset.y -= panelLocation.y;

	        mouse = true; // 드래그 시작 플래그 설정
	    }
	    @Override
	    public void mouseReleased(MouseEvent e) {
	        mouse = false; // 드래그 종료 플래그 해제
	    }
	    @Override
	    public void mouseDragged(MouseEvent e) {
	    	System.out.println(e.getLocationOnScreen().x - clickOffset.x);
	        if (mouse && (e.getLocationOnScreen().x - clickOffset.x < 5 && e.getLocationOnScreen().x - clickOffset.x >= -1590)) {
	            Point newLocation = e.getLocationOnScreen();
	            hospitalPanel.setLocation(newLocation.x - clickOffset.x, 0); // 패널 위치 이동
	        }
	    }

	    

	}
	
	
	public static void main(String[] args) {
		new myHome();
	}
}
