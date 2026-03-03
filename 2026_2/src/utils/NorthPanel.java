package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import main.Search;

public class NorthPanel extends JPanel{
	public static String text = "";
	Font f = new Font("맑은 고딕", 1, 10);
	
	JTextField tf = new JTextField();
	JLabel serch = new JLabel(getter.getImageIcon("datafiles/icon/search.png", 30, 30));
	
	JLabel l1 = new JLabel("자격증 목록", JLabel.CENTER) {{
		setFont(f);
	}};
	
	JLabel l2 = new JLabel("시험 일정", JLabel.CENTER) {{
		setFont(f);
	}};
	public static int vera = JLabel.BOTTOM;
	JFrame frame;
	public NorthPanel(JFrame f) {
		frame = f;
		serch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				text = tf.getText();
				if(Connections.select("SELECT * FROM lecture.certi where cname like ?;", "%" + text + "%").isEmpty()) {
					getter.mg("해당하는 자격증이 존재하지 않습니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
				new Search(-1, text);
				f.dispose();
			}
		});
		setLayout(new BorderLayout(15, 15));
		setBackground(Color.white);
		
		tf.setBorder(null);
		tf.setPreferredSize(new Dimension(300, 30));
		
		JPanel tfPanel = new JPanel(new BorderLayout()){ 
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.cyan);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawRoundRect(0, 0, getWidth()-1, getHeight()- 2, 25,25);
			}
		};
		tfPanel.setBackground(Color.white);
		tfPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		tfPanel.setPreferredSize(new Dimension(305, 35));
		tfPanel.add(tf);
		
		JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		mainPanel.setBackground(Color.white);
		
		mainPanel.add(new JLabel(getter.getImageIcon("datafiles/icon/logo.png", 40, 40)));
		mainPanel.add(new JLabel("Skills Qualification Association") {{
			setPreferredSize(new Dimension(190, 40));
			setVerticalAlignment(vera);
			setFont(new Font("맑은 고딕", 0, 13));
		}});
		mainPanel.add(tfPanel);
		mainPanel.add(serch);
		
		add(mainPanel, BorderLayout.NORTH);
		add(new JPanel(new GridLayout(0, 2, 0, 0)) {{
			setBackground(Color.white);
			add(new JPanel() {{ add(l1); setBackground(Color.white); }});
			add(new JPanel() {{ add(l2); setBackground(Color.white); }});
		}});
		
	}
}
