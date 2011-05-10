package com.mscg.jmp3.ui.util.input;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mscg.jmp3.util.Util;

public abstract class InputPanel extends JPanel {

    private static final long serialVersionUID = -3470919751170679651L;

    protected String label;

    protected static final int defaultTopBorder = 0;
    protected static final int defaultBottomBorder = 2;

    protected static Integer panelSize;

    public InputPanel(String label) {
        this(label, true);
    }

    public InputPanel(String label, int topBorder, int bottomBorder) {
        this(label, topBorder, bottomBorder, true);
    }

    protected InputPanel(String label, boolean init) {
        this(label, defaultTopBorder, defaultBottomBorder);
    }

    protected InputPanel(String label, int topBorder, int bottomBorder, boolean init) {
        this.label = label;
        if(init)
            initComponent(topBorder, bottomBorder);
    }

    protected void initComponent(int topBorder, int bottomBorder) {
        JLabel inputLabel = new JLabel(label.endsWith(":") ? label : label + ":");
        Component valueBox = getValueComponent();

        if(panelSize == null) {
            panelSize = Util.getPanelHeightForFont(valueBox.getFont());
        }

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(topBorder, 0, bottomBorder, 0));
        setMaximumSize(new Dimension(Short.MAX_VALUE, panelSize + topBorder + bottomBorder));

        Font font = inputLabel.getFont();
        Rectangle2D bounds = font.getStringBounds(inputLabel.getText(),
                                                  new FontRenderContext(font.getTransform(), true, false));

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.LINE_AXIS));
        wrapper.setMinimumSize(new Dimension(Math.max(140, (int)bounds.getWidth()), panelSize));
        wrapper.setPreferredSize(wrapper.getMinimumSize());
        wrapper.add(inputLabel);
        add(wrapper);

        add(valueBox);
    }

    protected abstract Component getValueComponent();

    public abstract String getValue();

}