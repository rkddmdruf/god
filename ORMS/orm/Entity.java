package orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import utils.Connections;
import utils.Data;

public class Entity<T extends Entity<T>> extends SaveList{
	Class<? extends Entity> clazz = this.getClass();
	String tableName = clazz.getSimpleName().toLowerCase();
	private List<Field> fs = getFields(clazz);
	List<Runnable> runs = new ArrayList<>();
	private static Connection getCon() throws Exception {
		return DriverManager.getConnection("jdbc:mysql://localhost/game_site?serverTimezone=UTC", "root", "1234");
	}
	
	private static List<Field> getFields(Class<?> c){
		return Arrays.asList(c.getDeclaredFields()).stream()
				.filter(e -> Modifier.isPrivate(e.getModifiers()))
				.peek(e -> e.setAccessible(true))
				.collect(Collectors.toList());
	}
	
	private static <E> List<Object> getValues(Class<?> c, E e){
		List<Object> list = new ArrayList<>();
		try {
			for(Field f : getFields(c)) {
				 Class<?> type = f.getType();
				 if(type == int.class || type == Integer.class) {
					 list.add(f.getInt(e));
				 }
				 if(type == float.class || type == Float.class) {
					 list.add(f.getDouble(e));
				 }
				 if(type == double.class || type == Double.class) {
					 list.add(f.getDouble(e));
				 }
				 if(type == String.class) {
					 list.add(f.get(e).toString());
				 }
				 if(type == LocalDate.class) {
					 list.add(LocalDate.parse(f.get(e).toString()));
				 }
				 if(type == LocalDateTime.class) {
					 list.add(LocalDateTime.parse(f.get(e).toString()));
				 }
				 
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return list;
	}
	
	public static <E extends Entity> List<E> findAll(Class<E> c) {
		List<E> list = (List<E>) getList(c);
		if(!list.isEmpty()) return list;
		String cName = c.getSimpleName().toLowerCase();
		Field[] fields = c.getDeclaredFields();

		try (Connection con = getCon();
			PreparedStatement s = con.prepareStatement("select * from " + cName);
			ResultSet re = s.executeQuery();){
			while (re.next()) {
				ResultSetMetaData rem = re.getMetaData();
				E instance = c.getDeclaredConstructor().newInstance();

				for (int i = 0; i < rem.getColumnCount(); i++) {
					fields[i].setAccessible(true);
					if(rem.getColumnTypeName(i + 1).equals("DATE"))
						fields[i].set(instance, LocalDate.parse(re.getObject(fields[i].getName()).toString()));
					else
						fields[i].set(instance, re.getObject(fields[i].getName()));
				}
				list.add(instance);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveList(c, list);
		return list;
	}

	public static <E extends Entity> E findById(Class<E> c, int id) {
		List<E> list = findAll(c);
		if (list.size() > id && id >= 0) {
			return list.get(id);
		}
		return null;
	}
	
	private static <E extends Entity> Boolean equalsValue(Class<E> c, E e, Nev n) {
		Field[] fs = e.getClass().getDeclaredFields();
		try {
			for (Field f : fs) {
				f.setAccessible(true);
				if(!Modifier.isPrivate(f.getModifiers())) continue;
				if(!f.getName().equals(n.name)) continue;
				if(n.value instanceof Integer) {
					int int1 = Integer.parseInt(f.get(e).toString());
					int int2 = Integer.parseInt(n.value.toString());
					if(n.eq.equals(">=")) return int1 >= int2;
					if(n.eq.equals("<=")) return int1 <= int2;
					if(n.eq.equals(">")) return int1 > int2;
					if(n.eq.equals("<")) return int1 < int2;
					if(n.eq.equals("=")) return int1 == int2;
				}
				if(n.value instanceof String) {
					if(!n.eq.equals("=")) throw new Exception("equals빼고는 못함");
					return n.value.equals(f.get(e).toString());
				}
			}
		} catch (Exception e2) {
			System.out.println(e2.getMessage());
		}
		return false;
	}
	
	public static <E extends Entity> List<E> where(Class<E> c, Nev...ns) {
		List<E> list = findAll(c);
		try {
			for(Nev n : ns) {
				list = list.stream()
					.filter(nnn -> equalsValue(c, nnn, n))
					.collect(Collectors.toList());
				System.out.println(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	public <E extends Entity> void create() {
		String cName = clazz.getSimpleName().toLowerCase();
		List<Field> fs = getFields(clazz);
		List<Object> values = getValues(clazz, this);
		String querySet = String.join(", ", fs.stream().map(e -> e.getName() + " = ?").collect(Collectors.toList()));
		String query = "insert into " + cName + " set " + querySet;
		if(map.get(clazz) == null) map.put(clazz, new HashMap<>());
		map.get(clazz).put(values.get(0), this);
		Connections.update(query, values.toArray());
	}

	public <E extends Entity<?>> void update(E e) {
		List<Field> fs = getFields(clazz);
		List<Object> afterValues = getValues(clazz, this);
		List<Object> beforeValues = getValues(clazz, e);
		try {
			if(!afterValues.get(0).equals(beforeValues.get(0))) throw new Exception("ID가 다릅니다.");
		} catch (Exception e2) {
			System.out.println(e2.getMessage());
			return;
		}
		beforeValues.add(beforeValues.get(0));
		beforeValues.remove(0);
		String first = fs.get(0).getName();
		fs.remove(0);
		String querySet = String.join(", ", fs.stream().map(x -> x.getName() + " = ?").collect(Collectors.toList()));
		String query = "update " + tableName + " set " + querySet + " where " + first + " = ?";
		
		Connections.update(query, beforeValues.toArray());
	}

	public void delete() {
		try {
			Connections.update("delete from " + tableName + " where " + fs.get(0).getName() + " = ?", fs.get(0).get(this));
			map.get(clazz).remove(fs.get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
