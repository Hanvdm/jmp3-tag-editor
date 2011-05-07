package com.mscg.jmp3.ui.panel.fileoperations.dialog;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.main.AppLaunch;
import com.mscg.jmp3.ui.panel.fileoperations.TagFromFilenameTab;
import com.mscg.jmp3.ui.renderer.elements.IconAndFileListElement;
import com.mscg.jmp3.util.pool.InterruptibleRunnable;
import com.mscg.jmp3.util.pool.runnable.CreateTagsRunnable;

public class ExecuteTagCreationDialog extends GenericFilesOperationDialog {

    private static final long serialVersionUID = 8774582234015557326L;

    private TagFromFilenameTab tab;
    private List<File> files;

    public ExecuteTagCreationDialog(TagFromFilenameTab tab) throws FileNotFoundException {
        super();
        initComponents(tab);
    }

    public ExecuteTagCreationDialog(TagFromFilenameTab tab, Dialog owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
        initComponents(tab);
    }

    public ExecuteTagCreationDialog(TagFromFilenameTab tab, Dialog owner) throws FileNotFoundException {
        super(owner);
        initComponents(tab);
    }

    public ExecuteTagCreationDialog(TagFromFilenameTab tab, Frame owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
        initComponents(tab);
    }

    public ExecuteTagCreationDialog(TagFromFilenameTab tab, Frame owner) throws FileNotFoundException {
        super(owner);
        initComponents(tab);
    }

    public ExecuteTagCreationDialog(TagFromFilenameTab tab, Window owner, ModalityType modalityType) throws FileNotFoundException {
        super(owner, modalityType);
        initComponents(tab);
    }

    public ExecuteTagCreationDialog(TagFromFilenameTab tab, Window owner) throws FileNotFoundException {
        super(owner);
        initComponents(tab);
    }

    protected void initComponents(TagFromFilenameTab tab) throws FileNotFoundException {
        this.tab = tab;
        initComponents();
    }

    protected void initComponents() throws FileNotFoundException {
        files = new LinkedList<File>();
        DefaultListModel listModel = (DefaultListModel)AppLaunch.mainWindow.getFileChooseCard().getFilesList().getModel();
        for(Object listElement : listModel.toArray()) {
            IconAndFileListElement fileListEl = (IconAndFileListElement) listElement;
            files.add(fileListEl.getFile());
        }

        super.initComponents();
    }

    /**
     * @return the tab
     */
    public TagFromFilenameTab getTab() {
        return tab;
    }

    /**
     * @param tab the tab to set
     */
    public void setTab(TagFromFilenameTab tab) {
        this.tab = tab;
    }

    @Override
    protected String getDialogTitle() {
        return Messages.getString("operations.file.taginfo.execute.title");
    }

    @Override
    protected InterruptibleRunnable getRunnable() {
        return new CreateTagsRunnable(files, this);
    }

}
