package com.mscg.jmp3.ui.util.input;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputPanel extends JPanel {

    private static final long serialVersionUID = -3470919751170679651L;

    protected String label;
    protected JTextField valueBox;

    public InputPanel(String label) {
        this(label, 0, 2);
    }

    public InputPanel(String label, int topBorder, int bottomBorder) {
        this.label = label;
        initComponent(topBorder, bottomBorder);
    }

    private void initComponent(int topBorder, int bottomBorder) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(topBorder, 0, bottomBorder, 0));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 20 + topBorder + bottomBorder));

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.LINE_AXIS));
        wrapper.setMinimumSize(new Dimension(140, 20));
        wrapper.setPreferredSize(wrapper.getMinimumSize());
        wrapper.add(new JLabel(label.endsWith(":") ? label : label + ":"));
        add(wrapper);
        valueBox = new JTextField();
        valueBox.setToolTipText(label);
        add(valueBox);
    }

    public String getValue() {
        return valueBox.getText();
    }

}