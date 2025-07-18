package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PopupMenuExample extends JFrame {
    private JPopupMenu popupMenu;
    private JMenuItem menuItem1, menuItem2;

    public PopupMenuExample() {
        setTitle("Popup Menu Example");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // PopupMenu 생성
        popupMenu = new JPopupMenu();
        menuItem1 = new JMenuItem("메뉴 항목 1");
        popupMenu.add(menuItem1);

        // 컨텐트 팬에 MouseListener 추가
        getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showPopupMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopupMenu(e);
            }

            private void showPopupMenu(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { // 우클릭 감지
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new PopupMenuExample();
    }
}
