package main;

import java.awt.*;
import javax.swing.*;

import utils.*;

public class GwoungGo extends BaseFrame{
	Row gInfor;
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(10, 10, 30,10), null);
	JPanel mainPanel = new sp.cp(null, sp.com(sp.em(30, 0, 30, 0), sp.line), null);
	JLabel img1, img2;
	int x1 = 0, x2 = 0;
	public GwoungGo(int ano) {
		gInfor = Query.selectText("SELECT * FROM parttimecat.advertise where ano = ?;", ano).get(0);
		img1 = new sp.cl(sp.getImg("advertise/" + ano + "-1.jpg", 250, 120));
		img2 = new sp.cl(sp.getImg("advertise/" + ano + "-2.jpg", 250, 120));
		setFrame("광고 정보", 288, 366, ()->{});
	}
	@Override
	protected void desing() {
		borderPanel.add(new sp.cta(gInfor.getString(1)).setting().font(sp.font(1, 17)), sp.n);
		borderPanel.add(new sp.cta(gInfor.getString(2)) {{
			setPreferredSize(new Dimension(10,  55));
		}}.setting().font(sp.font(0, 13)), sp.s);
		img1.setBounds(1, 31 ,250, 120);
		img2.setBounds(251, 31 ,250, 120);
		mainPanel.add(img1);
		mainPanel.add(img2);
		Timer timer = new Timer(20, e->{
			img1.setBounds(-x1 + 1, 31 ,250, 120);
			img2.setBounds(-x2 + 251, 31 ,250, 120);
			if(x1 == 250) {
				x1 = x1 -500;
			}
			if(x2 == 500) {
				x2 = 0;
			}
			x1++;
			x2++;
			System.out.println(x1);
			repaints();
		});
		timer.start();
		borderPanel.add(mainPanel);
		
		add(borderPanel);
	}

	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new GwoungGo(1);
	}
}
