package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;

import utils.*;

public class Month extends BaseFrame{
	LocalDate date = LocalDate.now().minusDays(LocalDate.now().getDayOfMonth()-1);
	LocalDate yo1Check = LocalDate.now().minusDays(LocalDate.now().getDayOfMonth()-1);
	final LocalDate now = LocalDate.now();
	
	String[] yo1 = "일,월,화,수,목,금,토".split(",");
	
	JLabel YM = new JLabel() {{
		setFont(sp.fontM(0, 20));
		setHorizontalAlignment(JLabel.CENTER);
	}};
	JButton left = new Cb("◀") {{
		setFont(sp.fontM(1, 22));
	}};
	JButton rigth = new Cb("▶") {{
		setFont(sp.fontM(1, 22));
	}};
	
	JPanel northPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
	}};
	JPanel days = new JPanel(new GridLayout(0, 7)) {{
		setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
		setBackground(Color.white);
	}};
	
	////////////////////////////***************
	///////////////////**********
	////////////*****
	//그 날짜 같으면 그전 달로 넘어가는거 안함
	/////////////*****
	/////////////////**********
	/////////////////////***************
	////////////////////////***************
	
	int doctor = 0;
	Month(int doctor){
		this.doctor = doctor;
		setFrame("달력", 550, 400, ()->{});
	}
	@Override
	protected void desing() {
		setMonth();
		northPanel.add(YM, sp.c);
		northPanel.add(left,sp.w);
		northPanel.add(rigth,sp.e);
		
		setDays();
		
		add(northPanel, sp.n);
		add(days);
	}

	@Override
	protected void action() {
		left.addActionListener(e->{
			if(now.isAfter(date)) {
				sp.ErrMes("이전 날짜는 선책할 수 없습니다.");
				return;
			}
			date = date.minusMonths(1);
			yo1Check = yo1Check.minusMonths(1);
			setday();
			setMonth();
			setDays();
		});
		
		rigth.addActionListener(e->{
			date = date.plusMonths(1);
			yo1Check = yo1Check.plusMonths(1);
			setday();
			setMonth();
			setDays();
		});
		
		
	}
	private void setday() {
		date = date.minusDays(date.getDayOfMonth() - 1);
		yo1Check = yo1Check.minusDays(yo1Check.getDayOfMonth() - 1);
	}
	private void setDays() {
		days.removeAll();
		for(String str : yo1) {
			days.add(new JLabel(str) {{
				setForeground(str.equals("일") || str.equals("토") ? Color.red : Color.black);
				setFont(sp.fontM(1, 16));
				setHorizontalAlignment(JLabel.CENTER);
			}});
		}
		if(date.getDayOfWeek().getValue() != 7) {
			for(int i = 0; i < date.getDayOfWeek().getValue(); i++) {
				days.add(new JLabel(" "));
			}
		}
		for(int i = 1; i <= date.lengthOfMonth(); i++) {final int index = i;
			days.add(new Cb(i + "") {{
				setForeground(yo1Check.getDayOfWeek().getValue() == 6 || yo1Check.getDayOfWeek().getValue() == 7 ? Color.red : Color.black);
				setFont(sp.fontM(1, 16));
				setHorizontalAlignment(JLabel.CENTER);
				addActionListener(e->{
					char[] monthC = (date.getMonthValue()+"").toCharArray();
					char[] dayC = Integer.toString(index).toCharArray();
					String dateString = date.getYear() + "-" + (monthC.length == 1 ? ("0" + new String(monthC)) : new String(monthC)) + 
							"-" + (dayC.length == 1 ? ("0" + new String(dayC)) : new String(dayC));
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate date = LocalDate.parse(dateString, formatter);
					if(date.isBefore(now.plusDays(1))) sp.ErrMes("오늘 이후의 날짜만 선택 가능합니다.");
					else {
						Query.reservation.update(date, sp.user.get(0).get(0), doctor);
						sp.InforMes("예약이 완료되었습니다");
						new Main();
						dispose();
					}
				});
			}});
			if(i != date.lengthOfMonth()) {
				yo1Check = yo1Check.plusDays(1);
			}
			
		}
		rePainted();
		
        
        

	}
	private void setMonth() {
		String[] str = date.toString().split("-");
		YM.setText(str[0] + "년 " + str[1] + "월");
		rePainted();
	}
	public static void main(String[] args) {
		new Month(1);
	}
}
