package orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.Connections;
import utils.Data;

public class Entity<T extends Entity<T>> {
	Class<?> clazz = Entity.class;

	Field[] field = clazz.getDeclaredFields();

	public static <E extends Entity> List<E> findAll(Class<E> c) {
		String cName = c.getSimpleName().toLowerCase();
		List<E> categorys = new ArrayList<>();
		Field[] fields = c.getDeclaredFields();

		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/game_site?serverTimezone=UTC", "root",
					"1234");
			Statement s = con.createStatement();
			ResultSet re = s.executeQuery("select * from " + cName);
			while (re.next()) {
				ResultSetMetaData rem = re.getMetaData();
				E instance = c.getDeclaredConstructor().newInstance();

				for (int i = 0; i < rem.getColumnCount(); i++) {
					fields[i].setAccessible(true);
					fields[i].set(instance, re.getObject(fields[i].getName()));
				}
				categorys.add(instance);
			}
			re.close();
			s.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return categorys;
	}

	public static <E extends Entity> E findById(Class<E> c, int id) {
		List<E> list = findAll(c);
		if(list.size() > id && id >= 0) {
			return list.get(id);
		}
		return null;
	}

	/*
	 * public static void create(T t) {
	 * Connections.update("insert into category values (? ,?)", category.ca_no,
	 * category.ca_name); }
	 * 
	 * public void update() { Class<?> clazz = this.getClass(); String tableName =
	 * clazz.getSimpleName().toLowerCase();
	 * 
	 * List<Object> values = new ArrayList<>(); Field[] field = clazz.getFields();
	 * try { for(Field f : field) { f.setAccessible(true); values.add(""); }
	 * 
	 * } catch (Exception e) { // TODO: handle exception }
	 * Connections.update("update " + tableName +
	 * " set ca_name = ? where ca_no = ?", values); }
	 * 
	 * public void delete() {
	 * Connections.update("delete from category where ca_no = ?", ca_no); }
	 */

}
