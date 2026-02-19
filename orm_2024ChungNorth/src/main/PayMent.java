package main;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

import utils.*;

public class PayMent extends CFrame{
	Font font = new Font("맑은 고딕", 1, 15);
	JPanel borderPanel = new JPanel(new BorderLayout(7,7)) {{
		setBorder(createEmptyBorder(0,10,15,10));
		setBackground(Color.white);
	}};
	Data user = UserU.getUser();
	
	List<Data> list = Connections.select("SELECT p_no, g_name, g_price FROM game_site.shoppingbasket \r\n"
			+ "join gameinformation on gameinformation.g_no = shoppingbasket.g_no\r\n"
			+ "where u_no = ?;", user.get(0));
	int price = 0;
	JButton buy = new JButtonC("결제하기");
	JButton close = new JButtonC("취소");
	public PayMent() {
		add(new JLabel("결제", JLabel.CENTER) {{
			setFont(font.deriveFont(24f));
		}}, BorderLayout.NORTH);
		UIManager.put("Label.font", font);
		setUserInfor();
		setBuyGame();
		setSouthPanel();
		add(borderPanel);
		close.addActionListener(e->{
			dispose();
			new Basket();
		});
		buy.addActionListener(e->{
			getter.mg("결제가 완료되었습니다.\n보유금액 -> " + (user.getInt(6) - price), JOptionPane.INFORMATION_MESSAGE);
			Connections.update("update user set u_price = ? where u_no = ?", user.getInt(6) - price, user.get(0));
			new Main();
			dispose();
		});
		setFrame("결제", 500, 450);
	}
	
	private void setSouthPanel() {
		JPanel panel = new JPanel(new BorderLayout(7, 7));
		panel.setBackground(Color.white);
		
		JPanel butPanel = new JPanel(new GridLayout(0, 2, 3, 3));
		butPanel.setBackground(Color.white);
		
		butPanel.add(buy);
		butPanel.add(close);
		
		panel.add(new JLabel("총포인트 :") {{
			setFont(font.deriveFont(24f));
		}}, BorderLayout.WEST);
		panel.add(new JLabel(getter.df.format(price).toString()) {{
			setFont(font.deriveFont(24f));
		}}, BorderLayout.EAST);
		panel.add(butPanel, BorderLayout.SOUTH);
		
		borderPanel.add(panel, BorderLayout.SOUTH);
	}

	private void setBuyGame() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		panel.setBorder(createMatteBorder(0, 0, 1, 0, Color.black));
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		gridPanel.setBackground(Color.white);
		
		for(int i = 0; i < list.size(); i++) {
			price += list.get(i).getInt(2);
			JLabel l = new JLabel(list.get(i).get(1) + " : " + list.get(i).get(2), JLabel.RIGHT);
			gridPanel.add(l);
		}
		
		panel.add(new JLabel("구매게임"), BorderLayout.WEST);
		panel.add(new JScrollPane(gridPanel) {{
			setBorder(null);
		}});
		borderPanel.add(panel);
	}

	private void setUserInfor() {
		JPanel p = new JPanel(new GridLayout(3, 2));
		p.setBackground(Color.white);
		p.setBorder(createMatteBorder(1,0,1,0, Color.black));
		
		p.add(new JLabel("구매자 :"));
		p.add(new JLabel(user.get(1).toString(), JLabel.RIGHT));
		p.add(new JLabel("아이디 :"));
		p.add(new JLabel(user.get(2).toString(), JLabel.RIGHT));
		p.add(new JLabel("구매일 :"));
		p.add(new JLabel(LocalDate.now().toString(), JLabel.RIGHT));
		
		borderPanel.add(p, BorderLayout.NORTH);
	}

	public static void main(String[] args) {
		new PayMent();
	}
}
