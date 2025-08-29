package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.BaseFrame;
import utils.Cb;
import utils.Query;
import utils.Row;
import utils.sp;

public class Doctor extends BaseFrame{
	Row row;
	List<Row> hospital = Query.hospitalTOdoctorALL.select();
	List<JButton> but = new ArrayList<>();
	JComboBox<String> cb = new JComboBox<String>();
	JPanel borderPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}};
	JPanel doctor = new JPanel(null);
	
	
	
	JPanel doctorInfor = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}};
	JPanel dt_doctorPanel = new JPanel(new BorderLayout());
	Doctor(Row row){
		this.row = row;
		setFrame("의사", 400, 600, ()->{});
	}
	JButton yeyag = new JButton("예약하기") {{
		setForeground(Color.white);
		setFont(sp.fontM(0, 15));
		setPreferredSize(new Dimension(240, 40));
		setBackground(new Color(100, 200, 220, 150));
		setBorder(BorderFactory.createLineBorder(Color.white));
	}};
	
	int dno;
	@Override
	protected void desing() {
		
		cb.addItem("전체");
		for(Row row : hospital) {
			cb.addItem(row.getString(5));
		}
		hospital = Query.hospitalTOdoctor.select(row.get(0));
		cb.setSelectedIndex(row.getInt(0));
		System.out.println(hospital);
		doctor.setBackground(Color.white);
		setDoctor();
		
		
		borderPanel.add(doctor);
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBackground(Color.white);
			add(cb);
		}}, sp.n);
		add(borderPanel);
	}

	@Override
	protected void action() {
		cb.addActionListener(e->{
			hospital = cb.getSelectedIndex() == 0 ? Query.hospitalTOdoctorALL.select() : Query.hospitalTOdoctor.select(cb.getSelectedIndex());
			setDoctor();
		});
		yeyag.addActionListener(e->{
			new Month(hospital.get(dno).getInt(0));
		});
	}
	/*for(int i = 0; i < doctor.getComponentCount(); i++) {final int index = i;
			((JButton) doctor.getComponent(i)).addActionListener(e->{
				System.out.println(hospital.get(index).get(1));
			});
	private void setDoctor() {
		doctor.removeAll();
		but.clear();
		for(int i = 0; i < hospital.size(); i++) {final int index = i;
			doctor.add(new Cb(new ImageIcon(new ImageIcon("src/doctor/" + hospital.get(i).getInt(0) + ".png")
					.getImage().getScaledInstance(250, 125, Image.SCALE_SMOOTH))));
			JButton button = (JButton) doctor.getComponent(index);
			button.setBounds(0, index * (button.getIcon().getIconHeight() + 10) + 20, button.getIcon().getIconWidth(), button.getIcon().getIconHeight());
		}
		repaint();revalidate();
	}*/
	
	private void setDoctor() {
		doctor.removeAll();
		but.clear();
		for(int i = 0; i < hospital.size(); i++) {final int index = i;
			but.add(new Cb(new ImageIcon(new ImageIcon("src/doctor/" + hospital.get(i).getInt(0) + ".png")
					.getImage().getScaledInstance(240, 120, Image.SCALE_SMOOTH))));
			but.get(index).setBounds(0, index * (but.get(index).getIcon().getIconHeight() + 10) + 20,
					but.get(index).getIcon().getIconWidth(), but.get(index).getIcon().getIconHeight());
			but.get(index).addActionListener(e->{
				setDoctorFrame(index);
			});
			doctor.add(but.get(index));
			
			
			doctor.add(new JLabel(hospital.get(index).getString(1) + " 의사") {{
				setFont(sp.fontM(1, 15));
				setBounds(250, index * (but.get(index).getIcon().getIconHeight() + 10) + 40, 100, 50);
			}});
			doctor.add(new JLabel(hospital.get(index).getString(5)) {{
				setFont(sp.fontM(1, 10));
				setBounds(250, index * (but.get(index).getIcon().getIconHeight() + 10) + 60, 100, 50);
			}});
		}
		repaint();revalidate();
	}
	private void setDoctorFrame(int dno) {
		this.dno = dno;
		setSize(500, 500);
		borderPanel.setVisible(false);
		dt_doctorPanel.add(new JLabel(new ImageIcon(new ImageIcon("src/doctor/" + hospital.get(dno).getInt(0) + ".png")
				.getImage().getScaledInstance(475, 200, Image.SCALE_SMOOTH))), sp.n);
		dt_doctorPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBackground(Color.white);
			add(new JLabel(hospital.get(dno).getString(1) + " 의사") {{
				setFont(sp.fontM(1, 22));
			}});
			add(new JLabel("내과 전문의") {{
				setFont(sp.fontM(0, 16));
			}});
		}}, sp.c);
		
		
		doctorInfor.add(dt_doctorPanel,sp.n);
		doctorInfor.add(new JPanel(new GridLayout(3,0)) {{
			setBackground(Color.white);
			add(new JLabel("소속 : " + hospital.get(dno).getString(5)));
			add(new JLabel("소개글 : " + hospital.get(dno).getString(2)));
			add(new JLabel("진료 날짜/시간 : " + hospital.get(dno).getString(3)));
		}}, sp.c);
		for(int i = 0; i < 3; i++) ((Container)doctorInfor.getComponent(1)).getComponent(i).setFont(sp.fontM(0, 16));
		doctorInfor.add(new JPanel() {{
			setBackground(Color.white);
			add(yeyag);
		}}, sp.s);
		add(doctorInfor);
		repaint();revalidate();
	}
	public static void main(String[] args) {
		new Doctor(Query.hospitalUserPoint.select(1).get(0));
	}
}
