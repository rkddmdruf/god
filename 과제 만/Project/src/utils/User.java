package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
	private static Data user;
	public static LocalDateTime loginDate = LocalDateTime.now();
	public static boolean admin = true;
	
	public static void setLoginDate() {
		loginDate = LocalDateTime.now();
	}
	
	public static void setUser(Data a) {
		user = a;
	}
	
	public static Data getUser() {
		if(user == null) return null;
		Data data = new Data();
		for(Object o : user) {
			data.add(o);
		}
		return data;
	}
}
