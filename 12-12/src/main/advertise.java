package main;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import utils.*;
import utils.sp.*;

public class advertise extends BaseFrame{

	JPanel borderPanel = new cp(new BorderLayout(20, 20), sp.em(10, 15, 50, 15), null);
	JPanel mainPanel = new cp(null, sp.line, null);
	JLabel i1 = null, i2 = null;
	int n = 0, x1 = 0, x2 = 500;
	List<Row> list = new ArrayList<>();
	public advertise(int n) {
		this.n = n;
		list = Query.selectText("select * from advertise where ano = ?", n);
		i1 = new cl(sp.getImg("datafiles/advertise/" + n + "-1.jpg", 500, 250)) {{
			setBounds(1,1, 500, 250);
			setBorder(sp.line);
		}};
		i2 = new cl(sp.getImg("datafiles/advertise/" + n + "-2.jpg", 500, 250)) {{
			setBounds(502,1, 500, 250);
			setBorder(sp.line);
		}};
		setFrame("광고 정보", 548, 527, ()->{});
	}
	@Override
	protected void desing() {
		borderPanel.add(new ca(list.get(0).getString(1)).setting().font(sp.font(1, 30)), sp.n);
		mainPanel.add(i1);
		mainPanel.add(i2);
		Timer t = new Timer(10, e->{
			x1 = x1 - 1;
			i1.setBounds(1 + x1, 1, 500, 250);
			x2 = x2 - 1;
			i2.setBounds(1 + x2, 1, 500, 250);
			if(x1 <= -500) x1 = 500;
			if(x2 <= -500) x2 = 500;
			rp();
		});
		t.start();
		WindowAdapter a = new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				t.stop();
				removeWindowListener(this);
			}
		};
		addWindowListener(a);
		borderPanel.add(mainPanel);
		borderPanel.add(new ca(list.get(0).getString(2)).setting().font(sp.font(0, 20)), sp.s);
		add(borderPanel);
	}

	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new advertise(1);
	}
}
