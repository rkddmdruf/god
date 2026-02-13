package orm;

import java.time.LocalDate;

public class Purchasegame extends Entity<Purchasegame> {
	private int p_no;
	private int u_no;
	private int g_no;
	private LocalDate p_birth;

	public static final Equalz P_NO = new Equalz("p_no", Purchasegame.class);
	public static final Equalz U_NO = new Equalz("u_no", Purchasegame.class);
	public static final Equalz G_NO = new Equalz("g_no", Purchasegame.class);
	public static final Equalz P_BIRTH = new Equalz("p_birth", Purchasegame.class);
	
	@Override
	public String toString() {
		return "Purchasegame [p_no=" + p_no + ", u_no=" + u_no + ", g_no=" + g_no + ", p_birth=" + p_birth + "]";
	}

	public int getP_no() {
		return p_no;
	}

	public void setP_no(int p_no) {
		this.p_no = p_no;
	}

	public int getU_no() {
		return u_no;
	}

	public void setU_no(int u_no) {
		this.u_no = u_no;
	}

	public int getG_no() {
		return g_no;
	}

	public void setG_no(int g_no) {
		this.g_no = g_no;
	}

	public LocalDate getP_birth() {
		return p_birth;
	}

	public void setP_birth(LocalDate p_birth) {
		this.p_birth = p_birth;
	}

}
