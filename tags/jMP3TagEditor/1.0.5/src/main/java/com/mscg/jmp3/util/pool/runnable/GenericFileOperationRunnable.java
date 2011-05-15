package com.mscg.jmp3.util.pool.runnable;

import com.mscg.jmp3.ui.panel.fileoperations.dialog.GenericFilesOperationDialog;
import com.mscg.jmp3.util.pool.InterruptibleRunnable;

public abstract class GenericFileOperationRunnable extends InterruptibleRunnable {

    protected GenericFilesOperationDialog dialog;

    public GenericFileOperationRunnable(GenericFilesOperationDialog dialog) {
        this.dialog = dialog;
    }

}
