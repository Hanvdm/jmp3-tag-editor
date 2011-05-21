package com.mscg.jmp3.ui.listener.encode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.main.AppLaunch;
import com.mscg.jmp3.ui.panel.EncodeFileCard;
import com.mscg.jmp3.ui.panel.fileoperations.dialog.EncodeFilesDialog;
import com.mscg.jmp3.util.Util;

public class StartEncodingListener implements ActionListener {

    private EncodeFileCard encodeFileCard;

    public StartEncodingListener(EncodeFileCard encodeFileCard) {
        this.encodeFileCard = encodeFileCard;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if(Util.isEmptyOrWhiteSpaceOnly(encodeFileCard.getDestinationFolder().getValue())) {
            AppLaunch.showError(new Exception(Messages.getString("operations.file.encode.destination.choose.empty")));
        }
        else {
            try {
                new EncodeFilesDialog(encodeFileCard, AppLaunch.mainWindow).setVisible(true);
            } catch (FileNotFoundException e) {
            }
        }
    }

}
