package com.mscg.jmp3.ui.listener.filetotag;

import java.awt.event.ActionListener;

import javax.swing.JList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericFilenameToTagListener implements ActionListener {

    protected JList actionsList;

    protected Logger LOG;

    public GenericFilenameToTagListener(JList actionsList) {
        this.actionsList = actionsList;
        LOG = LoggerFactory.getLogger(this.getClass());
    }

}
