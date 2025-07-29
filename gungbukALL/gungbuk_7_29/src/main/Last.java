package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import javax.swing.*;

import utils.Query;
import utils.Row;
import utils.sp;

public class Last extends JPanel{
	int i = 0;
	String[] str = "식품 문구 생활용품 뷰티 음료 전자제품 의류 유아 스포츠 도서".split(" ");
	JLabel label = new JLabel(str[0]) {{setFont(sp.fontM(1, 36));setPreferredSize(new Dimension(200,50));setHorizontalAlignment(JLabel.CENTER);}};
	JComboBox<String> cb = new JComboBox<String>("전체 카테고리".split(" "));
	PP[] pps = new PP[7];
	CardLayout card = new CardLayout();
	JPanel cardP = new JPanel(card);
	int j = 0;
	Last(JPanel p){
		setLayout(new BorderLayout());
		sp.setBorderLINE(label);
		add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{add(cb);}}, sp.n);
		add(new JPanel(new BorderLayout()) {{sp.setBorderLINE(this);
			add(new JPanel() {{add(label);}}, BorderLayout.NORTH);
			for(int i = 0; i < 10 ;i++)panels(cardP, i+1);
			add(cardP, BorderLayout.CENTER);
		}}, sp.c);
		action();
		p.add(this, "P5");
	}
	public void action() {
		label.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				i += e.getWheelRotation();
				if(i > 9 && e.getWheelRotation() == 1) {i = 0;}
				if(i < 0 && e.getWheelRotation() == -1) {i = 9;}
				label.setText(str[i]);repaint();revalidate();
				card.show(cardP, "A" + (i+1));
				System.out.println(i);
				repaint();revalidate();
			}
		});
	}
	
	public void panels(JPanel p, int pj) {
		p.add(new JPanel(new GridLayout(1, 7)) {{
			
			List<Row> list = Query.chat.select(pj);
			for(int i = 0; i < 330; i++) {if(list.get(0).getInt(7) * i > 320) {j = i-1;break;}}
			
			for(int i = 0 ; i < 7; i++) {final int in = i;
				add(new JPanel(new BorderLayout()) {{
					
						Color c = Color.red; if(list.get(0).getInt(7) != list.get(in).getInt(7)) {c = Color.blue;}
						add(pps[in] = new PP(c, list.get(in).getInt(7),j, list.get(in).getString(2)) {{setPreferredSize(new Dimension(50, 340));
							
						}}, sp.n);
					add(new JLabel(list.get(in).getString(1)) {{setHorizontalAlignment(JLabel.CENTER);setFont(sp.fontM(1, 14));
						sp.setBorderIE(this, -40, 0, 0, 0);
					}}, sp.c);
				}});
			}
		}}, "A"+ pj);
	}
}
class PP extends JPanel{
	Color c = null;
	final int H;
	int i = 0;
	String str = "";
	PP(Color c, int H, int i, String str){
		this.c = c;this.H = H;
		this.i = i;this.str  =str;
		this.addMouseMotionListener(new MouseAdapter() {
			@Override public void mouseMoved(MouseEvent e) {
				if(e.getX() >= 40 && e.getX() <= 95 && e.getY() <= 332 && e.getY() >= 328-(H*i)+3) {showPopupMenu(e.getX(), e.getY(), true);}
				else {showPopupMenu(e.getX(), e.getY(), false);};
			}
		});
	}
	private void showPopupMenu(int x, int y, boolean c) {
	    JPopupMenu popupMenu = new JPopupMenu();
	    JLabel label = new JLabel(str);
	    popupMenu.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
	    popupMenu.setBorder(BorderFactory.createLineBorder(Color.black));
	    popupMenu.add(label);
	    popupMenu.show(this, x, y);
	    popupMenu.setVisible(c);
	}
	
	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 340, 55, -340);
		g.setColor(Color.black);
		g.fillRect(40, 332,55, -H * i);
		g.drawString(H+"", 45+15, 328-(H*i)+3);
		g.setColor(c);
		g.fillRect(40+2, 330, 51, -(H * i)+3);
		
	}
}
