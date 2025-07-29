package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;

import utils.Query;
import utils.Row;

public class CB_UserUPDATE extends C_UserUPandUPDATE{

	public CB_UserUPDATE(String...string) {
		super("고객 수정", 1);
		List<Row> list = Query.UserUpdateSelect.select(string[0],string[1]);
		for(int i = 0; i < 6; i++) {TxtF[i].setText(list.get(0).get(i).toString());}
		up.setText("수정");end.setText("취소");
	}
	@Override
	public void action() {
		super.action();
		up.addActionListener(e->{
			Query.UserUpdate.updata(TxtF[2].getText(), TxtF[3].getText(), TxtF[4].getText(), TxtF[5].getText(), TxtF[0].getText(), TxtF[1].getText());
			JOptionPane.showMessageDialog(getContentPane(), "고객수정이 완료되었습니다.", "메시시", JOptionPane.INFORMATION_MESSAGE);
		});
		end.addActionListener(e->{
			dispose();
		});
	}
}
