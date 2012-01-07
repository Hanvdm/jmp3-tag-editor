package com.mscg.theme;

import java.net.URL;


public interface ThemeElementsLoader {

    public String getThemeName();

    public URL getUrlForImage(IconType iconType);

}
