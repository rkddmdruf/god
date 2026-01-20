package main;

public class User {
	private static Data user = Connections.select("select * from user").get(1);
	public static boolean admin;
	
	public static void setUser(Data d) {
		user = d;
	}
	public static int getUno() {
		return (user == null ? 0 : user.getInt(0));
	}
	public static Data getUser() {
		if(user == null) return new Data();
		Data data = new Data();
		for(Object object : user) 
			data.add(object);
		return data;
	}
}
