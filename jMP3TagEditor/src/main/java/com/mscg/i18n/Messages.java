package com.mscg.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
    private static final String BUNDLE_NAME = "com.mscg.i18n.messages";

    private static ResourceBundle RESOURCE_BUNDLE;

    public static void reloadBundle(String localeName) {
        Locale locale = null;
        if("default".equalsIgnoreCase(localeName))
            locale = Locale.getDefault();
        else {
            for(Locale availLocale : Locale.getAvailableLocales()) {
                if(localeName.equalsIgnoreCase(availLocale.getLanguage())) {
                    locale = availLocale;
                    break;
                }
            }
            if(locale == null)
                locale = Locale.getDefault();
        }
        RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }

    private Messages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
