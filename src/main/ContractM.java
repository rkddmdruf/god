package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.BaseFrame;
import utils.Query;
import utils.Row;

public class ContractM extends BaseFrame{

	JPanel topL = new JPanel(new FlowLayout()) {{
		this.setPreferredSize(new Dimension(200, 150));
	}};
	JPanel topR = new JPanel(new GridLayout(3,2)) {{
		this.setPreferredSize(new Dimension(200, 150));
	}};
	JPanel top = new JPanel(new FlowLayout(0, 100, 10)) {{
		this.setBackground(Color.red);
		this.add(topL);
		this.add(topR);
	}};
	JComboBox<String> cb;
	ContractM() {
		setFrame("보험 계약", 700, 700, ()->{});
	}
	@Override
	public void design() {
		List<Row> list = Query.select("select * from customer order by name asc");
		String[] nameData = new String[list.size()-1];
		for(int i = 1; i < list.size(); i++) {
			nameData[i-1] = list.get(i).getString(1);
		}
		cb = new JComboBox<String>(nameData) {{
			this.setPreferredSize(new Dimension(70, 40));
		}};
		topL.add(new JLabel("고객코드:"));
		topL.add(cb);
		topL.add(new JLabel("고객코드:"));
		topL.add(new JLabel("고객코드:"));
		topL.add(new JLabel("고객코드:"));
		topL.add(new JLabel("고객코드:"));
		topL.add(new JLabel("고객코드:"));
		add(top, BorderLayout.NORTH);
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		new ContractM();
	}
}
