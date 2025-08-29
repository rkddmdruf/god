package utils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DragPanel extends JPanel implements MouseListener, MouseMotionListener {

    private Point clickOffset; // 드래그 시작 위치와 컴포넌트의 상대적인 위치
    private boolean isDragging = false; // 드래그 중인지 여부

    public DragPanel() {
        addMouseListener(this);
        addMouseMotionListener(this);
        setPreferredSize(new Dimension(200, 200)); // 패널 크기 설정
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, getWidth(), getHeight()); // 배경 채우기
    }


    @Override
    public void mousePressed(MouseEvent e) {
        clickOffset = e.getPoint(); // 마우스 클릭 위치 저장
        SwingUtilities.convertPointToScreen(clickOffset, this); // 상대 좌표 -> 화면 좌표
        Point panelLocation = getLocationOnScreen();
        clickOffset.x -= panelLocation.x;
        clickOffset.y -= panelLocation.y;

        isDragging = true; // 드래그 시작 플래그 설정
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isDragging) {
            Point newLocation = e.getLocationOnScreen();
            setLocation(newLocation.x - clickOffset.x, 0); // 패널 위치 이동
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false; // 드래그 종료 플래그 해제
    }

    // MouseListener 인터페이스의 나머지 메서드 구현 (필요에 따라 구현)
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}


    public static void main(String[] args) {
        JFrame frame = new JFrame("Drag Panel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new DragPanel());
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}
