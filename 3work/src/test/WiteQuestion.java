package test;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import main.Z_SqlConnection;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WiteQuestion extends JComponent {
    int number;
    JFrame f = new JFrame();
    Font FontN = new Font("맑은 고딕", Font.BOLD, 16);
    JPanel publicP = new JPanel();
    JTable JT;
    private DefaultTableModel TModel;

    String[] tbdata = {"번호", "선생님", "타이틀", "질문", "답변", "문제이미지"};

    WiteQuestion(int number) {
        this.number = number;

        TModel = new DefaultTableModel(tbdata, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            Z_SqlConnection al = new Z_SqlConnection();
            ResultSet re = al.selectTlist("SELECT catalog.*, teacher.name FROM catalog "
                    + "JOIN teacher ON catalog.tno = teacher.tno "
                    + "WHERE uno = " + number);

            while (re.next()) {
                Image img = new ImageIcon("imgs/question/" + re.getInt("type") + ".jpg")
                        .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                JLabel icon = new JLabel(new ImageIcon(img));

                // 데이터 추가
                TModel.addRow(new Object[]{
                        re.getInt("tno"),
                        re.getString("name"),
                        re.getString("title"),
                        re.getString("questionexplan"),
                        re.getString("explan"),
                        icon
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JT = new JTable(TModel);
        JT.getColumnModel().getColumn(5).setCellRenderer(new LabelRenderer());

        // 🔹 TextArea 렌더러 적용 (질문, 답변 칼럼)
        for (int i = 2; i <= 4; i++) {
            JT.getColumnModel().getColumn(i).setCellRenderer(new TextAreaRenderer());
        }

        // 🔹 가운데 정렬 렌더러 적용 (번호, 선생님)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        JT.getColumn("번호").setCellRenderer(centerRenderer);
        JT.getColumn("선생님").setCellRenderer(centerRenderer);

        JT.setRowHeight(155);
        JT.setPreferredScrollableViewportSize(new Dimension(750, 350));

        JPanel P = new JPanel();
        P.setBackground(Color.white);
        P.setBounds(40, 150, 770, 400);
        P.add(new JScrollPane(JT));

        publicP.setBackground(Color.white);
        publicP.setLayout(null);
        publicP.add(P);
        f.add(publicP);
        f.setBounds(660, 340, 850, 600);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    // 🔹 JLabel 렌더러 (이미지 칼럼)
    static class LabelRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JLabel) {
                return (JLabel) value;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    // 🔹 JTextArea 렌더러 (질문, 답변 칼럼)
    static class TextAreaRenderer extends JTextArea implements TableCellRenderer {
        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setFont(new Font("맑은 고딕", Font.BOLD, 15));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");

            // 선택 시 색상 변경
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
        new WiteQuestion(1);
    }
}
