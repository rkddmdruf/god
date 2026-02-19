package main;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import utils.*;

public class Basket extends CFrame{
	JLabel delete = new JLabel(getter.getImage("X.png", 40,  40)) {{
		setBounds(50, 50, 50, 50);
	}};
	
	Font font = new Font("맑은 고딕", 1, 25);
	JPanel mainPanel = new JPanel(new GridLayout(0, 2, 3, 3)) {{
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(mainPanel) {{
		setPreferredSize(new Dimension(0, 300));
		getVerticalScrollBar().setUnitIncrement(20);
	}};
	JPanel inforAndMainPanel = new JPanel(new BorderLayout(7, 7)) {{
		setBackground(Color.white);
	}};
	JTextArea infor = new JTextArea() {{
		setFont(font.deriveFont(17f));
		setLineWrap(true);
		setFocusable(false);
		setCursor(Cursor.getDefaultCursor());
		setBorder(createLineBorder(Color.black));
	}};
	
	JPanel southPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(7,7,7,7));
	}};
	JLabel priceLabel = new JLabel("") {{
		setFont(font);
	}};
	JButton buy = new JButtonC("구매") {{
		setFont(font.deriveFont(16f));
		setPreferredSize(new Dimension(125, 40));
	}};
	int price = 0;
	Data user = UserU.getUser();
	List<Data> list = new ArrayList<>();
	public Basket() {
		UIManager.put("Label.font", font.deriveFont(18f));
		setMainPanel();
		inforAndMainPanel.add(sc, BorderLayout.NORTH);
		inforAndMainPanel.add(infor);
		add(inforAndMainPanel);
		setSouthPanel();
		UIManager.put("Label.font", font.deriveFont(13f));
		buy.addActionListener(e->{
			if(list.isEmpty()) {
				getter.mg("장바구니에 있는 게임이 없습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int result = JOptionPane.showConfirmDialog(null, "게임들을 구매하시겠습니까?\n" + priceLabel.getText(), "구매", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.NO_OPTION) return;
			if(user.getInt(6) >= price) {
				//결재폼
				dispose();
				return;
			}

			int result2 = JOptionPane.showConfirmDialog(null, "포인트가 부족하여 구매할 수 없습니다.\n"
					+ "포인트를 충전하시겠습니까? (충전 필요 포인트 : " + (getter.df.format(price - user.getInt(6))) + "포인트)"
					, "충전 필요", JOptionPane.YES_NO_OPTION);
			if(result2 == JOptionPane.NO_OPTION) return;
			//new 충전폼
			dispose();
			return;
		});
		setFrame("장바구니", 850, 500);
	}
	
	private void setSouthPanel() {
		southPanel.add(priceLabel, BorderLayout.WEST);
		southPanel.add(buy, BorderLayout.EAST);
		
		add(southPanel, BorderLayout.SOUTH);
	}

	private void setPrice() {
		priceLabel.setText("총 포인트 : " + getter.df.format(price));
	}
	
	List<Boolean> bool = new ArrayList<>();
	List<JPanel> panels = new ArrayList<>();
	int beforeSelectPanel = 0;
	private void setMainPanel() {
		mainPanel.removeAll();
		panels.clear();
		bool.clear();
		price = 0;
		list = Connections.select("SELECT p_no, gameinformation.g_no, g_name, g_price, g_lebu FROM game_site.shoppingbasket \r\n"
				+ "join gameinformation on gameinformation.g_no = shoppingbasket.g_no\r\n"
				+ "where u_no = ?;", user.get(0));
		
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			bool.add(false);
			Data data = list.get(i);
			price += data.getInt(3);
			
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			p.setBorder(createMatteBorder(2, 2, 2, 2, Color.black));
			p.setPreferredSize(new Dimension(0, 150));
			
			JLabel img = new JLabel(getter.getImage("gameimage/" + data.getInt(1) + ".jpg", 150, 150)) {
				@Override
				protected void paintComponent(Graphics g) {
					g.clearRect(0, 0, getWidth(), getHeight());
					super.paintComponent(g);
					if(bool.get(index)) {
						g.drawImage(new ImageIcon("datafiles/X.png").getImage(), 0, 0, 40, 40, null);
					}
				}
			};
			img.setPreferredSize(new Dimension(150,150));
			
			JPanel inforPanel = new JPanel(new GridLayout(2, 1));
			inforPanel.setBackground(Color.white);
			inforPanel.setBorder(createEmptyBorder(5,5,80, 5));
			
			inforPanel.add(new JLabel("게임명 : " + data.get(2)));
			inforPanel.add(new JLabel("포인트 : " + getter.df.format(data.getInt(3)) + "포인트"));
			
			p.add(inforPanel);
			p.add(img, BorderLayout.WEST);
			panels.add(p);
			mainPanel.add(p);
		}
		setPrice();
		
		for (int i = 0; i < list.size(); i++) {
			final int index = i;
			panels.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					setboool(index, true);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					setboool(index, false);
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getX() >= 0 && e.getX() <= 40 && e.getY() >= 0 && e.getY() <= 40) {
						getter.mg("장바구니에서 삭제가 완료되었습니다.", JOptionPane.INFORMATION_MESSAGE);
						Connections.update("delete from shoppingbasket where p_no = ?", list.get(index).get(0));
						infor.setText("");
						setMainPanel();
						return;
					}
					panels.get(beforeSelectPanel).setBorder(createMatteBorder(2, 2, 2, 2, Color.black));
					panels.get(beforeSelectPanel = index).setBorder(createMatteBorder(2, 2, 2, 2, Color.red));
					infor.setText(list.get(index).get(4).toString());
				}
			});
		}
		revalidate();
		repaint();
	}
	
	private void setboool(int i, boolean b) {
		bool.set(i, b);
		revalidate();
		repaint();
	}
	public static void main(String[] args) {
		new Basket();
	}
}
