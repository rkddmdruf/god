package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

import utils.BaseFrame;

public class ZT extends BaseFrame{

	ZT(){
		setFrame("테스트", 1000, 700, ()->{});
	}
	@Override
	public void desgin() {
		add(new JPanel (new BorderLayout()){{
			setBackground(Color.white); 
			setBorder(BorderFactory.createEmptyBorder(100,60,100,60));
			add(new HOME() {{setBackground(Color.white);}});
		}});
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new ZT();
	}
}
