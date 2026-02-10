package orm2;

public class test {
	public static void main(String[] args) {
		System.out.println(BaseEntity.class.getAnnotation(Table.class).value());
	}
}
