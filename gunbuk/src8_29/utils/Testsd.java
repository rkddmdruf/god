package utils;

import java.util.ArrayList;
import java.util.List;

public class Testsd {

	public static void main(String[] args) {
		List<String> list=  new ArrayList<String>();
		list.add("sdfsdf");
		list.add("sdfsdf");
		list.add("sdfsdf");
		list.add("sdfsdf");
		list.add("sdfsdf");
		
		System.out.println(list);
		list.removeAll(list);
		System.out.println(list);
	}
}
