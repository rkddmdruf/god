package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum Query {
	MovieSerch_All("SELECT * FROM moviedb.movie where g_no between ? and ?  and m_name like ? ;"),
	MovieSerch_예매순("SELECT movie.* , sum(reservation.r_people) as s  FROM moviedb.reservation \r\n"
			+ "left join movie on movie.m_no = reservation.m_no\r\n"
			+ "where g_no between ? and ?  and m_name like ? \r\n"
			+ "group by reservation.m_no order by s desc, reservation.m_no;"),
	
	MovieSerch_평점순("SELECT movie.*, avg(review.re_star) as d FROM moviedb.review\r\n"
			+ "		left join movie on movie.m_no = review.m_no\r\n"
			+ "		where g_no between ? and ? and m_name like ? \r\n"
			+ "		 group by review.m_no order by d desc, review.m_no;"),
	
	
	;
	String string;
	Query(String string) {
		this.string = string;
	}
	
	private static PreparedStatement getC(String string, Object...val) {
		PreparedStatement st = null;
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=Asia/Seoul&allowLoadLocalInfile=true", "root", "1234");
			st = c.prepareStatement(string);
			for(int i = 0; i < val.length; i++) {
				st.setObject(i+1, val[i]);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return st;
	}
	
	public List<Row> select(Object...val){
		List<Row> list = new ArrayList<>();
		try {
			ResultSet re = getC(string, val).executeQuery();
			while(re.next()) {
				Row row = new Row();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++)
					row.add(re.getObject(i+1));
				list.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static List<Row> select(String string, Object...val){
		List<Row> list = new ArrayList<>();
		try {
			ResultSet re = getC(string, val).executeQuery();
			while(re.next()) {
				Row row = new Row();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					row.add(re.getObject(i+1));
				}
				list.add(row);
			}
		} catch (SQLException e) {
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
}
