package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import utils.BaseFrame;
import utils.ButtonMake;
import utils.Query;
import utils.Row;

public class ContractM extends BaseFrame{

	JPanel topL = new JPanel(new GridLayout(4,2,-85,0)) {{
		this.setPreferredSize(new Dimension(200, 100));
	}};
	JPanel topR = new JPanel(new GridLayout(3,2,-85,0)) {{
		this.setPreferredSize(new Dimension(200, 100));
	}};
	JPanel top = new JPanel(new FlowLayout(0, 100, 15)) {{
		this.add(topL);this.add(topR);
	}};
	JPanel admin = new JPanel() {{this.setPreferredSize(new Dimension(700, 70));}};
	JPanel label = new JPanel();
	JPanel center = new JPanel(new BorderLayout()) {{this.add(admin, BorderLayout.NORTH);
		this.add(label, BorderLayout.CENTER);
		this.setBackground(Color.black);
	}};
	Container f = getContentPane();
	JComboBox<String> name;JComboBox<String> cName;JComboBox<String> adminName;
	JTextField code = new JTextField();JTextField day = new JTextField();
	JTextField phone = new JTextField();JTextField price = new JTextField();
	JTextField monthPrice = new JTextField();
	JButton 가입 = new JButton("가입");JButton 삭제 = new JButton("삭제");
	JButton fileSave = new JButton("텍스트 파일로 저장하기");
	Font myFont = new Font("맑은 고딕", Font.BOLD, 13);
	
	DefaultTableModel tmodel;
	JTable table;
	ContractM() {
		setFrame("보험 계약", 700, 700, ()->{new Admin();});
	}
	@Override
	public void design() {
		List<Row> list = Query.select("select customer.name,customer.birth ,customer.tel , contract.* "
				+ "from customer inner join contract "
				+ "on customer.code = contract.customerCode "
				+ "group by name order by name asc;");
		List<Row> list2 = Query.select("SELECT contractName FROM contract group by contractName order by contractName asc");
		List<Row> list3 = Query.select("SELECT name FROM company_선수번호.admin");
		code.setText(list.get(1).getString(3));day.setText(list.get(1).getString(1));phone.setText(list.get(1).getString(2));
		String[] nameData = new String[list.size()-1];
		String[] cNameData = new String[list2.size()-1];
		String[] aNameData = new String[list3.size()-1];
		for(int i = 1; i < list.size(); i++) {
			nameData[i-1] = list.get(i).getString(0);
		}
		for(int i = 1; i < list2.size(); i++) {
			cNameData[i-1] = list2.get(i).getString(0);
		}
		for(int i = 1; i < list3.size(); i++) {
			aNameData[i-1] = list3.get(i).getString(0);
		}
		name = new JComboBox<String>(nameData);
		cName = new JComboBox<String>(cNameData);
		adminName = new JComboBox<String>(aNameData);
		
		topL.add(new JLabel("고객코드:") {{this.setFont(myFont);}});topL.add(code);
		topL.add(new JLabel("고 객 명:") {{this.setFont(myFont);}});topL.add(name);
		topL.add(new JLabel("생년월일:") {{this.setFont(myFont);}});topL.add(day);
		topL.add(new JLabel("연 락 처:") {{this.setFont(myFont);}});topL.add(phone);
		
		topR.add(new JLabel("보험상품:") {{this.setFont(myFont);}});topR.add(cName);
		topR.add(new JLabel("가입금액:") {{this.setFont(myFont);}});topR.add(price);
		topR.add(new JLabel("월보험로:") {{this.setFont(myFont);}});topR.add(monthPrice);
		
		admin.add(new JLabel("담당자:") {{this.setFont(myFont);}});
		admin.add(adminName);admin.add(가입);
		admin.add(삭제);admin.add(fileSave);
		admin.add(new JButton("닫기") {{this.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {new Admin(); dispose();}
			});
		}});
		label.add(new JLabel("< 고객 보험 계약현황 >"));
		
		
		List<Row> list4 = Query.select("SELECT * FROM company_선수번호.contract where customerCode = ? order by regDate desc", code.getText());
		String[] Ttitle = new String[list4.get(0).size()];
		for(int i = 0; i < list4.get(0).size(); i++) {
			Ttitle[i] = list4.get(0).getString(i);
		}
		tmodel = new DefaultTableModel(Ttitle, 0) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {return false;}
		};
		String[] Tdata = new String[list4.get(0).size()];
		for(int i = 1; i < list4.size(); i++){
			for(int j = 0; j < list4.get(0).size(); j++) {
				Tdata[j] = list4.get(i).getString(j);
			}
			tmodel.addRow(Tdata);
		}
		table = new JTable(tmodel);
		//테이블 가운데 정렬
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel tcm = table.getColumnModel() ;     
        for(int i = 0 ; i < tcm.getColumnCount() ; i++){
        	tcm.getColumn(i).setCellRenderer(dtcr);  
        }
        //테이블 가운데 정렬
		add(new JPanel() {{this.setPreferredSize(new Dimension(0,400));
		this.add(new JScrollPane(table) {{this.setPreferredSize(new Dimension(685,500));}});
		}}, BorderLayout.SOUTH);
		add(center, BorderLayout.CENTER);
		add(top, BorderLayout.NORTH);
		getContentPane().setBackground(Color.yellow);
		
	}

	@Override
	public void action() {
		name.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Row> list = Query.select("select customer.name,customer.birth ,customer.tel , contract.* "
						+ "from customer inner join contract "
						+ "on customer.code = contract.customerCode "
						+ "where name = ? group by name order by name asc;", ""+name.getSelectedItem());
				code.setText(list.get(1).getString(3));day.setText(list.get(1).getString(1));phone.setText(list.get(1).getString(2));
				int Rows = tmodel.getRowCount();
				for(int i = 0; i < Rows; i++) {tmodel.removeRow(0);}
				List<Row> list1 = Query.select("SELECT * FROM company_선수번호.contract where customerCode = ? order by regDate desc", code.getText());
				String[] Tdata = new String[list1.get(0).size()];
				for(int i = 1; i < list1.size(); i++){
					for(int j = 0; j < list1.get(0).size(); j++) {
						Tdata[j] = list1.get(i).getString(j);
					}
					tmodel.addRow(Tdata);
				}
			}
		});
		가입.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!price.getText().equals("") && !monthPrice.getText().equals("")) {
					Query.upDate("insert into contract values(?, ?, ?, ?, ?,?)", code.getText(), cName.getSelectedItem().toString(),
							price.getText(), ""+LocalDate.now(),monthPrice.getText(),adminName.getSelectedItem().toString());
					int Rows = tmodel.getRowCount();
					for(int i = 0; i < Rows; i++) {tmodel.removeRow(0);}
					List<Row> list = Query.select("SELECT * FROM company_선수번호.contract where customerCode = ? order by regDate desc", code.getText());
					String[] Tdata = new String[list.get(0).size()];
					for(int i = 1; i < list.size(); i++){
						for(int j = 0; j < list.get(0).size(); j++) {
							Tdata[j] = list.get(i).getString(j);
						}
						tmodel.addRow(Tdata);
					}
				}
			}
		});
		삭제.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow()>-1) {
					String str = "" + table.getValueAt(table.getSelectedRow(), 0) + "("+ table.getValueAt(table.getSelectedRow(), 1) +")";
					int result = JOptionPane.showOptionDialog(getContentPane(), str+"을 삭제하시겠습니까?", "고객정보 삭제", 
							JOptionPane.YES_NO_OPTION , JOptionPane.QUESTION_MESSAGE, null, new String[]{"확인", "취소"}, null);
					if(result == JOptionPane.YES_OPTION) {
						Query.upDate("delete from contract where customerCode = ? and contractName = ? "
								+"and regPrice = ? and monthPrice = ? and "
								+"adminName = ?",""+table.getValueAt(table.getSelectedRow(), 0),""+table.getValueAt(table.getSelectedRow(), 1),
								""+table.getValueAt(table.getSelectedRow(), 2),""+table.getValueAt(table.getSelectedRow(), 4),
								""+table.getValueAt(table.getSelectedRow(), 5));
						tmodel.removeRow(table.getSelectedRow());
					}
				}
			}
		});
		fileSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fileLS();
			}
		});
	}
	private void fileLS() {
		FileDialog fdOpen = new FileDialog(this, "파일열기",FileDialog.SAVE);
		fdOpen.setVisible(true);
		String name = fdOpen.getFile();
		String path = fdOpen.getDirectory();
		if(path == null) {
			return;
		}
		File file = new File(path);
		
		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new FileWriter(file + "/" + name)); // 스트림연결
			String str = tmodel.getValueAt(0, 0)+"";
			br.write(new String("고객명 : " + this.name.getSelectedItem().toString() + "("+ str +")\n\n"));
			br.write("담당자명 : " + adminName.getSelectedItem().toString() + "\n\n");
			br.write("보험상품\t\t가입금액\t\t가입일\t월보험료\n");
			int rows = table.getRowCount();
			System.out.println(rows);
			for(int i = 0; i < table.getRowCount(); i++) {
				br.write(table.getValueAt(i,1) +"\t"+ table.getValueAt(i,2) +"\t" +table.getValueAt(i,3) +"\t"+ table.getValueAt(i,4) +"\n");
			}
			br.flush(); // 목적지로 분출
			br.close(); // 다 쓴 스트림 끊기
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new ContractM();
	}
}
