package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import utils.CFrame;
import utils.Connections;
import utils.Data;
import utils.NorthPanel;
import utils.RoundedButton;

public class Search extends CFrame{
	JPanel categoryPanel = new JPanel(new GridLayout(1, 0, 5, 5)) {{
		setBorder(createEmptyBorder(0, 250, 0, 0));
		setBackground(Color.white);
	}};

	JPanel mainPanel = new JPanel(new BorderLayout(5, 5)) {{
		add(categoryPanel, BorderLayout.NORTH);
		setBackground(Color.white);
	}};
	
	List<JButton> butList = new ArrayList<>();
	public Search() {
		NorthPanel.vera = JLabel.CENTER;
		borderPanel.add(new NorthPanel(), BorderLayout.NORTH);
		setCategoryPanel();
		borderPanel.add(mainPanel);
		setFrame("자격증 목록", 750, 525);
	}
	
	private void setCategoryPanel() {
		List<Data> list = Connections.select("SELECT cgno FROM lecture.course_registration\r\n"
				+ "join certi on certi.cno = course_registration.cno;");
		List<Integer> count = Arrays.asList(0, 0, 0, 0, 0, 0);
		list.forEach(e -> {
			//Integer[] i = Arrays.asList(e.getString(0).split(",")).stream();
		});
		UIManager.put("Button.font", new Font("맑은 고딕", 1, 12));
		JButton best = new RoundedButton("추천과정") {{
			setBackground(Color.blue);
			setForeground(Color.white);
		}};
		butList.add(best);
		categoryPanel.add(best);
		
		
	}

	public static void main(String[] args) {
		new Search();
	}
}
