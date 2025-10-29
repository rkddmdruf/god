package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import utils.*;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class MyPage2 extends BaseFrame{
	
	//해야할것 -> 테이블 액션**
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(10, 15, 20, 15), null);
	JPanel tablePanel = new sp.cp(new BorderLayout(), sp.em(30, 5, 5, 5), null);
	JButton[] northButton = {
			new sp.cb("내가 좋아한 알바").fontColor(Color.white).font(sp.font(0, 14)).BackColor(sp.color).setBorders(sp.line(sp.color)),
			new sp.cb("대기중인 알바")  .fontColor(sp.color).font(sp.font(0, 14)).BackColor(Color.white).setBorders(sp.line(sp.color)),
			new sp.cb("합격한 알바")   .fontColor(sp.color).font(sp.font(0, 14)).BackColor(Color.white).setBorders(sp.line(sp.color)),
	};
	//String[] likeT = "번호,카테고리,브랜드,브랜드명,알바,좋아요 한 날짜".split(",");
	//String[] waitT = "번호,지원 번호,브랜드,알바,지원 날짜,지원 자격".split(",");
	//String[] passT = "번호,지원 번호,브랜드,알바,지원 날짜,급여".split(",");
	List<Row> list = new ArrayList<>();
	List<Row> tableHeaderTitle = new ArrayList<>() {{
		add(new Row() {{
			for(String s : "번호,카테고리,브랜드,브랜드명,알바,좋아요 한 날짜".split(",")) add(s);
		}});
		add(new Row() {{
			for(String s : "번호,지원 번호,브랜드,알바,지원 날짜,지원 자격".split(",")) add(s);
		}});
		add(new Row() {{
			for(String s : "번호,지원 번호,브랜드,알바,지원 날짜,급여".split(",")) add(s);
		}});
	}};
	
	String[] queryString = {"SELECT category.cname, brand.bno, brand.bname, job.jname, likes.ldate FROM parttimecat.likes "
			+ "left join job on job.jno = likes.jno "
			+ "left join brand on job.bno = brand.bno "
			+ "left join category on category.cno = brand.cno "
			+ " where uno = ?;",
			
			"SELECT apply.apno, brand.bno, job.jname, apply.apdate, job.jgrade FROM parttimecat.apply "
			+ "left join job on apply.jno = job.jno "
			+ "left join brand on brand.bno = job.bno "
			+ "where uno = ? and apok = 0;",
			
			"SELECT apply.apno, brand.bno, job.jname, apply.apdate, job.jmoney FROM parttimecat.apply "
			+ "left join job on apply.jno = job.jno "
			+ "left join brand on brand.bno = job.bno "
			+ "where uno = ? and apok = 1;"
	};
	DefaultTableModel[] tmodel = {
			new DefaultTableModel(tableHeaderTitle.get(0).toArray(), 0) {
				@Override
				public boolean isCellEditable(int row, int col) {
					return false;
				}
			}
			, new DefaultTableModel(tableHeaderTitle.get(1).toArray(), 0) {
				@Override
				public boolean isCellEditable(int row, int col) {
					return false;
				}
			}
			, new DefaultTableModel(tableHeaderTitle.get(2).toArray(), 0) {
				@Override
				public boolean isCellEditable(int row, int col) {
					return false;
				}
			}};
	JTable table = new JTable(tmodel[0]);
	MouseAdapter ma = new MouseAdapter() {
	};
	public MyPage2() {
		setFrame("마이페이지", 650, 450, ()->{new Main();});
	}
	@Override
	protected void desing() {
		
		borderPanel.add(new sp.cp(new GridLayout(0, 3, 10,10),null, null) {{
			for(JButton but : northButton) add(but);
		}}, sp.n);
		
		table.getTableHeader().setBackground(sp.color);
		table.getTableHeader().setForeground(Color.white);
		setTable(0);
		
		tablePanel.add(new JScrollPane(table));
		borderPanel.add(tablePanel);
		
		add(borderPanel);
	}

	private void setTable(int n) {
		list = Query.select(queryString[n], sp.user.get(0));
		int i = 1;
		for(Row row : list) {
			row.add(0, i);
			i++;
		}
		table.setModel(tmodel[n]);
		Query.setTable(tmodel[n], list);
		System.out.println(list);
		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
		centerRender.setHorizontalAlignment(SwingConstants.CENTER);
		for(int j = 0; j < tmodel[n].getColumnCount(); j++) {
			table.getColumnModel().getColumn(j).setCellRenderer(centerRender);
		}
		if(n == 0) {
			table.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
						int row, int column) {
					return new JTextArea(value.toString()) {{
						setLineWrap(true);
					}};
				}
			});
		}
		table.getColumnModel().getColumn(n == 0 ? 4 : 3).setCellRenderer(new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				return new JTextArea(value.toString()) {{
					setLineWrap(true);
				}};
			}
		});
		table.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				return new JLabel(sp.getImg("src/brand/" + value + ".png", 120, 100));
			}
		});
		table.removeMouseListener(ma);
		table.addMouseListener(ma = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int min = table.getColumnModel().getColumn(0).getWidth() + table.getColumnModel().getColumn(1).getWidth();
				int max = min + table.getColumnModel().getColumn(2).getWidth();
				String s = "" + e.getY();
				int low = s.length() == 2 ? 0 : Integer.parseInt(s.substring(0, 1).toString());
				
				if(min < e.getX() && max > e.getX()) {
					sp.MyPage_BrandInf = true;
					new BrandInf(list.get( (s.length() == 2 ? 0 : Integer.parseInt(s.substring(0, 1).toString()) ) ).getInt(2));
					System.out.println("sdfsdfdsf");
					dispose();
				}
				
				boolean tf = tmodel[n].getColumnName(3).equals("알바");
				min = max + (tf ? 0 : table.getColumnModel().getColumn(3).getWidth());
				max = min + (tf ? table.getColumnModel().getColumn(3).getWidth() : table.getColumnModel().getColumn(4).getWidth());
				if(min < e.getX() && max > e.getX()) {
					Row row = Query.select("SELECT bname FROM parttimecat.brand where bno = ?;", list.get(low).get(2)).get(0);
					Row row1 = Query.select("SELECT * FROM parttimecat.job where jname like ?", ("%" + list.get(low).get(3) + "%")).get(0);
					row1.forEach(rows -> {
						row.add(rows);
					});
					new JobInfor(row);
					sp.MyPage_JobInf = true;
					dispose();
				}
			}
		});
		table.setRowHeight(100);
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(50);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(n == 0 ? 4 : 3).setPreferredWidth(200);
	}
	@Override
	protected void action() {
		for(int j = 0; j < 3; j++) { final int i = j;
			northButton[i].addActionListener(e->{
				if(northButton[i].getBackground() == Color.white) {
					for(JButton but : northButton) {
						but.setBackground(northButton[i] == but ? sp.color : Color.white);
						but.setForeground(northButton[i] == but ? Color.white : sp.color);
					}
					setTable(i);
				}
			});
			
		}
	}
	public static void main(String[] args) {
		new MyPage2();
	}
}
