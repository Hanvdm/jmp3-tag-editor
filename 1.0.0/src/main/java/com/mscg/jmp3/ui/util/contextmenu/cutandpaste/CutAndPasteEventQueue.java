package com.mscg.jmp3.ui.util.contextmenu.cutandpaste;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.mscg.jmp3.i18n.Messages;

public class CutAndPasteEventQueue extends EventQueue {
    protected void dispatchEvent(AWTEvent event) {
        super.dispatchEvent(event);

        // interested only in mouseevents
        if (!(event instanceof MouseEvent))
            return;

        MouseEvent me = (MouseEvent) event;

        // interested only in popuptriggers
        if (!me.isPopupTrigger())
            return;

        // me.getComponent(...) returns the heavy weight component on which
        // event occured
        Component comp = SwingUtilities.getDeepestComponentAt(me.getComponent(), me.getX(), me.getY());

        // interested only in textcomponents
        if (!(comp instanceof JTextComponent))
            return;

        // no popup shown by user code
        if (MenuSelectionManager.defaultManager().getSelectedPath().length > 0)
            return;

        // create popup menu and show
        JTextComponent tc = (JTextComponent) comp;
        JPopupMenu menu = new JPopupMenu();
        CutAction cutAction = new CutAction(tc, Messages.getString("contextmenu.cut"));
        cutAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        CopyAction copyAction = new CopyAction(tc, Messages.getString("contextmenu.copy"));
        copyAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        PasteAction pasteAction = new PasteAction(tc, Messages.getString("contextmenu.paste"));
        pasteAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        DeleteAction deleteAction = new DeleteAction(tc, Messages.getString("contextmenu.delete"));
        deleteAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
        SelectAllAction selectAllAction = new SelectAllAction(tc, Messages.getString("contextmenu.selectall"));
        selectAllAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));

        menu.add(cutAction);
        menu.add(copyAction);
        menu.add(pasteAction);
        menu.add(deleteAction);
        menu.addSeparator();
        menu.add(selectAllAction);

        for(int i = 0, l = menu.getComponentCount(); i < l; i++) {
            Component component = menu.getComponent(i);
            if(component instanceof GenericCutAndPasteAction) {
                GenericCutAndPasteAction action = (GenericCutAndPasteAction) component;
                action.setEnabled(action.checkEnabled());
            }
        }

        Point pt = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), tc);
        menu.show(tc, pt.x, pt.y);
    }
}
