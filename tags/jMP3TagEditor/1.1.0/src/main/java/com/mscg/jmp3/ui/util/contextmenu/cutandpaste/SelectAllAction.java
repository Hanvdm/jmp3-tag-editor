package com.mscg.jmp3.ui.util.contextmenu.cutandpaste;

import java.awt.event.ActionEvent;

import javax.swing.text.JTextComponent;

public class SelectAllAction extends GenericCutAndPasteAction {

    private static final long serialVersionUID = -2250383646223006444L;

    public SelectAllAction(JTextComponent comp, String name) {
        super(comp, name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        comp.selectAll();
        super.actionPerformed(e);
    }

    @Override
    public boolean checkEnabled() {
        return comp.isEnabled() && comp.getSelectedText() != null;
    }

}
