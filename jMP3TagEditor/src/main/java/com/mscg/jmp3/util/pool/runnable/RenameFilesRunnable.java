package com.mscg.jmp3.util.pool.runnable;

import com.mscg.jmp3.ui.panel.fileoperations.dialog.ExecuteRenameFilesDialog;


public class RenameFilesRunnable extends GenericFileOperationRunnable {

    public RenameFilesRunnable(ExecuteRenameFilesDialog dialog) {
        super(dialog);
    }

    @Override
    public void executeInterruptible() {
        try {

        } finally {
            dialog.setVisible(false);
        }
    }

}
