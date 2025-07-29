package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import utils.BaseFrame;
import utils.Query;
import utils.Row;

public class D_ShowUser extends BaseFrame{
	List<Row> list = Query.ShowUser.select();
	JTextField tf = new JTextField() {{setPreferredSize(new Dimension(100,20));}};
	String[] str = "조회,전체보기,수정,삭제,닫기".split(",");
	JButton[] but = new JButton[5];
	String[] name = "code,name,birth,tel,address,company".split(",");
	DefaultTableModel tmodel = new DefaultTableModel(name,0) {
		public boolean isCellEditable(int rowIndex, int mColIndex) {return false;}
	};
	JTable table = new JTable(tmodel);
	public D_ShowUser() {
		setFrame("고객 조회", 700, 750, ()->{});
	}
	@Override
	public void desgin() {
		add(new JPanel() {{
			add(new JLabel("성명") {{setFont(setBoldFont(12));}});add(tf);
			for(int i = 0; i < 5; i ++) {
				add(but[i] = new JButton(str[i]) {{setPreferredSize(new Dimension(60,25));}});
			}but[1].setPreferredSize(new Dimension(90,25));
		}}, BorderLayout.NORTH);
		
		Query.setTable(tmodel, list);
		add(new JScrollPane(table));
	}

	@Override
	public void action() {
		but[0].addActionListener(e->{
			if(tf.getText().toCharArray().length != 1) {
				List<Row> list = Query.ShowUserWhereName.select(tf.getText());
				int coun = table.getRowCount();
				for(int i = 0; i < coun; i++) {tmodel.removeRow(0);}
				Query.setTable(tmodel, list);
			}else {
				List<Row> oneName = new ArrayList<>();
				for(int i = 0; i < list.size(); i++) {
					if(list.get(i).get(1).toString().contains(tf.getText())) {
						Row row = new Row();
						for(int j = 0; j < list.get(i).size(); j++) {row.add(list.get(i).get(j));}
						oneName.add(row);
					}
				}
				Query.setTable(tmodel, oneName);
			}
		});
		but[1].addActionListener(e->{
			tf.setText("");
			List<Row> list = Query.ShowUser.select();
			Query.setTable(tmodel, list);
		});
		but[2].addActionListener(e->{
			new CB_UserUPDATE(table.getValueAt(table.getSelectedRow(), 0).toString(), table.getValueAt(table.getSelectedRow(), 1).toString());
		});
		but[3].addActionListener(e->{
			int result = JOptionPane.showConfirmDialog(getContentPane(), table.getValueAt(table.getSelectedRow(), 1).toString()+"님을 정말 삭제하시겠습니까?","고객정보 삭제", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				Query.UserDelete.updata(table.getValueAt(table.getSelectedRow(), 0).toString(), table.getValueAt(table.getSelectedRow(), 1).toString());
				tmodel.removeRow(table.getSelectedRow());
			}
		});
		but[4].addActionListener(e->{
			dispose();
			new B_Admin();
		});
	}

	public static void main(String[] args) {
		new D_ShowUser();
	}
}
