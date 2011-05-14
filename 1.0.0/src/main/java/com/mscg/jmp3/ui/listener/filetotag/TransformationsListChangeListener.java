package com.mscg.jmp3.ui.listener.filetotag;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TransformationsListChangeListener implements ListDataListener, ListSelectionListener {

    private JList list;
    private JButton button;

    public TransformationsListChangeListener(JList list, JButton button) {
        this.list = list;
        this.button = button;
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        checkEmptinessAndSelection();
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
        checkEmptinessAndSelection();
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        checkEmptinessAndSelection();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        checkEmptinessAndSelection();
    }

    public void checkEmptinessAndSelection() {
        button.setEnabled(list.getModel().getSize() != 0 &&
                          list != null &&
                          list.getSelectedIndices().length != 0);
    }

}
