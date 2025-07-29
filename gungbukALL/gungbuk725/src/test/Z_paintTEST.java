package test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Z_paintTEST extends JFrame {

    private List<ImageIcon> images = new ArrayList<>();
    private int currentImageIndex = 0;
    private JLabel imageLabel;
    private Timer timer;

    public Z_paintTEST() {
        setTitle("Image Blinking");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // 이미지 로드 (실제 이미지 경로로 변경해야 함)
        images.add(new ImageIcon(new ImageIcon(getClass().getResource("/img/1.jpg")).getImage()));
        images.add(new ImageIcon(new ImageIcon(getClass().getResource("/img/2.jpg")).getImage()));
        images.add(new ImageIcon(new ImageIcon(getClass().getResource("/img/3.jpg")).getImage()));

        imageLabel = new JLabel();
        imageLabel.setIcon(images.get(currentImageIndex));
        add(imageLabel);

        // 타이머 설정 (1초 간격)
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	System.out.println("df");
            }
        });

        setVisible(true);
        timer.start(); // 타이머 시작
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Z_paintTEST();
            }
        });
    }
}