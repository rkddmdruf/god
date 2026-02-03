package orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import utils.Connections;
import utils.Data;

public class Entity<T extends Entity<T>> extends SaveList{
	Class<? extends Entity> clazz = this.getClass();
	String tableName = clazz.getSimpleName().toLowerCase();
	Field[] fs = clazz.getDeclaredFields();
	
	private static Connection getCon() throws Exception {
		return DriverManager.getConnection("jdbc:mysql://localhost/game_site?serverTimezone=UTC", "root", "1234");
	}
	
	public static <E extends Entity> List<E> findAll(Class<E> c) {
		List<E> list = (List<E>) checkHaveList(c);
		if(list != null) return list;
		
		String cName = c.getSimpleName().toLowerCase();
		List<E> categorys = new ArrayList<>();
		Field[] fields = c.getDeclaredFields();

		try {
			Connection con = getCon();
			PreparedStatement s = con.prepareStatement("select * from " + cName);
			ResultSet re = s.executeQuery();
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
				System.out.println(instance);
				categorys.add(instance);
			}
			re.close();
			s.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		addList(categorys);
		return categorys;
	}

	public static <E extends Entity> E findById(Class<E> c, int id) {
		List<E> list = findAll(c);
		if (list.size() > id && id >= 0) {
			return list.get(id);
		}
		return null;
	}

	public static <E extends Entity> void where(Class<E> c, String...str) {
		List<E> list = (List<E>) getSaveList(c);
		if(list == null) { list = findAll(c); }
		Field[] fz = c.getDeclaredFields();
		for(int i = 0; i < fz.length; i++) {
			if(str.length > i && fz[i].getName().equals(str[i])) {
					fz[i].setAccessible(true);
					try {
						System.out.println(fz[i].get(c));
					} catch (Exception e2) {
						e2.printStackTrace();
					}
			}
		}
	}
	
	public static <E extends Entity> void create(Class<E> c, E e) {
		String cName = c.getSimpleName().toLowerCase();
		Field[] fs = e.getClass().getDeclaredFields();
		String query = "insert into " + cName + " values (";
		try {
			for (int i = 0; i < fs.length; i++) {
				fs[i].setAccessible(true);
				query += "?" + (i + 1 == fs.length ? ")" : ",");
			}
			Connection con = getCon();
			PreparedStatement ps = con.prepareStatement(query);
			for (int i = 0; i < fs.length; i++) {
				ps.setObject(i + 1, fs[i].get(e));
			}
			ps.executeUpdate();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public <E extends Entity<?>> void update(E e) {
		List<Object> values = new ArrayList<>();
		String query = "update " + tableName + " set ";
		try {
			for (Field f : fs) {
				f.setAccessible(true);
				if (f == fs[0])
					continue;
				values.add(f.get(e));
				query += f.getName() + " = ?" + (fs[fs.length - 1] == f ? "" : ", ");
			}
			values.add(fs[0].get(e));
			query += " where " + fs[0].getName() + " = ?";
			System.out.println(query);
			Connection con = getCon();
			PreparedStatement ps = con.prepareStatement(query);
			for (int i = 0; i < fs.length; i++) {
				ps.setObject(i + 1, values.get(i));
			}
			ps.executeUpdate();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public void delete() {
		try {
			Connections.update("delete from " + tableName + " where " + fs[0].getName() + " = ?", fs[0].get(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
