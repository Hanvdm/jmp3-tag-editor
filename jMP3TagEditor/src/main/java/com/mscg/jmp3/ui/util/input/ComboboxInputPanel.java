package com.mscg.jmp3.ui.util.input;

import java.awt.Component;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

public class ComboboxInputPanel extends InputPanel {

    private static final long serialVersionUID = 569903591307325159L;

    private JComboBox combobox;
    private ComboBoxModel comboboxModel;

    public ComboboxInputPanel(String label, ComboBoxModel comboboxModel) {
        this(label, defaultTopBorder, defaultBottomBorder, comboboxModel);
    }

    public ComboboxInputPanel(String label, int topBorder, int bottomBorder, ComboBoxModel comboboxModel) {
        super(label, topBorder, bottomBorder, false);
        this.comboboxModel = comboboxModel;
        initComponent(topBorder, bottomBorder);
    }

    @Override
    protected Component getValueComponent() {
        if(combobox == null) {
            combobox = new JComboBox();
            combobox.setModel(comboboxModel);
        }
        return combobox;
    }

    @Override
    public String getValue() {
        int selected = combobox.getSelectedIndex();
        if(selected >= 0)
            return combobox.getItemAt(selected).toString();
        else
            return null;
    }

}
