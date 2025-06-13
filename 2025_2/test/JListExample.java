package test;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.BaseFrame;
import utils.Query;
import utils.Row;

public class JListExample extends BaseFrame{

	JListExample() {
		setFrame("df", 500, 500, ()->{});
	}
	@Override
	public void desgin() {
		List<Row> list = Query.MainPomMoneyAsc.select();
		System.out.println(list.get(0));
		list = Query.MainPomMoneyDesc.select();
		System.out.println(list.get(0));
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}
    public static void main(String[] args) {
		new JListExample();
	}
}