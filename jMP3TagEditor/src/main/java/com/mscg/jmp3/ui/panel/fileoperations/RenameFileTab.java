package com.mscg.jmp3.ui.panel.fileoperations;

import java.io.FileNotFoundException;

import javax.swing.JLabel;


public class RenameFileTab extends GenericFileoperationTab {

    private static final long serialVersionUID = -257273605874322878L;

    public RenameFileTab() throws FileNotFoundException {
        super();
    }

    @Override
    protected void initComponents() throws FileNotFoundException {
        add(new JLabel("rename file"));
    }

}
