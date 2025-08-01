package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import utils.Query;
import utils.Row;
import utils.sp;

public class E_GumeList extends JPanel{
	int user = 0, global = 0, price = 0;
	
	List<Integer> totalPrice = new ArrayList<>();
	List<Row> listT = new ArrayList<Row>();
	ImageIcon imgs = new ImageIcon(new ImageIcon(getClass().getResource("/img/1.jpg")).getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH));
	JLabel img = new JLabel(imgs);
	JLabel priceJL = new JLabel(){{setFont(sp.fontM(1,24));setHorizontalAlignment(JLabel.RIGHT);}};
	JPanel MainP = new JPanel(new BorderLayout());
	String[] str = " ,상품명,수량,단가,합계,구매날짜".split(",");
	DefaultTableModel T = new DefaultTableModel(str, 1){
		@Override
		public boolean isCellEditable(int row, int column) {return false;}
	};
	
	
	JTable t = new JTable(T) {{
		DefaultTableCellRenderer cell = new DefaultTableCellRenderer() {{setHorizontalAlignment(JLabel.CENTER);}};
		JTableHeader header = this.getTableHeader();
		header.setBackground(Color.white); header.setReorderingAllowed(true);
		for(String s : str) {this.getColumn(s).setCellRenderer(cell);}
		this.setRowHeight(120);this.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
	}};
	E_GumeList(){}
	E_GumeList(JPanel p, int user){
		this.user = user;
		listT = Query.GumeList.select(user+"");//정보
		System.out.println(listT);
		List<Row> list = new ArrayList<Row>();//정보를 테이블 형태에 맞게 가공
		for(int i = 0; i < listT.size(); i++) {
			Row row = new Row();
			row.add(new ImageIcon(Query.getImge.getImge(listT.get(i).get(7)).getScaledInstance(150, 120, Image.SCALE_SMOOTH)));
			row.add(listT.get(i).getString(1));row.add(listT.get(i).getString(2)+"개");
			row.add(new DecimalFormat("###,###").format(listT.get(i).getInt(3)) + "원");
			row.add(new DecimalFormat("###,###").format(listT.get(i).getInt(4)) + "원" );
			totalPrice.add(listT.get(i).getInt(4));row.add(listT.get(global).getString(5));
			list.add(row);
		}
		Query.setTable(T, list);
		setBackground(Color.white);
		setLayout(new BorderLayout());
		add(new JPanel(new GridLayout(1, 2)) {{
			add(new JLabel("구매목록") {{setFont(sp.fontM(1,24));}});Price();
			add(priceJL);
		}},BorderLayout.NORTH);
		
		MainP.setBorder(BorderFactory.createLineBorder(Color.black));
		MainP.add(new JScrollPane(t) {{getVerticalScrollBar().setBackground(Color.white);}});
		this.add(MainP, BorderLayout.CENTER);
		
		t.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {if (e.getButton() == MouseEvent.BUTTON3) {
            	showPopupMenu(e.getX(), e.getY()); 
            	t.setRowSelectionInterval(t.rowAtPoint(e.getPoint()), t.rowAtPoint(e.getPoint()));
            }}
        });
		p.add(this, "P4");
    }
	private void showPopupMenu(int x, int y) {
	    JPopupMenu popupMenu = new JPopupMenu();
	    JButton button1 = new JButton("삭제");
	    button1.addActionListener(e -> {
	    	int GSR = t.getSelectedRow();
	    	
	    	Query.reviewDelet.update(listT.get(GSR).get(6));
	        Query.GumeDelet.update(listT.get(GSR).get(6));
	        listT.remove(GSR);T.removeRow(GSR); totalPrice.remove(GSR);price = 0;
	        sp.InforMes("삭제가 완료되었습니다.");
	        Price();popupMenu.setVisible(false);
	    });
	    popupMenu.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
	    popupMenu.add(button1);
	    popupMenu.show(t, x, y);
	}


	public void Price() {for(int j : totalPrice) price += j; priceJL.setText("총 금액: " + new DecimalFormat("###,###").format(price) + "원");}
    static class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        	JLabel label = label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER); 
            if (value instanceof ImageIcon) {
                label.setIcon((ImageIcon) value);
            } else {
                label.setIcon(null);
            }
            return label;
        }
    }
}
