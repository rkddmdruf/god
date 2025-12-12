package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public enum Query {
	Main_top5("SELECT count(job.bno) as c,job.bno FROM parttimecat.apply\r\n"
			+ "join job on apply.jno = job.jno group by job.bno order by c desc limit 5"),
	Main_getCno("SELECT brand.cno FROM parttimecat.apply\r\n"
			+ "join job on job.jno = apply.jno join brand on brand.bno = job. bno\r\n"
			+ "where uno = ? order by apdate desc limit 1;"),
	Main_get6Job("SELECT job.* FROM parttimecat.job\r\n"
			+ "join brand on brand.bno = job.bno\r\n"
			+ "where cno = ? order by jno;"),
	
	pass_nomal("select job.* from job left join brand on brand.bno = job.bno where cno between ? and ? order by jno"),
	pass_money("select job.* from job left join brand on brand.bno = job.bno where cno between ? and ? order by jmoney desc, jno"),
	pass_likes("SELECT job.*, count(apply.jno) as c FROM parttimecat.apply\r\n"
			+ "right join job on apply.jno = job.jno left join brand on job.bno = brand.bno\r\n"
			+ "where brand.cno between ? and ?\r\n"
			+ " group by job.jno order by c desc, jno;"),
	
	brand("select * from brand where cno between ? and ?"),
	
	MyPage_Likes("SELECT cname, job.bno, brand.bname, job.jname, likes.ldate FROM parttimecat.job\r\n"
			+ "join brand on brand.bno = job.bno join category on category.cno = brand.cno join likes on job.jno = likes.jno\r\n"
			+ "where uno = ?;"),
	MyPage_witeJob("SELECT apply.apno , job.bno, job.jname, apply.apdate, job.jgrade FROM parttimecat.job\r\n"
			+ "join brand on brand.bno = job.bno join category on category.cno = brand.cno join apply on apply.jno = job.jno\r\n"
			+ "where uno = ? and apply.apok = 0;"),
	MyPage_Pass("SELECT apply.apno , job.bno, job.jname, apply.apdate, job.jmoney FROM parttimecat.job\r\n"
			+ "join brand on brand.bno = job.bno join category on category.cno = brand.cno join apply on apply.jno = job.jno\r\n"
			+ "where uno = ? and apply.apok = 1;"),
	
	;
	String string;
	Query(String string) {
		this.string = string;
	}
	
	public static PreparedStatement getC(String string, Object...val) {
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/parttimecat?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement ps = c.prepareStatement(string);
			for(int i = 0; i < val.length; i++) {
				ps.setObject(i+1, val[i]);
			}
			return ps;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Row> selectText(String string, Object...val){
		List<Row> list = new ArrayList<Row>();
		try {
			ResultSet re = getC(string, val).executeQuery();
			while(re.next()) {
				Row row = new Row();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					row.add(re.getObject(i+1));
				}
				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<Row> select(Object...val){
		List<Row> list = new ArrayList<Row>();
		try {
			ResultSet re = getC(string, val).executeQuery();
			while(re.next()) {
				Row row = new Row();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					row.add(re.getObject(i+1));
				}
				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void update(Object...val) {
		try {
			getC(string, val).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void update(String string, Object...val) {
		try {
			getC(string, val).executeUpdate();
		} catch (Exception e) {
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
