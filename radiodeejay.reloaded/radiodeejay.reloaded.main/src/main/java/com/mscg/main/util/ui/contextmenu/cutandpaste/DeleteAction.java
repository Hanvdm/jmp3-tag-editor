package com.mscg.main.util.ui.contextmenu.cutandpaste;

import java.awt.event.ActionEvent;

import javax.swing.text.JTextComponent;

public class DeleteAction extends GenericCutAndPasteAction {

    private static final long serialVersionUID = 199608488339893847L;

    public DeleteAction(JTextComponent comp, String name) {
        super(comp, name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        comp.replaceSelection(null);
        super.actionPerformed(e);
    }

    @Override
    public boolean checkEnabled() {
        return comp.isEditable() && comp.isEnabled() && comp.getSelectedText() != null;
    }

}
