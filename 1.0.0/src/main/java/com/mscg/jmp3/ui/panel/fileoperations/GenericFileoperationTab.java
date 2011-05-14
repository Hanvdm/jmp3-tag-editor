package com.mscg.jmp3.ui.panel.fileoperations;

import java.io.FileNotFoundException;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericFileoperationTab extends JPanel {

    private static final long serialVersionUID = 7012340730082037461L;

    protected Logger LOG;

    public GenericFileoperationTab() throws FileNotFoundException {
        LOG = LoggerFactory.getLogger(this.getClass());
        initComponents();
    }

    protected abstract void initComponents() throws FileNotFoundException;

}
