package test;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ImageSelectionExample extends JFrame implements ActionListener {

    private JButton openButton;
    private JLabel imageLabel;
    private JFileChooser fileChooser;

    public ImageSelectionExample() {
        setTitle("이미지 파일 선택 및 불러오기");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        openButton = new JButton("이미지 선택");
        openButton.addActionListener(this);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        fileChooser = new JFileChooser();
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(imageFilter);


        JPanel panel = new JPanel(new BorderLayout());
        panel.add(openButton, BorderLayout.NORTH);
        panel.add(new JScrollPane(imageLabel), BorderLayout.CENTER); // ScrollPane 추가

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton) {
            int returnValue = fileChooser.showOpenDialog(this);
            System.out.println(fileChooser.getSelectedFile());
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                
                try {
                   // 이미지 파일 읽기
                    Image image = ImageIO.read(selectedFile);
                    if (image != null) {
                        // 이미지 크기 조절
                        Image scaledImage = image.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
                        ImageIcon imageIcon = new ImageIcon(scaledImage);
                        imageLabel.setIcon(imageIcon);
                        imageLabel.setText(""); // 기존 텍스트 제거
                    } else {
                         imageLabel.setIcon(null);
                        imageLabel.setText("이미지를 불러올 수 없습니다.");
                    }


                } catch (IOException ex) {
                    imageLabel.setIcon(null);
                    imageLabel.setText("이미지를 불러오는 중 오류가 발생했습니다.");
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageSelectionExample::new);
    }
}