package utils;

import realOrm.*;
import realOrm.Db.*;

public class UserU {
	private static User user = Entity.findByIds(User.class, 4);
	public static boolean admin;
	
	public static void setUser(User d) {
		user = d;
	}
	public static User getUser() {
		return user == null ? null : user;
	}
}
