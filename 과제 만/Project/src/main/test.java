package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.Data;

public class test {

	public static void main(String[] args) {
		List<Data> list = new ArrayList<>();
		for(int i = 0; i < 10; i++) {
			list.add(new Data() {{
				addAll(Arrays.asList(1, "강응결", 3));
				addAll(Arrays.asList(2, "김하하", 2));
				addAll(Arrays.asList(3, "김가가", 1));
			}});
		}
	}
	
}
