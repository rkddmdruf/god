package main;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;

import utils.*;

public class Main2_LoginFrame extends CFrame{

	public Main2_LoginFrame() {
		List<Data> list = Connections.select("select * from loginuser");
		getter.main2 = this;
		getter.infor(list + " ");
		setFrameCd("로그인", 600, 350, () -> {});
		if(list.isEmpty()) {
			borderPanel.add(new LoginPanel(null, this));
			revalidate();
			repaint();
		} else {
			User.setUser(Connections.select("select * from user where uno = ?", list.get(0).get(0)).get(0));
			new Main1();
			dispose();
		}
	}
	
	public static void main(String[] args) {
		new Main2_LoginFrame();
	}
}
