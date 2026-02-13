package orm;

import java.time.LocalDate;

public class User extends Entity<User> {

	private int u_no;
	private String u_name;
	private String u_id;
	private String u_pw;
	private String u_nick;
	private LocalDate u_bd;
	private int u_price;
	private int u_Disclosure;
	private Object u_img;

	public static final Equalz U_NO = new Equalz("u_no", User.class);
	public static final Equalz U_NAME = new Equalz("u_name", User.class);
	public static final Equalz U_ID = new Equalz("u_id", User.class);
	public static final Equalz U_PW = new Equalz("u_pw", User.class);
	public static final Equalz U_NICK = new Equalz("u_nick", User.class);
	public static final Equalz U_BD = new Equalz("u_bd", User.class);
	public static final Equalz U_PRICE = new Equalz("u_price", User.class);
	public static final Equalz U_DISCLOSURE = new Equalz("u_Disclosure", User.class);
	public static final Equalz U_IMG = new Equalz("u_img", User.class);
	
	
	public String getU_nick() {
		return u_nick;
	}

	public void setU_nick(String u_nick) {
		this.u_nick = u_nick;
	}

	public Object getU_img() {
		return u_img;
	}

	public void setU_img(Object u_img) {
		this.u_img = u_img;
	}

	@Override
	public String toString() {
		return "User [u_no=" + u_no + ", u_name=" + u_name + ", u_id=" + u_id + ", u_pw=" + u_pw + ", u_nick=" + u_nick
				+ ", u_bd=" + u_bd + ", u_price=" + u_price + ", u_Disclosure=" + u_Disclosure + ", u_img=" + u_img
				+ "]";
	}

	public int getU_no() {
		return u_no;
	}

	public void setU_no(int u_no) {
		this.u_no = u_no;
	}

	public String getU_name() {
		return u_name;
	}

	public void setU_name(String u_name) {
		this.u_name = u_name;
	}

	public String getU_id() {
		return u_id;
	}

	public void setU_id(String u_id) {
		this.u_id = u_id;
	}

	public String getU_pw() {
		return u_pw;
	}

	public void setU_pw(String u_pw) {
		this.u_pw = u_pw;
	}

	public LocalDate getU_bd() {
		return u_bd;
	}

	public void setU_bd(LocalDate u_bd) {
		this.u_bd = u_bd;
	}

	public int getU_price() {
		return u_price;
	}

	public void setU_price(int u_price) {
		this.u_price = u_price;
	}

	public int getU_Disclosure() {
		return u_Disclosure;
	}

	public void setU_Disclosure(int u_Disclosure) {
		this.u_Disclosure = u_Disclosure;
	}

}
