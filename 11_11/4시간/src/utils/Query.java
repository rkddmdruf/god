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
	Main_brand5("SELECT brand.bno, count(brand.bno) as c FROM parttimecat.apply \r\n"
			+ "left join job on job.jno = apply.jno left join brand on brand.bno  = job.bno\r\n"
			+ "group by brand.bno order by c desc, bno limit 5;"),
	Main_category("SELECT brand.cno FROM parttimecat.apply \r\n"
			+ "left join job on job.jno = apply.jno left join brand on brand.bno = job.bno\r\n"
			+ "where uno = ? order by apdate desc;"),
	Main_job6("SELECT job.* FROM parttimecat.job left join brand on brand.bno = job.bno where brand.cno = ? order by rand() limit 6;"),
	
	ceyung_nomal("SELECT job.jno, jname, cno FROM parttimecat.job\r\n"
			+ "left join brand on brand.bno = job.bno \r\n"
			+ "where cno between ? and ? and jname like ? order by jno;"),
	
	ceyung_best("SELECT job.jno, jname, count(apply.jno) as cs FROM parttimecat.job\r\n"
			+ "left join apply on apply.jno = job.jno left join brand on brand.bno = job.jno\r\n"
			+ "where cno between ? and ? and jname like ?\r\n"
			+ "group by apply.jno order by cs desc, job. jno"),
	ceyung_money("SELECT job.jno, jname FROM parttimecat.job\r\n"
			+ "			left join brand on brand.bno = job.bno\r\n"
			+ "			where cno between ? and ? and jname like ? order by jmoney desc, jno;"),
	;
	String string;
	Query(String string){
		this.string = string;
	}
	private static PreparedStatement ps(String string, Object...val) {
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
	public static void update(String string, Object...val){
		try {
			ps(string, val).executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void setTable(DefaultTableModel model, List<Row> list) {
		int n = model.getRowCount();
		for(int i = 0; i < n; i++) {
			model.removeRow(0);
		}
		list.forEach(e -> {
			Vector<Object> row = new Vector<>();
			e.forEach(rows -> row.add(rows));
			model.addRow(row);
		});
	}
}
