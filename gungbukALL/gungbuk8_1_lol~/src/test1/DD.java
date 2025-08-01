package test1;

import javax.swing.JLabel;

import utils.sp;

public class DD extends BaseFrame1{

	//"/img/"+actionList.get(global-1).getInt(0)+".jpg"
	JLabel lb = new JLabel(sp.getImg("src/img/1.jpg", 300, 300)) {{setBorder(sp.line);}};
	
	public DD() {
		setFrame("dd", 500, 500, () -> System.out.print("exit"));
	}
	
	
	public static void main(String[] args) {
		new DD();
	}


	@Override
	protected void des() {
		add(lb);
		
	}


	@Override
	protected void act() {
		// TODO Auto-generated method stub
		
	}
	
	

}
