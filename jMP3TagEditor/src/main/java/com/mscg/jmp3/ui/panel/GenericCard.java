package com.mscg.jmp3.ui.panel;

import java.io.FileNotFoundException;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.jmp3.ui.frame.MainWindow;

public abstract class GenericCard extends JPanel {

    private static final long serialVersionUID = -1890172586837189557L;

    protected Logger LOG;
    protected MainWindow mainWindow;

    public GenericCard(MainWindow mainWindow) throws FileNotFoundException {
        LOG = LoggerFactory.getLogger(this.getClass());
        this.mainWindow = mainWindow;

        initComponents();
    }

    protected abstract void initComponents() throws FileNotFoundException;

}
