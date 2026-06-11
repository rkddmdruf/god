package orms;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * BoxLayout 기반 레이아웃 유틸. {@code box(정렬, 앞·요소간·뒤 여백, 컴포넌트…)}로 행/열을 조립한다.
 * 단일 위젯은 maxSize=preferredSize로 고정해 의도치 않은 확대를 막고, HGAP/VGAP glue로 정렬·밀기.
 */
public final class BoxUI {

    /**
     * 폭 고정 JLabel. 폼 라벨 컬럼 정렬용.
     */
    public static JLabel L(String text, int width) {
        var l = new JLabel(text);
        var pref = l.getPreferredSize();
        // width가 텍스트 실제 폭보다 좁으면 글자가 짤린다 → 둘 중 큰 값으로 (정렬 폭은 유지, 짤림 방지)
        l.setPreferredSize(new Dimension(Math.max(width, pref.width), pref.height));
        return l;
    }

    /**
     * ui() 단독 행으로 들어가서 h 픽셀 세로 간격.
     */
    public static JComponent VGAP(int h) {
        return (JComponent) Box.createVerticalStrut(h);
    }

    public static JComponent VGAP() {
        return (JComponent) Box.createVerticalGlue();
    }

    public static JComponent HGAP() {
        return (JComponent) Box.createHorizontalGlue();
    }

    /** 가로로 꽉 채움(maxSize 가로 MAX). box가 preferred로 덮지 않음. */
    public static <T extends JComponent> T fillWidth(T c) {
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
        return c;
    }

    /** 세로로 꽉 채움(maxSize 세로 MAX). 테이블/스크롤 등 세로 확장용. */
    public static <T extends JComponent> T fillHeight(T c) {
        c.setMaximumSize(new Dimension(c.getPreferredSize().width, Integer.MAX_VALUE));
        return c;
    }

    /** 가로·세로 모두 채움(maxSize 양축 MAX). */
    public static <T extends JComponent> T fill(T c) {
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return c;
    }

    public static int ROW = 0;
    public static int COL = 1;

    public static JPanel box(int alignment, int start, int inner, int end, JComponent... comps) {
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, alignment));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(alignment == ROW ? Box.createHorizontalStrut(start) : Box.createVerticalStrut(start));
        for (int i = 0; i < comps.length; ++i) {
            var c = comps[i];
            c.setAlignmentX(Component.CENTER_ALIGNMENT);
            // 중첩 box(JPanel)·glue(Filler)·fill 명시(maxSize set)는 늘어나야 하므로 제외, 단일 위젯만 크기 고정
            if (!(c instanceof JPanel) && !(c instanceof Box.Filler) && !c.isMaximumSizeSet()) c.setMaximumSize(c.getPreferredSize());
            panel.add(c);
            if (i < comps.length - 1) panel.add(alignment == ROW ? Box.createHorizontalStrut(inner) : Box.createVerticalStrut(inner));
        }
        panel.add(alignment == ROW ? Box.createHorizontalStrut(end) : Box.createVerticalStrut(end));
        // ROW 박스는 부모(세로 박스) 안에서 가로로 꽉 차게 → 내부 HGAP glue 작동
        if (alignment == ROW) panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }

}
