package com.mscg.jmp3.ui.util.input;

import java.awt.Component;

import javax.swing.JTextField;

public class TextBoxInputPanel extends InputPanel {

    private static final long serialVersionUID = -3068044248405173368L;

    protected JTextField valueBox;

    public TextBoxInputPanel(String label, int topBorder, int bottomBorder) {
        super(label, topBorder, bottomBorder);
    }

    public TextBoxInputPanel(String label) {
        super(label);
    }

    @Override
    protected Component getValueComponent() {
        if(valueBox == null) {
            valueBox = new JTextField();
            valueBox.setToolTipText(label);
        }
        return valueBox;
    }

    @Override
    public String getValue() {
        return valueBox.getText();
    }

}
