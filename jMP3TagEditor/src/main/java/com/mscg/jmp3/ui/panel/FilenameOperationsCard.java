package com.mscg.jmp3.ui.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.main.AppLaunch;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.ui.frame.MainWindow;
import com.mscg.jmp3.ui.panel.fileoperations.RenameFileTab;
import com.mscg.jmp3.ui.panel.fileoperations.TagFromFilenameTab;
import com.mscg.jmp3.ui.panel.fileoperations.dialog.ExecuteTagCreationDialog;

public class FilenameOperationsCard extends GenericCard {

    private static final long serialVersionUID = -2264127654101769869L;
    private JButton startButton;
    private JTabbedPane tabbedPane;
    private TagFromFilenameTab tagFromFilenameTab;
    private RenameFileTab renameFileTab;

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
        startButton.addActionListener(new StartButtonListener());
        buttonsPanel.add(startButton);

        buttonsPanel.add(Box.createVerticalGlue());

        add(buttonsPanel, BorderLayout.LINE_END);

        tabbedPane = new JTabbedPane();
        tagFromFilenameTab = new TagFromFilenameTab();
        tabbedPane.addTab(Messages.getString("operations.file.tagfromfile"), null,
                          tagFromFilenameTab, Messages.getString("operations.file.tagfromfile.tooltip"));
        renameFileTab = new RenameFileTab();
        tabbedPane.addTab(Messages.getString("operations.file.renamefile"), null,
                          renameFileTab, Messages.getString("operations.file.renamefile.tooltip"));
        add(tabbedPane, BorderLayout.CENTER);
    }

    private class StartButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ev) {
            Component shownCard = tabbedPane.getSelectedComponent();
            if(shownCard == tagFromFilenameTab) {
                LOG.debug("Starting tag creation");
                try {
                    ExecuteTagCreationDialog dialog = new ExecuteTagCreationDialog(tagFromFilenameTab,
                                                                                   AppLaunch.mainWindow,
                                                                                   true);
                    dialog.setTab(tagFromFilenameTab);
                    dialog.setVisible(true);
                } catch(Exception e) {
                    LOG.error("Cannot show tag creation dialog", e);
                    AppLaunch.showError(e);
                }
            }
            else if(shownCard == renameFileTab) {
                LOG.debug("Starting file rename");
            }
            else {
                LOG.warn("Cannot determine which panel is active");
                AppLaunch.showError(new Exception("Uknown panel selected"));
            }
        }

    }
}
