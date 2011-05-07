package com.mscg.jmp3.ui.util.input;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class InputPanel extends JPanel {

    private static final long serialVersionUID = -3470919751170679651L;

    protected String label;

    protected static final int defaultTopBorder = 0;
    protected static final int defaultBottomBorder = 2;

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
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(topBorder, 0, bottomBorder, 0));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 20 + topBorder + bottomBorder));

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.LINE_AXIS));
        wrapper.setMinimumSize(new Dimension(140, 20));
        wrapper.setPreferredSize(wrapper.getMinimumSize());
        wrapper.add(new JLabel(label.endsWith(":") ? label : label + ":"));
        add(wrapper);
        Component valueBox = getValueComponent();
        add(valueBox);
    }

    protected abstract Component getValueComponent();

    public abstract String getValue();

}