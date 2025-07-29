package main;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import java.util.ArrayList;
import java.util.List;

import utils.BaseFrame;
import utils.Query;
import utils.Row;
import utils.sp;

public class C_Detail extends BaseFrame{

	int user = 0;
	int product = 0;
	List<Row> list = new ArrayList<Row>();
	JTextField number = new JTextField();
	JButton[] but = {new JButton("장바구니"), new JButton("구매")};
	C_Detail(int user, int product){
		this.product = product;
		this.user = user;
		setFrame("상세정보", 300, 500, ()->{new AMain(user, -1);});
	}
	@Override
	public void desgin() {
		list = Query.productANDreviewWPNO.select(""+product);
		List<Row> check = Query.chat.select(list.get(0).get(5));
		for(int i = 0 ; i < check.size(); i++) {
			if(check.get(i).get(7) == null) {
				check.get(i).clear();
			}else if(check.get(0).getInt(7) != check.get(i).getInt(7)) {
				check.get(i).clear();
			}
		}
		add(new JPanel(new BorderLayout()) {{
			setBackground(Color.white);
			setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			add(new JLabel() {{
				setBorder(BorderFactory.createEmptyBorder(0,0,0,0));setBorder(BorderFactory.createLineBorder(Color.black));
				setIcon(new ImageIcon(Query.getImge.getImge(product).getScaledInstance(265, 180, Image.SCALE_SMOOTH)));
			}}, BorderLayout.NORTH);
			add(new TrianglePanel() {{setPreferredSize(new Dimension(265, 180));}}, sp.n);
			add(new JPanel(new GridLayout(6,1)) {{
				setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
				setBackground(Color.white);
				add(new JLabel("상품명 : " + list.get(0).getString(1)));
				String str = list.get(0).getString(2), c = "";// str은 문자열 c는 자른 문자열 저장
				for(int i = 0; i <= str.toCharArray().length / 18; i++) {
					if(i == str.toCharArray().length / 18) {c = c + str.substring(i * 18, str.toCharArray().length);
					//빠져 나가기전에 마지막에 못한 거부터 마지막 문자열까지 문자가 35면 18부터 마지막 35까지
					}else {c = c + str.substring(i * 18, (i+1)*18) + "\n";}
					//이건 문자열 자르기
				}
				add(new JTextArea("설명 : " + c) {{setFont(sp.fontM(1,12));}});
				add(new JLabel("카테고리 : " + list.get(0).getString(8)));add(new JLabel("가격 : " + new DecimalFormat("###,###").format(list.get(0).getInt(3))));
				if(list.get(0).get(7) == null) {add(new JLabel("평점 : 0.0"));
				}else {double d = Math.round(Double.parseDouble(list.get(0).getString(7).toString()) * 10) / 10.0; add(new JLabel("평점 : " + d));}
				
				add(new JPanel(new BorderLayout()) {{setBackground(Color.white);add(new JLabel("수량 : "), BorderLayout.WEST);add(number);}});
			}}, BorderLayout.CENTER);
			
			add(new JPanel(new GridLayout(0,2, 10, 0)) {{
				setBackground(Color.white);
				for(JButton j : but) {
					j.setPreferredSize(new Dimension(0, 35));
					add(j);
				}
			}},BorderLayout.SOUTH);
		}}, BorderLayout.CENTER);
	}
	
	@Override
	public void action() {
		for(JButton b : but) {
			b.addActionListener(e->{
				if(e.getSource() == but[0]) {
					try {
						Integer j = Integer.parseInt(number.getText());
						sp.InforMes("장바구니에 등록되었습니다.");
						Query.insert.update(""+user, ""+product, ""+j);
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(this, "수량을 확인하세요.","경고",JOptionPane.ERROR_MESSAGE);
					}
				}
				if(e.getSource() == but[1]) {
					try {
						Integer j = Integer.parseInt(number.getText());
						int i = list.get(0).getInt(4)-j;
						if(i < 0) {
							JOptionPane.showMessageDialog(this, "재고가 부족합니다.", "경고", JOptionPane.INFORMATION_MESSAGE);
						}else {
							JOptionPane.showMessageDialog(this, "구매가 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
							Query.개수update.update(i+"", list.get(0).getString(0));
							Query.orderinsert.update(user+"", list.get(0).getString(0), j+"", LocalDate.now()+"");
						}
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(this, "수량을 확인하세요.","경고",JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
	}
	
	
	public static void main(String[] args) {
		new C_Detail(1, 1);
	}
}
class TrianglePanel extends JPanel {
    @Override
	public void paint(Graphics g) {
        int[] xPoints = {5, 15, 25, 15};
        int[] yPoints = {30, 25, 30, 5};
        int nPoints = 4;

        int[] xPoints2 = {29, 1, 15};
        int[] yPoints2 = {15, 15, 25};
        int nPoints2 = 3; 
        for(int i = 0 ; i < 4; i++) {
        	xPoints[i] += 225;
        }
        for(int i = 0 ; i < 3; i++) {
        	xPoints2[i] += 225;
        }
        g.setColor(Color.RED);
       
        g.drawImage(new ImageIcon("src/img/1.jpg").getImage(), 0,0, 265,180,null);
        g.fillPolygon(xPoints, yPoints, nPoints);
        g.fillPolygon(xPoints2, yPoints2, nPoints2);
    }
}
