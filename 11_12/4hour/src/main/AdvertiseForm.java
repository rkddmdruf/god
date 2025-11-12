package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class AdvertiseForm extends BaseFrame{

	JPanel borderPanel = new cp(new BorderLayout(15,15), sp.em(10, 10, 40, 10), null);
	Row row = null;
	JLabel i1 = null;
	JLabel i2 = null;
	JPanel main = new cp(null, sp.line, null);
	int x1, x2;
	public AdvertiseForm(int ano) {
		i1 = new cl(sp.getImg("datafiles/advertise/" + ano + "-1.jpg", 500, 240)) {{
			setBounds(x1 + 1, 1, 500, 240);
		}}.setBorders(sp.line);
		i2 = new cl(sp.getImg("datafiles/advertise/" + ano + "-2.jpg", 500, 240)) {{
			setBounds(x2 + 501, 1, 500, 240);
		}}.setBorders(sp.line);
		row = Query.selectText("select * from advertise where ano = ?", ano).get(0);
		setFrame("광고 정보", 540, 502, ()->{});
	}
	@Override
	protected void desing() {
		borderPanel.add(new cta(row.getString(1)) {{
			setPreferredSize(new Dimension(100, 70));
		}}.setting().font(sp.font(1, 25)), sp.n);
		borderPanel.add(new cta(row.getString(2)) {{
			setPreferredSize(new Dimension(100, 70));
		}}.setting().font(sp.font(0, 20)), sp.s);
		
		main.add(i1);
		main.add(i2);
		Timer timer = new Timer(10, e->{
			i1.setBounds(-(x1) + 1, 1, 500, 240);
			i2.setBounds(-(x2) + 501, 1, 500, 240);
			x1 = x1 + 1;
			x2 = x2 + 1;
			if(x1 == 500) x1 = -500;
			if(x2 == 1000) x2 = 0;
			repaints();
		});
		timer.start();
		borderPanel.add(main);
		add(borderPanel);
	}

	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new AdvertiseForm(1);
	}
}
