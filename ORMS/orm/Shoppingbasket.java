package orm;

public class Shoppingbasket extends Entity<Shoppingbasket> {

	private int p_no;
	private int g_no;
	private int u_no;
	
	public static final Equalz P_NO = new Equalz("p_no");
	public static final Equalz G_NO = new Equalz("g_no");
	public static final Equalz U_NO = new Equalz("u_no");

	@Override
	public String toString() {
		return "Shoppingbasket [p_no=" + p_no + ", g_no=" + g_no + ", u_no=" + u_no + "]";
	}

	public int getP_no() {
		return p_no;
	}

	public void setP_no(int p_no) {
		this.p_no = p_no;
	}

	public int getG_no() {
		return g_no;
	}

	public void setG_no(int g_no) {
		this.g_no = g_no;
	}

	public int getU_no() {
		return u_no;
	}

	public void setU_no(int u_no) {
		this.u_no = u_no;
	}

}
