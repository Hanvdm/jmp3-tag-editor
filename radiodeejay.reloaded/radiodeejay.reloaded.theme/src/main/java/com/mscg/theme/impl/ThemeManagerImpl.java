package com.mscg.theme.impl;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.settings.SettingsService;
import com.mscg.theme.IconType;
import com.mscg.theme.ThemeElementsLoader;
import com.mscg.theme.ThemeManager;

public class ThemeManagerImpl implements ThemeManager {

    private static final Logger LOG = LoggerFactory.getLogger(ThemeManagerImpl.class);

    private SettingsService settings;
    private Map<String, Set<ThemeElementsLoader>> themeLoaders;

    public ThemeManagerImpl(SettingsService settings, BundleContext bundleContext) {
        this.settings = settings;
        themeLoaders = new HashMap<String, Set<ThemeElementsLoader>>();

        ServiceReference packageAdminRef = null;
        try {
            packageAdminRef = bundleContext.getServiceReference(PackageAdmin.class.getCanonicalName());
            PackageAdmin packageAdmin = (PackageAdmin)bundleContext.getService(packageAdminRef);
            Bundle bundle = bundleContext.getBundle();
            Bundle fragments[] = packageAdmin.getFragments(bundle);
            if(fragments != null) {
                for(Bundle fragment : fragments) {
                    String themeElLoaderClassName = (String)fragment.getHeaders().get("Theme-Elements-Loader-Class");
                    try {
                        Class themeElLoaderClass = bundle.loadClass(themeElLoaderClassName);
                        ThemeElementsLoader loader = (ThemeElementsLoader)themeElLoaderClass.newInstance();

                        Set<ThemeElementsLoader> loaders = themeLoaders.get(loader.getThemeName());
                        if(loaders == null) {
                            loaders = new LinkedHashSet<ThemeElementsLoader>();
                            themeLoaders.put(loader.getThemeName(), loaders);
                        }
                        loaders.add(loader);

                    } catch(Exception e) {
                        LOG.warn("Cannot load theme elements loader of class \"" + themeElLoaderClassName + "\" " +
                        		"from fragment " + fragment.toString(),
                                 e);
                    }

                }
                if(LOG.isInfoEnabled())
                    LOG.info("Configured themes are: " + themeLoaders.keySet());
            }
            else {
                LOG.warn("No fragments found for theme manager");
            }
        } finally {
            if(packageAdminRef != null)
                bundleContext.ungetService(packageAdminRef);
        }
    }

    public synchronized URL getIcon(IconType iconType) {
        Set<ThemeElementsLoader> loaders = themeLoaders.get(settings.getValue("window.theme"));
        URL url = null;
        if(loaders != null) {
            for(ThemeElementsLoader loader : loaders) {
                url = loader.getUrlForImage(iconType);
                if(url != null)
                    break;
            }
        }
        return url;
    }

}
