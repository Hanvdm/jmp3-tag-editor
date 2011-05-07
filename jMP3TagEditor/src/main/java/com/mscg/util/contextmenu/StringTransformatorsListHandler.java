package com.mscg.util.contextmenu;

import java.awt.event.MouseEvent;

import javax.swing.JList;

import com.mscg.jmp3.ui.listener.filetotag.EditTransformationListener;

public class StringTransformatorsListHandler extends ContextMenuListHandler {

    private EditTransformationListener listener;

    public StringTransformatorsListHandler(JList list, JPopupMenuFactory popupMenuFactory) {
        super(list, popupMenuFactory);
        listener = new EditTransformationListener(list);
    }

    @Override
    public void mouseClicked(MouseEvent ev) {
        if(ev.getClickCount() == 2) {
            int idx = list.getSelectedIndex();
            if(idx < 0)
                return;
            listener.setListIndex(idx);
            listener.actionPerformed(null);
        }
    }

}
