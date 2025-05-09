package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import utils.BaseFrame;
import utils.ButtonMake;
import utils.Query;
import utils.Row;
public class Main extends BaseFrame{
	JLabel title = new JLabel("관리자 로그인", JLabel.CENTER) {{
		this.setFont(new Font("맑은 고딕", Font.BOLD, 24));
	}};
	
	JPanel namePw = new JPanel() {{
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));
		}};
	
	JTextField name = new JTextField() {{
		this.setPreferredSize(new Dimension(100, 25));
	}};
	JPasswordField pw = new JPasswordField() {{
		this.setPreferredSize(new Dimension(100, 25));
	}};
	
	Font font = new Font("맑은 고딕", Font.BOLD, 12);
	ButtonMake check = new ButtonMake("확인", 60, 30, font);
	ButtonMake end = new ButtonMake("종료", 60, 30, font);
	JPanel butn = new JPanel();
	Main() {
		setFrame("로그인", 400, 250, ()-> {});
	}
	
	@Override
	public void design() {
		add(title, BorderLayout.NORTH);
		
		namePw.add(new JPanel() {{ 
			this.setLayout(new BorderLayout(25,10)); 
			this.add(new JLabel("이름"), BorderLayout.WEST);
			this.add(name);
		}}, BorderLayout.NORTH);
		
		namePw.add(new JPanel() {{ 
			this.setLayout(new BorderLayout()); 
			this.add(new JLabel("비밀번호"), BorderLayout.WEST);
			this.add(pw);
		}}, BorderLayout.SOUTH);
		add(namePw, BorderLayout.CENTER);
		
		butn.add(check);
		butn.add(end);
		add(butn,BorderLayout.SOUTH);
	}
	
	@Override
	public void action() {
		end.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {dispose();}
		});
		check.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Row> list = Query.select("select * from company_선수번호.admin where name = ? and passwd = ?", 
						name.getText(), new String(pw.getPassword()));
				if(!list.isEmpty()) {
					dispose();
					new Admin();
				}
			}
		});
	}
	public static void main(String[] args) {
		new Main();
	}
}
