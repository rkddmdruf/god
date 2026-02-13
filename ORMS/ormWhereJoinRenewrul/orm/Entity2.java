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
		if(ns.length == 0) query += " * ";
		for(Object obj : ns) {
			if(obj instanceof Nev) {
				Nev n = (Nev) obj;
				query += n.c.getSimpleName().toLowerCase() + "." + n.name;
			}else if(obj instanceof Class)
				query += ((Class) obj).getSimpleName().toLowerCase() + ".*";
			else if(obj instanceof String)
				query += obj;
			query += (obj == ns[ns.length - 1] ? " " : ", ");
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
	
	public Entity2 join(Nev...ns) {
		for(Nev n : ns) {
			String cName = n.c.getSimpleName().toLowerCase();
			String czName = clazz.getSimpleName().toLowerCase();
			
			query += " join " + cName + " on " + cName + "." + n.name + " = " + czName + "." + n.name;
		}
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
	
	public Entity2 group(Nev n) {
		query += " \n group by " + n.c.getSimpleName().toLowerCase() + "." + n.name;
		return this;
	}
	
	
	public Entity2 order(Nev...ns) {
		query += " \n order by ";
		for(Nev n : ns) {
			if(n.c != null) query += n.c.getSimpleName().toLowerCase() + ".";
			query += n.name + " " + n.value + (n == ns[ns.length - 1] ? " " : " , ");
		}
		return this;
	}
	public Entity2 order(Class<?> c, String...str) {
		query += " \n order by ";
		for(String s : str) {
			query += c.getSimpleName().toLowerCase() + "." + s + (s == str[str.length - 1] ? " " : " , ");
		}
		return this;
	}
	
	public Entity2 limit(int index) {
		query += " \n limit ?";
		values.add(index);
		return this;
	}
	
	public List<Data> push() {
		System.out.println(query);
		List<Data> list = Connections.select(query, values.toArray());
		query = "";
		values = new ArrayList<>();
		clazz = null;
		return list;
	}
}

class Nevs{
	
}