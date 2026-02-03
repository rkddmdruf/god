package orm;

import java.time.LocalDate;

public class Gameinformation extends Entity<Gameinformation> {

	private int g_no;
	private String g_name;
	private int g_price;
	private LocalDate g_birth;
	private int ca_no;
	private int g_limit;
	private String g_expl;

	@Override
	public String toString() {
		return "Gameinformation [g_no=" + g_no + ", g_name=" + g_name + ", g_price=" + g_price + ", g_birth=" + g_birth
				+ ", ca_no=" + ca_no + ", g_limit=" + g_limit + ", g_expl=" + g_expl + "]";
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

	public String getG_expl() {
		return g_expl;
	}

	public void setG_expl(String g_expl) {
		this.g_expl = g_expl;
	}

}
