package com.mscg.jmp3.ui.util.contextmenu.cutandpaste;

import java.awt.event.ActionEvent;

import javax.swing.text.JTextComponent;

public class CopyAction extends GenericCutAndPasteAction {

    private static final long serialVersionUID = 348020293322220140L;

    public CopyAction(JTextComponent comp, String name) {
        super(comp, name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        comp.copy();
        super.actionPerformed(e);
    }

    @Override
    public boolean checkEnabled() {
        return comp.isEnabled() && comp.getSelectedText() != null;
    }

}
