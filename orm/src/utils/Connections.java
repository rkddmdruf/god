package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class Connections{

	private static Connection c;
	private static PreparedStatement ps;
	private static ResultSet re;
	
	private static void getC(String s, Object...val) throws SQLException{
		c = DriverManager.getConnection("jdbc:mysql://localhost/game_site?serverTimezone=UTC", "root", "1234");
		ps = c.prepareStatement(s);
		for(int i = 0; i < val.length; i++) {
			ps.setObject(i + 1, val[i]);
		}
	}
	
	public static List<Data> select(String s, Object...val){
		List<Data> list = new ArrayList<>();
		try {
			getC(s, val);
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
			clozed();
		}
		return list;
	}
	
	public static void update(String s, Object...val) {
		try {
			getC(s, val);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static void clozed() {
		try {
			if(re != null && !re.isClosed()) re.close();
			if(ps != null && !ps.isClosed()) ps.close();
			if(c != null && !c.isClosed()) c.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
