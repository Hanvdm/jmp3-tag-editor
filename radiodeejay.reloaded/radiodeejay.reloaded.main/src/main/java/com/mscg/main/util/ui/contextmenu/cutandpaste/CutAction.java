package com.mscg.main.util.ui.contextmenu.cutandpaste;

import java.awt.event.ActionEvent;

import javax.swing.text.JTextComponent;

class CutAction extends GenericCutAndPasteAction {

    private static final long serialVersionUID = -677800950359874404L;

    public CutAction(JTextComponent comp, String name) {
        super(comp, name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        comp.cut();
        super.actionPerformed(e);
    }

    @Override
    public boolean checkEnabled() {
        return comp.isEditable() && comp.isEnabled() && comp.getSelectedText() != null;
    }
}
