package main;

public class User {
	private static Data user;
	public static boolean admin = false;
	public static void setUser(Data u) {
		user = u;
	}
	public static Integer getUno() {
		if(user == null) return null;
		return Integer.parseInt(user.get(0).toString());
	}
	public static void logOut() {
		user = null;
	}
}
