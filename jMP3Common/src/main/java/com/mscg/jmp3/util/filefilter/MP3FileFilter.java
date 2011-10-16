package com.mscg.jmp3.util.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.mscg.jmp3.i18n.Messages;

public class MP3FileFilter extends FileFilter {
    @Override
    public String getDescription() {
        return Messages.getString("files.chooser.filefilter.name");
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".mp3");
    }
}