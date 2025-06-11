package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public enum Query {
	Login("SELECT name, passwd FROM company_선수번호.admin"),
	UserUP("insert into customer values(?, ?, ?, ?, ?, ?)"),
	ShowUser("SELECT * FROM company_선수번호.customer"),
	ShowUserWhereName("SELECT * FROM company_선수번호.customer where name = ?"),
	UserUpdateSelect("SELECT * FROM company_선수번호.customer where code = ? and name = ?"),
	UserDelete("delete from customer where code = ? and name = ?"),
	UserUpdate("update customer set birth = ?, tel = ?, address = ?, company = ? where code = ? and name = ?"),
	UserName("select name from customer order by name asc"),
	UserContract("SELECT contractName FROM company_선수번호.contract group by contractName order by contractName asc"),
	UserCBT("SELECT code, birth, tel FROM company_선수번호.customer where name = ?"),
	adminName("SELECT name FROM company_선수번호.admin order by name asc"),
	adminColumn("SELECT * FROM company_선수번호.admin"),
	contractColumn("SELECT * FROM company_선수번호.contract"),
	customerColumn("SELECT * FROM company_선수번호.customer"),
	tableQuery("SELECT * FROM company_선수번호.contract where customerCode = ? order by regDate desc"),
	tableUpdate("insert into contract values(?,?,?,?,?,?)"),
	tabledelete("delete from contract where customerCode = ? and contractName = ? and regPrice = ? and regDate = ? and monthPrice = ? and adminName = ?");
	final String string;
	
	Query(String string){
		this.string = string;
	}
	public String label() {
		return string;
	}
	
	
	public String[] dbname(){
		String[] str = null;
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/company_선수번호?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
			ResultSetMetaData rsm = c.createStatement().executeQuery(string).getMetaData();
			str = new String[rsm.getColumnCount()];
			for(int i = 0; i < rsm.getColumnCount(); i++) {str[i] = rsm.getColumnName(i+1).toString();}
			c.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return str;
		
	}
	public List<Row> select(String...val){
		List<Row> list = new ArrayList<>();
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/company_선수번호?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
			PreparedStatement ps = c.prepareStatement(string);
			for(int i = 0; i < val.length;i++) {
				ps.setObject(i+1, val[i]);
			}
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();
			while(rs.next()) {
				Row row = new Row();
				for(int i = 0; i < rsm.getColumnCount(); i++) {
					row.add(rs.getObject(i+1));
				}
				list.add(row);
			}
			rs.close();
			ps.close();
			c.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return list;	
	}
	
	public void updata(String...val) {
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/company_선수번호?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
			PreparedStatement ps = c.prepareStatement(string);
			for(int i = 0; i < val.length;i++) {
				ps.setObject(i+1, val[i]);
			}
			ps.executeUpdate();
			ps.close();
			c.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	public static void setTable(DefaultTableModel model, List<Row> list) {
		int count = model.getRowCount();
		for(int i = 0; i < count; i++) {
			model.removeRow(0);
		}
		for(int i = 0; i < list.size(); i++) {
			Vector<Object> row = new Vector<Object>();
			for(int j = 0; j < list.get(i).size(); j++) {
				row.add(list.get(i).get(j));
			}
			model.addRow(row);
		}
	}
}
