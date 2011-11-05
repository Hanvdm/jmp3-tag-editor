package com.mscg.theme;

import java.io.InputStream;

public interface ThemeElementsLoader {

    public String getThemeName();

    public InputStream getStreamForImage(String imageName);

}
