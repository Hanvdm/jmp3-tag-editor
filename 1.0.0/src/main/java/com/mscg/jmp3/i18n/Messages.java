package com.mscg.jmp3.i18n;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class Messages {
    private static final String BUNDLE_NAME = "com.mscg.jmp3.i18n.messages";

    private static Properties RESOURCE_BUNDLE;

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

        RESOURCE_BUNDLE = new Properties();

        try {
            for(String bundle : getBundleNames(locale)) {
                Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(bundle);
                InputStream is = null;
                while(resources.hasMoreElements()) {
                    try {
                        URL res = resources.nextElement();
                        is = res.openStream();
                        Properties tmp = new Properties();
                        tmp.load(is);
                        RESOURCE_BUNDLE.putAll(tmp);
                    } finally {
                        try {
                            is.close();
                        } catch(Exception e){}
                    }
                }
            }

        } catch(Exception e) {
            throw new RuntimeException("Cannot initialize localization properties", e);
        }

    }

    private static List<String> getBundleNames(Locale locale) {
        List<String> bundles = new LinkedList<String>();

        String bundleName = BUNDLE_NAME;
        bundleName = bundleName.replace('.', '/') + ".properties";
        bundles.add(bundleName);

        bundleName = BUNDLE_NAME;
        String localeSuffix = locale.getLanguage();
        if (localeSuffix.length() > 0)
            bundleName += "_" + localeSuffix;
        bundleName = bundleName.replace('.', '/') + ".properties";
        bundles.add(bundleName);

        bundleName = BUNDLE_NAME;
        localeSuffix = locale.toString();
        if (localeSuffix.length() > 0)
            bundleName += "_" + localeSuffix;
        else if (locale.getVariant().length() > 0)
            bundleName += "___" + locale.getVariant();
        bundleName = bundleName.replace('.', '/') + ".properties";
        bundles.add(bundleName);

        return bundles;
    }

    private Messages() {
    }

    public static String getString(String key) {
        String ret = RESOURCE_BUNDLE.getProperty(key);
        if(ret == null)
            ret = "!" + key + "!";
        return ret;
    }
}
