package com.mscg.jmp3.util.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.mscg.jmp3.i18n.Messages;

public class ImageFileFilter extends FileFilter {
    @Override
    public String getDescription() {
        return Messages.getString("operations.file.taginfo.cover.filefilter.name");
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg") ||
               f.getName().toLowerCase().endsWith(".jpeg") ||
               f.getName().toLowerCase().endsWith(".png");
    }
}