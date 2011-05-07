package com.mscg.jmp3.ui.panel.fileoperations;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.mscg.i18n.Messages;
import com.mscg.util.transformation.FilenameTransformationsPanel;


public class TagFromFilenameTab extends GenericFileoperationTab {

    private static final long serialVersionUID = 1071623235908117442L;

    public TagFromFilenameTab() throws FileNotFoundException {
        super();
    }

    @Override
    protected void initComponents() throws FileNotFoundException {
        setLayout(new BorderLayout());

        JPanel transformationsPanel = new FilenameTransformationsPanel();
        transformationsPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("operations.file.maintransform.title")));
        add(transformationsPanel, BorderLayout.PAGE_START);

    }

}
