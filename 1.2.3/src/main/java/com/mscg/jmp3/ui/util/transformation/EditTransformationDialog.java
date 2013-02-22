package com.mscg.jmp3.ui.util.transformation;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.FileNotFoundException;

public class EditTransformationDialog extends AddTransformationDialog {

    private static final long serialVersionUID = 8991148909024272219L;

    public EditTransformationDialog() throws FileNotFoundException {
        super();
    }

    public EditTransformationDialog(Dialog owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
    }

    public EditTransformationDialog(Dialog owner) throws FileNotFoundException {
        super(owner);
    }

    public EditTransformationDialog(Frame owner, boolean modal) throws FileNotFoundException {
        super(owner, modal);
    }

    public EditTransformationDialog(Frame owner) throws FileNotFoundException {
        super(owner);
    }

    public EditTransformationDialog(Window owner, ModalityType modalityType) throws FileNotFoundException {
        super(owner, modalityType);
    }

    public EditTransformationDialog(Window owner) throws FileNotFoundException {
        super(owner);
    }

    @Override
    protected void initTransformatorsListSelection() {
        // so selection is needed here
    }

}
