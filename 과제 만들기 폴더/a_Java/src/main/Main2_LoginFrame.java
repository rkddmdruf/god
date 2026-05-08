package main;

import utils.CFrame;

public class Main2_LoginFrame extends CFrame{

	public Main2_LoginFrame() {
		borderPanel.add(new LoginPanel(null, this));
		setFrameCd("로그인", 600, 350, () -> {});
	}
	
	public static void main(String[] args) {
		new Main2_LoginFrame();
	}
}
