package com.mscg.main.theme;

import com.mscg.theme.StandardAbstractThemeElementsLoader;

public class MainDefaultThemeLoader extends StandardAbstractThemeElementsLoader {

    @Override
    public String getThemeName() {
        return "default";
    }

    @Override
    protected String getThemeFolder() {
        return "com/mscg/main/theme/default/";
    }

}
