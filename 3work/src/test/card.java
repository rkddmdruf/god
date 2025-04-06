package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class card extends JComponent{
	JFrame f = new JFrame();
	JButton[] JP = new JButton[3];
	CardLayout card = new CardLayout();
	JPanel cardL = new JPanel(card);
	card(){
		for(int i = 0; i < 3 ;i++) {
			JP[i] = new JButton();
			JP[i].setBounds(0,0,100,100);
			JP[i].setBackground(new Color(0,i*60,0));
			String name = "P" + i;
			cardL.add(JP[i], name);
		}
		JP[0].addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { card.show(cardL, "P1"); }
		});
		JP[1].addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { card.show(cardL, "P2"); }
		});
		JP[2].addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { card.show(cardL, "P0"); }
		});
		f.add(cardL);
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.setSize(100,100);
		f.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		new card();
	}
	
}
