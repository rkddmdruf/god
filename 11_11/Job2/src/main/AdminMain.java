package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import utils.BaseFrame;
import utils.Query;
import utils.Row;
import utils.sp;
import utils.sp.cl;
import utils.sp.cp;
import utils.sp.cta;

public class AdminMain extends BaseFrame{
	List<Row> list = new ArrayList<>();
	JComboBox<String> cb = new JComboBox<String>("최신순   , 오래된순   ".split(", "));
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(10, 10, 10, 10), null);
	JLabel upload = new JLabel(sp.getImg("icon/insert.png", 50, 50)) {{ setVerticalAlignment(JLabel.TOP); }};
	JPanel scrollPanel = new cp(new GridLayout(0, 2, 15,15), sp.em(20, 25, 20, 25), sp.gray);
	AdminMain(){
		setFrame("관리자 메인", 450, 475, ()->{});
	}
	@Override
	protected void desing() {
		Timer timer = new Timer(1000, e2->{ setIcons(); }); timer.start();
		addWindowListener(new WindowAdapter() { @Override public void windowClosed(WindowEvent e) { timer.stop(); } });
		borderPanel.add(new sp.cp(new BorderLayout(), sp.em(0, 5, 10, 5), null) {{
			add(new cp(new FlowLayout(FlowLayout.LEFT, 0, 20), null, null) {{ add(cb); }}, sp.w);
			add(upload, sp.e);
		}}, sp.n);
		borderPanel.add(new JScrollPane(scrollPanel) {{
			getVerticalScrollBar().setUnitIncrement(20);
		}});
		setPanel();
		add(borderPanel);
	}

	private void setPanel() {
		list.clear();
		scrollPanel.removeAll();

		String string = "SELECT job.jno, apply.apno, user.uname, apply.apdate, job.jname, apply.apok FROM parttimecat.apply "
				+ "left join user on user.uno = apply.uno left join job on job.jno = apply.jno ";
		list = Query.selectText(string + (cb.getSelectedIndex() == 1 ? " order by apno desc" : ""));
		
		for(Row row : list) {
			scrollPanel.add(new sp.cp(new BorderLayout(), sp.com(sp.line((row.getInt(5) == 0 ? sp.color :Color.black)), sp.em(20, 0, 20, 0)), (row.getInt(5) == 0 ? null : sp.color))
			{{
				MouseAdapter m = new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { new Pass(row.getInt(1)); } };
				addMouseListener(m);
				setPreferredSize(new Dimension(150, 150));
				Color color = row.getInt(5) == 0 ? Color.black : Color.white;
				add(new sp.cl("(" + row.getString(1) + ") " + row.getString(2)).font(sp.font(1, 13)).fontColor(color), sp.n);
				add(new sp.cl(row.getString(3)) {{ setOpaque(false); }}.font(sp.font(1, 13)).fontColor(color));
				add(new sp.cta(row.getString(4)) {{ setOpaque(false); addMouseListener(m);}}.setting().font(sp.font(0, 12)).fontColor(color),sp.s);
			}});
		}
		SwingUtilities.invokeLater(() -> { ((JScrollPane) borderPanel.getComponent(1)).getVerticalScrollBar().setValue(0); });
		repaints();
	}
	private void setIcons() {
		try {
			BufferedImage originalImage = ImageIO.read(new File("icon/insert.png"));
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
	        }
	        upload.setIcon(new ImageIcon(new ImageIcon(redImage).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	protected void action() {
		cb.addItemListener(e->{
			setPanel();
		});
	}
	public static void main(String[] args) {
		new AdminMain();
	}

}
