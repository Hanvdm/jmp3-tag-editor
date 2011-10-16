package com.mscg.jmp3.ui.util.input;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConstantInputPanel extends InputPanel {

    private static final long serialVersionUID = -7557034981359755947L;

    protected JPanel wrapper;
    protected JLabel value;

    public ConstantInputPanel(String label, int topBorder, int bottomBorder) {
        super(label, topBorder, bottomBorder);
    }

    public ConstantInputPanel(String label) {
        super(label);
    }

    @Override
    public Component getValueComponent() {
        if(value == null) {
            value = new JLabel();
            value.setToolTipText(label);

            wrapper = new JPanel(new BorderLayout());
            wrapper.add(value, BorderLayout.CENTER);
        }
        return wrapper;
    }

    @Override
    public String getValue() {
        return value.getText();
    }

    @Override
    public void setValue(String value) {
        this.value.setText(value);
    }

}
