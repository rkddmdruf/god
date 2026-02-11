package orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
	
	public static List<JoinTable> join(Class<? extends Entity> c1, Class<? extends Entity> c2, String cName) {
		List<JoinTable> list = new ArrayList<>();
		List<Entity> lists1 = (List<Entity>) findAll(c1);
		List<Entity> lists2 = (List<Entity>) findAll(c2);
		List<Field> fs1 = getFields(c1);
		List<Field> fs2 = getFields(c2);
		
		boolean c1NameIsEmpty = false;
		int fieldIndex1 = 0;
		int fieldIndex2 = 0;
		
		for(int i = 0; i < fs1.size(); i++) 
			if(fs1.get(i).getName().equals(cName)) {
				c1NameIsEmpty = true;
				fieldIndex1 = i;
			}
		for(int i = 0; i < fs2.size(); i++) 
			if(fs2.get(i).getName().equals(cName)) {
				c1NameIsEmpty = true;
				fieldIndex2 = i;
			}
		
		if(!c1NameIsEmpty) return list;
		
		try {
			for(Entity entity1 : lists1) {
				for(Entity entity2 : lists2) {
					JoinTable t = new JoinTable();
					Field f1 = c1.getDeclaredField(cName);
					f1.setAccessible(true);
					Field f2 = c2.getDeclaredField(cName);
					f2.setAccessible(true);
					if(f1.get(entity1).toString().equals(f2.get(entity2).toString())) {
						t.addAll(Arrays.asList(c1.getDeclaredFields()).stream()
								.peek(e -> e.setAccessible(true))
								.filter(e -> Modifier.isPrivate(e.getModifiers()))
								.map(e -> getValue1(e, entity1))
								.collect(Collectors.toList()));
						t.addAll(Arrays.asList(c2.getDeclaredFields()).stream()
								.peek(e -> e.setAccessible(true))
								.filter(e -> Modifier.isPrivate(e.getModifiers()))
								.map(e -> getValue1(e, entity2))
								.collect(Collectors.toList()));
						list.add(t);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	private static Object getValue1(Field e, Entity en) {
		try {
			return e.get(en);
		} catch (Exception e2) {
			System.out.println(e2.getMessage());
		}
		return null;
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
		System.out.println("map에 없음");
		String cName = c.getSimpleName().toLowerCase();
		Field[] fields = Arrays.asList(c.getDeclaredFields()).stream().peek(e -> e.setAccessible(true)).toArray(Field[]::new);
		try (Connection con = getCon();
			PreparedStatement s = con.prepareStatement("select * from " + cName);
			ResultSet re = s.executeQuery();){
			while (re.next()) {
				ResultSetMetaData rem = re.getMetaData();
				E instance = c.getDeclaredConstructor().newInstance();
				for (int i = 0; i < rem.getColumnCount(); i++) {
					String columnName = rem.getColumnName(i + 1);
					Object obj = re.getObject(columnName);
					if(rem.getColumnTypeName(i + 1).equals("DATE"))
						obj = LocalDate.parse(re.getObject(columnName).toString());
					else if(rem.getColumnTypeName(i + 1).equals("TIME"))
						obj = LocalDateTime.parse(re.getObject(columnName).toString(), DateTimeFormatter.ISO_TIME);
					fields[i].set(instance, obj);
				}
				list.add(instance);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveList(c, list);
		return list;
	}
	
	public static <E extends Entity> E findByIds(Class<E> c, int id) {
		if(map.get(c) == null) findAll(c);
		return (E) map.get(c).get(id);
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
					if(n.eq.equals("contains"))
						return f.get(e).toString().contains(n.value.toString());
					if(!n.eq.equals("=")) throw new Exception("equals빼고는 못함");
					return f.get(e).toString().equals(n.value);
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return false;
	}
	
	private List<Field> nonIdField() {
		return getFields(getClass()).stream().filter(e -> !e.isAnnotationPresent(Id.class)).collect(Collectors.toList());
	}
	
	private Field IdField() {
		return getFields(getClass()).stream().filter(e -> e.isAnnotationPresent(Id.class)).collect(Collectors.toList()).get(0);
	}
	
	public static <E extends Entity> List<E> where(Class<E> c, Nev...ns) {
		List<E> list = findAll(c);
		try {
			for(Nev n : ns) {
				list = list.stream()
					.filter(nnn -> equalsValue(c, nnn, n))
					.collect(Collectors.toList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public <E extends Entity> void insert() {
		String cName = clazz.getSimpleName().toLowerCase();
		List<Field> fs = getFields(clazz);
		List<Object> values = getValues(clazz, this);
		String querySet = String.join(", ", fs.stream().map(e -> "?").collect(Collectors.toList()));
		String fsNames = String.join(", ", fs.stream().map(e -> e.getName()).collect(Collectors.toList()));
		String query = "insert into " + cName + "(" + fsNames + ")" +" values (" + querySet + ")";
		if(map.get(clazz) == null) findAll(getClass());
		Connections.update(query, values.toArray());
		map.get(clazz).put(values.get(0), this);
	}
	
	public Object getValue1(Field f) {
		try { return f.get(this); }
		catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	public <E extends Entity<?>> void update() {
		List<Object> values = Arrays.asList(nonIdField().stream().map(e -> getValue1(e)).collect(Collectors.toList()), getValue1(IdField()));
		String querySet = String.join(", ", nonIdField().stream().map(x -> x.getName() + " = ?").collect(Collectors.toList()));
		String query = "update " + tableName + " set " + querySet + " where " + IdField().getName() + " = ?";
		Connections.update(query, values.toArray());
		map.get(getClass()).replace(values.get(values.size() - 1), this);
	}
	
	public void delete() {
		try {
			//이거 FORIGENKEY? 그 계층구조 때문에 삭제 안됌
			Connections.update("delete from " + tableName + " where " + fs.get(0).getName() + " = ?", fs.get(0).get(this));
			map.get(clazz).remove(fs.get(0).get(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
