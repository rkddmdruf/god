package orm;

import java.sql.Connection;
import java.util.Arrays;

import utils.Connections;

public class Entity2{
	private static String query = "";
	private static Class<?> clazz;
	public static Entity2 select(String...strings) {
		String s = String.join(", ", Arrays.asList(strings));
		if(s.isEmpty()) s = "*";
		query += "select " + s;
		return new Entity2();
	}
	
	public Entity2 from(Class<?> c) {
		clazz = c;
		query += " from " + clazz.getSimpleName().toLowerCase();
		return this;
	}
	
	public Entity2 join(Class<?> c, String string) {
		String c1Name = c.getSimpleName().toLowerCase();
		String c2Name = clazz.getSimpleName().toLowerCase();
		String joinColumn = "." + string;
		query += "\n join " + c1Name + " on " + c1Name + joinColumn + " = " + c2Name + joinColumn;
		return this;
	}
	
	private void whereSet() {
		query += " \n where ";
	}
	public Entity2 where(Class<?> c, Nev...ns) {
		whereSet();
		return this;
	}
	public Entity2 where(Nev...ns) {
		whereSet();
		return this;
	}
	public void commit() {
		System.out.println(query);
	}
}
