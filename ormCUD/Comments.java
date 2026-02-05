package orm;

public class Comments extends Entity<Comments>{
	
	private int c_no;
	private int u_no;
	private int g_no;
	private int c_star;
	private String c_content;

	
	public int getC_no() {
		return c_no;
	}


	public void setC_no(int c_no) {
		this.c_no = c_no;
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


	public int getC_star() {
		return c_star;
	}


	public void setC_star(int c_star) {
		this.c_star = c_star;
	}


	public String getC_content() {
		return c_content;
	}


	public void setC_content(String c_content) {
		this.c_content = c_content;
	}


	@Override
	public String toString() {
		return "Comments [c_no=" + c_no + ", u_no=" + u_no + ", g_no=" + g_no + ", c_star=" + c_star + ", c_content="
				+ c_content + "]";
	}
}
