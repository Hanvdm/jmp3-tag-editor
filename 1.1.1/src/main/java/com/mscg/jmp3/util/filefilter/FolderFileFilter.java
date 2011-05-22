package com.mscg.jmp3.util.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.mscg.jmp3.i18n.Messages;

public class FolderFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        return f.isDirectory();
    }

    @Override
    public String getDescription() {
        return Messages.getString("operations.file.encode.destination.choose.filefilter");
    }

}
