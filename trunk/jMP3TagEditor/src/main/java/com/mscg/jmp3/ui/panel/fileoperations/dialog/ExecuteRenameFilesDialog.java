package com.mscg.jmp3.ui.panel.fileoperations.dialog;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.FileNotFoundException;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.util.pool.InterruptibleRunnable;
import com.mscg.jmp3.util.pool.runnable.RenameFilesRunnable;

public class ExecuteRenameFilesDialog extends GenericFilesOperationDialog {

    private static final long serialVersionUID = -3630221636283924977L;

    public ExecuteRenameFilesDialog() throws FileNotFoundException {
        super();
    }

    public ExecuteRenameFilesDialog(Dialog owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
    }

    public ExecuteRenameFilesDialog(Dialog owner) throws FileNotFoundException {
        super(owner);
    }

    public ExecuteRenameFilesDialog(Frame owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
    }

    public ExecuteRenameFilesDialog(Frame owner) throws FileNotFoundException {
        super(owner);
    }

    public ExecuteRenameFilesDialog(Window owner, ModalityType modalityType) throws FileNotFoundException {
        super(owner, modalityType);
    }

    public ExecuteRenameFilesDialog(Window owner) throws FileNotFoundException {
        super(owner);
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
