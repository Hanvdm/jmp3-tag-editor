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
        super(label, topBorder, bottomBorder);
    }

    public CheckboxInputPanel(String label) {
        super(label);
    }

    @Override
    protected Component getValueComponent() {
        if(checkbox == null) {
            checkbox = new JCheckBox();
            wrapper = new JPanel(new BorderLayout());
            wrapper.add(checkbox, BorderLayout.CENTER);
        }
        return wrapper;
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
