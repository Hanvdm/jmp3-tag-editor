package com.mscg.main.util.ui.contextmenu.cutandpaste;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

import javax.swing.text.JTextComponent;

public class PasteAction extends GenericCutAndPasteAction {

    private static final long serialVersionUID = -4578342048116795805L;

    public PasteAction(JTextComponent comp, String name) {
        super(comp, name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        comp.paste();
        super.actionPerformed(e);
    }

    @Override
    public boolean checkEnabled() {
        if (comp.isEditable() && comp.isEnabled()) {
            Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
            return contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        } else
            return false;
    }

}
