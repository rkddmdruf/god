package orm;

public class Category extends Entity<Category>{
	@Id
	private int ca_no;
	private String ca_name;
	
	public static final Equalz CA_NO = new Equalz("ca_no");
	public static final Equalz CA_NAME = new Equalz("ca_name");
	
	public Category() {	}
	
	public Category(int ca_no, String ca_name) {
		super();
		this.ca_no = ca_no;
		this.ca_name = ca_name;
	}
	
	public int getCa_no() {
		return ca_no;
	}
	public String getCa_name() {
		return ca_name;
	}
	public void setCa_no(int i) {
		ca_no = i;
	}
	public void setCa_name(String i) {
		ca_name = i;
	}
	
	@Override
	public String toString() {
		return "ca_no : " + ca_no + ", ca_name : " + ca_name;
	}
}