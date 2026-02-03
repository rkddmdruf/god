package orm;

import java.sql.Statement;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		/*
		 * Category c = new Category(); c.setCa_name("테스트 입니다."); c.setCa_no(4);
		 * 
		 * Category.create(Category.class, c);
		 * 
		 * List<Category> list = Category.findAll(Category.class); list.get(6).delete();
		 */
		List<Category> list = Category.findAll(Category.class);
		List<Comments> list1 = Comments.findAll(Comments.class);
		List<Comments> list2 = Comments.findAll(Comments.class);
		List<Category> list3 = Category.findAll(Category.class);
		List<Category> list4 = Category.findAll(Category.class);
		Category c = Entity.findById(Category.class, 6);
		System.out.println(c);
	}
}
