package com.mscg.jmp3.ui.listener.filetotag;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

import javax.swing.JList;

import com.mscg.jmp3.main.AppLaunch;
import com.mscg.util.transformation.AddTransformationDialog;

public class AddTransformationListener extends GenericFilenameToTagListener {

    public AddTransformationListener(JList actionsList) {
        super(actionsList);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        try {
            AddTransformationDialog dialog = new AddTransformationDialog(AppLaunch.mainWindow, true);
            dialog.setVisible(true);
            LOG.debug("Response from dialog. Saved: " + dialog.isSaved());
        } catch (FileNotFoundException e) {
            LOG.error("Cannot create dialog to add transformations", e);
            AppLaunch.manageError(e);
        }
    }

}
