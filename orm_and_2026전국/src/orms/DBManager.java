package orms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import utils.Connections;

public class DBManager {
	private static String url = ("jdbc:mysql://localhost/emp?serverTimezone=Asia/Seoul");
	private static String id = "root";
	private static String pw = "1234";
	
	private static Connection getConnection() throws SQLException{
			return DriverManager.getConnection(url, id, pw);
	}
	
	public static PreparedStatement execute(String str, Object...val) throws SQLException{
		PreparedStatement ps = getConnection().prepareStatement(str);
		for(int i = 0; i < val.length; i++) {
			ps.setObject(i + 1, val[i]);
		}
		return ps;
	}
}
