package utils;

public class Html {
	String str = "";
	public Html() {
		str += "<html>";
	}
	
	public Html add(String s) {
		str += s;
		return this;
	}
	public Html nextLine() {
		str += "<br>";
		return this;
	}
	public Html forgeColor(String color) {
		str += "<font color='" + color + "'";
		return this;
	}
	public Html endForgeColor() {
		str += "</font color>";
		return this;
	}
	public String getString() {
		return str + "</html>";
	}
}
