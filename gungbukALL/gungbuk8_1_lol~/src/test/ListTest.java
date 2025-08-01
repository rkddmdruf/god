package test;

import java.util.ArrayList;
import java.util.List;

public class ListTest {

	public static void main(String[] args) {
		List<List<Integer>> list1= new ArrayList<List<Integer>>();
		List<List<Integer>> list2= new ArrayList<List<Integer>>();
		for(int i = 0; i < 1000; i++) {
			list1.add(new ArrayList<Integer>());
			list1.get(i).add(i);
			list1.get(i).add(0);
		}
		System.out.println(list1);
		for(int i = 0; i < 1000; i++) {
			list2.add(new ArrayList<Integer>());
			list2.get(i).add(list1.get(i).get(0));
			list2.get(i).add(i+1);
			list2.get(i).add(i+2);
			list2.get(i).add(i+3);
		}
		System.out.println(list2);
	}
}
