package com.mscg.util.contextmenu;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


public class ContextMenuListHandler extends MouseAdapter implements PopupMenuListener {

    protected int idx;

    protected JList list;
    protected JPopupMenuFactory popupMenuFactory;

    public ContextMenuListHandler(JList list, JPopupMenuFactory popupMenuFactory) {
        super();

        this.list = list;
        this.popupMenuFactory = popupMenuFactory;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            idx = list.locationToIndex(new Point(e.getX(), e.getY()));

            if(idx < 0)
                return;

            JPopupMenu contextMenu = popupMenuFactory.getPopupMenu(list, idx);

            // ContextMenuListHandler also handles popup events for the list
            contextMenu.addPopupMenuListener(this);
            // display the popup at the cursor location
            contextMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
        // the user changed his mind and the menu was not selected,clear any
        // state for the current popup
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
        // either the user selected a menu item or changed his mind. No need
        // to do anything here.
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
        if(list.getSelectedIndices().length <= 1)
            list.setSelectedIndex(idx);
    }
}