package realOrm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import utils.Connections;
import utils.Data;

public class Entity2{
	private String query = "";
	private Class<?> clazz;
	private List<Object> values = new ArrayList<>();
	
	private void addQuery(String query) {
		this.query += query;
	}
	public static Entity2 select(Object...ns) {
		Entity2 e = new Entity2();
		e.addQuery("select ");
		if(ns.length == 0) e.addQuery(" * ");
		for(Object obj : ns) {
			if(obj instanceof Nev) {
				Nev n = (Nev) obj;
				e.addQuery(n.c.getSimpleName().toLowerCase() + "." + n.name);
			}else if(obj instanceof Class)
				e.addQuery(((Class) obj).getSimpleName().toLowerCase() + ".*");
			else if(obj instanceof String)
				e.addQuery(obj.toString());
			e.addQuery(obj == ns[ns.length - 1] ? " " : ", ");
		}
		return e;
	}
	
	public Entity2 from(Class<?> c) {
		clazz = c;
		addQuery(" \n from " + clazz.getSimpleName().toLowerCase());
		return this;
	}
	
	public Entity2 rightJoin(Class<?> c, String string) {
		addQuery(" \n right ");
		return join(c, string);
	}
	
	public Entity2 rightJoin(Nev...ns) {
		addQuery(" \n right ");
		return join(ns);
	}
	
	public Entity2 join(Class<?> c, String string) {
		String c1Name = c.getSimpleName().toLowerCase();
		String c2Name = clazz.getSimpleName().toLowerCase();
		String joinColumn = "." + string;
		addQuery("\n join " + c1Name + " on " + c1Name + joinColumn + " = " + c2Name + joinColumn);
		return this;
	}
	
	public Entity2 join(Nev...ns) {
		for(Nev n : ns) {
			String cName = n.c.getSimpleName().toLowerCase();
			String czName = clazz.getSimpleName().toLowerCase();
			addQuery(" join " + cName + " on " + cName + "." + n.name + " = " + czName + "." + n.name);
		}
		return this;
	}
	
	public Entity2 where(Nev...ns) {
		addQuery(" \n where ");
		for(int i = 0; i < ns.length; i++) {
			Nev n = ns[i];
			addQuery(n.c.getSimpleName().toLowerCase() + "." + n.name + " " + n.eq + " ? " + (i == ns.length - 1 ? "" : " and "));
			if(n.eq.equals("like")) values.add("%" + n.value + "%");
			else values.add(n.value);
		}
		return this;
	}
	
	public Entity2 group(Nev n) {
		addQuery(" \n group by " + n.c.getSimpleName().toLowerCase() + "." + n.name);
		return this;
	}
	
	
	public Entity2 order(Nev...ns) {
		addQuery(" \n order by ");
		for(Nev n : ns) {
			if(n.c != null) addQuery( n.c.getSimpleName().toLowerCase() + ".");
			addQuery(n.name + " " + n.value + (n == ns[ns.length - 1] ? " " : " , "));
		}
		return this;
	}
	public Entity2 order(Class<?> c, String...str) {
		addQuery(" \n order by ");
		for(String s : str) {
			addQuery(c.getSimpleName().toLowerCase() + "." + s + (s == str[str.length - 1] ? " " : " , "));
		}
		return this;
	}
	
	public Entity2 limit(int index) {
		addQuery(" \n limit ?");
		values.add(index);
		return this;
	}
	
	public List<Data> toList(){
		System.out.println(query);
		List<Data> list = Connections.select(query, values.toArray());
		return list;
	}
	
	public List<Tuple> push() {
		System.out.println(query);
		List<Tuple> list = new ArrayList<>();
		try (Connection c = DriverManager.getConnection("jdbc:mysql://localhost/game_site?serverTimezone=UTC", "root", "1234");
				PreparedStatement ps = c.prepareStatement(query);){
			for(int i = 0; i < values.size(); i++) {
				ps.setObject(i + 1, values.get(i));
			}
			ResultSet re = ps.executeQuery();
			while(re.next()) {
				Tuple map = new Tuple();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					map.put(re.getMetaData().getColumnName(i + 1), re.getObject(i + 1));
				}
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}