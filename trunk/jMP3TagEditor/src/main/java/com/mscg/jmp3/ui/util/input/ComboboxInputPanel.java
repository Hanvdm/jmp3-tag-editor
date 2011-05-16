package com.mscg.jmp3.ui.util.input;

import java.awt.Component;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

public class ComboboxInputPanel extends InputPanel {

    private static final long serialVersionUID = 569903591307325159L;

    private JComboBox combobox;
    private ComboBoxModel comboboxModel;
    private Object selectedItem;
    private boolean editable;

    public ComboboxInputPanel(String label, ComboBoxModel comboboxModel) {
        this(label, defaultTopBorder, defaultBottomBorder, comboboxModel, false, null);
    }

    public ComboboxInputPanel(String label, ComboBoxModel comboboxModel, Object selectedItem) {
        this(label, defaultTopBorder, defaultBottomBorder, comboboxModel, false, selectedItem);
    }

    public ComboboxInputPanel(String label, int topBorder, int bottomBorder, ComboBoxModel comboboxModel) {
        this(label, topBorder, bottomBorder, comboboxModel, false, null);
    }

    public ComboboxInputPanel(String label, int topBorder, int bottomBorder, ComboBoxModel comboboxModel,
                              Object selectedItem) {
        this(label, topBorder, bottomBorder, comboboxModel, false, selectedItem);
    }

    public ComboboxInputPanel(String label, ComboBoxModel comboboxModel, boolean editable) {
        this(label, defaultTopBorder, defaultBottomBorder, comboboxModel, editable, null);
    }

    public ComboboxInputPanel(String label, ComboBoxModel comboboxModel, boolean editable,
                              Object selectedItem) {
        this(label, defaultTopBorder, defaultBottomBorder, comboboxModel, editable, selectedItem);
    }

    public ComboboxInputPanel(String label, int topBorder, int bottomBorder, ComboBoxModel comboboxModel,
                              boolean editable) {
        this(label, topBorder, bottomBorder, comboboxModel, editable, null);
    }

    public ComboboxInputPanel(String label, int topBorder, int bottomBorder, ComboBoxModel comboboxModel,
                              boolean editable, Object selectedItem) {
        super(label, topBorder, bottomBorder, false);
        this.comboboxModel = comboboxModel;
        this.editable = editable;
        this.selectedItem = selectedItem;
        initComponent(topBorder, bottomBorder);
    }

    @Override
    public Component getValueComponent() {
        if(combobox == null) {
            combobox = new JComboBox();
            combobox.setModel(comboboxModel);
            combobox.setEditable(editable);
            if(comboboxModel.getSize() >= 1) {
                if(selectedItem == null)
                    combobox.setSelectedIndex(0);
                else {
                    combobox.setSelectedItem(selectedItem);
                    selectedItem = null;
                }
            }
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

    @Override
    public void setValue(String value) {
        for(int i = 0, l = comboboxModel.getSize(); i < l; i++) {
            Object element = comboboxModel.getElementAt(i);
            if(element.toString().equals(value)) {
                combobox.setSelectedItem(value);
                break;
            }
        }
    }

}
