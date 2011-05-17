package com.mscg.jmp3.ui.panel.fileoperations.dialog;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.FileNotFoundException;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.ui.panel.EncodeFileCard;
import com.mscg.jmp3.util.pool.InterruptibleRunnable;
import com.mscg.jmp3.util.pool.runnable.EncodeFilesRunnable;

public class EncodeFilesDialog extends GenericFilesOperationDialog {

    private static final long serialVersionUID = -7862490993740909177L;

    private EncodeFileCard encodeFileCard;

    public EncodeFilesDialog(EncodeFileCard encodeFileCard) throws FileNotFoundException {
        super();
        initComponents(encodeFileCard);
    }

    public EncodeFilesDialog(EncodeFileCard encodeFileCard, Dialog owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
        initComponents(encodeFileCard);
    }

    public EncodeFilesDialog(EncodeFileCard encodeFileCard, Dialog owner) throws FileNotFoundException {
        super(owner);
        initComponents(encodeFileCard);
    }

    public EncodeFilesDialog(EncodeFileCard encodeFileCard, Frame owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
        initComponents(encodeFileCard);
    }

    public EncodeFilesDialog(EncodeFileCard encodeFileCard, Frame owner) throws FileNotFoundException {
        super(owner);
        initComponents(encodeFileCard);
    }

    public EncodeFilesDialog(EncodeFileCard encodeFileCard, Window owner, ModalityType modalityType) throws FileNotFoundException {
        super(owner, modalityType);
        initComponents(encodeFileCard);
    }

    public EncodeFilesDialog(EncodeFileCard encodeFileCard, Window owner) throws FileNotFoundException {
        super(owner);
        initComponents(encodeFileCard);
    }

    protected void initComponents(EncodeFileCard encodeFileCard) throws FileNotFoundException {
        this.encodeFileCard = encodeFileCard;
        initComponents();
    }

    @Override
    protected String getDialogTitle() {
        return Messages.getString("operations.file.encode.dialog.title");
    }

    @Override
    protected InterruptibleRunnable getRunnable() {
        return new EncodeFilesRunnable(this, encodeFileCard);
    }

}
