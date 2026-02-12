package orm;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import utils.Connections;
import utils.Data;

public class Entity2 extends SaveList{
	private static String query = "";
	private static Class<?> clazz;
	private List<Object> values = new ArrayList<>();
	
	public static Entity2 select(Object...ns) {
		query += "select ";
		System.out.println(ns.length);
		for(int i = 0; i < ns.length; i++) {
			if(ns[i] instanceof Class) {
				query += ((Class) ns[i]).getSimpleName().toLowerCase() + ".*" + (i == ns.length - 1 ? " " : ", ");
				continue;
			}
			Nev n = (Nev) ns[i];
			query += n.c.getSimpleName().toLowerCase() + "." + n.name + (i == ns.length - 1 ? " " : ", ");
		}
		return new Entity2();
	}
	
	public Entity2 from(Class<?> c) {
		clazz = c;
		query += " \n from " + clazz.getSimpleName().toLowerCase();
		return this;
	}
	
	public Entity2 join(Class<?> c, String string) {
		String c1Name = c.getSimpleName().toLowerCase();
		String c2Name = clazz.getSimpleName().toLowerCase();
		String joinColumn = "." + string;
		query += "\n join " + c1Name + " on " + c1Name + joinColumn + " = " + c2Name + joinColumn;
		return this;
	}
	
	public Entity2 where(Nev...ns) {
		query += " \n where ";
		for(int i = 0; i < ns.length; i++) {
			Nev n = ns[i];
			query += n.c.getSimpleName().toLowerCase() + "." + n.name + " " + n.eq + " ? " + (i == ns.length - 1 ? "" : " and ");
			if(n.eq.equals("like")) values.add("%" + n.value + "%");
			else values.add(n.value);
		}
		return this;
	}
	public List<Data> commit() {
		System.out.println(query);
		return Connections.select(query, values.toArray());
	}
}
