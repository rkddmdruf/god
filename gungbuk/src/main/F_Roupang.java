package main;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import utils.Query;
import utils.Row;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
public class F_Roupang extends JPanel{
	Timer timer = new Timer(100000, new ActionListener() {@Override public void actionPerformed(ActionEvent e) {}});
	String[] str = "전체,결제 완료,배송준비,배송중,배송 완료".split(",");
	String[] name = "상품 수량 단가 합계 주문일".split(" ");
	JLabel[] label = new JLabel[4];
	JLabel[] delivery = new JLabel[4];
	JLabel[] deliveryimg = new JLabel[4];
	ImageIcon[] deliveryimgB = new ImageIcon[4];
	ImageIcon[] deliveryimgG = new ImageIcon[4];
	JPanel[] PP = new JPanel[4];
	JPanel PPS = new JPanel(new BorderLayout());
	List<Row> list = new ArrayList<Row>();
	List<Row> listTable = new ArrayList<>();
	JComboBox<String> cb = new JComboBox<>(str) {{setPreferredSize(new Dimension(100, 30));}};
	DefaultTableModel T = new DefaultTableModel(name, 1) {
		public boolean isCellEditable(int row, int column) {
			for(int i = 0; i < name.length; i++) {
				if (column == i) {return false;}
			} return true;
		};
	};
	DecimalFormat DF = new DecimalFormat("###,###");
	DefaultTableCellRenderer cell = new DefaultTableCellRenderer() {{setHorizontalAlignment(JLabel.CENTER);}};
	JTable t = new JTable(T) {{for(String s : name) {this.getColumn(s).setCellRenderer(cell);}}};
	int OZ = 1;int sele = 0;
	public F_Roupang() {}
	F_Roupang(JPanel p, int user){
		Painted[] p1 = new Painted[4];
		for(int i = 0 ; i < 4; i++) {
			p1[i] = new Painted(i);
			PP[i] = new JPanel(new BorderLayout());
			PP[i].add(p1[i]);
		}
		list = Query.Roupanglsit.select(user+"");
		if(list.isEmpty()) {
			setLayout(new BorderLayout());
			setBackground(Color.white);
			add(new JPanel(new BorderLayout()) {{
				add(new JLabel("배송정보") {{setFont(setBoldFont(24));}}, BorderLayout.WEST);
			}},BorderLayout.NORTH);
			add(new JPanel(new BorderLayout()) {{
				setBorder(BorderFactory.createLineBorder(Color.black));
				add(new JLabel("배송정보가 없습니다.") {{setFont(setBoldFont(24));setHorizontalAlignment(JLabel.CENTER);}});
			}});
		}else {
			System.out.println(list);
			deliveryimgG[0] =new ImageIcon(new ImageIcon(getClass().getResource("/delivery/02.jpg"))
					.getImage().getScaledInstance(60, 70, Image.SCALE_SMOOTH));
			deliveryimgB[0] = new ImageIcon(new ImageIcon(getClass().getResource("/delivery/02.jpg"))
					.getImage().getScaledInstance(60, 70, Image.SCALE_SMOOTH));
			deliveryimg[0] = new JLabel(deliveryimgG[0]);
			for(int i = 1 ; i < 4; i++) {
				
				deliveryimgG[i] = new ImageIcon(new ImageIcon(getClass().getResource("/delivery/" + ((i*10) +1) + ".jpg"))
						.getImage().getScaledInstance(60, 70, Image.SCALE_SMOOTH));
				deliveryimgB[i] = new ImageIcon(new ImageIcon(getClass().getResource("/delivery/" + ((i*10) +2) + ".jpg"))
						.getImage().getScaledInstance(60, 70, Image.SCALE_SMOOTH));
				deliveryimg[i] = new JLabel(deliveryimgG[i]);
			}
			
			setLayout(new BorderLayout());
			setBackground(Color.white);
			add(new JPanel(new BorderLayout()) {{
				add(new JLabel("배송정보") {{setFont(setBoldFont(24));}}, BorderLayout.WEST);
				add(cb, BorderLayout.EAST);
			}},BorderLayout.NORTH);
			add(new JPanel(new BorderLayout()) {{
				setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
				
				for(int i = 0; i < list.size(); i++) {
					Row row = new Row();
					row.add(list.get(i).get(8)); row.add(list.get(i).get(3));
					row.add(DF.format(list.get(i).get(7))); row.add(DF.format(list.get(i).getInt(7) * list.get(i).getInt(3)));
					row.add(list.get(i).get(4)); listTable.add(row);
				}
				Query.setTable(T, listTable);
				JScrollPane sc = new JScrollPane(t);
				add(sc);
			}}, BorderLayout.CENTER);
			t.setRowSelectionInterval(0, 0);
			
			
			add(new JPanel(new BorderLayout()) {{
				for(int i = 0; i < 4;i++) {label[i] = new JLabel();}
				setBorder(BorderFactory.createEmptyBorder(0,0,180,0));
				
				add(new JPanel(new BorderLayout()) {{
					add(label[0] = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/img/"+ list.get(t.getSelectedRow()).getInt(2)+".jpg"))
							.getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH)))
					{{setBorder(BorderFactory.createLineBorder(Color.black));}},BorderLayout.CENTER);
					
					add(new JPanel(new GridLayout(3, 1)) {{
						setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
						add(label[1] = new JLabel(listTable.get(t.getSelectedRow()).getString(0)));
						add(label[2] = new JLabel(listTable.get(t.getSelectedRow()).getString(2)+"원 " + listTable.get(t.getSelectedRow()).getString(1)+"개")
						{{setPreferredSize(new Dimension(200, 10));}});
						add(label[3] = new JLabel(listTable.get(t.getSelectedRow()).getString(3)+"원"));
					}}, BorderLayout.EAST);
				}}, BorderLayout.WEST);
				add(new JPanel(new BorderLayout()) {{
					sele = list.get(t.getSelectedRow()).getInt(6);
					
					add(new JPanel(new GridLayout(0, 4)) {{
						for(int i = 0; i < 4; i++) {
							add(deliveryimg[i]);
						}
						for(int i = 1; i <= sele; i++) {
							deliveryimg[i].setIcon(deliveryimgB[i]);
						}
						
						timer = new Timer(1000, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if(OZ == 0) {deliveryimg[sele].setIcon(deliveryimgB[sele]); OZ++;
								}else if(OZ == 1) {deliveryimg[sele].setIcon(deliveryimgG[sele]); OZ--;}
							}
						});
						timer.start();
					}}, BorderLayout.NORTH);
					PPS.add(PP[sele]);
					add(PPS, BorderLayout.CENTER);////////////////////////////
					add(new JPanel(new GridLayout(0,4)) {{
						setBorder(BorderFactory.createEmptyBorder(5,50, 0, 5));
						for(int i = 0; i < 4; i++) {
							String[] deliveryS = "결제완료,     배송준비,           배송중,          배송완료".split(",");
							add(delivery[i] = new JLabel(deliveryS[i]));
						}
					}}, BorderLayout.SOUTH);
				}}, BorderLayout.CENTER);
			}}, BorderLayout.SOUTH);
			
			//Query.GumeList.select(user+"");
			action();
		}
		p.add(this, "P5");
	}
	
	private void action() {
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for(int i = 0 ; i < list.size(); i++) {
					if(list.get(i).getString(8).equals(t.getValueAt(t.getSelectedRow(), 0).toString())) {
						label[0].setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/img/"+ list.get(i).getInt(2)+".jpg"))
						.getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH)));
					}
				}
				label[1].setText(listTable.get(t.getSelectedRow()).getString(0));
				label[2].setText(listTable.get(t.getSelectedRow()).getString(2)+"원 " + listTable.get(t.getSelectedRow()).getString(1)+"개");
				label[3].setText(listTable.get(t.getSelectedRow()).getString(3));
				deliveryimg[sele].setIcon(deliveryimgG[sele]);
				for(int i = 0; i < 4; i++) {
					deliveryimg[i].setIcon(deliveryimgG[i]);
				}
				sele = list.get(t.getSelectedRow()).getInt(6);
				for(int i = 0 ; i < list.size(); i++) {
					if(T.getValueAt(t.getSelectedRow(), 0).equals(list.get(i).get(8))) {
						sele = list.get(i).getInt(6);
					}
				}
				for(int i = 1; i <= sele; i++) {
					deliveryimg[i].setIcon(deliveryimgB[i]);
				}
				timer.stop();
				timer = new Timer(1000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(OZ == 0) {deliveryimg[sele].setIcon(deliveryimgB[sele]); OZ++;
						}else if(OZ == 1) {deliveryimg[sele].setIcon(deliveryimgG[sele]); OZ--;}
					}
				});
				timer.start();
				PPS.removeAll();
				PPS.repaint();
				PPS.add(PP[sele]);
				
			}
		});
		cb.addActionListener(e->{
			listTable = new ArrayList<>();
			if(cb.getSelectedIndex() == 0) {
				for(int i = 0; i < list.size(); i++) {
					Row row = new Row();
					row.add(list.get(i).get(8)); row.add(list.get(i).get(3));
					row.add(DF.format(list.get(i).get(7))); row.add(DF.format(list.get(i).getInt(7) * list.get(i).getInt(3)));
					row.add(list.get(i).get(4)); listTable.add(row);
				}
			}else {
				for(int i = 0; i < list.size(); i++) {
					if(list.get(i).getInt(6) == cb.getSelectedIndex()-1) {
						Row row = new Row();
						row.add(list.get(i).get(8));row.add(list.get(i).get(3));
						row.add(DF.format(list.get(i).get(7)));row.add(DF.format((list.get(i).getInt(7) * list.get(i).getInt(3))));
						row.add(list.get(i).getString(4));
						listTable.add(row);
					}
				}
			}Query.setTable(T, listTable);
		});
	}
	private Font setBoldFont(int i) {
		return new Font("맑은 고딕", Font.BOLD, i);
	}
}

class Painted extends JPanel{ 
	int number = 0;
	Painted(int number){
		this.number = number;
	}
  
	@Override 
	public void paint(Graphics g) {
		int fo = getWidth()/4;
		int ait = getWidth()/8;
		int[] ints = {fo-ait, fo+fo-ait, fo+fo+fo-ait, fo+fo+fo+fo-ait};
		
		for(int i = 0; i < 3; i++) {
			if(i >= number) {g.setColor(Color.LIGHT_GRAY);
			}else {g.setColor(new Color(100,150,250));}
			
			g.fillRect(ints[i]+8, getHeight()/2 + 5, fo, 6);
		}
		for(int i = 0 ; i < 4; i++) {
			if(i > number) {g.setColor(Color.LIGHT_GRAY);
			}else {g.setColor(new Color(100,150,250));}
			
			g.fillOval(ints[i], getHeight()/2, 16, 16);
		}
		
	} 
}
 
