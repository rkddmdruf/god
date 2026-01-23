package main;

public class User {
	private static Data user = Connections.select("select * from user").get(1);
	public static boolean admin;
	
	public static void setUser(Data d) {
		user = d;
	}
	
	public static int getUno() {
		return Integer.parseInt(user.get(0).toString());
	}
	public static Data getUser() {
		if(user == null) return null;
		Data d = new Data();
		for(Object obj : user) {
			d.add(obj);
		}
		return d;
	}
}
