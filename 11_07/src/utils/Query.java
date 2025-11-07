package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public enum Query {
	Main_brandTOP5("SELECT job.bno, count(apno) as counts FROM parttimecat.apply \r\n"
			+ "left join job on job.jno = apply.jno\r\n"
			+ "group by apply.jno order by counts desc, apply.jno asc limit 5;"),
	Main_bestJob("SELECT * FROM parttimecat.job where jno in(?,?,?,?,?,?);"),
	
	
	
	;
	String string;
	Query(String string){
		this.string = string;
	}
	public static PreparedStatement ps(String string, Object...val) {
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/parttimecat?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement ps = c.prepareStatement(string);
			for(int i = 0; i < val.length; i++) {
				ps.setObject(i+1, val[i]);
			}
			return ps;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static List<Row> selectText(String string, Object...val){
		List<Row> list = new ArrayList<Row>();
		try {
			ResultSet re = ps(string, val).executeQuery();
			while(re.next()) {
				Row row = new Row();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					row.add(re.getObject(i+1));
				}
				list.add(row);
			}
			re.close();
			return list;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
	public List<Row> select(Object...val){
		List<Row> list = new ArrayList<Row>();
		try {
			ResultSet re = ps(string, val).executeQuery();
			while(re.next()) {
				Row row = new Row();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					row.add(re.getObject(i+1));
				}
				list.add(row);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
	public void update(String string, Object...val){
		try {
			ps(string, val).executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void setTable(DefaultTableModel model, List<Row> list) {
		for(int i = 0; i < model.getRowCount(); i++) {
			model.removeRow(0);
		}
		list.forEach(e -> {
			Vector<Object> row = new Vector<>();
			e.forEach(rows -> row.add(rows));
			model.addRow(row);
		});
	}
}
