package com.mscg.jmp3.ui.renderer.elements;

import java.io.File;

import javax.swing.Icon;

public class IconAndFileListElement extends IconAndTextListElement {

    private static final long serialVersionUID = 1449289897203931657L;

    protected File file;

    public IconAndFileListElement(Icon icon, File file) {
        super(icon, file.getName());
    }

    public IconAndFileListElement(Icon icon) {
        super(icon);
    }

    public IconAndFileListElement(File file) {
        super(file.getName());
    }

    public File getFile() {
        return file;
    }

}
