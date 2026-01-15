package main;

public class User {
	private static Data user = Connections.select("select * from user where u_no = 1").get(0);
	public static boolean admin = false;
	public static void setUser(Data u) {
		user = u;
	}
	public static Data getData() {
		return user;
	}
	public static Integer getUno() {
		if(user == null) return null;
		return Integer.parseInt(user.get(0).toString());
	}
	public static void logOut() {
		user = null;
	}
}
