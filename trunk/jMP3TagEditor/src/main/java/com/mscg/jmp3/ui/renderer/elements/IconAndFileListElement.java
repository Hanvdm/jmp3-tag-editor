package com.mscg.jmp3.ui.renderer.elements;

import java.io.File;

import javax.swing.Icon;

public class IconAndFileListElement extends IconAndTextListElement {

    private static final long serialVersionUID = 1449289897203931657L;

    protected File file;

    public IconAndFileListElement(Icon icon, File file) {
        super(icon, null);
        setFile(file);
    }

    public IconAndFileListElement(File file) {
        super(null, null);
        setFile(file);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        text = file.getName();
    }

}
