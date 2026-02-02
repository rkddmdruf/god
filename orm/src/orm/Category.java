package orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import utils.Connections;
import utils.Data;

public class Category extends Entity{
	private int ca_no;
	private String ca_name;
	/*
	 * public static List<Category> findAll() { List<Data> list =
	 * Connections.select("select * from category"); List<Category> categorys = new
	 * ArrayList<>(); for (Data data : list) { Category category = new Category();
	 * category.ca_no = data.getInt(0); category.ca_name = data.get(1).toString();
	 * 
	 * categorys.add(category); } return categorys; }
	 */
	
	public static Category findById(int id) {
		List<Data> list = Connections.select("select * from category where ca_no = ?", id);
		if(list.isEmpty()) {
			return null;
		}
		Category category = new Category();
		category.ca_no = list.get(0).getInt(0);
		category.ca_name = list.get(0).get(1).toString();
		
		return category;
	}
	
	public void setCa_name(String string) {
		ca_name = string;
	}
	
	public static void create(Category category) {
		Connections.update("insert into category values (? ,?)", category.ca_no, category.ca_name);
	}

	public void update() {
		Connections.update("update category set ca_name = ? where ca_no = ?", ca_name, ca_no);
	}

	public void delete() {
		Connections.update("delete from category where ca_no = ?", ca_no);
	}
	
	@Override
	public String toString() {
		return "ca_no : " + ca_no + ", ca_name : " + ca_name;
	}
}