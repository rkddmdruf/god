package utils;

import java.time.LocalDate;

public class User{
	private static Data user = Connections.select("select * from user").get(0);
	public static boolean admin;
	
	public static void setUser(Data d) {
		user = d;
	}
	
	public static Data getUser() {
		if(user == null || user.isEmpty()) return null;
		Data data = new Data();
		for(Object o : user) {
			data.add(o);
		}
		return data;
	}
	
	public static LocalDate getBirth() {
		return LocalDate.parse(user.getString(4));
	}
	
	public static int getUno() {
		return user.getInt(0);
	}
}
