package main;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import utils.*;

public class PepleCenter extends BaseFrame{
	List<Row> list = Query.question.select();
	JPanel mainPanel = new JPanel(new GridLayout(0,1)) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}};
	PepleCenter(){
		setFrame("고객센터", 400, 600, ()->{});
	}

	@Override
	protected void desing() {
		for(Row row : list) {
			mainPanel.add(new JLabel(row.getString(1)));
			
		}
		add(new JPanel(null) {{
			mainPanel.setBounds(15,30,350, 5000);
			add(mainPanel);
		}});
	}

	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new PepleCenter();
	}
}
