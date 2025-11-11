package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import utils.*;
import utils.sp.cl;
import utils.sp.cp;

public class MyPage extends BaseFrame{
	int butNumber = 0;
	JButton[] but = {new sp.cb("내가 좋아한 알바").fontColor(Color.white).BackColor(sp.color).font(sp.font(0, 14)).setBorders(sp.line(sp.color))
				,    new sp.cb("대기중인 알바")  .fontColor(sp.color).BackColor(Color.white).font(sp.font(0, 14)).setBorders(sp.line(sp.color))
				,    new sp.cb("합격한 알바")   .fontColor(sp.color).BackColor(Color.white).font(sp.font(0, 14)).setBorders(sp.line(sp.color))
	};
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(10, 10, 10, 10), null);
	JPanel northPanel = new sp.cp(new GridLayout(0, 3, 10, 10), null, null) {{	for(JButton b : but) add(b);	}};
	JPanel mainPanel = new sp.cp(new BorderLayout(), sp.com(sp.em(40, 5, 30, 5), sp.line), null);
	String[] ILike = "번호,카테고리,브랜드,브랜드명,알바,좋아요 한 날짜".split(",");
	String[] wait = "번호,지원 번호,브랜드,알바,지원 날짜,급여".split(",");
	String[] good = "번호,지원 번호,브랜드,알바,지원 날짜,급여".split(",");
	DefaultTableModel[] model = {
			new DefaultTableModel(ILike, 0) { @Override public boolean isCellEditable(int row, int col) { return false; } } 
			,new DefaultTableModel(wait, 0) { @Override public boolean isCellEditable(int row, int col) { return false; } } 
			,new DefaultTableModel(good, 0) { @Override public boolean isCellEditable(int row, int col) { return false; } } };
	JTable[] t = { new JTable(model[0]), new JTable(model[1]), new JTable(model[2]) };
	JScrollPane sc = new JScrollPane(t[0]);
	public MyPage() {
		setFrame("마이페이지", 650, 500, ()->{});
	}
	@Override
	protected void desing() {

		
		for(int i = 0; i < 3; i++) {final int index = i;
			DefaultTableCellRenderer center = new DefaultTableCellRenderer();
			center.setHorizontalAlignment(JLabel.CENTER);
			for(int j = 0; j < 6; j++) {
				t[i].getColumnModel().getColumn(j).setCellRenderer(center);
			}

			t[i].setRowHeight(100);
			t[i].getTableHeader().setBackground(sp.color);
			t[i].getTableHeader().setForeground(Color.white);
			t[i].getColumnModel().getColumn(0).setPreferredWidth(30);
			t[i].getColumnModel().getColumn(1).setPreferredWidth(50);
			t[i].getColumnModel().getColumn(2).setPreferredWidth(90);
			t[i].getColumnModel().getColumn(i == 0 ? 4 : 3).setPreferredWidth(200);
			
			t[i].getColumnModel().getColumn(i == 0 ? 4 : 3).setCellRenderer(new TableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
						int row, int column) {
					JTextArea t = new sp.cta(value.toString()).setting();
					return t;
				}
			});
			t[i].getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
						int row, int column) {
					JLabel l = new sp.cl(sp.getImg("brand/" + value + ".png", 100, 100));
					return l;
				}
			});
			t[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(t[index].getSelectedColumn() == 2) {
						new BrandInfor(Integer.parseInt(t[index].getValueAt(t[index].getSelectedRow(), t[index].getSelectedColumn()).toString()));
					}
					if(t[index].getSelectedColumn() == 4 || t[index].getSelectedColumn() == 3) {
						new JobInfor(Query.selectText("SELECT * FROM parttimecat.job where jname like ?;"
								, "%" +  t[index].getValueAt(t[index].getSelectedRow(), t[index].getSelectedColumn()).toString() + "%").get(0).getInt(0));
					}
				}
			});
		}
		t[0].getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JTextArea t = new sp.cta(value.toString()).setting();
				return t;
			}
		});
		tables();
		borderPanel.add(northPanel, sp.n);
		mainPanel.add(sc);
		borderPanel.add(mainPanel);
		add(borderPanel);
		
	}

	@Override
	protected void action() {
		for(int j = 0; j < 3; j++) { final int i = j;
			but[i].addActionListener(e->{
				butNumber = i;
				for(int s = 0; s < 3; s++) {
					but[s].setBackground(Color.white);
					but[s].setForeground(sp.color);
				}
				but[i].setBackground(sp.color);
				but[i].setForeground(Color.white);
				tables();
			});
		}
	}
	
	public void tables() {
		List<Row> list = new ArrayList<>();
		if(butNumber == 0) list = Query.MyPage_ILike.select(sp.user.get(0));
		else if(butNumber == 1) list = Query.MyPage_waitJob.select(sp.user.get(0));
		else if(butNumber == 2) list = Query.MyPage_good.select(sp.user.get(0));
		
		int i = list.size();
		for(Row row : list)  if(i -- > 0)  list.get(i).add(0, i+1);
		Query.setTable(model[butNumber], list);

		mainPanel.removeAll();
		mainPanel.add(sc = new JScrollPane(t[butNumber]));
		repaints();
	}
	
	/*
	 * public void ILike(){ List<Row> list =
	 * Query.MyPage_ILike.select(sp.user.get(0)); int i = list.size(); for(Row row :
	 * list) if(i --> 0) list.get(i).add(0, i+1); Query.setTable(model, list);
	 * t.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer() {
	 * 
	 * @Override public Component getTableCellRendererComponent(JTable table, Object
	 * value, boolean isSelected, boolean hasFocus, int row, int column) { JTextArea
	 * t = new sp.cta(value.toString()).setting(); return t; }
	 * 
	 * }); t.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderer() {
	 * 
	 * @Override public Component getTableCellRendererComponent(JTable table, Object
	 * value, boolean isSelected, boolean hasFocus, int row, int column) { JLabel l
	 * = new sp.cl(sp.getImg("brand/" + value + ".png", 100, 100)); return l; } });
	 * t.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
	 * 
	 * @Override public Component getTableCellRendererComponent(JTable table, Object
	 * value, boolean isSelected, boolean hasFocus, int row, int column) { JTextArea
	 * t = new sp.cta(value.toString()).setting(); return t; }
	 * 
	 * }); repaints(); }
	 * 
	 * public void waits(){ List<Row> list =
	 * Query.MyPage_waitJob.select(sp.user.get(0)); int i = list.size(); for(Row row
	 * : list) if(i --> 0) list.get(i).add(0, i+1); Query.setTable(model, list); }
	 * public void good(){ List<Row> list =
	 * Query.MyPage_good.select(sp.user.get(0)); int i = list.size(); for(Row row :
	 * list) if(i --> 0) list.get(i).add(0, i+1); Query.setTable(model, list);
	 * tables(); }
	 */
	public static void main(String[] args) {
		new MyPage();
	}
}
