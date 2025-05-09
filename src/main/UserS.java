package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import utils.BaseFrame;
import utils.ButtonMake;
import utils.Query;
import utils.Row;
import java.util.List;
public class UserS extends BaseFrame{

	
	Font font = new Font("맑은 고딕", Font.BOLD, 12);
	JTextField tf = new JTextField() {{this.setPreferredSize(new Dimension(100, 20));}};
	ButtonMake show = new ButtonMake("조회", 60, 25, font);
	ButtonMake allShow = new ButtonMake("전체 보기", 90, 25, font);
	ButtonMake change = new ButtonMake("수정", 60, 25, font);
	ButtonMake delet = new ButtonMake("삭제", 60, 25, font);
	ButtonMake end = new ButtonMake("닫기", 60, 25, font);
	
	JPanel topP = new JPanel() {{this.add(new JLabel("성명"));
		this.add(tf); this.add(show); this.add(allShow); this.add(change);
		this.add(delet); this.add(end);}};
		
	JTable T;
	String[] colum;
	Object[] data;
	DefaultTableModel tmodel;
	List<Row> list;
	UserS(){
		setFrame("고객 조회", 600, 600, ()->{});
	}
	
	@Override
	public void design() {

		list = Query.select("SELECT * FROM company_선수번호.customer order by name asc");
		int columSize = list.get(0).size();
		int rowSize = list.size()-1;// 칼럼 이름 때문에 하나 빼야함
		colum = new String[columSize];
		for(int i  = 0; i < columSize; i++) {
			colum[i] = list.get(0).getString(i);
		}
		tmodel = new DefaultTableModel(colum,0) {
	        public boolean isCellEditable(int rowIndex, int mColIndex) {
	                return false;
	            }
        };
        data = new Object[columSize];
        for(int i = 0; i < rowSize; i++) {
	        for(int j = 0; j < columSize; j++) {
	        	data[j] = list.get(i+1).get(j);
	        }
	        tmodel.addRow(data);
        }
        
        T = new JTable(tmodel);
		add(new JScrollPane(T), BorderLayout.CENTER);
		add(topP, BorderLayout.NORTH);
	}

	@Override
	public void action() {
		show.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				list = Query.select("SELECT * FROM company_선수번호.customer where name = ?", tf.getText());
				int rowCount = tmodel.getRowCount();
				for(int i = 0; i < rowCount; i++) {tmodel.removeRow(0);}
				
				if(!list.isEmpty() && (tf.getText().toCharArray().length >= 2)) {
					
					for(int i = 0; i < list.get(0).size(); i++) {data[i] = list.get(1).get(i);}
					
					tmodel.addRow(data);
				}
				list = Query.select("SELECT * FROM company_선수번호.customer");
				if(!list.isEmpty() && (tf.getText().toCharArray().length) == 1) {
					for(int i = 1; i < list.size(); i++) {
						String str = list.get(i).getString(1);
						if(str.substring(0,1).equals(tf.getText())) {
							for(int j = 0; j < list.get(0).size(); j++) {
								data[j] = list.get(i).get(j);
							}
							tmodel.addRow(data);
						}
					}
				}
			}
		});
		allShow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				list = Query.select("SELECT * FROM company_선수번호.customer order by name asc");
				int rowCount = tmodel.getRowCount();
				for(int i = 0; i < rowCount; i++) {tmodel.removeRow(0);}
				
		        for(int i = 1; i < list.size(); i++) {
			        for(int j = 0; j < list.get(0).size(); j++) {
			        	data[j] = list.get(i).get(j);
			        }
			        tmodel.addRow(data);
		        }
			}
		});
		change.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = "" + T.getValueAt(T.getSelectedRow(), 1);
				new UserS_change(str);
			}
		});
		delet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(T.getSelectedRow()>-1) {
					String str = "" + T.getValueAt(T.getSelectedRow(), 1);
					int result = JOptionPane.showOptionDialog(getContentPane(), str+"님을 정말 삭제하시겠습니까?", "고객정보 삭제", 
							JOptionPane.YES_NO_OPTION , JOptionPane.QUESTION_MESSAGE, null, new String[]{"확인", "취소"}, null);
					if(result == JOptionPane.YES_OPTION) {
						tmodel.removeRow(T.getSelectedRow());
					}
				}
			}
		});
		end.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {dispose();}});
	}
}
