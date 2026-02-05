package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import utils.*;

public class Charge extends CFrame{
	Font font = new Font("맑은 고딕", 1, 24);
	JPanel borderPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(10,10,10,10));
	}};
	JButton charge = new JButtonC("충전!");
	
	List<JPanel> panels = new ArrayList<>();
	
	String[] name = ",초,울트라,울트라슈퍼,레전드".split(",");
	int[] point = {5, 10, 15, 20, 25};
	JLabel pointLabel = new JLabel("충전 포인트 : +0");
	Data user = UserU.getUser();
	
	int price = 0;
	public Charge() {
		borderPanel.add(new JLabel("포인트 충전 선택") {{
			setFont(font);
		}}, BorderLayout.NORTH);
		UIManager.put("Label.font", font.deriveFont(15f));
		setMainPanel();
		UIManager.put("Label.font", font.deriveFont(18f));
		setChargePanel();
		add(borderPanel);
		setAction();
		setFrame("충전", 700, 350);
	}
	
	private void setAction() {
		for(int i = 0; i < panels.size(); i++) {
			final int index = i;
			panels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setPointLabel(index);
				}
			});
		}
		charge.addActionListener(e->{
			if(price == 0) {
				getter.mg("결제할 금액을 선택해주세요", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Connections.update("update user set u_price = ? where u_no = ?", user.getInt(6) + price,user.get(0));
			getter.mg("충전이 완료되었습니다.", JOptionPane.INFORMATION_MESSAGE);
			new Main();
			dispose();
		});
	}
	private void setPointLabel(int i) {
		for(int si = 0; si < 5; si++) {
			JComponent l1 = ((JComponent) panels.get(si).getComponent(0));
			l1.setBorder(createMatteBorder(1, 1, 0, 1, Color.black));
			l1.setForeground(Color.black);
			
			JComponent l2 = ((JComponent) panels.get(si).getComponent(1));
			l2.setBorder(createLineBorder(Color.black));
			l2.setForeground(Color.black);
		}
		JComponent l1 = ((JComponent) panels.get(i).getComponent(0));
		l1.setBorder(createMatteBorder(1, 1, 0, 1, Color.red));
		l1.setForeground(Color.red);
		
		JComponent l2 = ((JComponent) panels.get(i).getComponent(1));
		l2.setBorder(createLineBorder(Color.red));
		l2.setForeground(Color.red);
		
		pointLabel.setText("충전 포인트 : +" + getter.df.format(point[i] * 1000) + "포인트");
		price = point[i] * 1000;
		revalidate();
		repaint();
	}
	
	private void setChargePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		
		JPanel gridPanel = new JPanel(new GridLayout(3, 1, 5, 5));
		gridPanel.setBackground(Color.white);
		
		gridPanel.add(new JLabel("충전할 아이디 : " + user.get(2)));
		pointLabel.setFont(new JLabel().getFont());
		gridPanel.add(pointLabel);
		gridPanel.add(charge);
		
		panel.add(gridPanel, BorderLayout.EAST);
		borderPanel.add(panel, BorderLayout.SOUTH);
	}

	private void setMainPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 5, 5, 5));
		panel.setBackground(Color.white);
		
		for(int i = 0; i < 5; i++) {
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			p.add(new JLabel(name[i] + "대박", JLabel.CENTER) {{
				setBorder(createMatteBorder(1, 1, 0, 1, Color.black));
			}}, BorderLayout.NORTH);
			p.add(new JLabel(getter.df.format(point[i] * 1000) + "포인트", JLabel.CENTER) {{
				setBorder(createLineBorder(Color.black));
			}});
			
			panels.add(p);
			panel.add(p);
		}
		borderPanel.add(panel);
	}

	public static void main(String[] args) {
		new Charge();
	}
}
