package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import utils.*;
import utils.sp.*;

public class MyPage extends BaseFrame{

	JPanel borderPanel = new sp.cp(new BorderLayout(0, 40), sp.em(0, 10, 10, 10), null);
	JPanel northPanel = new cp(new GridLayout(0,3, 15,15), null, null);
	String[] butString = "내가 좋아한 알바,대기중인 알바,합격한 알바".split(",");
	JButton[] but = new cb[3];
	int clickBut = 0;
	DefaultTableModel tmodel = null;
	MyPage(){
		setFrame("마이페이지", 650, 500, ()->{});
	}
	@Override
	protected void desing() {
		for(int i = 0; i < 3; i++) {
			but[i] = new cb(butString[i]).setBorders(sp.line(sp.color)).fontColor(sp.color).size(0, 30).font(sp.font(0, 15)).backColor(Color.white);
			northPanel.add(but[i]);
		}
		
		but[0].setBackground(sp.color);
		but[0].setForeground(Color.white);
		
		borderPanel.add(northPanel, sp.n);
		borderPanel.add(new cl(""));// 이거 그냥 있는거 에러 방지
		setLikesJob();
		add(borderPanel);
	}

	@Override
	protected void action() {
		for(int i = 0; i < 3; i++) {
			final int index = i;
			but[i].addActionListener(e->{
				if(clickBut != index) {
					clickBut = index;
					setButColor();
					if(clickBut == 0)
						setLikesJob();
					else
						setApplyJob();
					rp();
				}
			});
		}
	}
	private void setLikesJob() {
		borderPanel.remove(1);
		String[] string = "번호, 카테고리, 브랜드, 브랜드명, 알바, 좋아요 한 날짜".split(", ");
		List<Row> list = Query.MyPage_Likes.select(sp.user.getString(0));
		setRank(list);
		DefaultTableModel t = new DefaultTableModel(string ,0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(t);
		
		setCenter(table, string.length);
		table.setRowHeight(100);
		setImg(table, 2);
		setJTextArea(table, 4);
		setJTextArea(table, 1);
		table.addMouseListener(setBrandAction(table));
		table.addMouseListener(setJobAction(table, 4));
		table.getTableHeader().setBackground(sp.color);
		table.getTableHeader().setForeground(Color.white);
		Query.setTable(t, list);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(200);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		borderPanel.add(new JScrollPane(table));
		
	}
	private void setApplyJob() {
		borderPanel.remove(1);
		String[] string = ("번호, 지원 번호, 브랜드, 알바, 지원 날짜, " + (clickBut == 1 ? "지원 자격" : "급여")).split(", ");
		List<Row> list = (clickBut == 1 ? Query.MyPage_witeJob : Query.MyPage_Pass).select(sp.user.get(0));
		setRank(list);
		for(int i = 0; i < list.size(); i++) {
				list.get(i).set(5, (clickBut == 2 ? sp.form.format(list.get(i).getInt(5)) :
					list.get(i).getInt(5) == 0 ? "무관" : (list.get(i).getInt(5) == 1 ? "대학" : "고등")));
		}
		DefaultTableModel t = new DefaultTableModel(string ,0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(t);
		setCenter(table, string.length);
		table.setRowHeight(100);
		setImg(table,2);
		setJTextArea(table, 3);
		table.addMouseListener(setBrandAction(table));
		table.addMouseListener(setJobAction(table, 3));
		table.getTableHeader().setBackground(sp.color);
		table.getTableHeader().setForeground(Color.white);
		Query.setTable(t, list);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(200);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		borderPanel.add(new JScrollPane(table));
	}
	
	private MouseAdapter setBrandAction(JTable table) {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					if(table.getSelectedColumn() == 2) {
						new brandInfor(Integer.parseInt(table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString()));
					}
				}
			}
		};
	}
	private MouseAdapter setJobAction(JTable table, int index) {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					if(table.getSelectedColumn() == index) {
						new JobInfor(Query.selectText("select * from job where jname = ?"
							, table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString()).get(0).getInt(0)
						);
					}
				}
			}
		};
	}
	private void setCenter(JTable table, int length) {
		DefaultTableCellRenderer center = new DefaultTableCellRenderer();
		center.setHorizontalAlignment(JLabel.CENTER);
		for(int i = 0; i < length; i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(center);
		}
	}
	private void setImg(JTable table, int index) {
		table.getColumnModel().getColumn(index).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				return new cl(sp.getImg("datafiles/brand/" + value + ".png", 100,100));
			}
		});
	}
	private void setJTextArea(JTable table, int index) {
		table.getColumnModel().getColumn(index).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				return new ca(value.toString()).setting();
			}
		});
	}
	private void setRank(List<Row> list) {
		for(int i = 0; i < list.size(); i++) 
			list.get(i).add(0, i+1);
	}

	private void setButColor() {
		for(JButton b : but) {
			b.setBackground(but[clickBut] == b ? sp.color : Color.white);
			b.setForeground(but[clickBut] == b ? Color.white : sp.color);
		}
		rp();
	}
	public static void main(String[] args) {
		new MyPage();
	}
}
