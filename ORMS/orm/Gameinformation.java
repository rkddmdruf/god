package orm;

import java.time.LocalDate;

public class Gameinformation extends Entity<Gameinformation> {
	@Id
	private int g_no;
	private String g_name;
	private int g_price;
	private LocalDate g_birth;
	private int ca_no;
	private int g_limit;
	private String g_lebu;
	
	public static final Equalz G_NO = new Equalz("g_no");
	public static final Equalz G_NAME = new Equalz("g_name");
	public static final Equalz G_PRICE = new Equalz("g_price");
	public static final Equalz G_BIRTH = new Equalz("g_birth");
	public static final Equalz CA_NO = new Equalz("ca_no");
	public static final Equalz G_LIMIT = new Equalz("g_limit");
	public static final Equalz G_LEBU = new Equalz("g_lebu");

	@Override
	public String toString() {
		return "Gameinformation [g_no=" + g_no + ", g_name=" + g_name + ", g_price=" + g_price + ", g_birth=" + g_birth
				+ ", ca_no=" + ca_no + ", g_limit=" + g_limit + ", g_expl=" + g_lebu + "]";
	}

	public int getG_no() {
		return g_no;
	}

	public void setG_no(int g_no) {
		this.g_no = g_no;
	}

	public String getG_name() {
		return g_name;
	}

	public void setG_name(String g_name) {
		this.g_name = g_name;
	}

	public int getG_price() {
		return g_price;
	}

	public void setG_price(int g_price) {
		this.g_price = g_price;
	}

	public LocalDate getG_birth() {
		return g_birth;
	}

	public void setG_birth(LocalDate g_birth) {
		this.g_birth = g_birth;
	}

	public int getCa_no() {
		return ca_no;
	}

	public void setCa_no(int ca_no) {
		this.ca_no = ca_no;
	}

	public int getG_limit() {
		return g_limit;
	}

	public void setG_limit(int g_limit) {
		this.g_limit = g_limit;
	}

	public String getG_lebu() {
		return g_lebu;
	}

	public void setG_lebu(String g_lebu) {
		this.g_lebu = g_lebu;
	}


}
