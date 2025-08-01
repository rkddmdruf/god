package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import javax.swing.*;

import utils.Query;
import utils.Row;
import utils.sp;

public class Last2 extends JPanel{
	int i = 0;
	String[] str = "식품 문구 생활용품 뷰티 음료 전자제품 의류 유아 스포츠 도서".split(" ");
	JLabel label = new JLabel(str[0]) {{setFont(sp.fontM(1, 36));setPreferredSize(new Dimension(200,50));setHorizontalAlignment(JLabel.CENTER);}};
	JComboBox<String> cb = new JComboBox<String>("전체 카테고리".split(" "));
	PP[] pps = new PP[7];
	Last2(JPanel p){
		setLayout(new BorderLayout());
		sp.setBorderLINE(label);
		add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{add(cb);}}, sp.n);
		add(new JPanel(new BorderLayout()) {{sp.setBorderLINE(this);
			add(new JPanel() {{add(label);}}, BorderLayout.NORTH);
			add(new JPanel(new FlowLayout(1, 100,50)) {{
				List<Row> list = Query.chat.select(1);
				for(int i = 0 ; i < 7; i++) {final int in = i;
					add(new JPanel(new BorderLayout()) {{
						setPreferredSize(new Dimension(50, 340));
							Color c = Color.red; if(list.get(0).getInt(7) != list.get(in).getInt(7)) {c = Color.blue;}
							add(pps[in] = new PP(c, list.get(in).getInt(7)) {{
								this.addMouseListener(new MouseAdapter() {
									@Override
									public void mouseEntered(MouseEvent e) {
										System.out.println("hi");
									}
								});
							}}, sp.c);
						add(new JLabel(list.get(in).getString(1)) {{setHorizontalAlignment(JLabel.CENTER);setFont(sp.fontM(1, 14));
						}}, sp.s);
					}});
				}
			}}, BorderLayout.CENTER);
		}}, sp.c);
		action();
		p.add(this, "P5");
	}
	public void action() {
		label.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				i += e.getWheelRotation();
				label.setText(str[i]);repaint();revalidate();
				
			}
		});
	}
}
class PP extends JPanel{
	Color c = null;
	int H = 0;
	PP(Color c, int H){
		this.c = c;
		this.H = H;
	}
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 332,55, -H * 17-3);
		g.drawString(H+"", 15, 328-(H*17));
		g.setColor(c);
		g.fillRect(2, 330, 51, -H * 17);
		
	}
	
}
