package main;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class MainPom extends JFrame{
	JPanel jpanel = new JPanel(new BorderLayout());
	JPanel jpanelp = new JPanel();
	JPanel jpanelc = new JPanel();
	JPanel jpanelm = new JPanel(new FlowLayout());
	
	JLabel ClothingStore = new JLabel("ClothingStore");
	JTextField 검색 = new JTextField();
	Dimension 검색창사이즈= new Dimension(300, 30);
	JButton b1 = new JButton("검색");
	JButton b2 = new JButton("로그인");
	Dimension pSize = new Dimension(120, 60);
	
	public MainPom(int check)
	{
		setBounds(460, 240, 1000, 600);
		Container f = getContentPane();
		ClothingStore.setFont(new Font("", Font.BOLD, 30));
		b1.setBackground(Color.blue);
		b1.setForeground(Color.white);
		b2.setBackground(Color.blue);
		b2.setForeground(Color.white);
		
		setTitle("테스트");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBackground(Color.white);
		setLayout(null);
		
		//가장위에 검색 로그인
		jpanelm.setBounds(20, 0, 950, 60);
		jpanelm.setBackground(Color.white);
		
		jpanelm.add(ClothingStore);
		검색.setPreferredSize(검색창사이즈);
		jpanelm.add(검색);
		b1.addActionListener(new MyListener());
		ImageIcon imges = new ImageIcon("imgs/유저이미지/1.png");
		Image img = imges.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); 
		JLabel ll = new JLabel(new ImageIcon(img));
		JLabel nameMoney = new JLabel("");
		try {
			Connection c = DriverManager.getConnection(UUP.url(), UUP.username(), UUP.password());
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from clothingstore.user");
			while(rs.next()) {
				if(rs.getInt("u_no") == check) {
					break;
				}
			}

			String str = rs.getString("u_name") + System.lineSeparator() + rs.getInt("u_price");
			nameMoney.setText(str);
			c.close();
			s.close();
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		jpanelm.add(b1);
		jpanelm.add(ll);
		jpanelm.add(nameMoney);

		

		//카테고리, 상품 틀
		jpanel.setBounds(20, 60, 950, 480);
		
		//상품
		jpanelp.setBorder(BorderFactory.createLineBorder(Color.black));
		jpanelp.setBackground(Color.white);
		
		//카테고리
		jpanelc.setBackground(Color.white);
		jpanelc.setBorder(BorderFactory.createLineBorder(Color.black));
		jpanelc.setPreferredSize(pSize);
		
		
		
		jpanel.add(jpanelc, BorderLayout.WEST);
		jpanel.add(jpanelp,BorderLayout.CENTER);
		add(jpanelm);
		add(jpanel);
		
		
		setSize(1000, 600);
		setResizable(false);
		setVisible(true);
//		ImageIcon imges = new ImageIcon("imgs/상품이미지/1.jpg");
//		Image img = imges.getImage().getScaledInstance(100, 110, Image.SCALE_SMOOTH); 
//		JLabel ll = new JLabel(new ImageIcon(img));
//		
//		//jpanel.setPreferredSize(pSize);
//		jpanel.add(ll, BorderLayout.CENTER);
//		jpanel.add(new Label("ABCDEFGHIJKLNMOP"), BorderLayout.SOUTH);
//		jpanel.setBorder(BorderFactory.createLineBorder(Color.black));
//		
//		jpanelp.add(jpanel);
		

	}
class MyListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == b2) {
			setVisible(false);
			new Login();
		}
	}
}

public static void main(String[] args) {
	new MainPom(1);
}
}
