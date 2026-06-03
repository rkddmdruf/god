package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import utils.CFrame;
import utils.Connections;
import utils.Data;
import utils.UI;
import utils.User;

public class EmpList extends CFrame{
	
	DefaultTableModel tm = new DefaultTableModel("직원코드,이름,부서,지책,입사일,상태".split(","), 0);
	
	JTable table = new JTable(tm) {{
		setShowGrid(false);
		
		JTableHeader th =  getTableHeader();
		th.setBorder(null);
		th.setDefaultRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				label.setBorder(null);
				return label;
			}
		});
	}};
	
	JScrollPane sc = new JScrollPane(table);
	JTextField tf = new JTextField() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			if(tf.getText().isBlank()) {
				FontMetrics fm = getFontMetrics(getFont());
				int y = (getHeight() / 2) + (fm.getAscent() / 2);
				g.drawString("이름 또는 코드 검색", 5, y);
			}
		};
	};
	JComboBox<String> team = new JComboBox<String>("전체 부서, 개발, 영업, 관리, 생산".split(", "));
	JButton serch = new JButton("검색") {{
		setBackground(Color.white);
	}};
	JButton empInsert = new JButton("+직원 등록");
	JButton empInforChange = new JButton("상세/수정");
	JButton delete = new JButton("삭제") {{
		setBackground(Color.white);
	}};
	boolean serchBool = false;
	public EmpList() {
		borderPanel.add(new JPanel(new BorderLayout(0, 0)) {{
			setBackground(Color.white);
			setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			add(new JLabel("로그인: " + (User.admin ? "admin" : User.getUser().getString(1))), BorderLayout.WEST);
			add(new JLabel(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(User.loginDate) + "", JLabel.RIGHT), BorderLayout.EAST);
		}}, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.setBackground(Color.white);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		panel.add(new JPanel(new BorderLayout()) {{
			setBackground(Color.white);
			int[] arrint = { 150, 100, 70, 100, 100 };
			UI.sz(30, arrint, tf, team, serch, empInsert, empInforChange); 
			JPanel p = UI.panel(tf, team, serch, UI.sp(50), empInsert, empInforChange);
			p.setBackground(Color.white);
			add(p, BorderLayout.WEST);
		}});
		panel.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)) {{
			setBackground(Color.white);
			add(delete);
		}});
		
		borderPanel.add(panel, BorderLayout.NORTH);
		sc.setBorder(BorderFactory.createLineBorder(Color.black));
		sc.setBackground(Color.white);
		borderPanel.add(new JPanel(new BorderLayout(5, 5)) {{
			setBackground(Color.white);
			setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
			add(sc);
			add(new JLabel("총 5명 표시 중") {{
				setFont(new Font("맑은 고딕", 1, 10));
			}}, BorderLayout.SOUTH);
		}});
		setTable();
		team.addItemListener(e -> {
		    if (e.getStateChange() == ItemEvent.SELECTED)
		        setTable();
		});
		serch.addActionListener(e -> {
			serchBool = true;
			setTable();
		});
		setFrameCd("직원 목록", 625, 400, () -> {});
		
	}
	
	private void setTable() {
		List<Data> list = Connections.select("select * from emps");
		if(team.getSelectedIndex() != 0) {
			list = list.stream()
					.filter(e -> e.getInt(3) == team.getSelectedIndex() - 1)
					.collect(Collectors.toList());
		}
		if(serchBool) {
			for(Data data : list) {
				String text = tf.getText();
				if(!data.getString(1).contains(text) && !data.getString(2).contains(text))
					list.remove(data);
			}
		}
		
		//"직원코드,이름,부서,지책,입사일,상태"
		for(Data data : list) {
			Vector<Object> v = new Vector<>();
			v.add(data.getString(2));
			v.add(data.getString(1));
			tm.addRow(v);
		}
		
		serchBool = false;
		revalidate();
		repaint();
	}
	public static void main(String[] args) {
		new EmpList();
	}
}
