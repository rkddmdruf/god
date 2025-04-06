package main;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.List;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
public class QuestionUp extends JComponent implements DropTargetListener{
	JFrame f = new JFrame();
	JPanel publicP = new JPanel();
	JLabel QL = new JLabel("질문 등록");
	JLabel img = new JLabel();
	
	JTextField title = new JTextField("제목", 30);
	JTextArea 질문내용 = new JTextArea("질문내용", 500, 10);
	JTextArea ta = new JTextArea();

	JButton QB = new JButton("질문 등록");
	JButton Tchoies = new JButton("선생님을 선택해주세요.");
	DropTarget dt;
	Object str;
	
	int cTt = 0;
	int cT = 0;
	static int number;
	int T = 0;
	int imgCheck = 0;
	int imgnumber;
	String ss;
	public QuestionUp(int number, int T, String stitle, String s질문내용, int imgnumber) {
		this.T = T;
		ss = "" + imgnumber;
		this.imgnumber = imgnumber;
		if(!stitle.equals("")) {
			title.setText(stitle);
		}
		if(!s질문내용.equals("")) {
			질문내용.setText(s질문내용);
		}
		QuestionUp.number = number;
		publicP.setLayout(null);
		publicP.setBackground(Color.white);
		
		QL.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		QL.setBounds(445, 0, 110, 50);
		
		QB.setBounds(800, 400, 150, 40);
		QB.setBackground(new Color(30, 120, 20));
		QB.setForeground(Color.white);
		QB.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		QB.addActionListener(new 질문등록Action());
		
		Tchoies.setBounds(550, 400, 200, 40);
		Tchoies.setBackground(Color.white);
		Tchoies.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		if(T == 0) {
			publicP.add(Tchoies);
			Tchoies.addActionListener(new TMyAction());
		}else {
			Tchoies.addActionListener(new TMyAction());
			try {
				Connection c = DriverManager.getConnection(Z_UUP.url(),Z_UUP.username(), Z_UUP.password());
				Statement cs = c.createStatement();
				ResultSet re = cs.executeQuery("select * from question.teacher where tno = " + T);
				re.next();
				Tchoies.setText(re.getString("name") + " 선생님");
				publicP.add(Tchoies);
				re.close();
				cs.close();
				c.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		title.setBounds(550, 60, 400, 35);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		title.setBorder(BorderFactory.createLineBorder(Color.black));
		title.addKeyListener(new titleKeyListener());
		title.addMouseListener(new MyMouseListener());
		
		ta.setBounds(30, 60, 500, 385);
		ta.setBorder(BorderFactory.createLineBorder(Color.black));
		ta.setEnabled(false);
		img.setBounds(30, 60, 500, 385);
		img.setBorder(BorderFactory.createLineBorder(Color.black));
		
		dt = new DropTarget(img, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);
		dt = new DropTarget(ta, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);
		질문내용.setBounds(550, 110, 400, 270);
		질문내용.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		질문내용.setBorder(BorderFactory.createLineBorder(Color.black));
		질문내용.addMouseListener(new MyMouseListener());
		질문내용.addKeyListener(new 질문내용KeyListener());
		
		f.add(ta);
		publicP.add(질문내용);
		publicP.add(title);
		publicP.add(QL);
		publicP.add(QB);
		publicP.add(img);
		
		if(imgnumber != 0) {
			Image imgs = new ImageIcon("imgs/question/" + imgnumber +".jpg").getImage().getScaledInstance(500, 250, Image.SCALE_SMOOTH);
			img.setBorder(BorderFactory.createLineBorder(Color.black));
			img.setIcon(new ImageIcon(imgs));
			publicP.add(img);
			f.setVisible(true);
			imgCheck = 1;
		}
		
		f.add(publicP);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.addWindowListener(new WindowAdapter() {
			@Override 
				public void windowClosing(WindowEvent e) {
				new B_Umain(number);
				f.setVisible(false);
			} 
		}); 
		f.setSize(1000, 500);
		f.setVisible(true);
	}
	
class MyMouseListener implements MouseListener{
			@Override public void mouseClicked(MouseEvent e) {
				if(e.getSource() == title) {if(title.getText().equals("제목")) {title.setText("");}}
				else if(e.getSource() == 질문내용) {if(질문내용.getText().equals("질문내용")) {질문내용.setText("");}}
			}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {
				if(title.getText().equals("")) {title.setText("제목");}
				if(질문내용.getText().equals("")) {질문내용.setText("질문내용");}
			}

}

class titleKeyListener implements KeyListener{

		@Override public void keyTyped(KeyEvent e) {}
		@Override public void keyReleased(KeyEvent e) {
			cTt = title.getText().length();
			if(cTt >= 30) {
				title.setEditable(false);
				title.setBackground(Color.white);
				title.setBorder(BorderFactory.createLineBorder(Color.black));
				title.setBounds(550, 60, 400, 35);
				if(e.getExtendedKeyCode() == 8) {title.setEditable(true);}
			}
			if(cTt < 30) {title.setEditable(true);}
		}
		@Override public void keyPressed(KeyEvent e) {}
	}
class 질문내용KeyListener implements KeyListener{

	@Override public void keyTyped(KeyEvent e) {}
	@Override public void keyReleased(KeyEvent e) {
		cT = 질문내용.getText().length();
		System.out.println(cT);
		if(cT >= 500) {
			질문내용.setEditable(false);
			질문내용.setBackground(Color.white);
			질문내용.setBorder(BorderFactory.createLineBorder(Color.black));
			질문내용.setBounds(550, 110, 400, 270);
			if(e.getExtendedKeyCode() == 8) {질문내용.setEditable(true);}
		}
		if(cT < 500) {질문내용.setEditable(true);}
	}
	@Override public void keyPressed(KeyEvent e) {}
}

	@Override public void dragEnter(DropTargetDragEvent dtde) {}
	@Override public void dragOver(DropTargetDragEvent dtde) {}
	@Override public void dropActionChanged(DropTargetDragEvent dtde) {}
	@Override public void dragExit(DropTargetEvent dte) {}
	@Override public void drop(DropTargetDropEvent dtde) {
		if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0)
        {
            dtde.acceptDrop(dtde.getDropAction());
            Transferable tr = dtde.getTransferable();
            try
            {
                //파일명 얻어오기
                List list = (List) tr.getTransferData(DataFlavor.javaFileListFlavor);
                //파일명 출력
                for(int i=0;i < list.size();i++){str = list.get(i);}
                
                
                String s = "" + str;
                ss = s.substring(s.length()-6, s.length()-4);
                for(int i = 1; i <= 9; i++) {
                	if(ss.equals("\\"+i)) {
                		ss = s.substring(s.length()-5, s.length()-4);
                	}
                }
                imgnumber = Integer.parseInt(ss);
                String str = ".jpg";
                if(s.contains(str)) {
        			Image imgs = new ImageIcon(s).getImage().getScaledInstance(500, 250, Image.SCALE_SMOOTH);
        			img.setBorder(BorderFactory.createLineBorder(Color.black));
        			img.setIcon(new ImageIcon(imgs));
        			publicP.add(img);
        			imgCheck = 1;
                }
            }
            catch (Exception e)
            {

                e.printStackTrace();

            }

        }
	}	
class 질문등록Action implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		int check  = 0;
		int scheck = 0;
		String titles = title.getText();
		String 질문내용s = 질문내용.getText();
		if(T == 0) {
			check = 1;
			int result = JOptionPane.showConfirmDialog(publicP, "질문 : 선생님을 선택하지 않았습니다. 선생님 폼으로 이동하시겠습니까?","Confirm", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.CLOSED_OPTION) {}
			if(result == JOptionPane.YES_OPTION) {
				new TList(number, titles, 질문내용s, Integer.parseInt(ss), 0);
				f.setVisible(false);
			}
			if(result == JOptionPane.NO_OPTION) {
				check = 1;
			}
		}
		if(title.getText().equals("제목") || 질문내용.getText().equals("질문내용") || title.getText().equals("") || 질문내용.getText().equals("")) {
			JOptionPane.showMessageDialog(publicP, "경고 : 내용을 입력해주세요.", "경고", JOptionPane.INFORMATION_MESSAGE);
			check = 1;
		}
		if(imgCheck == 0) {
			JOptionPane.showMessageDialog(publicP, "경고 : 사진을 선택해주세요.");
			check = 1;
		}
		boolean tf1 = Z_Scheck.체크(title.getText());
		boolean tf2 = Z_Scheck.체크(질문내용.getText());
		if(tf1 == true || tf2 == true) {
			JOptionPane.showMessageDialog(publicP, "경고 : 비속어는 사용하실 수 없습니다.");
			check = 1;
		}
		
        
		if(check != 1) {
			try {
				int i = 0;
				Connection c = DriverManager.getConnection(Z_UUP.url(),Z_UUP.username(), Z_UUP.password());
				Statement s = c.createStatement();
				ResultSet re;
				PreparedStatement pre;
				int type = Integer.parseInt(ss);
				String now = "" + LocalDate.now();
				s.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
				pre = c.prepareStatement("insert into question.catalog values (?, ?, ?, ?, ?, ?, ?, ?)");
				
				re = s.executeQuery("select cno from question.catalog order by cno asc");
				while(re.next()) {i = re.getInt("cno");}
				i++;
				pre.setInt(1, i);pre.setInt(2, number);pre.setInt(3, T);
				pre.setInt(4, type);pre.setString(5, titles);pre.setString(6, now);
				pre.setString(7, 질문내용.getText());pre.setString(8, null);
				
				pre.executeUpdate();
				JOptionPane.showMessageDialog(publicP, "정보 : 질문이 등록되었습니다.");			
				f.setVisible(false);
				new B_Umain(number);
				pre.close();
				re.close();
				s.close();
				c.close();
			} catch (Exception e2) {
				
			}
			

		}
	}
}
class TMyAction implements ActionListener{
	@Override public void actionPerformed(ActionEvent e) {
		String stitle = title.getText();
		String s질문내용 = 질문내용.getText();
		if(imgnumber != 0) {
			new TList(number, stitle, s질문내용, imgnumber, 0);
		}else {new TList(number, stitle, s질문내용, 0, 0);}
		f.setVisible(false);
	}
}
	public static void main(String[] args) {
		new QuestionUp(1, 0, "","", 0);
	}
}
