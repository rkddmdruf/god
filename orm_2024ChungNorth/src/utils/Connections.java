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

	private static final String URL = "jdbc:mysql://localhost/game_site?serverTimezone=UTC";
	private static final String ID = "root";
	private static final String PW = "1234";
	
	
	private static Connection getConnection() throws Exception{
		return DriverManager.getConnection(URL, ID, PW);
	}
	@FunctionalInterface
	public interface SqlAction {
		void run(PreparedStatement stmt) throws Exception;
	}
	public static List<Data> select(String s, Object...val){
		List<Data> list = new ArrayList<>();
		try (Connection c = getConnection();
			 PreparedStatement ps = c.prepareStatement(s)){
			for(int i = 0; i < val.length; i++)
				ps.setObject(i + 1, val[i]);
			ResultSet re = ps.executeQuery();
			while(re.next()) {
				Data data = new Data();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					data.add(re.getObject(i + 1));
				}
				list.add(data);
			}
			re.close();
			System.out.println("sdfsd");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
	
	public static void update(String s, Object...val) {
		try (Connection c = getConnection();
			 PreparedStatement ps = c.prepareStatement(s)){
			for(int i = 0; i < val.length; i++) {
				System.out.println(val[i].getClass());
				ps.setObject(i + 1, val[i]);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
