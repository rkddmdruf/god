package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import utils.Query;
import utils.Row;
import utils.sp;

public class A_Admin_deliveryM extends JPanel{

	int[] payAB = {0,0,0,0};
	String[] labels = "결제 전 : ,결제 완료 : ,배송준비 : ,배송중 : ".split(",");
	JLabel[] label = {new JLabel(), new JLabel(), new JLabel(), new JLabel()};
	int global = 0;
	List<Row> orderList = Query.getOrder.select();
	List<JCheckBox> CheckList = new ArrayList<JCheckBox>();
	JComboBox<String> cb = new JComboBox<String>("전체,결제전,배송준비,배송중".split(","));
	JButton 일괄처리 = new JButton("일괄처리");
	DefaultTableModel T = new DefaultTableModel(" ,번호,상품명,회원명,수량,가격,총금액,배송현황".split(","), 0) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return column == 0;
		}
	};
	JTable t = new JTable(T);
	DefaultTableCellRenderer render = new DefaultTableCellRenderer() {
		@Override
		public JComponent getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
			CheckList.get(row).setBackground(Color.white);CheckList.get(row).setHorizontalAlignment(JCheckBox.CENTER);
			return CheckList.get(row);
		}
	};
	
	public A_Admin_deliveryM(JPanel p) {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
		t.getColumn(" ").setCellRenderer(render);
		
		setLayout(new BorderLayout());
		add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{add(cb);add(일괄처리);}},BorderLayout.NORTH);
		add(new JPanel(new BorderLayout()) {{
			add(new JScrollPane(t));
			resetTable(11,19);
		}});
		add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			for(int i = 0; i < 4; i++) {
				label[i].setText(labels[i] + payAB[i]);
				label[i].setFont(sp.fontM(1, 16));
				add(label[i]);
			}
		}}, BorderLayout.SOUTH);
		p.add(this, "P4");
		action();
	}
	
	private void showPopupMenu(int x, int y) {
	    JPopupMenu popupMenu = new JPopupMenu();
	    
	    JButton button1 = new JButton(t.getValueAt(t.getSelectedRow(), 7).toString());
	    button1.setBackground(Color.white);
	    button1.addActionListener(e -> {
	    	if(orderList.get(t.getSelectedRow()).getInt(6) == 0) {
				Query.payUpdate.update(1, orderList.get(t.getSelectedRow()).get(0));
			}else if(orderList.get(t.getSelectedRow()).getInt(6) == 1) {
				Query.deliveryUpdate.update(orderList.get(t.getSelectedRow()).getInt(7)+1, orderList.get(t.getSelectedRow()).get(0));
			}
	        popupMenu.setVisible(false);
	        if(cb.getSelectedIndex() == 0) {resetTable(11,19);}if(cb.getSelectedIndex() == 1) {resetTable(11,0);}
			if(cb.getSelectedIndex() == 2) {resetTable(1,19);}if(cb.getSelectedIndex() == 3) {resetTable(2,19);}
	    });
	    popupMenu.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
	    popupMenu.add(button1);
	    popupMenu.show(t, x, y);
	}
	
	private void action() {
		
		t.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					t.setRowSelectionInterval(t.rowAtPoint(e.getPoint()), t.rowAtPoint(e.getPoint()));
	            	showPopupMenu(e.getX(), e.getY()); 
	            	
				}
				if(t.getSelectedColumn() == 0) {
					CheckList.get(t.getSelectedRow()).setSelected(!CheckList.get(t.getSelectedRow()).isSelected());
					repaint();revalidate();
				}
			}
		});
		일괄처리.addActionListener(e->{
			int check = 0;
			for(int i = 0; i < CheckList.size(); i++) {
				if(!CheckList.get(i).isSelected()) {check++;}
				else if(CheckList.get(i).isSelected()) {
					if(orderList.get(i).getInt(6) == 0) {
						Query.payUpdate.update(1, orderList.get(i).get(0));
					}else if(orderList.get(i).getInt(6) == 1) {
						Query.deliveryUpdate.update(orderList.get(i).getInt(7)+1, orderList.get(i).get(0));
					}
				}
			}
			if(check == CheckList.size()) {sp.ErrMes("배송처리할 리스트를 선택하세요");}
			if(cb.getSelectedIndex() == 0) {resetTable(11,19);}if(cb.getSelectedIndex() == 1) {resetTable(11,0);}
			if(cb.getSelectedIndex() == 2) {resetTable(1,19);}if(cb.getSelectedIndex() == 3) {resetTable(2,19);}
		});
		cb.addActionListener(e->{
			if(cb.getSelectedIndex() == 0) {resetTable(11,19);}if(cb.getSelectedIndex() == 1) {resetTable(11,0);}
			if(cb.getSelectedIndex() == 2) {resetTable(1,19);}if(cb.getSelectedIndex() == 3) {resetTable(2,19);}
		});
	}

	void resetTable(int a, int b) {
		CheckList.clear();for(int i = 0 ; i < 4; i++) {payAB[i] = 0;}
		// 변수 초기화?
		if(a == 11 && b == 19) {
			orderList = Query.getOrder.select();
		}else if(b == 19){
			orderList = Query.getOrderDW.select(a);
		}else if(a == 11){
			orderList = Query.getOrderPW.select(b);
		}
		List<Row> list = new ArrayList<Row>();
		for(int i = 0; i < orderList.size();i++) {
			Row row = new Row();
			CheckList.add(new JCheckBox());
			row.add(CheckList.get(i));row.add(i+1);row.add(orderList.get(i).get(1));row.add(orderList.get(i).get(2));
			row.add(orderList.get(i).get(3));row.add(new DecimalFormat("###,###").format(orderList.get(i).get(4)));
			row.add(new DecimalFormat("###,###").format(orderList.get(i).get(5)));
			if(orderList.get(i).getInt(6) == 0) {
				row.add("결제전");
				payAB[0] += 1;
			}else if(orderList.get(i).getInt(6) == 1) {
				if(orderList.get(i).getInt(7)==0) {row.add("결제완료");payAB[1] += 1;}
				if(orderList.get(i).getInt(7)==1) {row.add("배송준비");payAB[2] += 1;}
				if(orderList.get(i).getInt(7)==2) {row.add("배송중");payAB[3] += 1;}
			}
			list.add(row);
		}
		Query.setTable(T, list);
		for(int i = 0; i < 4; i++) {
			label[i].setText(labels[i] + payAB[i]);
			label[i].setFont(sp.fontM(1, 16));
		}
		repaint();revalidate();
	}
}

