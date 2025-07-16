package main;

import utils.BaseFrame;

public class C_Detail extends BaseFrame{

	int user = 0;
	C_Detail(int user){
		this.user = user;
		setFrame("상세정보", 300, 550, ()->{});
	}
	@Override
	public void desgin() {
		
	}

	@Override
	public void action() {
		
	}

	public static void main(String[] args) {
		new C_Detail(1);
	}
}
