package com.mscg.jmp3.ui.listener.filetotag;

import java.awt.event.ActionEvent;

import javax.swing.JList;

import com.mp3.ui.MainWindowInterface;
import com.mscg.jmp3.transformator.StringTransformator;
import com.mscg.jmp3.ui.renderer.elements.StringTransformatorElement;
import com.mscg.jmp3.ui.util.transformation.AddTransformationDialog;

public class EditTransformationListener extends RemoveTransformationsListener {

    public EditTransformationListener(JList actionsList, Integer index) {
        super(actionsList, index);
    }

    public EditTransformationListener(JList actionsList) {
        super(actionsList);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        try {
            StringTransformatorElement element = (StringTransformatorElement)actionsList.getModel().getElementAt(index);

            AddTransformationDialog addDialog = new AddTransformationDialog(MainWindowInterface.getInstance(), true);
            addDialog.setTransformator(element.getTransformator());
            addDialog.setVisible(true);

            if(addDialog.isSaved()) {
                LOG.debug("Replacing edited transformator");
                StringTransformator savedTransformator = addDialog.getTransformator();
                if(savedTransformator != null) {
                    element.setTransformator(savedTransformator);
                    actionsList.invalidate();
                }
            }

        } catch (Exception e) {
            LOG.error("Cannot launch transformation editor", e);
            MainWindowInterface.showError(e);
        }
    }

}
