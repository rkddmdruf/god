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

import main.BrandInfor;

public enum Query {
	Main_brandTOP5("SELECT job.bno, count(apno) as counts FROM parttimecat.apply \r\n"
			+ "left join job on job.jno = apply.jno\r\n"
			+ "group by apply.jno order by counts desc, apply.jno asc limit 5;"),
	Main_bestJob("SELECT * FROM parttimecat.job where jno in(?,?,?,?,?,?);"),
	
	JobInfor_job("SELECT job.* FROM parttimecat.job where jno = ?;"),
	JobInfor_brand("select brand.* from brand left join job on brand.bno = job.bno where job.jno = ?;"),
	JobInfor_like_YN("SELECT * FROM parttimecat.likes where uno = ? and jno = ?;"),
	JobInfor_apply_YN("SELECT * FROM parttimecat.apply where jno = ? and uno = ?;"),
	
	PullOut_nomal("SELECT job.jno, job.jname FROM parttimecat.job left join brand on brand.bno = job.bno "
			+ "where cno >= ? and cno <= ? and jname like ? order by jno;"),
	PullOut_best("SELECT job.jno, job.jname, count(apply.apno) as counts FROM parttimecat.job  "
			+ "left join brand on brand.bno = job.bno "
			+ "left join apply on apply.jno = job.jno "
			+ " where cno >= ? and cno <= ? and jname like ? group by job.jno  order by counts desc, jno;"),
	PullOut_money("SELECT job.jno, job.jname FROM parttimecat.job left join brand on brand.bno = job.bno  "
			+ "where cno >= ? and cno <= ? and jname like ? order by jmoney desc, jno;"),
	
	Brand_getBrand("SELECT * FROM parttimecat.brand where cno between ? and ?;"),
	
	BrandInfor_getBrand("SELECT brand.*, category.cname FROM parttimecat.brand "
			+ "left join category on category.cno = brand.cno "
			+ "where bno = ?;"),
	BrandInfor_getJobs("SELECT * FROM parttimecat.job where bno = ?;"),
	
	Serch_ALL("SELECT * FROM parttimecat.brand;"),
	
	MyPage_ILike("SELECT category.cname, brand.bno, brand.bname, job.jname, likes.ldate FROM parttimecat.likes "
			+ "left join job on likes.jno = job.jno left join brand on brand.bno = job. bno left join category on brand.cno = category.cno "
			+ "where uno = ?;"),
	MyPage_waitJob("SELECT apply.apno, brand.bno, job.jname, apdate, job.jgrade FROM parttimecat.apply "
			+ "left join job on job.jno = apply.jno left join brand on brand.bno = job.bno "
			+ "where uno = ? and apply.apok = 0;"),
	MyPage_good("SELECT apply.apno, brand.bno, job.jname, apdate, job.jmoney FROM parttimecat.apply "
			+ "left join job on job.jno = apply.jno left join brand on brand.bno = job.bno "
			+ "where uno = ? and apply.apok = 1;"),
	
	Chat_User("SELECT category.cname, count(category.cno) as c FROM parttimecat.apply "
			+ "left join job on job.jno = apply.jno left join brand on job.bno = brand.bno left join category on category.cno = brand.cno "
			+ "where uno = ? group by category.cno order by c desc, category.cno limit 5;"),
	Chat_All("SELECT brand.bname, count(brand.bno) as c FROM parttimecat.apply "
			+ "left join job on job.jno = apply.jno left join brand on job.bno = brand.bno "
			+ "group by brand.bno order by c desc limit 5;"),
	
	Pass("select * from brand where bno = ?"),
	
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
