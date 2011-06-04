package com.mscg.jmp3.theme;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.mscg.jmp3.settings.Settings;

public class ThemeManager {

    public static enum IconType {
        NEXT("arrow_right"),
        PREV("arrow_left"),
        EXIT("exit"),
        FILE("file_generic"),
        MP3("mp3"),
        ADD("add"),
        REMOVE("remove"),
        START("start"),
        STOP("stop"),
        ADD_SMALL("add-small"),
        REMOVE_SMALL("remove-small"),
        EDIT_SMALL("edit-small"),
        RUN_SMALL("run-small"),
        ARROW_UP("arrow-up"),
        ARROW_DOWN("arrow-down"),
        FOLDER("folder"),
        FILE_GENERIC_SMALL("file_generic_small"),
        FILE_IMAGE_SMALL("file_image_small");

        private String fileName;

        private IconType(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }

    }

    private static Icon mp3Icon;

    private ThemeManager() {

    }

    public static URL getIcon(IconType iconType) throws FileNotFoundException {
        String theme = Settings.getSetting("window.theme");
        String fileName = "./themes/" + theme + "/" + iconType.getFileName() + ".png";
        try {
            File file = new File(fileName);
            if(!file.exists())
                throw new FileNotFoundException("Can't open file \"" + fileName + "\"");
            return file.toURI().toURL();
        } catch(MalformedURLException e) {
            throw new FileNotFoundException("Can't open file \"" + fileName + "\": " + e.getMessage());
        }
    }

    public static Icon getMp3Icon() throws FileNotFoundException {
        if(mp3Icon == null)
            mp3Icon = new ImageIcon(getIcon(IconType.MP3));
        return mp3Icon;
    }

    public static URL getMainIcon(boolean small) throws FileNotFoundException {
        return openImageFile("./icon/icon-" + (small ? "small" : "large") + ".png");
    }

    public static URL getLameLogo() throws FileNotFoundException {
        return openImageFile("./icon/lame-logo.png");
    }

    private static URL openImageFile(String fileName) throws FileNotFoundException {
        try {
            File file = new File(fileName);
            if(!file.exists())
                throw new FileNotFoundException("Can't open file \"" + fileName + "\"");
            return file.toURI().toURL();
        } catch(MalformedURLException e) {
            throw new FileNotFoundException("Can't open file \"" + fileName + "\": " + e.getMessage());
        }
    }

}
