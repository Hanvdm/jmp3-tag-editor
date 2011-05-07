package com.mscg.jmp3.ui.panel;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.ui.frame.MainWindow;
import com.mscg.jmp3.ui.panel.fileoperations.RenameFileTab;
import com.mscg.jmp3.ui.panel.fileoperations.TagFromFilenameTab;

public class FilenameOperationsCard extends GenericCard {

    private static final long serialVersionUID = -2264127654101769869L;
    private JButton startButton;

    public FilenameOperationsCard(MainWindow mainWindow) throws FileNotFoundException {
        super(mainWindow);
    }

    @Override
    protected void initComponents() throws FileNotFoundException {
        setLayout(new BorderLayout());

        // buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));

        buttonsPanel.add(Box.createVerticalGlue());

        startButton = new JButton();
        startButton.setIcon(new ImageIcon(ThemeManager.getIcon(IconType.START)));
        startButton.setToolTipText(Messages.getString("operations.start"));
        buttonsPanel.add(startButton);

        buttonsPanel.add(Box.createVerticalGlue());

        add(buttonsPanel, BorderLayout.LINE_END);

        // tabbed panel for actions on files
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(Messages.getString("operations.file.tagfromfile"), null,
                          new TagFromFilenameTab(), Messages.getString("operations.file.tagfromfile.tooltip"));
        tabbedPane.addTab(Messages.getString("operations.file.renamefile"), null,
                          new RenameFileTab(), Messages.getString("operations.file.renamefile.tooltip"));
        add(tabbedPane, BorderLayout.CENTER);
    }
}
