package main;

import javax.swing.*;
import java.awt.*;
public class F_Roupang extends JPanel{
	String[] str = "전체,결제 완료,배송준비,배송중,배송 완료".split(",");
	JComboBox<String> cb = new JComboBox<>(str) {{setPreferredSize(new Dimension(100, 30));}};
	F_Roupang(JPanel p, int user){
		setLayout(new BorderLayout());
		setBackground(Color.white);
		setLayout(new BorderLayout());
		add(new JPanel(new BorderLayout()) {{
			add(new JLabel("장바구니") {{setFont(setBoldFont(24));}}, BorderLayout.WEST);
			add(cb, BorderLayout.EAST);
		}},BorderLayout.NORTH);
		p.add(this, "P5");
	}
	

	private Font setBoldFont(int i) {
		
		return new Font("맑은 고딕", Font.BOLD, i);
	}
}
