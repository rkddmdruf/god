package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDate;

import utils.BaseFrame;
import utils.ButtonMake;
import utils.Query;

public class UserM extends BaseFrame{
	JPanel main = new JPanel() {{
		this.setLayout(new GridLayout(6,2));
		this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
	}};
	JTextField UC = new JTextField() {{this.setEnabled(false);}};
	JTextField UN = new JTextField();
	JTextField UBD = new JTextField();
	JTextField USN = new JTextField();
	JTextField UHC = new JTextField();
	JTextField UW = new JTextField();
	
	Font font = new Font("맑은 고딕", Font.BOLD, 12);
	ButtonMake up = new ButtonMake("추가", 60, 30, font);
	ButtonMake end = new ButtonMake("닫기", 60, 30, font);
	
	UserM(){
		setFrame("고객 등록", 500, 300, ()->{new Admin();});
	}
	@Override
	public void design() {
		main.add(new JLabel("고객코드:"));
		main.add(UC);
		main.add(new JLabel("*고객명:"));
		main.add(UN);
		main.add(new JLabel("*생년월일(YYYY_MM_DD):"));
		main.add(UBD);
		main.add(new JLabel("*연락처:"));
		main.add(USN);
		main.add(new JLabel("주소:"));
		main.add(UHC);
		main.add(new JLabel("회사:"));
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
				if(UN.getText().equals("") || UBD.getText().equals("") || USN.getText().equals("")) {
					JOptionPane.showMessageDialog(getContentPane(), "필수항목(*)을 모두 입력하세요", "고객등록 에러", JOptionPane.ERROR_MESSAGE);
				}else {
					if(UHC.getText().equals("") && UW.getText().equals("")) {
						Query.upDate("insert into customer values(?, ?, ?, ? , null,  null)", UC.getText(), UN.getText(), UBD.getText(), UBD.getText());
					}else if(UHC.getText().equals("") && !UW.getText().equals("")) {
						Query.upDate("insert into customer values(?, ?, ?, ? , null, ?)", UC.getText(), UN.getText(), UBD.getText(), UBD.getText(), UW.getText());
					}else if(!UHC.getText().equals("") &&UW.getText().equals("")) {
						Query.upDate("insert into customer values(?, ?, ?, ? ,?,  null)", UC.getText(), UN.getText(), UBD.getText(), UBD.getText(),UHC.getText());
					}
					Query.upDate("insert into customer values(?, ?, ?, ? ,?, ?)", UC.getText(), UN.getText(), UBD.getText(), UBD.getText(),UHC.getText(), UW.getText());
					JOptionPane.showMessageDialog(getContentPane(), "고객추가가완료되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
				}
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
}
