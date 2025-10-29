package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import utils.*;
import utils.sp.cl;
import utils.sp.cp;

public class Gwoung_go extends BaseFrame{
	Timer timer;
	List<Row> list = new ArrayList<Row>();
	JPanel BorderPanel = new sp.cp(new BorderLayout(), sp.em(10, 10, 50, 10), null);
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
		setFrame("광고 정보", 550,550, ()->{timer.stop();
			new Main();
		});
	}
	
	@Override
	protected void desing() {
		
		BorderPanel.add(new JTextArea(list.get(0).getString(1)) {{
			setFont(sp.font(1, 30));
			setEditable(false);
			setFocusable(false);
			setLineWrap(true);
		}}, sp.n);
		img.add(new sp.cl(sp.getImg("advertise/" + list.get(0).getInt(0) + "-1.JPG",500, 250)) {{
			setBounds(0,0,500, 250);
		}});
		img.add(new sp.cl(sp.getImg("advertise/" + list.get(0).getInt(0) + "-2.JPG", 500, 250)) {{
			setBounds(500,0,500, 250);
		}});
		
		timer = new Timer(10, e->{
			img.getComponent(0).setBounds(-x + X0, 0, 500, 250);
			img.getComponent(1).setBounds(-x + 500 + X1, 0, 500, 250);
			x++;
			if(x % 500 == 0 && x != 0) {
				if(zero) {
					X0 = x + 500;
				}else {
					X1 = x;
				}
				zero = !zero;
			}
		});
		timer.start();
		
		BorderPanel.add(new sp.cp(new FlowLayout(),null, null) {{
			add(new sp.cp(new BorderLayout(), sp.line, null) {{  add(img);  }});
		}});
		BorderPanel.add(new JTextArea(list.get(0).getString(2)) {{
			setFont(sp.font(0, 24));
			setEditable(false);
			setFocusable(false);
			setLineWrap(true);
		}}, sp.s);
		add(BorderPanel);
	}

	@Override
	protected void action() {}
	public static void main(String[] args) {
		new Gwoung_go(1);
	}
}
