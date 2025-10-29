package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public enum Query {
	user("SELECT * FROM parttimecat.user where uid = ? and upw = ?;"),
	category("SELECT * FROM parttimecat.category;"),
	job("select * from job;"),
	userLike("SELECT * FROM parttimecat.likes where uno = ? and jno = ?;"),
	likeDel("delete from likes where jno = ? and uno = ?;"),
	likeInsert("insert into likes values(0,?,?,?);"),
	brand_name_XY("SELECT bxx, byy, bname FROM parttimecat.brand where bno = ?;"),
	applyYes_NO("SELECT * FROM parttimecat.apply where uno = ? and apok = 1 and jno = ?;"),
	applyInsert("insert into apply values(0,?,?,?,0);"),
	Top5("SELECT count(job.jno) AS `count`, job.bno, brand.bname FROM parttimecat.job"
	+ " LEFT JOIN brand ON job.bno = brand.bno group by bno order by `count` desc, bno asc limit 5;"),
	userJobCno("SELECT apply.*, job.bno, brand.cno FROM parttimecat.apply "
	+ "LEFT JOIN job ON apply.jno = job.jno LEFT JOIN brand ON job.bno = brand.bno where uno = ? order by apdate desc;"),
	CnoJob("SELECT brand.bname, job.* FROM parttimecat.brand"
	+ " left join job on brand.bno = job.bno where cno = ?;"),
	JobInfor("SELECT * FROM parttimecat.job where jno = ?;"),
	
	
	ceyung_asc("SELECT brand.bname, job.*, brand.cno FROM parttimecat.job"
			+ " left join brand on job.bno = brand.bno"
			+ " where cno >= ? and cno <= ? and jname LIKE ? order by jno asc;"),
	ceyung_money("SELECT brand.bname, job.*, brand.cno FROM parttimecat.job"
			+ " left join brand on job.bno = brand.bno"
			+ "  where cno >= ? and cno <= ? and jname LIKE ? order by jmoney desc;"),
	ceyung_ingi("SELECT brand.bname, job.*, count(apply.jno) as counts FROM parttimecat.job "
			+ "left join brand on job.bno = brand.bno left join apply on job.jno = apply.jno "
			+ "where cno >= ? and cno <= ? and jname LIKE ? group by jno order by counts desc;"),
	
	
	
	Gwoung_go("select * from advertise where ano = ?"),
	
	Brand("SELECT * FROM parttimecat.brand where cno >=? and cno <= ?"),
	BrandInf("SELECT brand.*, category.cname FROM parttimecat.brand left join category on category.cno = brand.cno where bno = ?;"),
	BrandInfJob("SELECT * FROM parttimecat.job where bno = ?;"),
	
	AdminMain("SELECT apply.*, job.jname, user.uname FROM parttimecat.apply left join job on job.jno = apply.jno left join user on user.uno = apply.uno order by apno desc;"),
	
	Pass_Yes_No_user("SELECT * FROM parttimecat.user where uno = ?;"),
	Pass_Yes_No_job("SELECT brand.bname, job.* FROM parttimecat.brand "
			+ "left join job on brand.bno = job.bno where jno = ?;"),
	Pass_Yes_No_update_apok("update apply set apok = 1 where apno = ?;"),
	
	UpLoad("SELECT * FROM parttimecat.brand where cno = ?;"),
	UpLoadjob("insert into job values(0,?,?,?,?,?,?,?,?)"),
	UpLoad광고("insert into advertise values(0,?,?)"),
	;
	String string;
	Query(String string){
		this.string = string;
	}
	
	private static PreparedStatement PS(String string, Object...val) {
		try {
			int nulls = 0;
			Connection c =  DriverManager.getConnection("jdbc:mysql://localhost/parttimecat?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement ps = c.prepareStatement(string);
			for(int i = 0; i < val.length; i++) {
				if(val[i] != null) ps.setObject((i+1) - nulls, val[i]);
				else nulls++;
			}
			return ps;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public List<Row> select(Object...val) {
		List<Row> list = new ArrayList<Row>();
		try {
			ResultSet re = PS(string, val).executeQuery();
			while(re.next()) {
				Row row = new Row();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					row.add(re.getObject(i+1));
				}
				list.add(row);
			}
			re.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
	public static List<Row> select(String string, Object...val) {
		List<Row> list = new ArrayList<Row>();
		try {
			ResultSet re = PS(string, val).executeQuery();
			while(re.next()) {
				Row row = new Row();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					row.add(re.getObject(i+1));
				}
				list.add(row);
			}
			re.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
	public void update(Object...val){
		List<Row> list = new ArrayList<>();
		try {
			PS(string, val).executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
