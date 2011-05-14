package com.mscg.jmp3.ui.panel.fileoperations.dialog;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.FileNotFoundException;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.ui.panel.fileoperations.RenameFileTab;
import com.mscg.jmp3.util.pool.InterruptibleRunnable;
import com.mscg.jmp3.util.pool.runnable.RenameFilesRunnable;

public class ExecuteRenameFilesDialog extends GenericFilesOperationDialog {

    private static final long serialVersionUID = -3630221636283924977L;

    private RenameFileTab tab;

    public ExecuteRenameFilesDialog(RenameFileTab tab) throws FileNotFoundException {
        super();
        initComponents(tab);
    }

    public ExecuteRenameFilesDialog(RenameFileTab tab, Dialog owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
        initComponents(tab);
    }

    public ExecuteRenameFilesDialog(RenameFileTab tab, Dialog owner) throws FileNotFoundException {
        super(owner);
        initComponents(tab);
    }

    public ExecuteRenameFilesDialog(RenameFileTab tab, Frame owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
        initComponents(tab);
    }

    public ExecuteRenameFilesDialog(RenameFileTab tab, Frame owner) throws FileNotFoundException {
        super(owner);
        initComponents(tab);
    }

    public ExecuteRenameFilesDialog(RenameFileTab tab, Window owner, ModalityType modalityType) throws FileNotFoundException {
        super(owner, modalityType);
        initComponents(tab);
    }

    public ExecuteRenameFilesDialog(RenameFileTab tab, Window owner) throws FileNotFoundException {
        super(owner);
        initComponents(tab);
    }

    protected void initComponents(RenameFileTab tab) throws FileNotFoundException {
        this.tab = tab;
        initComponents();
    }

    /**
     * @return the tab
     */
    public RenameFileTab getTab() {
        return tab;
    }

    @Override
    protected String getDialogTitle() {
        return Messages.getString("operations.file.rename.execute.title");
    }

    @Override
    protected InterruptibleRunnable getRunnable() {
        return new RenameFilesRunnable(this);
    }

}
