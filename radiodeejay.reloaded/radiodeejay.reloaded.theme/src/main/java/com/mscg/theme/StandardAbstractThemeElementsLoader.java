package com.mscg.theme;

import java.io.InputStream;

public abstract class StandardAbstractThemeElementsLoader implements ThemeElementsLoader {

    protected abstract String getThemeFolder();

    protected String getDefaultExtension() {
        return "png";
    }

    public InputStream getStreamForImage(String imageName) {
        int index = imageName.lastIndexOf('.');
        if(index < 0)
            imageName += "." + getDefaultExtension();
        return this.getClass().getClassLoader().getResourceAsStream(getThemeFolder() + imageName);
    }

}
