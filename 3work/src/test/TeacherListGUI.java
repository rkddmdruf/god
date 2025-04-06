package test;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Teacher {
    String name, school, imagePath;
    int totalQuestions, correctAnswers;
    
    public Teacher(String name, String school, String imagePath, int totalQuestions, int correctAnswers) {
        this.name = name;
        this.school = school;
        this.imagePath = imagePath;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
    }
    
    public double getAnswerRate() {
        return totalQuestions == 0 ? 0 : (correctAnswers * 100.0) / totalQuestions;
    }
}

public class TeacherListGUI extends JFrame {
    private JComboBox<String> filterBox;
    private JTable teacherTable;
    private DefaultTableModel tableModel;
    private List<Teacher> teachers;
    private List<Teacher> displayedTeachers;

    public TeacherListGUI() {
        setTitle("선생님 목록");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 상단 필터 박스
        String[] filters = {"전체", "답변률순", "문제풀이가 많은 순"};
        filterBox = new JComboBox<>(filters);
        filterBox.addActionListener(e -> updateTable());
        add(filterBox, BorderLayout.NORTH);
        
        // 테이블 설정
        String[] columnNames = {"사진", "이름", "학교", "답변 개수", "답변률"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        teacherTable = new JTable(tableModel);
        teacherTable.setRowHeight(60);
        teacherTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = teacherTable.getSelectedRow();
                    if (row >= 0) {
                    	System.out.println(row);
                        JOptionPane.showMessageDialog(null, displayedTeachers.get(row).name + " 선택됨");
                    }
                }
            }
        });
        add(new JScrollPane(teacherTable), BorderLayout.CENTER);

        // 데이터 추가
        loadTeachers();
        updateTable();
    }
    
    private void loadTeachers() {
        teachers = new ArrayList<>();
        teachers.add(new Teacher("김선미", "가까대학교", "kim.jpg", 50, 37));
        teachers.add(new Teacher("이은지", "효은대학교", "lee.jpg", 3, 3));
    }
    
    private void updateTable() {
        tableModel.setRowCount(0);
        String selectedFilter = (String) filterBox.getSelectedItem();
        
        displayedTeachers = new ArrayList<>(teachers);
        if ("답변률순".equals(selectedFilter)) {
            displayedTeachers.sort(Comparator.comparingDouble(Teacher::getAnswerRate).reversed());
        } else if ("문제풀이가 많은 순".equals(selectedFilter)) {
            displayedTeachers.sort(Comparator.comparingInt(t -> -t.totalQuestions));
        }
        
        for (Teacher t : displayedTeachers) {
            tableModel.addRow(new Object[]{t.imagePath, t.name, t.school, t.totalQuestions, String.format("%.2f%%", t.getAnswerRate())});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TeacherListGUI().setVisible(true));
    }
}
