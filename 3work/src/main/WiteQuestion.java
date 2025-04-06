package main;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class WiteQuestion extends JComponent{
	JFrame f = new JFrame();
	
	CardLayout card = new CardLayout();
	JPanel[] P = {new JPanel(), new JPanel(), new JPanel()};
	JPanel publicP = new JPanel();
	JPanel cardP = new JPanel(card);
	JPanel TP = new JPanel();
	
	JLabel[] l = {new JLabel(), new JLabel()};
	JTextArea[] ta = {new JTextArea(),new JTextArea()};
	List<JTextArea>[] exta = new ArrayList[3];
	
	JButton logo = new JButton();
	JButton 변경 = new JButton("변경");
	JLabel label = new JLabel("내 질문");
	
	JComboBox<String> cb;
	String[] Data = {"전체", "답변완료", "답변 미완료"};
	String[] tbdata = {"번호", "선생님", "타이틀", "질문", "답변", "문제이미지"};
	
	private DefaultTableModel TModel;
	static JTable JT[] = new JTable[3];
	static Font FontN = new Font("맑은 고딕", Font.BOLD, 15);
	String order;
	
	int i = 0;
	int row = 0;
	int cardCK = 0;
	List<Integer>[] cno = new ArrayList[3];
	WiteQuestion(int number, int T, int QnM){
		cb = new JComboBox<String>(Data);
		cb.setBounds(600, 100, 200, 40);
		cb.setFont(new Font("맑은 고딕", Font.BOLD, 19));
		order = cb.getSelectedItem().toString();
		System.out.println(order);
		if(QnM == 1) {
			order = "답변 미완료";
			System.out.println(order);
		}
		
		
		변경.setBounds(1000,1000,65, 35);
		변경.setEnabled(false);
		변경.setFont(FontN);
		변경.setBackground(Color.white);
		cardP.setBounds(40,150, 770, 400);
		for(int s = 0; s < 3 ; s++) {
			exta[s] = new ArrayList<>();
			cno[s] = new ArrayList<>();
			TModel = new DefaultTableModel(tbdata,0){
	            @Override public boolean isCellEditable(int row, int column) {return false;}};
			try {
				Connection c = DriverManager.getConnection(Z_UUP.url(),Z_UUP.username(), Z_UUP.password());
				Statement cs = c.createStatement();
				ResultSet re = cs.executeQuery("select catalog.*, teacher.name from catalog "
						+ "join teacher on catalog.tno = teacher.tno "
						+ "where uno = " + number);
				if(s == 1) {
					re = cs.executeQuery("select catalog.*, teacher.name from catalog "
							+ "join teacher on catalog.tno = teacher.tno "
							+ "where uno = " + number + " and explan is not null");
				}
				if(s == 2) {
					re = cs.executeQuery("select catalog.*, teacher.name from catalog "
							+ "join teacher on catalog.tno = teacher.tno "
							+ "where uno = " + number + " and explan is null");
				}
				while(re.next()) {
					Image img = new ImageIcon("imgs/question/" + re.getInt("type") + ".jpg")
							.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
					JLabel icon = new JLabel();
					icon.setIcon(new ImageIcon(img));
					cno[s].add(re.getInt("cno"));
					l[0].setText(new String(""+(i+1)));
					l[1].setText(re.getString("name"));
					ta[0].setText(re.getString("title"));
					ta[1].setText(re.getString("questionexplan"));
					exta[s].add(new JTextArea());
					exta[s].get(i).setText(re.getString("explan"));
					
					re.getString("explan");
					TModel.addRow(new Object[]{l[0].getText(), l[1].getText(),ta[0].getText(),ta[1].getText(),exta[s].get(i).getText(),icon});
					i++;
				}
				re.close();
				cs.close();
				c.close();
			} catch (Exception e) {e.getStackTrace();}
			i = 0;
	        JT[s] = new JTable(TModel);
	        JT[s].setFont(FontN);
	        JT[s].getColumnModel().getColumn(5).setCellRenderer(new LabelRenderer());
	        for (int i = 2; i < 5; i++) {JT[s].getColumnModel().getColumn(i).setCellRenderer(new TextAreaRenderer());}
	        
	        JT[s].setRowHeight(155);
	        JT[s].setPreferredScrollableViewportSize(new Dimension(750,350));
	
	        DefaultTableCellRenderer c = new DefaultTableCellRenderer();
	        c.setHorizontalAlignment(JLabel.CENTER);
	        JT[s].getColumn("번호").setCellRenderer(c);
	        JT[s].getColumn("선생님").setCellRenderer(c);
	        
	        JT[s].getColumn("번호").setPreferredWidth(20);
	        JT[s].getColumn("선생님").setPreferredWidth(40);
	        JT[s].getColumn("타이틀").setPreferredWidth(70);
	        JT[s].getColumn("질문").setPreferredWidth(70);
	        JT[s].getColumn("답변").setPreferredWidth(70);
	        JT[s].getColumn("문제이미지").setPreferredWidth(150);
	        

	        P[s].setBackground(Color.white);
	        P[s].setBounds(40,150, 770, 400);
	        JScrollPane JSJT = new JScrollPane(JT[s]);
	        P[s].add(JSJT);
	        cardP.add(P[s], "P"+s);	       
	        JT[s].addMouseListener(new MouseAdapter() {
	        	@Override
	            public void mouseReleased(MouseEvent e) {
	                if (SwingUtilities.isRightMouseButton(e)) {
	                    변경.setLocation(e.getX()+15
	                    		, e.getY()+160-(JSJT.getVerticalScrollBar().getValue()));
	                    변경.setEnabled(true);
	                }
	            }
	        	public void mouseClicked(MouseEvent e) {
	        		if(!SwingUtilities.isRightMouseButton(e)) {
		            	변경.setLocation(1000, 1000);
		            	row = JT[cardCK].getSelectedRow()+1;
		            	System.out.println(cno[cardCK].get(row-1));
	        		}
	            }
	        });
		}// .반복문끝=--------------------------------------------
		
		card.show(cardP,"P0");
		if(QnM == 1) {
			cb.setSelectedIndex(2);
			card.show(cardP, "P2");
		}
		
		cb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == cb) {
					order = cb.getSelectedItem().toString();
					if(order.equals("전체")) {card.show(cardP, "P0");cardCK = 0;row=0;}
					if(order.equals("답변완료")) {card.show(cardP, "P1");cardCK = 1;row=0;}
					if(order.equals("답변 미완료")) {card.show(cardP, "P2");cardCK = 2;row=0;}
				}
			}
		});
        변경.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(row != 0) {
					if(exta[cardCK].get(row-1).getText().equals("")) {
						
						new TList(number, "", "", 0, cno[cardCK].get(row-1));
						f.setVisible(false);
					}
					if(!(exta[cardCK].get(row-1).getText().equals(""))) {
						JOptionPane.showMessageDialog(f, "경고 -> 답변이 있는 경우는 선생님을 변경할 수 없습니다.");
					}
					
				}
			}
		});
		
		Image img = new ImageIcon("imgs/icon/logo.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		logo.setBounds(0,0,70,70);
		logo.setIcon(new ImageIcon(img));
		logo.setBackground(Color.white);
		logo.setBorder(BorderFactory.createLineBorder(Color.white));
		logo.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {new B_Umain(number);f.setVisible(false);}});
		
		label.setBounds(400,0, 150, 50);
		label.setFont(new Font("맑은 고딕", Font.BOLD, 22));

		
		publicP.setBackground(Color.white);
		publicP.setLayout(null);
		
		publicP.add(변경);
		publicP.add(cardP);
		publicP.add(cb);
		publicP.add(label);
		publicP.add(logo);
		f.add(publicP);
		f.setBounds(660, 340, 850,600);
		f.setDefaultCloseOperation(f.DO_NOTHING_ON_CLOSE);
		//f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.addWindowListener(new WindowAdapter() {
			@Override 
			public void windowClosing(WindowEvent e) {
			new B_Umain(number);
			f.setVisible(false);
			} 
		});
		f.setVisible(true);
	}
    static class LabelRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JLabel) {
                JLabel label = (JLabel) value;
                return label;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    static class TextAreaRenderer extends JTextArea implements TableCellRenderer {
        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setFont(FontN);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            return this;
        }
    }
	public static void main(String[] args) {
		new WiteQuestion(1, 0, 0);
	}
}
