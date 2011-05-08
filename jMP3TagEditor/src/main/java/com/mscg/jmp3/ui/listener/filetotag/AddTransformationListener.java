package com.mscg.jmp3.ui.listener.filetotag;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.mscg.jmp3.main.AppLaunch;
import com.mscg.jmp3.transformator.StringTransformator;
import com.mscg.jmp3.ui.renderer.elements.StringTransformatorElement;
import com.mscg.jmp3.ui.util.transformation.AddTransformationDialog;

public class AddTransformationListener extends GenericFilenameToTagListener {

    public AddTransformationListener(JList actionsList) {
        super(actionsList);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        try {
            AddTransformationDialog dialog = new AddTransformationDialog(AppLaunch.mainWindow, true);
            dialog.setVisible(true);
            if(dialog.isSaved()) {
                StringTransformator transformator = dialog.getTransformator();
                if(transformator != null) {
                    LOG.debug("Adding transformator \"" + transformator.getName() + "\"");
                    DefaultListModel model = (DefaultListModel) actionsList.getModel();
                    model.addElement(new StringTransformatorElement(transformator));
                }
            }
        } catch (FileNotFoundException e) {
            AppLaunch.manageError(e);
        }
    }

}
