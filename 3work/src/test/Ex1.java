package test;

import java.awt.*;

import javax.swing.*;

public class Ex1 extends JFrame{
	
    Ex1(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        JPanel jp = new JPanel();
        setLayout(null);
        jp.setBounds(0,0,485,100);
        jp.setBackground(Color.black);
        add(jp);
        setVisible(true);
        
    }
    
    public static void main(String[] args) {
         new Ex1();
    } 
}
