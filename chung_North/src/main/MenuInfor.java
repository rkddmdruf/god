package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import utils.*;
import utils.sp.*;

public class MenuInfor extends BaseFrame{

	JPanel borderPanel = new cp(new BorderLayout(), sp.em(5, 5, 5, 5), null);
	JPanel northPanel;
	JPanel southPanel = new cp(new BorderLayout(7, 7), null ,null).size(0,  150);
	
	JButton puls = new cb("+").BackColor(Color.blue).fontColor(Color.white).font(sp.font(1, 24));
	JButton mins = new cb("-").BackColor(Color.blue).fontColor(Color.white).font(sp.font(1, 24));
	JButton buy = new cb("구매").fontColor(Color.white).BackColor(Color.blue).font(sp.font(1, 24));
	JButton dispose = new cb("취소").BackColor(Color.red).fontColor(Color.white).font(sp.font(1, 24));
	
	JLabel pay = new cl("").font(sp.font(1, 24));
	JLabel countLabel = new cl("1").font(sp.font(1, 24)).setBorders(sp.line).setHo(JLabel.CENTER);
	Row food;
	int count = 1;
	int price = 0;
	public MenuInfor(int f_no) {
		food = Query.select("select * from food where f_no = ?", f_no).get(0);
		setFrame("메뉴 정보", 450 + 16, 600 + 39, ()->{});
	}
	
	@Override
	protected void desing() {
		setpay();
		northPanel = new cp(new BorderLayout(), null, null) {{
			add(new ca("이름: " + food.getString(1)).font(sp.font(1, 24)).setting(), sp.n);
			add(pay, sp.s);
		}};
		borderPanel.add(northPanel, sp.n);
		borderPanel.add(new cl(sp.getImg("datafiles/foods/" + food.getInt(0) + ".jpg", 440, 300)).setBorders(sp.line));
		borderPanel.add(new cp(new BorderLayout(), null,null) {{
			setPreferredSize(new Dimension(0, 150));
		}}, sp.s);
		southPanel.add(countLabel);
		southPanel.add(mins, sp.w);
		southPanel.add(puls, sp.e);
		southPanel.add(new cp(new GridLayout(0, 2, 5, 5), null, null) {{
			add(dispose); add(buy);
		}}.size(0, 75), sp.s);
		borderPanel.add(southPanel, sp.s);
		add(borderPanel);
	}

	private void setCount() {
		countLabel.setText(count+"");
		setpay();
	}
	private void setpay() {
		int foodP = food.getInt(2);
		pay.setText("가격: " + new DecimalFormat("###,###").format(foodP * count));
		price = foodP * count;
		if(sp.getBirthday(LocalDate.parse(sp.user.getString(4)))) {
			pay.setText("가격: " + new DecimalFormat("###,###").format((foodP * count) / 2));
			price = (foodP * count) / 2;
		}
		
		RePaint();
	}
	@Override
	protected void action() {
		dispose.addActionListener(e->{
			new Kiosc();
			dispose();
		});
		buy.addActionListener(e->{
			int result = JOptionPane.showConfirmDialog(null, "결제하시겠습니까?", "결재", JOptionPane.YES_NO_OPTION);
			if(result != JOptionPane.YES_OPTION) {return;}
			if(sp.user.getInt(5) < price) {
				sp.err("잔액이 부족합니다.");
				return;
			}
			sp.Infor("결제가 완료 되었습니다.");
			Query.update("insert into fb values(0, ?, ?, ?);", sp.user.get(0), food.get(0), count);
			Query.update("update user set u_price = ? where u_no = ?;", sp.user.getInt(5) - price, sp.user.get(0));
			sp.user = Query.select("select * from user where u_no = ?", sp.user.get(0)).get(0);
			System.out.println(sp.user.get(5));
			new Kiosc();
			dispose();
		});
		mins.addActionListener(e->{
			puls.setEnabled(true);
			if(count > 1) count--;
			setCount();
			if(count == 1) mins.setEnabled(false);
		});
		puls.addActionListener(e->{
			mins.setEnabled(true);
			if(count < 10) count++;
			setCount();
			if(count == 10) puls.setEnabled(false);
		});
	}
	public static void main(String[] args) {
		new MenuInfor(1);
	}
}
