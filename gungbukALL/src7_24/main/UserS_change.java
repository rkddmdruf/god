package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;
import utils.BaseFrame;
import utils.ButtonMake;
import utils.Query;
import utils.Row;

public class UserS_change extends BaseFrame{
	JPanel main = new JPanel() {{
		this.setLayout(new GridLayout(6,2));
		this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
	}};
	JTextField UC = new JTextField() {{this.setEnabled(false);}};
	JTextField UN = new JTextField() {{this.setEnabled(false);}};
	JTextField UBD = new JTextField();
	JTextField USN = new JTextField();
	JTextField UHC = new JTextField();
	JTextField UW = new JTextField();
	
	Font font = new Font("맑은 고딕", Font.BOLD, 12);
	ButtonMake up = new ButtonMake("수정", 60, 30, font);
	ButtonMake end = new ButtonMake("닫기", 60, 30, font);
	String str;
	UserS_change(String str){
		this.str = str;
		setFrame("고객 수정", 500, 300, ()->{});
	}
	@Override
	public void design() {
		List<Row> list = Query.select("select * from customer where name = ?", str);
		UC.setText(list.get(1).getString(0)); UN.setText(list.get(1).getString(1)); UBD.setText(list.get(1).getString(2));
		USN.setText(list.get(1).getString(3)); UHC.setText(list.get(1).getString(4)); UW.setText(list.get(1).getString(5));
		main.add(new JLabel("고객코드 :"));
		main.add(UC);
		main.add(new JLabel("고객명 :"));
		main.add(UN);
		main.add(new JLabel("생년월일 :"));
		main.add(UBD);
		main.add(new JLabel("연락처 :"));
		main.add(USN);
		main.add(new JLabel("주소 :"));
		main.add(UHC);
		main.add(new JLabel("회사명 :"));
		main.add(UW);
		add(main, BorderLayout.CENTER);
		add(new JPanel() {{this.add(up); this.add(end);}}, BorderLayout.SOUTH);
		
	}
	
	@Override
	public void action() {
		end.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) {dispose();}});
		up.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int Y_N = -1;
				try {
					Query.upDate("update customer set code = ?, birth = ?, tel = ?, address = ?, company = ? where name= ?",
							UC.getText(), UBD.getText(), USN.getText(), UHC.getText(), UW.getText(), str);
				} catch (Exception e1) {
					Y_N = Y_N * -1;
				}
				if(Y_N == -1) {JOptionPane.showMessageDialog(getContentPane(), "고객 수정이 완료되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);}
				else {JOptionPane.showMessageDialog(getContentPane(), "입력을 확인해주세요", "고객수정 에러", JOptionPane.ERROR_MESSAGE);}
			}
		});
		UBD.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getExtendedKeyCode() == 10) {
					String str = UBD.getText();
					String YMD = "S" + LocalDate.now().toString().substring(2, 4) +( Integer.parseInt(str.substring(0, 4)) + 
							Integer.parseInt(str.substring(5, 7)) + Integer.parseInt(str.substring(8, 10)));
					UC.setText(YMD);
				}
			}
		});
	}
	public static void main(String[] args) {
		new UserS_change("권우진");
	}
}
