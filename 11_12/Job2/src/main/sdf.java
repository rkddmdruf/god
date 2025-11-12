package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class sdf extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private BufferedImage originalImage;
    private double zoomFactor = 1.0;
    private double currentX = 0; // 현재 보이는 영역의 시작 X 좌표 (이미지 좌표계)
    private double currentY = 0; // 현재 보이는 영역의 시작 Y 좌표 (이미지 좌표계)

    public sdf(String imagePath) {
        try {
            originalImage = ImageIO.read(new File(imagePath));
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (originalImage == null) return;

        int srcW = (int) (getWidth() / zoomFactor);
        int srcH = (int) (getHeight() / zoomFactor);
        int srcX = (int) currentX;
        int srcY = (int) currentY;

        // 이미지 경계 체크 (생략됨)

        // 특정 영역(src)을 패널 전체(dst)에 그립니다.
        g.drawImage(originalImage,
                    0, 0, getWidth(), getHeight(), // 대상 사각형 (패널 크기)
                    srcX, srcY, srcX + srcW, srcY + srcH, // 소스 사각형 (이미지 내 확대 영역)
                    this);
    }

    // MouseWheelListener 구현 (확대/축소 로직)
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
            zoomFactor *= 1.1; // 확대
        } else {
            zoomFactor /= 1.1; // 축소
        }
        // zoomFactor 경계 설정 (최소/최대 배율) 로직 추가 필요
        repaint(); // 화면 갱신
    }

    // MouseListener 및 MouseMotionListener 구현 (드래그로 화면 이동 로직)
    // ... (구현 생략)

    // 메인 프레임 예시
    public static void main(String[] args) {
        JFrame frame = new JFrame("Image Zoom Demo");
        sdf panel = new sdf("지도.png");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
