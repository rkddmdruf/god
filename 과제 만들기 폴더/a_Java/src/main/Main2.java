package main;

import utils.CFrame;

public class Main2 extends CFrame{

	public Main2() {
		setFrameCd("로그인", 500, 250, () -> {});
	}
	
	public static void main(String[] args) {
		new Main2();
	}
}
