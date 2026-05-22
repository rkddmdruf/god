package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import utils.Connections;
import utils.Data;
import utils.User;
import utils.getter;

public class ShoppingPanel extends JPanel{
	
	JPanel panel = new JPanel(new GridLayout(0, 3, 10, 10)) {{
		setBackground(getter.color);
	}};
	
	JScrollPane sc = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
		JScrollPane s = this;
		setBorder(null);
		setBackground(getter.color);
		addMouseWheelListener(e -> {
			s.getVerticalScrollBar().setValue(s.getVerticalScrollBar().getValue() + (e.getWheelRotation() * 40));
		});
	}};
	
	List<Integer> snoList = new ArrayList<>();
	
	Main1 main;
	public ShoppingPanel(Main1 main1) {
		main = main1;
		setBackground(getter.color);
		setLayout(new BorderLayout(10, 10));
		add(new JLabel("장바구니") {{
			setFont(new Font("맑은 고딕", 1, 14));
			setForeground(Color.white);
		}}, BorderLayout.NORTH);
		add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(getter.color);
			setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
			JButton but = new JButton("선택한 게임구매") {{
				setBackground(new Color(0, 150, 0));
				setForeground(Color.white);
			}};
			but.addActionListener(e -> {
				if(snoList.isEmpty()) {
					getter.err("선택한 게임이 없습니다.");
					return;
				}
				Data user = User.getUser();
				
				String str = String.join(", ", snoList.stream().map(c -> c.toString()).collect(Collectors.toList()));
				List<Data> list = Connections.select("SELECT game.* FROM game.shopping\r\n"
						+ " join game on game.gno = shopping.gno\r\n"
						+ "	where sno in(" + str + ");");
				if(JOptionPane.showConfirmDialog(null
						, "게임을 구매하시겠습니까?"
						+ "\n보유 잔액 : " + getter.df.format(user.getInt(5)) + "원"
						+ "\n가격 : " + getter.df.format(list.stream().mapToInt(c -> c.getInt(4)).sum()) + "원"
						
						, "구매"
						, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					Connections.update("delete from shopping where sno in (" + str + ");");
					for(Data data : list)
						Connections.update("insert into buygame values(0, ?, ?, ?)", data.getInt(0), user.getInt(0), LocalDate.now());
					getter.infor("구매가 완료되었습니다.");
					user.set(5, user.getInt(5) - list.stream().mapToInt(c -> c.getInt(4)).sum());
					User.setUser(user);
					Connections.update("update user set price = ? where uno = ?", user.get(5), user.get(0));
					main.setMainPanel();
					return;
				}
			});
			add(but);
		}}, BorderLayout.SOUTH);
		setPanel();
		add(sc);
		revalidate();
		repaint();
	}
	
	private void setPanel() {
		
		List<Data> list = Connections.select("SELECT game.*, sno FROM game.shopping \r\n"
				+ "join game on game.gno = shopping.gno\r\n"
				+ "where uno = ?;", User.getUser().get(0));
		System.out.println(list);
		if(list.isEmpty()) {
			panel.setLayout(new BorderLayout());
			panel.add(new JLabel("장바구니에 게임이 없습니다.", JLabel.CENTER) {{
				setForeground(Color.white);
				setFont(new Font("맑은 고딕", 1, 20));
			}});
			return;
		}
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(getter.color);
			p.setBorder(BorderFactory.createLineBorder(Color.white));
			p.setPreferredSize(new Dimension(0, 100));
			p.add(new JLabel(data.getString(1)) {{
				setForeground(Color.white);
				setFont(new Font("맑은 고딕", 0, 15));
				while(getFontMetrics(getFont()).stringWidth(getText()) > (800 - 20) / 3) {
					setFont(getFont().deriveFont((float) (getFont().getSize() - 1)));
				}
				setPreferredSize(new Dimension(0, 25));
			}}, BorderLayout.NORTH);
			p.add(new JLabel(getter.getImage("datafiles/games/" + data.getInt(0) + ".jpg", 150, 75)) {{
				addMouseListener(new MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent e) {
						if(e.getButton() == MouseEvent.BUTTON3) {
							if(JOptionPane.showConfirmDialog(null, "장바구니에서 삭제하시겠습니까?", "삭제", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
								Connections.update("delete from shopping where sno = ?;", data.getInt(data.size() - 1));
								snoList.remove(snoList.indexOf(data.getInt(data.size() - 1)));
								panel.remove(p);
								panel.revalidate();
								panel.repaint();
							}
							
						}
					};
				});
			}}, BorderLayout.WEST);
			
			JPanel priceCheckPanel = new JPanel(new BorderLayout());
			priceCheckPanel.setBackground(getter.color);
			priceCheckPanel.add(new JCheckBox() {{
				setSelected(true);
				snoList.add(data.getInt(data.size() -1));
				setBackground(getter.color);
				setVerticalAlignment(JLabel.BOTTOM);
				addActionListener(e -> {
					if(isSelected())
						snoList.add(data.getInt(data.size() - 1));
					else
						snoList.remove(snoList.indexOf(data.getInt(data.size() - 1)));
				});
			}}, BorderLayout.EAST);
			priceCheckPanel.add(new JLabel(" " + getter.df.format(data.getInt(4)) + "원") {{
				setForeground(Color.white);
				setVerticalAlignment(JLabel.TOP);
			}});
			p.add(priceCheckPanel);
			panel.add(p);
		}
		for(int i = 0; i < 18 - list.size(); i++) {
			panel.add(new JLabel(""));
		}
	}

}
