package com.mscg.jmp3.util;

import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class Util {

    public static Integer panelHeight;

    public static int getPanelHeightForFont(Font font) {
        if(panelHeight == null) {
            JPanel panel = new JPanel();
            JTextField field = new JTextField();
            panel.add(field);
            panelHeight = (int)field.getPreferredSize().getHeight();
        }
        return panelHeight;
//        LineMetrics lineMetrics = font.getLineMetrics("f_^", new FontRenderContext(font.getTransform(), true, false));
//        System.out.println("Line metrics: " + lineMetrics.getHeight() + ", " + lineMetrics.getAscent());
//        Rectangle2D bound = font.getStringBounds("_^", new FontRenderContext(font.getTransform(), true, false));
//        System.out.println("Bounds: " + bound);
//        return ((int)Math.floor((bound.getHeight() - bound.getY()) / 10) * 10);
    }

}
