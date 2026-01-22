package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Connections {
	private static Connection c;
	private static PreparedStatement ps;
	private static ResultSet re;
	
	private static void getC(String s, Object...val) throws SQLException{
		c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=Asia/Seoul", "root", "1234");
		ps = c.prepareStatement(s);
		for(int i = 0; i < val.length; i++) {
			ps.setObject(i + 1, val[i]);
		}
	}
	
	public static List<Data> select(String s, Object...objects){
		List<Data> list = new ArrayList<>();
		try {
			getC(s, objects);
			re = ps.executeQuery();
			while(re.next()) {
				Data data = new Data();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					data.add(re.getObject(i + 1));
				}
				list.add(data);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			closed();
		}
		return list;
	}
	public static void update(String s, Object...objects) {
		try {
			getC(s, objects);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	private static void closed() {
		try {
			if(c != null && !c.isClosed()) c.close();
			if(ps != null && !ps.isClosed()) ps.close();
			if(re != null && !re.isClosed()) re.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
class Data extends ArrayList<Object>{
	public int getInt(int index) {
		return Integer.parseInt(get(index).toString());
	}
}