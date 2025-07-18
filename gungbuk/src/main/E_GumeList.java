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

import utils.Query;
import utils.Row;

public class E_GumeList extends JPanel{
	int user = 0, global = 0, price = 0;
	
	List<Integer> totalPrice = new ArrayList<>();
	List<Row> listT = new ArrayList<Row>();
	ImageIcon imgs = new ImageIcon(new ImageIcon(getClass().getResource("/img/1.jpg")).getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH));
	JLabel img = new JLabel(imgs);
	JLabel priceJL = new JLabel(){{setFont(setBoldFont(24));setHorizontalAlignment(JLabel.RIGHT);}};
	JPanel MainP = new JPanel(new BorderLayout());
	String[] str = " ,상품명,수량,단가,합계,구매날짜".split(",");
	DefaultTableModel T = new DefaultTableModel(str, 1){
		@Override
		public boolean isCellEditable(int row, int column) {
			for(int i = 0; i < str.length; i++) {
				if (column == i) {return false;}
			} return true;
		}
	};
	
	DefaultTableCellRenderer cell = new DefaultTableCellRenderer() {{setHorizontalAlignment(JLabel.CENTER);}};
	JTable t = new JTable(T) {{for(String s : str) {this.getColumn(s).setCellRenderer(cell);
		this.setRowHeight(120);this.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
	}}};
	E_GumeList(){}
	E_GumeList(JPanel p, int user){
		this.user = user;
		listT = Query.GumeList.select(user+"");
		List<Row> list = new ArrayList<Row>();
		for(global = 0; global < listT.size(); global++) {
			Row row = new Row();
			row.add(new ImageIcon(new ImageIcon(getClass().getResource("/img/"+listT.get(global).getInt(2)+".jpg"))
					.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH)));
			row.add(listT.get(global).getString(8));row.add(listT.get(global).getString(3)+"개");
			row.add(new DecimalFormat("###,###").format(listT.get(global).getInt(7)) + "원");
			row.add(new DecimalFormat("###,###").format(listT.get(global).getInt(7) * listT.get(global).getInt(3)) + "원" );
			totalPrice.add(listT.get(global).getInt(7) * listT.get(global).getInt(3));
			row.add(listT.get(global).getString(4));
			list.add(row);
		}
		Query.setTable(T, list);
		
		setBackground(Color.white);
		setLayout(new BorderLayout());
		add(new JPanel(new GridLayout(1, 2)) {{
			add(new JLabel("구매목록") {{setFont(setBoldFont(24));}});Price();
			add(priceJL);
		}},BorderLayout.NORTH);
		
		MainP.setBorder(BorderFactory.createLineBorder(Color.black));
		MainP.add(new JScrollPane(t) {{setBackground(Color.white);}});
			
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
	    	Query.reviewDelet.update(listT.get(GSR).getString(0));
	        Query.GumeDelet.update(listT.get(GSR).getString(0));
	        T.removeRow(GSR); totalPrice.remove(GSR);price = 0;
	        Price();
	        popupMenu.show(t, 2000,2000);
	    });
	    popupMenu.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
	    popupMenu.add(button1);
	    popupMenu.show(t, x, y);
	}



	private Font setBoldFont(int i) {
		return new Font("맑은 고딕", Font.BOLD, i);
	}
	public void Price() {
		for(int j : totalPrice) {price += j;}
		priceJL.setText("총 금액: " + new DecimalFormat("###,###").format(price) + "원");
	}
    static class ImageRenderer extends DefaultTableCellRenderer {
        JLabel label;

        public ImageRenderer() {
            label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER); // 이미지 중앙 정렬
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon) {
                label.setIcon((ImageIcon) value);
            } else {
                label.setIcon(null);
            }
            return label;
        }
    }
}
