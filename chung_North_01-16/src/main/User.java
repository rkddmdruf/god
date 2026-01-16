package main;

public class User {
	private static Data user = Connections.select("select * from user where u_no = ?", 1).get(0);
	public static boolean admin = false;
	
	public static void setUser(Data d) {
		user = d;
	}
	
	public static Data getData() {
		if(user == null) return null;
		Data d = new Data();
		for(Object u : user)
			d.add(u);
		return d;
	}
	
	public static int getUno() {
		if(user == null) return 0;
		return Integer.parseInt(user.get(0).toString());
	}
}
