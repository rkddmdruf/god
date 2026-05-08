package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import builder.SimpleJarBuilder2;
import utils.CFrame;
import utils.getter;

public class Installer extends CFrame{
	
	JPanel panel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
	}};
	
	double bar = 3.7;
	int barGuage = 0;
	
	Thread thread;
	JButton button = new JButton("설치") {{
		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(Color.black));
		setPreferredSize(new Dimension(80, 25));
	}};
	
	JCheckBox checkBox = new JCheckBox("바로 실행") {{
		setBackground(Color.white);
	}};
	JLabel installInfor = new JLabel();
	public Installer() {
		borderPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		panel.add(new JLabel("설치") {{
			setFont(new Font("맑은 고딕", 1, 25));
		}}, BorderLayout.NORTH);
		borderPanel.add(panel, BorderLayout.NORTH);
		installInfor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		borderPanel.add(installInfor);
		
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(button);
			add(checkBox);
		}}, BorderLayout.SOUTH);
		
		lodingBar();
		
		button.addActionListener(e -> {
			try {
				File file = new File(new File(Installer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Nexus.jar");
				if(file.isFile()) {
					installInfor.setText("이미 설치 되어있습니다.");
					if(JOptionPane.showConfirmDialog(null, "앱을 실행 하시겠습니까?", "앱 실행", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						Desktop.getDesktop().open(file);
						dispose();
					}
					return;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			if(thread == null) return;
			if(button.getText().equals("마침")) {
				if(checkBox.isSelected()) {
					try {
						Desktop.getDesktop().open(
							new File(
								new File(
										Installer.class
										.getProtectionDomain()
										.getCodeSource()
										.getLocation()
										.toURI()
								).getParent() + "\\Nexus.jar"
							)
						);
					} catch (Exception e2) {
						getter.infor("에러남" + e2.getMessage());
					}
				}
				dispose();
				return;
			}
				thread.start();
				button.setEnabled(false);
				installInfor.setText("설치중...");
		});
		setFrameCd("설치", 400, 250, () -> {});
	}

	public void lodingBar() {
		JLabel l = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.green);
				Rectangle2D.Double r = new Rectangle2D.Double(0, 0, bar * barGuage, getHeight());
				g2.fill(r);
			}
		};
		l.setBorder(BorderFactory.createLineBorder(Color.black));
		l.setPreferredSize(new Dimension(0, 25));
		panel.add(l);
		thread = new Thread(() -> {
			try {
				while(barGuage < 100) {
					barGuage ++;
					l.revalidate();
					l.repaint();
					int r = (int) (Math.random() * 30) + 50;
					System.out.println(r);
					Thread.sleep(r);
				}
				new SimpleJarBuilder2();
				installInfor.setText("설치 완료!");
				button.setEnabled(true);
				button.setText("마침");
			} catch (Exception e) {
				
			} finally {
				Thread.interrupted();
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				thread.interrupt();
			}
		});
	}
	
	public static void main(String[] args) {
		new Installer();
	}
}
