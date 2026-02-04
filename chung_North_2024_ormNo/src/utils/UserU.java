package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UserU {
	private static Data user = Connections.select("select * from user").get(0);
	public static boolean admin;
	
	public static void setUser(Data d) {
		user = d;
	}
	public static Data getUser() {
		if(user == null) return null;
		Data data = new Data();
		for(Object d : user)
			data.add(d);
		return data;
	}
}
