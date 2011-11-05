package com.mscg.i18n.impl;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

import com.mscg.i18n.LocalizationService;
import com.mscg.settings.SettingsService;
import com.mscg.settings.listener.SettingsChangeListener;

public class LocalizationServiceImpl implements LocalizationService, SettingsChangeListener {

    private static Properties RESOURCE_BUNDLE;

    private BundleContext bundleContext;
    private Locale locale;


    public LocalizationServiceImpl(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public synchronized String getString(String key) {
        String ret = RESOURCE_BUNDLE.getProperty(key);
        if(ret == null)
            ret = "!" + key + "!";
        return ret;
    }

    public void onSettingsInit(SettingsService settings) {
        onSettingsChanged("locale", "", settings.getValue("locale"));
    }

    public synchronized void onSettingsChanged(String key, String oldValue, String newValue) {
        if("locale".equals(key)) {
            if("default".equals(newValue))
                locale = Locale.getDefault();
            else {
                for(Locale availLocale : Locale.getAvailableLocales()) {
                    if(newValue.equalsIgnoreCase(availLocale.getLanguage())) {
                        locale = availLocale;
                        break;
                    }
                }
                if(locale == null)
                    locale = Locale.getDefault();
            }
            reloadBundles();
        }
    }

    protected void reloadBundles() {
        RESOURCE_BUNDLE = new Properties();

        try {
            Bundle bundle = bundleContext.getBundle();
            for(String bundleName : getBundleNames(locale)) {
                Enumeration<URL> resources = bundle.getResources(bundleName);
                while(resources != null && resources.hasMoreElements()) {
                    InputStream is = null;
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
            throw new RuntimeException("Cannot read localization bundles", e);
        }
    }

    protected List<String> getBundleNames(Locale locale) {
        Set<String> bundleBasenames = new LinkedHashSet<String>();
        Bundle bundle = bundleContext.getBundle();
        ServiceReference packageAdminRef = null;
        try {
            packageAdminRef = bundleContext.getServiceReference(PackageAdmin.class.getCanonicalName());
            PackageAdmin packageAdmin = (PackageAdmin)bundleContext.getService(packageAdminRef);
            for(Bundle fragment : packageAdmin.getFragments(bundle)) {
                String localizationFilesStr = (String)fragment.getHeaders().get("Localization-Files");
                if(localizationFilesStr != null) {
                    String localizationFiles[] = localizationFilesStr.split(",");
                    for(String localizationFile : localizationFiles) {
                        bundleBasenames.add(localizationFile.trim());
                    }
                }
            }
        } finally {
            if(packageAdminRef != null)
                bundleContext.ungetService(packageAdminRef);
        }

        List<String> bundles = new LinkedList<String>();

        for(String bundleBasename : bundleBasenames) {
            String bundleName = bundleBasename;
            bundleName = bundleName.replace('.', '/') + ".properties";
            bundles.add(bundleName);

            bundleName = bundleBasename;
            String localeSuffix = locale.getLanguage();
            if (localeSuffix.length() > 0)
                bundleName += "_" + localeSuffix;
            bundleName = bundleName.replace('.', '/') + ".properties";
            bundles.add(bundleName);

            bundleName = bundleBasename;
            localeSuffix = locale.toString();
            if (localeSuffix.length() > 0)
                bundleName += "_" + localeSuffix;
            else if (locale.getVariant().length() > 0)
                bundleName += "___" + locale.getVariant();
            bundleName = bundleName.replace('.', '/') + ".properties";
            bundles.add(bundleName);
        }

        return bundles;
    }

}
