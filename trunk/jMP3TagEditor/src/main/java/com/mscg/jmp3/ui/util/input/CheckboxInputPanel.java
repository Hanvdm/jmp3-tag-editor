package com.mscg.jmp3.ui.util.input;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class CheckboxInputPanel extends InputPanel {

    private static final long serialVersionUID = -4161735650509687770L;

    private JPanel wrapper;
    private JCheckBox checkbox;

    public CheckboxInputPanel(String label, int topBorder, int bottomBorder) {
        this(label, topBorder, bottomBorder, false);
    }

    public CheckboxInputPanel(String label) {
        this(label, false);
    }

    public CheckboxInputPanel(String label, int topBorder, int bottomBorder, boolean selected) {
        super(label, topBorder, bottomBorder);
        checkbox.setSelected(selected);
    }

    public CheckboxInputPanel(String label, boolean selected) {
        super(label);
        checkbox.setSelected(selected);
    }

    @Override
    public Component getValueComponent() {
        if(checkbox == null) {
            checkbox = new JCheckBox();
            wrapper = new JPanel(new BorderLayout());
            wrapper.add(checkbox, BorderLayout.CENTER);
        }
        return wrapper;
    }

    public JCheckBox getCheckbox() {
        return checkbox;
    }

    @Override
    public String getValue() {
        return Boolean.toString(checkbox.isSelected());
    }

    @Override
    public void setValue(String value) {
        checkbox.setSelected(Boolean.parseBoolean(value));
    }

}
