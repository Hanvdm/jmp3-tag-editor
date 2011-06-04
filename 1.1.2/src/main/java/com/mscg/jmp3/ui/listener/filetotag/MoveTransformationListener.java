package com.mscg.jmp3.ui.listener.filetotag;

import java.awt.event.ActionEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.mscg.jmp3.main.AppLaunch;

public class MoveTransformationListener extends RemoveTransformationsListener {

    protected boolean moveUp;

    public MoveTransformationListener(JList actionsList, Integer index, boolean moveUp) {
        super(actionsList, index);
        this.moveUp = moveUp;
    }

    public MoveTransformationListener(JList actionsList, boolean moveUp) {
        super(actionsList);
        this.moveUp = moveUp;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        try {
            DefaultListModel listModel = (DefaultListModel) actionsList.getModel();

            Object element = listModel.elementAt(index);
            listModel.remove(index);
            listModel.add(index + (moveUp ? -1 : 1), element);
            actionsList.setSelectedValue(element, true);

        } catch(Exception e) {
            LOG.error("Cannot move transformation", e);
            AppLaunch.showError(e);
        }
    }

}
