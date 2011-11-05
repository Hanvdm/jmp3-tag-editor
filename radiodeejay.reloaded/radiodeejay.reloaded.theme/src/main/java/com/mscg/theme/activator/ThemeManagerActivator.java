package com.mscg.theme.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.mscg.settings.SettingsService;
import com.mscg.theme.ThemeManager;
import com.mscg.theme.impl.ThemeManagerImpl;

public class ThemeManagerActivator implements BundleActivator {

    private ServiceReference settingsReference;
    private ServiceRegistration themeManagerRegistration;

    public void start(BundleContext bundleContext) throws Exception {
        settingsReference = bundleContext.getServiceReference(SettingsService.class.getCanonicalName());
        if(settingsReference == null)
            throw new NullPointerException("Cannot find service " + SettingsService.class.getCanonicalName());

        ThemeManager themeManager = new ThemeManagerImpl((SettingsService)bundleContext.getService(settingsReference),
                                                         bundleContext);
        themeManagerRegistration = bundleContext.registerService(ThemeManager.class.getCanonicalName(),
                                                                 themeManager,
                                                                 null);

    }

    public void stop(BundleContext bundleContext) throws Exception {
        if(themeManagerRegistration != null)
            themeManagerRegistration.unregister();
        if(settingsReference != null)
            bundleContext.ungetService(settingsReference);
    }

}
