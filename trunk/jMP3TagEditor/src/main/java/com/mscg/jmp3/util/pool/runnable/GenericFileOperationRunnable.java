package com.mscg.jmp3.util.pool.runnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.jmp3.ui.panel.fileoperations.dialog.GenericFilesOperationDialog;
import com.mscg.jmp3.util.pool.InterruptibleRunnable;

public abstract class GenericFileOperationRunnable extends InterruptibleRunnable {

    protected GenericFilesOperationDialog dialog;

    protected Logger LOG;

    public GenericFileOperationRunnable(GenericFilesOperationDialog dialog) {
        LOG = LoggerFactory.getLogger(this.getClass());

        this.dialog = dialog;
    }

}
