package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import utils.*;

public class Gwoung_go extends BaseFrame{
	Timer timer;
	List<Row> list = new ArrayList<Row>();
	JPanel BorderPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(10,10,50,10));
	}};
	JPanel img = new JPanel(null) {{
		setPreferredSize(new Dimension(500, 250));
	}};
	int number;
	int x = 0;
	int X0 = 0, X1 = 0;
	boolean zero = true;
	Gwoung_go(int number){
		list = Query.Gwoung_go.select(number);
		this.number = number;
		setFrame("광고 정보", 550,550, ()->{timer.stop();});
	}
	
	
	@Override
	protected void desing() {
		
		BorderPanel.add(new JTextArea(list.get(0).getString(1)) {{
			setFont(sp.font(1, 30));
			setLineWrap(true);
		}}, sp.n);
		img.add(new JLabel(new ImageIcon(new ImageIcon("src/advertise/" + list.get(0).getInt(0) + "-1.JPG").getImage().getScaledInstance(500, 250, Image.SCALE_SMOOTH))) {{
			setBounds(0,0,500, 250);
		}});
		img.add(new JLabel(new ImageIcon(new ImageIcon("src/advertise/" + list.get(0).getInt(0) + "-2.JPG").getImage().getScaledInstance(500, 250, Image.SCALE_SMOOTH))) {{
			setBounds(500,0,500, 250);
		}});
		timer = new Timer(10, e->{
			System.out.println("sdf");
			img.getComponent(0).setBounds(-x + X0, 0, 500, 250);
			img.getComponent(1).setBounds(-x + 500 + X1, 0, 500, 250);
			x++;
			if(x % 500 == 0 && x != 0) {
				
				if(zero) {
					X0 = x + 500;
				}else if (!zero){
					X1 = x;
				}
				zero = !zero;
			}
		});
		timer.start();
		
		BorderPanel.add(new JPanel(new FlowLayout()) {{
			setBackground(Color.white);
			add(new JPanel(new BorderLayout()) {{
				setPreferredSize(new Dimension(500, 250));
				add(img);
				setBorder(sp.line);
			}});
		}});
		BorderPanel.add(new JTextArea(list.get(0).getString(2)) {{
			setFont(sp.font(0, 24));
			setLineWrap(true);
		}}, sp.s);
		add(BorderPanel);
	}

	@Override
	protected void action() {
		
	}
	public static void main(String[] args) {
		new Gwoung_go(1);
	}
}
