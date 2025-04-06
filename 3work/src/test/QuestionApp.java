package test;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class QuestionApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuestionFrame().setVisible(true));
    }
}

class QuestionFrame extends JFrame {
    public QuestionFrame() {
        setTitle("내 질문");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // 상단 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("📚 내 질문");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);
        
        // 콤보 박스
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JComboBox<String> filterCombo = new JComboBox<>(new String[]{"전체", "수학", "과학", "영어"});
        filterPanel.add(filterCombo);
        add(filterPanel, BorderLayout.SOUTH);
        
        // 테이블 데이터
        String[] columnNames = {"번호", "선생님", "타이틀", "질문", "답변", "문제이미지"};
        Object[][] data = {
            {"1", "박지우", "머리로 고민", "역사가 흘러가는 이유?", "자연법칙 때문입니다.", "수학 문제 1"},
            {"2", "김성민", "머리를 써야 하는 문제", "어떤 기법 사용?", "연립방정식을 사용.", "문제 예제"}
        };
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}
