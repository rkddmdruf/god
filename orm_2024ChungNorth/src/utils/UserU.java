package utils;

import orm.*;
import ormDb.*;

public class UserU {
	private static User user = Entity.findByIds(User.class, 1);
	public static boolean admin;
	
	public static void setUser(User d) {
		user = d;
	}
	public static User getUser() {
		return user == null ? null : user;
	}
}
