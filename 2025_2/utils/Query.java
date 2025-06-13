package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public enum Query {
	MainPomTree("SELECT c_name FROM clothingstore.category"),
	MainPomTree2("SELECT sb_name, c_no FROM clothingstore.subcategory where c_no = ?"),
	MainPomProduct("SELECT * FROM clothingstore.productlist"),
	MainPomProductW("SELECT * FROM clothingstore.productlist where p_no = 1"),
	MainPomMoneyDesc("SELECT * FROM clothingstore.productlist order by p_price desc"),
	MainPomMoneyAsc("SELECT * FROM clothingstore.productlist order by p_price Asc"),
	MainPomReview("SELECT productlist.*, review.r_star FROM productlist JOIN review on p_no = pu_no order by r_star desc"),
	Login("select * from user where u_id = ? and u_pw = ?");
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
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/clothingstore?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
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
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/clothingstore?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
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
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/clothingstore?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
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
