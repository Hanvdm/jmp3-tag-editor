package com.mscg.jmp3.ui.util.contextmenu;

import javax.swing.JList;
import javax.swing.JPopupMenu;

public interface JPopupMenuFactory {

    public JPopupMenu getPopupMenu(JList list, int index);

}
