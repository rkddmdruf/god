package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import utils.BaseFrame;

public class PanelRemove extends BaseFrame{
	
	JPanel p = new JPanel();
	
	PanelRemove(){
		setFrame("dsf", 500,500,()->{});
	}
	@Override
	public void desgin() {
		List<JPanel> list = new ArrayList<>();
		int[] j = {0,1,2,3};
		for(int i : j) {
			System.out.println(i);
			list.add(new JPanel() {{setBackground(new Color(i*50));setPreferredSize(new Dimension(100,100));}});
			p.add(list.get(i));
		}
		add(p);
	}
	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new PanelRemove();
	}
}
