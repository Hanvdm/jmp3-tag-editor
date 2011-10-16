package com.mscg.jmp3.ui.panel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.mp3.ui.MainWindowInterface;
import com.mp3.ui.panel.GenericStartableCard;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.theme.ThemeManager.IconType;
import com.mscg.jmp3.ui.panel.fileoperations.RenameFileTab;
import com.mscg.jmp3.ui.panel.fileoperations.TagFromFilenameTab;
import com.mscg.jmp3.ui.panel.fileoperations.dialog.ExecuteRenameFilesDialog;
import com.mscg.jmp3.ui.panel.fileoperations.dialog.ExecuteTagCreationDialog;
import com.mscg.jmp3.ui.panel.fileoperations.dialog.GenericFilesOperationDialog;

public class FilenameOperationsCard extends GenericStartableCard {

    private static final long serialVersionUID = -2264127654101769869L;

    private JTabbedPane tabbedPane;
    private TagFromFilenameTab tagFromFilenameTab;
    private RenameFileTab renameFileTab;

    public FilenameOperationsCard(MainWindowInterface mainWindow) throws FileNotFoundException {
        super(mainWindow);
    }

    @Override
    protected ActionListener getStartButtonListener() {
        return new StartButtonListener();
    }

    @Override
    protected Icon getStartButtonIcon() throws FileNotFoundException {
        return new ImageIcon(ThemeManager.getIcon(IconType.START));
    }

    @Override
    protected String getStartButtonText() {
        return Messages.getString("operations.start");
    }

    @Override
    protected Component getCenterComponent() throws FileNotFoundException {
        tabbedPane = new JTabbedPane();
        tagFromFilenameTab = new TagFromFilenameTab();
        tabbedPane.addTab(Messages.getString("operations.file.tagfromfile"), null,
                          tagFromFilenameTab, Messages.getString("operations.file.tagfromfile.tooltip"));
        renameFileTab = new RenameFileTab();
        tabbedPane.addTab(Messages.getString("operations.file.renamefile"), null,
                          renameFileTab, Messages.getString("operations.file.renamefile.tooltip"));
        return tabbedPane;
    }

    private class StartButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ev) {
            Component shownCard = tabbedPane.getSelectedComponent();
            GenericFilesOperationDialog dialog = null;
            if(shownCard == tagFromFilenameTab) {
                LOG.debug("Starting tag creation");
                try {
                    dialog = new ExecuteTagCreationDialog(tagFromFilenameTab,
                                                          MainWindowInterface.getInstance(),
                                                          true);
                    dialog.setVisible(true);
                } catch(Exception e) {
                    LOG.error("Cannot show tag creation dialog", e);
                    MainWindowInterface.showError(e);
                }
            }
            else if(shownCard == renameFileTab) {
                LOG.debug("Starting file rename");
                LOG.debug("Starting tag creation");
                try {
                    dialog = new ExecuteRenameFilesDialog(renameFileTab,
                                                          MainWindowInterface.getInstance(),
                                                          true);
                    dialog.setVisible(true);
                } catch(Exception e) {
                    LOG.error("Cannot show file rename dialog", e);
                    MainWindowInterface.showError(e);
                }
            }
            else {
                LOG.warn("Cannot determine which panel is active");
                MainWindowInterface.showError(new Exception("Uknown panel selected"));
            }
        }

    }
}
