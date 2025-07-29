package test;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JTableComboBoxEditorExample extends JFrame {

    public JTableComboBoxEditorExample() {
        String[] columnNames = {"Name", "Category"};
        Object[][] data = {
                {"Item 1", "Category A"},
                {"Item 2", "Category B"},
                {"Item 3", "Category A"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);

        // 콤보 박스 편집기 설정
        String[] categories = {"Category A", "Category B", "Category C"};
        JComboBox<String> comboBox = new JComboBox<>(categories);
        DefaultCellEditor editor = new DefaultCellEditor(comboBox);
        table.getColumnModel().getColumn(0).setCellEditor(editor);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setVisible(true);
    }

    public static void main(String[] args) {
        new JTableComboBoxEditorExample();
    }
}
