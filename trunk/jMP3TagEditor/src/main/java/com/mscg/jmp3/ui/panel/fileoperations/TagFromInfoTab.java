package com.mscg.jmp3.ui.panel.fileoperations;

import java.io.FileNotFoundException;

import javax.swing.JLabel;


public class TagFromInfoTab extends GenericFileoperationTab {

    private static final long serialVersionUID = 6965212967559350777L;

    public TagFromInfoTab() throws FileNotFoundException {
        super();
    }

    @Override
    protected void initComponents() throws FileNotFoundException {
        add(new JLabel("tag from info"));
    }

}
