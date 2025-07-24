package main;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import utils.BaseFrame;
import utils.Query;
import utils.Row;
import utils.sp;

public class BLogin extends BaseFrame{

	JTextField id = new JTextField();
	JPasswordField pw = new JPasswordField();
	JButton login = new JButton("로그인");
	
	public BLogin() {
		setFrame("로그인", 300, 250, ()->{});
	}
	
	@Override
	public void desgin() {
		UIManager.put("OptionPane.background", new ColorUIResource(Color.white));
		UIManager.put("Panel.background", new ColorUIResource(Color.white));
		getContentPane().setBackground(Color.white);
		add(new JPanel() {{
			setBackground(Color.white);
			add(new JLabel("Roupang") {{
				setFont(sp.fontM(1,30));
				setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
			}});
		}}, BorderLayout.NORTH);
		add(new JPanel(new BorderLayout()) {{
			add(new JPanel(new GridLayout(2,2,-100,10)) {{
				setBorder(BorderFactory.createEmptyBorder(0,30,0,30));setBackground(Color.white);
				add(new JLabel("ID  ") {{setFont(sp.fontM(1,20));}},BorderLayout.WEST);add(id);
				add(new JLabel("PW") {{setFont(sp.fontM(1,20));}},BorderLayout.WEST);add(pw);
			}}, BorderLayout.CENTER);
			add(new JPanel(new BorderLayout()) {{
				setBackground(Color.white);
				add(login);setBorder(BorderFactory.createEmptyBorder(10,30, 40, 30));
			}}, BorderLayout.SOUTH);
		}}, BorderLayout.CENTER);
	}

	@Override
	public void action() {
		login.addActionListener(e->{
			if(id.getText().equals("") || pw.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "빈칸이 있습니다.", "경고", JOptionPane.ERROR_MESSAGE);
			}else {
				List<Row> list = Query.Login.select(id.getText(), pw.getText());
				if(list.isEmpty()) {
					if(id.getText().equals("admin") && pw.getText().equals("1234")) {
						JOptionPane.showMessageDialog(this, "관리자님 환영합니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
						new AMain(1119, -1);
						dispose();
					}else {
						JOptionPane.showMessageDialog(this, "없는 회원입니다.", "경고", JOptionPane.ERROR_MESSAGE);
						id.setText("");pw.setText("");id.requestDefaultFocus();
					}
				}else {
					List<Row> orderC = Query.LoginOrder.select(list.get(0).getString(0));
					if(orderC.isEmpty()) {
						new AMain(list.get(0).getInt(0), -1);
					}else {
						List<Row> counts = Query.LoginJOp.select(list.get(0).getString(0));
						int result = JOptionPane.showConfirmDialog(this, 
								list.get(0).getString(3)+"님이 가장 많이 구매한 카테고리는 " + counts.get(0).getString(2) + "입니다.\n 해당 카테고리상품을 확인하시겠습니까?",
								"확인질문",JOptionPane.YES_NO_OPTION);
						if(result == JOptionPane.YES_OPTION) {
							new AMain(list.get(0).getInt(0), counts.get(0).getInt(1));
						}else if(result == JOptionPane.NO_OPTION) {
							new AMain(list.get(0).getInt(0), -1);
						}
						
					}
					dispose();
				}
				
			}
		});
	}
	public static void main(String[] args) {
		new BLogin();
	}
	/*setBorder(BorderFactory.createEmptyBorder(10,30,40,30));
			add(new JPanel(new BorderLayout()) {{
				add(new JLabel("ID  ") {{setFont(setBoldFont(20));}},BorderLayout.WEST);
				add(id);
			}},BorderLayout.NORTH);
			add(new JPanel(new BorderLayout()) {{
				add(new JLabel("PW") {{setFont(setBoldFont(20));}},BorderLayout.WEST);
				add(pw);
			}},BorderLayout.SOUTH);*/
}
