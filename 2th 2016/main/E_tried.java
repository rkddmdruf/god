package main;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import utils.BaseFrame;
import utils.Query;
import utils.Row;

public class E_tried extends BaseFrame{
	Font font = setBoldFont(12);
	int tfnumber = 0;
	
	List<Row> list = Query.UserName.select();
	List<Row> Namelist = Query.UserContract.select();
	List<Row> adminList = Query.adminName.select();
	String[] tName = Query.contractColumn.dbname();
	
	String[] str = "고객코드,고 객 명,생년월일,연 락 처,보험상품,가입금액,월보험료".split(",");
	JTextField[] tf = new JTextField[5];
	
	String[] butName = "가입 삭제 파일로저장 닫기".split(" ");
	JButton[] but = new JButton[4];
	
	JPanel topL = new JPanel(new GridLayout(4,2,-85,0)) {{setPreferredSize(new Dimension(200,100));}};
	JPanel topR = new JPanel(new GridLayout(3,2,-85,0)) {{setPreferredSize(new Dimension(200,100));}};
		
	JComboBox<String> cbName = new JComboBox<String>() {{for(int i = 0; i < list.size(); i++) {this.addItem(list.get(i).getString(0));setFont(font);}}};
	JComboBox<String> cbcName = new JComboBox<String>() {{for(int i = 0; i < Namelist.size(); i++) {this.addItem(Namelist.get(i).getString(0));setFont(font);}}};
	JComboBox<String> cbAdminName = new JComboBox<String>() {{for(int i = 0; i < adminList.size(); i++) {this.addItem(adminList.get(i).getString(0));setFont(font);}}};
	
	DefaultTableModel tmodel = new DefaultTableModel(tName, 0);
	JTable table = new JTable(tmodel);
	DefaultTableCellRenderer dtc = new DefaultTableCellRenderer();
	
	public E_tried() {
		setFrame("보험계약", 700, 700, ()->{new B_Admin();});
	}
	@Override
	public void desgin() {

		for(int i = 0; i < 4; i++) {
			topL.add(new JLabel(str[i]+":") {{this.setFont(font);}});
			if(i != 1) {topL.add(tf[tfnumber] = new JTextField());tfnumber++;}
			else {topL.add(cbName);}
		}
		for(int i = 4; i < 7; i++) {
			topR.add(new JLabel(str[i]+":") {{this.setFont(font);}});
			if(i != 4) {topR.add(tf[tfnumber] = new JTextField());tfnumber++;}
			else {topR.add(cbcName);}
		}
		add(new JPanel(new FlowLayout(0,50,15)) {{
			add(topL);add(topR);setBorder(BorderFactory.createEmptyBorder(0,100,20,0));
		}}, BorderLayout.NORTH);
		//상단/////////////////////////////////
		add(new JPanel(new FlowLayout()) {{
			add(new JLabel("담당자 : ") {{setFont(font);}});
			add(cbAdminName);
			for(int i = 0; i < 4; i ++) {
				but[i] = new JButton(butName[i]);
				add(but[i]);
			}
		}}, BorderLayout.CENTER);
		add(new JScrollPane(table), BorderLayout.SOUTH);
		setTF();setTable();
	}
	
	@Override
	public void action() {
		cbName.addActionListener(e->{
			setTF();setTable();
		});
		but[0].addActionListener(e->{
			if(!tf[3].getText().equals("") && !tf[4].getText().equals("")) {
				Query.tableUpdate.updata(tf[0].getText(), cbcName.getSelectedItem().toString(), tf[3].getText(), 
						LocalDate.now().toString(), tf[4].getText(), cbAdminName.getSelectedItem().toString());
				setTF();setTable();}});
		but[1].addActionListener(e->{
			int result = JOptionPane.showConfirmDialog(getContentPane(), tf[0].getText()+"("+table.getValueAt(table.getSelectedRow(), 1)+")을 삭제하시겠습니까?",
					"계약정보 삭제", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				String[] str = new String[6];
				for(int i = 0; i < 6; i++) {
					str[i] = tmodel.getValueAt(table.getSelectedRow(), i).toString();
				}
				Query.tabledelete.updata(str);
				setTF();setTable();
			}
		});
		but[2].addActionListener(e->{
			FileDialog fdOpen = new FileDialog(this, "텍스트 파일로 저장하기",FileDialog.SAVE);
			fdOpen.setVisible(true);
			String name = fdOpen.getFile();
			String path = fdOpen.getDirectory();
			if(path == null) {
				return;
			}
			File file = new File(path);
			BufferedWriter br = null;
			try {
				br = new BufferedWriter(new FileWriter(file + "\\" + name)); // 스트림연결
				String str = tmodel.getValueAt(0, 0)+"";
				br.write(new String("고객명 : " + this.cbName.getSelectedItem().toString() + "("+ str +")\n\n"));
				br.write("담당자명 : " + cbAdminName.getSelectedItem().toString() + "\n\n");
				br.write("보험상품\t\t가입금액\t\t가입일\t월보험료\n");
				int rows = table.getRowCount();
				for(int i = 0; i < table.getRowCount(); i++) {
					br.write(table.getValueAt(i,1) +"\s\s\t"+ table.getValueAt(i,2) +"\s\s\t" +table.getValueAt(i,3) +"\s\s\t"+ table.getValueAt(i,4) +"\n");
				}
				br.flush(); // 목적지로 분출
				br.close(); // 다 쓴 스트림 끊기
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		but[3].addActionListener(e->{
			new B_Admin();
			dispose();
		});
	}
	public void setTF() {
		List<Row> list = Query.UserCBT.select(cbName.getSelectedItem().toString());
		for(int i = 0; i < 3; i++) {
			tf[i].setText(list.get(0).getString(i));
		}
	}
	public void setTable() {
		List<Row> tablelist = Query.tableQuery.select(tf[0].getText());
		Query.setTable(tmodel, tablelist);
		dtc.setHorizontalAlignment(JLabel.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		for(int i = 0; i < table.getColumnCount(); i++) {
			tcm.getColumn(i).setCellRenderer(dtc);
		}
	}
	public static void main(String[] args) {
		new E_tried();
	}
}
