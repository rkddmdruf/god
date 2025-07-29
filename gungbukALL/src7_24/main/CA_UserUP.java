package main;

import javax.swing.JOptionPane;

import utils.Query;

public class CA_UserUP extends C_UserUPandUPDATE{
	
	public CA_UserUP() {
		super("고객 등록",0);
		TxtF[1].setEnabled(true);
		for(int i = 1; i <= 3; i++) {
			Label[i].setText("*"+str[i]+" :");
		}
		Label[2].setText("*"+str[2]+"(YYYY-MM-DD) :");
	}
	@Override
	public void action() {
		super.action();
		up.addActionListener(e->{
			if(!TxtF[1].getText().equals("") && !TxtF[2].getText().equals("") && !TxtF[3].getText().equals("")) {
				Query.UserUP.updata(TxtF[0].getText(),TxtF[1].getText(),TxtF[2].getText(),TxtF[3].getText(),TxtF[4].getText(),TxtF[5].getText());
				JOptionPane.showMessageDialog(getContentPane(), "고객추가가 완료되었습니다.", "메시지",JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(getContentPane(), "필수항목(*)을 모두 입력하세요", "고객등록 에러",JOptionPane.ERROR_MESSAGE);
			}
		});
		end.addActionListener(e->{
			dispose();
			new B_Admin();
		});
	}
}
