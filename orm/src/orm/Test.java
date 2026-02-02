package orm;

import java.util.List;

public class Test {
	public static void main(String[] args) {
		System.out.println(Entity.findById(Category.class, 7));
	}
}
