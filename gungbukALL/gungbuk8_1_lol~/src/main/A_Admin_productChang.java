package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;

import utils.BaseFrame;
import utils.Query;
import utils.Row;
import utils.sp;

public class A_Admin_productChang extends BaseFrame{
	int number = 0;int global = 1;
	String[] label = "카테고리 상품명 설명 가격 수량".split(" ");
	JTextField[] tf = new JTextField[4];
	JLabel imgL = new JLabel() {{setPreferredSize(new Dimension(265, 180));setBackground(Color.white);}};
	
	JButton[] but = {new JButton("수정"), new JButton("등록")};
	
	String[] cbName = "식품 문구 생활용품 뷰티 음료 전자제품 의류 유아 스포츠 도서".split(" ");
	JComboBox<String> cb = new JComboBox<String>(cbName);
	List<Row> list = new ArrayList<>();
	int doubleCheck = 1;
	private JFileChooser fileChooser;
	public A_Admin_productChang(int number) {
		this.number = number;
		setFrame("상품수정", 300, 500, ()->{});
	}
	
	
	@Override
	protected void desgin() {
		System.out.println(number);
		 fileChooser = new JFileChooser();
	        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
	                "Image files", ImageIO.getReaderFileSuffixes());
	        fileChooser.setFileFilter(imageFilter);
		if(number != 0) {
			imgL.setIcon(new ImageIcon(Query.getImge.getImge(number).getScaledInstance(265, 180, Image.SCALE_SMOOTH)));
			list = Query.productAll.select(number);
		}else {cb = new JComboBox<String>(" , , , , , , , , , ".split(","));}
		
			add(new JPanel(new BorderLayout()) {{
				setBackground(Color.white);
				sp.setBorderIE(this,10, 10, 10, 10);
				sp.setBorderLINE(imgL);
				add(imgL, sp.n);
				add(new JPanel(new BorderLayout()) {{
					add(new JPanel(new GridLayout(5,0, 0, 15)) {{
						setBackground(Color.white);sp.setBorderIE(this, 10, 5, 10, 0);for(String s : label) {add(new JLabel(s) {{setFont(sp.fontM(1, 15));}});}
					}}, sp.w);
					add(new JPanel(new GridLayout(5,0, 0, 15)) {{setBackground(Color.white);
						sp.setBorderIE(this, 10, 5, 10, 0);add(cb);
						if(number != 0) {cb.setSelectedIndex(list.get(0).getInt(5)-1);}
						for(int i = 0; i < 4; i++) {
							if(number != 0) {tf[i] = new JTextField(list.get(0).getString(i+1));
							}else {tf[i] = new JTextField();}
							add(tf[i]);
						}
					}}, sp.c);
					if(number != 0) {add(but[0], sp.s);}else {add(but[1], sp.s);}
					
				}}, sp.c);
			}});
		
	}

	@Override
	protected void action() {
		for(int i = 0; i < 2; i++) {final int in = i;
			but[i].addActionListener(e->{
				System.out.println(cb.getSelectedIndex());
				int a = 0, b = 0; boolean c = true;
				for(int j = 0; j <= 2; j+=2) {
					if(tf[j].getText().equals("") || tf[j+1].getText().equals("")) {c = false;}}
					if(but[1] == e.getSource() && fileChooser.getSelectedFile() == null) {c = false;}
				if(!c) {sp.ErrMes("빈칸이 있습니다.");
				}else{
					try {
						a = Integer.parseInt(tf[2].getText()); b = Integer.parseInt(tf[3].getText());
						if(a <= 0 || b <= 0) {Integer.parseInt("ERROR");}
						else if(in == 0){
							Query.productUpdate.update(cb.getSelectedIndex()+1, tf[0].getText(), tf[1].getText(), tf[2].getText(), tf[3].getText(), number);
							if(fileChooser.getSelectedFile() != null) {
								Query.updateimg.upImg(fileChooser.getSelectedFile().toString(), number);
							}
							sp.InforMes("수정이 완료되었습니다.");//new AMain(1119, -1);dispose();
						}else if(in == 1) {
							if(!Query.product글자비교.select(tf[0].getText()).isEmpty()) {
								sp.ErrMes("이미존재하는 상품입니다.");
							}else {
								
								Query.productinsert.update(tf[0].getText(), tf[1].getText(), tf[2].getText(), tf[3].getText(), cb.getSelectedIndex()+1);
								Query.updateimg.upImg(fileChooser.getSelectedFile().toString(), Query.product.select().get(Query.product.select().size()-1).getInt(0));
								sp.InforMes("등록이 완료되었습니다.");
							}
						}
					} catch (Exception e2) {
						System.out.println(e2.getMessage());
						sp.ErrMes("가격과 수량을 1이상의 숫자로 입력하세요.");
					}
				}
			});
		}
		cb.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				cb.removeAllItems();
				for(String s : cbName) {cb.addItem(s);}
			}
		});
		imgL.addMouseListener(new MouseAdapter() {
			@Override public void mouseExited(MouseEvent e) { doubleCheck = 1; }
			@Override public void mouseClicked(MouseEvent e) {
				doubleCheck++;
				if(doubleCheck > 2) { 
					int returnValue = fileChooser.showOpenDialog(new JFrame());
		            System.out.println(fileChooser.getSelectedFile());
		            if (returnValue == JFileChooser.APPROVE_OPTION) {
		                File selectedFile = fileChooser.getSelectedFile();
		                
		                try {
		                   // 이미지 파일 읽기
		                    Image image = ImageIO.read(selectedFile);
		                    if (image != null) {
		                        // 이미지 크기 조절
		                        Image scaledImage = image.getScaledInstance(imgL.getWidth(), imgL.getHeight(), Image.SCALE_SMOOTH);
		                        ImageIcon imageIcon = new ImageIcon(scaledImage);
		                        imgL.setIcon(imageIcon);
		                        imgL.setText("");
		                    }
		                } catch (IOException ex) {
		                    ex.printStackTrace();
		                }
		            }
					doubleCheck = 1; 
				}
			}
		});
	}
	public static void main(String[] args) {
		new A_Admin_productChang(1);
	}
	
}