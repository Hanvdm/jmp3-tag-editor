package com.mscg.jmp3.ui.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class IconedListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -4493176853637858944L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(value != null && value instanceof IconedListElement) {
            setIcon(((IconedListElement) value).getIcon());
        }
        return this;
    }

}
