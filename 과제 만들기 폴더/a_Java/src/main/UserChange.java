package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import utils.Connections;
import utils.Data;
import utils.User;
import utils.getter;

public class UserChange extends JPanel{

	JLabel userImage = new JLabel(getter.getImage("datafiles/user/" + User.getUser().getInt(0) + ".png", 225, 225)) {{
		setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	}};
	
	JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(getter.color);
	}};
	JButton change = new JButton("수정") {{
		setBackground(new Color(0, 150, 0));
		setForeground(Color.white);
	}};
	
	JTextField[] tfs = new JTextField[3];
	Main1 main1;
	
	int imgN = User.getUser().getInt(4);
	int saveImgN = -1;
	 
	public UserChange(Main1 main1) {
		this.main1 = main1;
		this.setLayout(new BorderLayout());
		setBackground(getter.color);
		setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));// 750, 680
		setNorthPanel();
		scurti();
		change.addActionListener(e -> {
			for(JTextField s : tfs)
				if(s.getText().isBlank()) {
					getter.err("빈칸이 있으면 안됩니다!");
					return;
				}
			String[] str = "이름이, 아이디가, 비밀번호가".split(", ");
			for(int i = 0; i < 3; i++)
				if(tfs[i].getText().length() > 15) {
					getter.err(str[i] + " 15자 이상이면 안됩니다!");
					return ;
				}
			boolean equalsInfor = false;
			for(int i = 0; i < 3; i++)
				if(!tfs[i].getText().equals(User.getUser().getString(i + 1)))
					equalsInfor = true;
			if(User.getUser().getInt(4) != imgN)
				equalsInfor = true;
			
			if(!equalsInfor) {
				getter.err("수정 사항이 없어요.");
				return;
			}
			Connections.update("update user set uname = ?, id = ?, pw = ?, iconNumber = ? where uno = ?;", tfs[0].getText(), tfs[1].getText(), tfs[2].getText(), imgN, User.getUser().get(0));// 유저 업데이트
			User.setUser(Connections.select("select * from user where uno = ?", User.getUser().getInt(0)).get(0));
			
			getter.infor("정보가 수정됐답니다~");
			scurti();
			return;
		});
		add(mainPanel);
	}

	private void setNorthPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
		panel.setBackground(getter.color);
		panel.add(userImage, BorderLayout.WEST);
		
		JPanel userPanel = new JPanel(new GridLayout(2, 1, 50, 50));
		userPanel.setBackground(getter.color);
		userPanel.setBorder(BorderFactory.createEmptyBorder(75, 10, 75, 10));
		userPanel.add(new JLabel(User.getUser().getString(1)) {{
			setForeground(Color.white);
			setFont(new Font("맑은 고딕", 1, 30));
		}});
		
		JPanel buttonPanel = new JPanel(new GridLayout(0, 3, 10, 10));
		buttonPanel.setBackground(getter.color);
		buttonPanel.add(new JButton("정보변경") {{
			setBackground(new Color(0, 150, 0));
			setForeground(Color.white);
			addActionListener(e -> {
				scurti();
			});
		}});
		buttonPanel.add(new JButton("라이브러리") {{
			setBackground(new Color(0, 150, 0));
			setForeground(Color.white);
			addActionListener(e -> {
				setLibraryPanel();
			});
		}});
		buttonPanel.add(new JLabel(User.getUser().getString(5) + "원 보유중") {{
			setForeground(Color.white);
		}});
		userPanel.add(buttonPanel);
		
		panel.add(userPanel);
		add(panel, BorderLayout.NORTH);
	}
	
	private void setLibraryPanel() {
		mainPanel.removeAll();
		JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		panel.setBackground(getter.color);
		List<Data> list = Connections.select("SELECT game.*, buygame.* FROM game.buygame \r\n"
				+ "join game on game.gno = buygame.gno where uno = ?;", User.getUser().get(0));
		String[] categorys = Connections.select("select * from category").stream().map(e -> e.getString(1)).toArray(String[]::new);
		
		for(Data data : list) {
			JPanel p = new JPanel(new BorderLayout(10, 10));
			p.setBackground(getter.color);
			
			JPanel namePanel = new JPanel(new BorderLayout(5, 5));
			namePanel.setBackground(getter.color);
			namePanel.add(new JLabel(data.getString(1)) {{
				setForeground(Color.white);
				setFont(new Font("맑은 고딕", 1, 19));
			}}, BorderLayout.NORTH);
			List<String> strs = new ArrayList<>();
			for(String s : data.getString(2).split(",")) {
				strs.add(categorys[Integer.parseInt(s) - 1]);
			}
			namePanel.add(new JLabel("카테고리 : " + String.join(", ", strs)) {{
				setForeground(Color.white);
			}});
			namePanel.add(new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)) {{
				setBackground(getter.color);
				
				JButton but = new JButton("상점 페이지로 이동");
				but.setBackground(new Color(0, 150, 0));
				but.setForeground(Color.white);
				but.addActionListener(e -> {
					main1.mainPanel.removeAll();
					main1.mainPanel.setBorder(null);
					main1.mainPanel.add(new gamePanel(main1, Connections.select("select * from game where gno = ?", data.getInt(0)).get(0)));
					main1.revalidate();
					main1.repaint();
				});
				add(but);
			}}, BorderLayout.EAST);
			p.add(namePanel);
			p.add(new JLabel(getter.getImage("datafiles/games/" + data.getInt(0) + ".jpg", 200, 100)), BorderLayout.WEST);
			panel.add(p);
		}
		
		mainPanel.add(new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			setBackground(getter.color);
			setBorder(null);
			addMouseWheelListener(e -> {
				getVerticalScrollBar().setValue(getVerticalScrollBar().getValue() + (e.getWheelRotation() * 40));
			});
		}});
		revalidate();
		repaint();
	}
	
	private void scurti() {
		mainPanel.removeAll();
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panel.setBackground(getter.color);
		
		JTextField tf = new JTextField() {{
			setPreferredSize(new Dimension(225, 25));
		}};
		
		panel.add(new JLabel("") {{
			setPreferredSize(new Dimension(1500, 125));
		}});
		panel.add(new JLabel("보안을 위해 비밀번호를 입력해주세요.") {{
			setForeground(Color.white);
		}});
		panel.add(new JLabel("") {{
			setPreferredSize(new Dimension(1500, 2));
		}});
		panel.add(tf);
		panel.add(new JLabel("") {{
			setPreferredSize(new Dimension(1500, 5));
		}});
		panel.add(new JButton("확인") {{
			setBackground(new Color(0, 150, 0));
			setForeground(Color.white);
			addActionListener(e -> {
				if(tf.getText().equals(User.getUser().getString(3))) {
					setChangeUserInfor();
				}else {
					getter.err("비밀번호가 다릅니다.");
					tf.setText("");
				}
			});
		}});
		mainPanel.add(panel);
		revalidate();
		repaint();
	}
	
	private void setChangeUserInfor() {
		mainPanel.removeAll();
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(getter.color);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
		
		
		JPanel imagePanel = new JPanel(new BorderLayout(10, 10));
		imagePanel.setBackground(getter.color);
		imagePanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
		//이미지 전에 선택한거 저장
		JLabel bigImage = new JLabel(getter.getImage("datafiles/user/" + User.getUser().getInt(4) + ".png", 150, 150));
		bigImage.setBorder(BorderFactory.createLineBorder(Color.blue));

		JLabel smallImage = new JLabel("", JLabel.LEFT);
		smallImage.setPreferredSize(new Dimension(40, 40));
		smallImage.setVerticalAlignment(JLabel.BOTTOM);
		smallImage.setBorder(BorderFactory.createLineBorder(Color.red));
		
		imagePanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {{
			setBackground(getter.color);
			add(new JLabel("") {{
				setPreferredSize(new Dimension(1500, 110));
			}});
			add(smallImage);
		}});
		imagePanel.add(bigImage, BorderLayout.WEST);
		
		JPanel gridPanel = new JPanel(new GridLayout(2, 2, 25, 25));
		gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 50, 20));
		gridPanel.setBackground(getter.color);
		
		String[] str = "이름, ID, PASS WORD".split(",");
		for(int i = 0; i < 3; i++) {
			JPanel p = new JPanel(new BorderLayout(10, 10));
			p.setBackground(getter.color);
			p.add(new JLabel(str[i]) {{
				setForeground(Color.white);
			}}, BorderLayout.NORTH);
			p.add(tfs[i] = new JTextField(User.getUser().getString(1 + i)));
			gridPanel.add(p);
		}

		JComboBox<Integer> imageNumber = new JComboBox<Integer>() {{
			System.out.println(User.getUser());
			for(int i = 1; i <= 30; i++)
				addItem(i);
			
		}};
		imageNumber.setSelectedIndex(User.getUser().getInt(4) - 1);
		imageNumber.addItemListener(e -> { 
			if(e.getStateChange() != ItemEvent.SELECTED) return;
			smallImage.setIcon(getter.getImage("datafiles/user/" + imgN + ".png", 40, 40));
			bigImage.setIcon(getter.getImage("datafiles/user/" + (imageNumber.getSelectedIndex() + 1) + ".png", 150, 150));
			saveImgN = imgN;
			imgN = imageNumber.getSelectedIndex() + 1;
		});
		smallImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(saveImgN == -1) return;
				imgN = saveImgN;
				bigImage.setIcon(getter.getImage("datafiles/user/" + imgN + ".png", 150, 150));
				imageNumber.setSelectedIndex(imgN - 1);
			}
		});
		JPanel imageComboBox_ChangeButtonPanel = new JPanel(new BorderLayout(10 ,10));
		imageComboBox_ChangeButtonPanel.setBackground(getter.color);
		imageComboBox_ChangeButtonPanel.add(new JLabel("사진 고르기 및 수정") {{
			setForeground(Color.white);
		}}, BorderLayout.NORTH);
		imageComboBox_ChangeButtonPanel.add(new JPanel(new GridLayout(0, 2, 10, 10)) {{
			setBackground(getter.color);
			add(imageNumber);
			add(change);
		}});
		
		gridPanel.add(imageComboBox_ChangeButtonPanel);
		panel.add(gridPanel);
		panel.add(imagePanel, BorderLayout.NORTH);
		mainPanel.add(panel);
		revalidate();
		repaint();
	}
}
