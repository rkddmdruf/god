package main;

import java.awt.BorderLayout;

import static javax.swing.BorderFactory.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MovieInfor extends JFrame{
	
	JPanel borderPanel = new JPanel(new BorderLayout(5, 5)) {{
		setBorder(createEmptyBorder(5,5,5,5));
	}};
	JPanel mainPanel = new JPanel(new BorderLayout(5,5)) {{
		setBorder(createEmptyBorder(10, 5, 5, 5));
	}};
	JScrollPane sc = new JScrollPane(mainPanel);
	
	JPanel moviePoster = new JPanel(new BorderLayout());
	JButton but = new CustumButton("예매하기");
	JTextArea movieInfor = new JTextArea();
	JScrollPane movieInforSc = new JScrollPane(movieInfor);
	JPanel chatPanel = new JPanel(new BorderLayout());
	Data movie;
	int u_no = 0, m_no = 0;
	public MovieInfor(int u_no, int m_no) {
		this.u_no = u_no; this.m_no = m_no;
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		moviePoster.add(new JLabel());
		borderPanel.add(new NorthPanel(this, u_no), BorderLayout.NORTH);
		borderPanel.add(sc);
		add(borderPanel);
		new A_setFrame(this, "영화 정보", 650, 500);
	}
	
	private ImageIcon getImage(String file, int w, int h) {
		return null;
	}
	public static void main(String[] args) {
		new MovieInfor(1, 1);
	}
}
