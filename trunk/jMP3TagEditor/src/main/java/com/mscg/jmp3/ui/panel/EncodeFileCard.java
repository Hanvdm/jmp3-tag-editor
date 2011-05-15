package com.mscg.jmp3.ui.panel;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JLabel;

import com.mscg.jmp3.ui.frame.MainWindow;

public class EncodeFileCard extends GenericStartableCard {

    private static final long serialVersionUID = 1615134393808569470L;

    public EncodeFileCard(MainWindow mainWindow) throws FileNotFoundException {
        super(mainWindow);
    }

    @Override
    protected ActionListener getStartButtonListener() {
        return null;
    }

    @Override
    protected Component getCenterComponent() throws FileNotFoundException {
        return new JLabel("encode");
    }

}
