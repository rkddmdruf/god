package utils;

import javax.swing.*;
import java.awt.*;

public class UI {

    public static JPanel panel(Component... children) {
        var p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        for (var c : children) p.add(c);
        return p;
    }

    public static Component br() {
        var p = new JPanel();
        p.setPreferredSize(new Dimension(9999, 0));
        return p;
    }

    public static Component sp(int w) {
        var p = new JPanel();
        p.setPreferredSize(new Dimension(w, 0));
        p.setOpaque(false);
        return p;
    }

    public static Component vsp(int h) {
        var p = new JPanel();
        p.setPreferredSize(new Dimension(9999, h));
        p.setOpaque(false);
        return p;
    }

    public static void sz(int h, int[] w, JComponent... cs) {
        for (int i = 0; i < cs.length; i++) cs[i].setPreferredSize(new Dimension(w[i], h));
    }
    

    public static JScrollPane scroll(JComponent c, int w, int h) {
        var s = new JScrollPane(c);
        s.setPreferredSize(new Dimension(w, h));
        return s;
    }
}