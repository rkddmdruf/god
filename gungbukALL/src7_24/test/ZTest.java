package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import utils.Row;

public class ZTest {

	public static void main(String[] args) {
		List<ArrayList<Integer>> list = new ArrayList<>();
		for(int i = 0; i < 10; i++) {
			ArrayList<Integer> row = new ArrayList<>();
			row.add(100);
			row.add((int) (Math.random() * 45));
			list.add(row);
		}
		Collections.sort(list, Comparator.comparingInt((List<Integer> o) -> o.get(1)).reversed());
												//	  (o -> o.get(0));오름       Comparator.comparingInt().reversed());내림
		System.out.println(list);
	}
}
