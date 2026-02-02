package main;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import utils.*;

public class Serch extends CFrame{
	JComboBox<Object> category = new JComboBox<Object>() {{
		for (Data data : Connections.select("SELECT ca_name FROM category;")) addItem(data.get(0));
	}};
	JComboBox<Object> order = new JComboBox<Object>("오름차순 / 내림차순".split(" / "));
	public Serch() {
		add(new JLabel("검색", JLabel.CENTER));
		setFrame("검색", 400, 400);
	}
	public static void main(String[] args) {
		new Serch();
	}
}
