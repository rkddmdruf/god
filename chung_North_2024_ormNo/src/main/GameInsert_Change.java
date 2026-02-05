package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static javax.swing.BorderFactory.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import utils.*;

public class GameInsert_Change extends CFrame{
	JPanel borderPanel = new JPanel(new BorderLayout(20, 20)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(10, 10, 10, 10));
	}};
	JTextField name = new JTextField();
	JTextField point = new JTextField();
	JComboBox<Object> category = new JComboBox<Object>(){{
		setBackground(Color.white);
		addItem("");
		for(Data data : Connections.select("select * from category")) addItem(data.get(1));
	}};
	
	JRadioButton allAge = new JRadioButton("전체") {{
		setBackground(Color.white);
	}};
	JRadioButton adultAge = new JRadioButton("19세") {{
		setBackground(Color.white);
	}};
	
	JLabel img = new JLabel("") {{
		setPreferredSize(new Dimension(200, 200));
		setBorder(createLineBorder(Color.black));
	}};
	
	JTextArea infor = new JTextArea() {{
		setLineWrap(true);
		setFont(new Font("맑은 고딕", 1, 14));
	}};
	
	JButton but = new JButtonC("");
	Data data = new Data();
	int gno;
	public GameInsert_Change(int gno) {
		this.gno = gno;
		if(gno != 0) {
			category.removeItemAt(0);
			data = Connections.select("select * from gameinformation where g_no = ?", gno).get(0);
			name.setText(data.get(1).toString());
			point.setText(data.get(2).toString());
			category.setSelectedIndex(data.getInt(4) - 1);
			infor.setText(data.get(6).toString());
			if(data.getInt(5) == 0) 
				allAge.setSelected(true);
			else 
				adultAge.setSelected(true);
			img.setIcon(getter.getImage("gameimage/" + gno + ".jpg", 200, 200));
		}
		UIManager.put("Label.font", new Font("맑은 고딕", 1, 16));
		setMainPanel();
		
		borderPanel.add(new JScrollPane(infor));
		but.setText(gno == 0 ? "추가" : "수정");
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(but);
		}}, BorderLayout.SOUTH);
		add(borderPanel);
		
		category.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED)
				if(gno == 0 && category.getItemAt(0).toString().isEmpty()) {
					category.removeItemAt(0);
				}
		});
		
		img.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
		        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JPG Images", "jpg", "jpeg"));
		        fileChooser.showOpenDialog(null);
				File inputFile = fileChooser.getSelectedFile();
				if(inputFile == null) return;
	            try {
					BufferedImage image = ImageIO.read(inputFile);
					img.setIcon(new ImageIcon(image.getScaledInstance(200, 200, 4)));
					revalidate();
					repaint();
				} catch (IOException e1) { 
					
				}
			}
		});
		but.addActionListener(e->{
			String n = name.getText();
			String p = point.getText();
			String i = infor.getText();
			int ageLimit = allAge.isSelected() ? 0 : 1;
			if(!allAge.isSelected() && !adultAge.isSelected()) {
				ageLimit = -1;
			}
			int c = category.getSelectedIndex() + 1;
			
			if(gno == 0 && category.getSelectedItem().toString().isEmpty() && img.getIcon() == null) {
				getter.mg("빈칸이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(n.isEmpty() || p.isEmpty() || i.isEmpty()) {
				getter.mg("빈칸이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				int pointz = Integer.parseInt(p);
				if(pointz <= 0 || pointz > 500000) {
					throw new Exception();
				}
			} catch (Exception e2) {
				getter.mg("포인트 가격은 0보다 커야하고 50만 보다는 작아야합니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			List<Data> list = Connections.select("select g_name from gameinformation where g_no != ?", (gno == 0 ? -1 : gno));
			list.forEach(s -> {
				if(n.equals(s.get(0).toString())) {
					getter.mg("중복된 게임명이 있습니다.", JOptionPane.ERROR_MESSAGE);
					name.setText("");
					return;
				}
			});
			if(ageLimit == -1) {
				getter.mg("나이제한을 선택하지 않았습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String s = (gno == 0 ? "등록이" : "수정이") + "완료되었습니다.";
			getter.mg(s, JOptionPane.INFORMATION_MESSAGE);
			if(gno == 0) Connections.update("insert into gameinformation values(0, ?, ?, ?, ?, ?, ?);", n, p, LocalDate.now(), c, ageLimit, i);
			else Connections.update("update gameinformation set g_name = ?, g_price = ?, g_birth = ?, ca_no = ?, g_limit = ?, g_lebu = ? where g_no = ?", n, p, LocalDate.now(), c, ageLimit, i, gno);
			new AdminMain();
			dispose();
		});
		
		setFrame(gno == 0 ? "게임 추가" : "게임 수정", 650, 500);
	}
	
	private void setMainPanel() {
		JPanel p = new JPanel(new BorderLayout(10, 10));
		p.setBackground(Color.white);
		
		JPanel mainPanel = new JPanel(new GridLayout(4, 1, 3, 3));
		mainPanel.setBackground(Color.white);
		
		JPanel p1 = new JPanel(new BorderLayout());
		p1.setBackground(Color.white);
		p1.add(new JLabel("게임명 :") {{
			setPreferredSize(new Dimension(150, 30));
		}}, BorderLayout.WEST);
		p1.add(name);
		mainPanel.add(p1);
		
		JPanel p2 = new JPanel(new BorderLayout());
		p2.setBackground(Color.white);
		p2.add(new JLabel("포인트 :") {{
			setPreferredSize(new Dimension(150, 30));
		}}, BorderLayout.WEST);
		p2.add(point);
		mainPanel.add(p2);
		
		JPanel p3 = new JPanel(new BorderLayout());
		p3.setBackground(Color.white);
		p3.add(new JLabel("카테고리 :") {{
			setPreferredSize(new Dimension(150, 30));
		}}, BorderLayout.WEST);
		p3.add(category);
		mainPanel.add(p3);
		
		JPanel p4 = new JPanel(new BorderLayout());
		p4.setBackground(Color.white);
		p4.add(new JLabel("나이제한 :") {{
			setPreferredSize(new Dimension(150, 30));
		}}, BorderLayout.WEST);
		p4.add(new JPanel(new GridLayout(0, 2)) {{
			setBackground(Color.white);
			add(allAge);
			add(adultAge);
		}});
		mainPanel.add(p4);
		
		p.add(mainPanel);
		p.add(img, BorderLayout.WEST);
		borderPanel.add(p, BorderLayout.NORTH);
	}

	public static void main(String[] args) {
		new GameInsert_Change(0);
	}
}
