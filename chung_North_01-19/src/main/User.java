package main;

public class User {
	private static Data user = Connections.select("select * from user").get(2);
	public static boolean admin = false;
	
	public static void setUser(Data u) {
		user = u;
	}
	
	public static int getUno() {
		if(user == null || user.isEmpty()) return 0;
		return user.getInt(0);
	}
	
	public static Data getUser() {
		if(user == null) return null;
		Data data = new Data();
		for(Object d : user)
			data.add(d);
		return data;
	}
}
