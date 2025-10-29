package main;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import utils.*;
import utils.sp.cl;

public class AdminMain extends BaseFrame{
	List<Row> list = Query.AdminMain.select();
	JPanel borderPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(sp.em(10, 15, 10, 15));
	}};
	Timer timer;
	JComboBox<String> cb = new JComboBox<String>("최신순,오래된순".split(","));
	JLabel icon = new JLabel();
	JPanel scrollPanel = new JPanel(new GridLayout(0, 2,15,15)) {{
		setBorder(sp.em(15, 25,15,25));
	}};
	AdminMain(){
		setFrame("관지자 메인", 500, 500, ()->{new Login();timer.stop();});
	}
	@Override
	protected void desing() {
		icon.setIcon(setIcon());
		borderPanel.add(new sp.cp(new BorderLayout(), null, null) {{
			add(new sp.cp(new FlowLayout(FlowLayout.LEFT), null, null) {{  add(cb); }});
			add(icon, sp.e);
		}}, sp.n);
		setScroll(list);
		JScrollPane scroll = new JScrollPane(scrollPanel) {{
			setBackground(Color.white);
			setBorder(BorderFactory.createCompoundBorder(sp.em(10, 0, 0, 0), sp.line));
			this.getVerticalScrollBar().setUnitIncrement(20);
		}};
		borderPanel.add(scroll);
		add(borderPanel);
		SwingUtilities.invokeLater(() -> {
			scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMinimum());
		});
		timer = new Timer(1000, e->{
			icon.setIcon(setIcon());
		});
		timer.start();
	}

	private ImageIcon setIcon() {
		try {
			BufferedImage originalImage = ImageIO.read(getClass().getResource("/icon/insert.png"));
			BufferedImage redImage = new BufferedImage(
	                originalImage.getWidth(),
	                originalImage.getHeight(),
	                BufferedImage.TYPE_INT_ARGB
	        );
			int r = ((int) (Math.random() * 255) + 1), g = ((int) (Math.random() * 255) + 1), b = ((int) (Math.random() * 255) + 1);
			int randomColor = new Color(r, g, b).getRGB();
	        for (int y = 0; y < originalImage.getHeight(); y++) {
	            for (int x = 0; x < originalImage.getWidth(); x++) {
	            	int rgb = originalImage.getRGB(x, y);
	            	if(rgb == -1)redImage.setRGB(x, y, rgb);
	            	else redImage.setRGB(x, y, randomColor);
	            }
	            System.out.println();
	        }
			return new ImageIcon(redImage.getScaledInstance(40, 55, Image.SCALE_SMOOTH));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ImageIcon();
		}
	}
	@Override
	protected void action() {
		icon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new UpLoad();
				timer.stop();
				dispose();
			}
		});
	}
	
	public void setScroll(List<Row> list) {
		for(int j = 0; j < list.size(); j++) {final int i = j;
			scrollPanel.add(new JPanel(new BorderLayout()) {{
				setBackground(list.get(i).getInt(4) == 0 ? Color.white : sp.color);
				setBorder(BorderFactory.createCompoundBorder(sp.line(list.get(i).getInt(4) == 0 ? sp.color : Color.black), sp.em(33, 0, 33, 0)));
				
				add(new sp.cl("(" + list.get(i).getString(0) + ") " + list.get(i).getString(6))
					.fontColor(list.get(i).getInt(4) == 0 ? Color.black : Color.white)
					.font(sp.font(1, 12)), sp.n);
				add(new sp.cl(list.get(i).getString(2))
					.fontColor(list.get(i).getInt(4) == 0 ? Color.black : Color.white)
					.font(sp.font(1, 12)), sp.c);
				
			    /*add(new JLabel("(" + list.get(i).getString(0) + ") " + list.get(i).getString(6)) {{
					setForeground(list.get(i).getInt(4) == 0 ? Color.black : Color.white);
					setFont(sp.font(1, 12));
				}}, sp.n);
				add(new JLabel(list.get(i).getString(2)) {{
					setForeground(list.get(i).getInt(4) == 0 ? Color.black : Color.white);
					setFont(sp.font(1, 12));
				}}, sp.c);
				*/
				
				add(new JTextArea(list.get(i).getString(5)) {{
					setForeground(list.get(i).getInt(4) == 0 ? Color.black : Color.white);
					setFont(sp.font(1, 12));
					setFocusable(false);
					setLineWrap(true);
					setBackground(list.get(i).getInt(4) == 0 ? Color.white : sp.color);
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					this.addMouseListener(new actions(i));
				}}, sp.s);
				this.addMouseListener(new actions(i));
			}});
		}
	}
	class actions extends MouseAdapter{
		int i;
		actions(int i){
			this.i = i;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			new Pass_Yes_No(list.get(i).getInt(3),list.get(i).getInt(0),list.get(i).getString(2), list.get(i).getInt(1));
			timer.stop();
			dispose();
		}
	}
	public static void main(String[] args) {
		new AdminMain();
	}
}
