package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import utils.*;
import utils.sp.*;

public class AdminMain extends BaseFrame{
	JPanel borderPanel = new cp(new BorderLayout(20,20), sp.em(10, 10, 10, 10), null);
	JPanel northPanel = new cp(new BorderLayout(), sp.em(0, 10,0, 10) ,null);
	JPanel mainPanel = new cp(new GridLayout(0,2,20,20), sp.em(20, 25, 20, 25), sp.gray);
	List<Row> list = new ArrayList<>();
	JComboBox<String> order = new JComboBox<String>("최신순, 오래된순".split(", ")) {{
		setPreferredSize(new Dimension(100, 25));
	}};
	JLabel l = new cl("");
	Timer t = new Timer(1000, e->{
		setColor();
	});
	AdminMain(){
		setFrame("관리자 메인", 525, 525, ()->{t.stop();new Login();});
	}
	@Override
	protected void desing() {
		System.out.println(list);
		northPanel.add(new cp(new BorderLayout(), null, null) {{
			add(order, sp.s);
		}}, sp.w);
		northPanel.add(l, sp.e);
		borderPanel.add(northPanel,sp.n);
		setPanel();
		borderPanel.add(new JScrollPane(mainPanel));
		add(borderPanel);
		t.start();
		setColor();
	}

	@Override
	protected void action() {
		order.addItemListener(e->{
			setPanel();
			rp();
		});
	}
	private void setPanel() {
		if(order.getSelectedIndex() == 0)
			list = Query.selectText("select apply.*, job.jname, user.uname from apply"
					+ " join job on job.jno = apply.jno join user on user.uno = apply.uno order by apdate, apno");
		else
			list = Query.selectText("select apply.*, job.jname, user.uname from apply "
					+ "join job on job.jno = apply.jno join user on user.uno = apply.uno order by apdate desc, apno desc");
		mainPanel.removeAll();
		for(int i = 0; i < list.size(); i++) {final int index = i;
			MouseAdapter m = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new PullOut(list.get(index).getInt(0), list.get(index).getInt(1), list.get(index).getInt(3));
					dispose();
				}
			};
			mainPanel.add(new cp(new BorderLayout(), sp.com(sp.line(list.get(index).getInt(4) == 0 ? sp.color : Color.black)
					, sp.em(25, 0, 25, 0)), list.get(index).getInt(4) == 0 ? Color.white : sp.color) 
			{{
				addMouseListener(m);
				add(new ca("(" + list.get(index).getInt(0) + ") "  + list.get(index).getString(6) + "\n\n" + list.get(index).getString(2)) {{
					setBackground(list.get(index).getInt(4) == 0 ? Color.white : sp.color);
					setForeground(list.get(index).getInt(4) == 0 ? Color.black : Color.white);
					addMouseListener(m);
				}}.setting().font(sp.font(1, 12)));
				add(new ca(list.get(index).getString(5)) {{
					setBackground(list.get(index).getInt(4) == 0 ? Color.white : sp.color);
					setForeground(list.get(index).getInt(4) == 0 ? Color.black : Color.white);
					addMouseListener(m);
				}}.setting().font(sp.font(0, 12)), sp.s);
			}});
		}
		rp();
	}
	private void setColor() {
		try {
			BufferedImage originalImage = ImageIO.read(new File("datafiles/icon/insert.png"));
			BufferedImage redImage = new BufferedImage(
	                originalImage.getWidth(),
	                originalImage.getHeight(),
	                BufferedImage.TYPE_INT_ARGB
	        );
			Color c = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
			for(int x = 1; x < originalImage.getHeight(); x++) {
				for(int y = 1; y < originalImage.getWidth(); y++) {
					if(originalImage.getRGB(x, y) != -1)
						redImage.setRGB(x, y, c.getRGB());
					else
						redImage.setRGB(x, y, Color.white.getRGB());
				}
			}
			l.setIcon(new ImageIcon(new ImageIcon(redImage).getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		rp();
	}
	public static void main(String[] args) {
		new AdminMain();
	}
}
