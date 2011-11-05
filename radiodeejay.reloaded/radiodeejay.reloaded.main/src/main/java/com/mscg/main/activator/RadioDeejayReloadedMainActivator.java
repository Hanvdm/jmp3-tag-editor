package com.mscg.main.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.mscg.i18n.LocalizationService;
import com.mscg.settings.SettingsService;
import com.mscg.theme.ThemeManager;

public class RadioDeejayReloadedMainActivator implements BundleActivator {

    private ServiceReference settingsReference;
    private ServiceReference localizationReference;
    private ServiceReference themeManagerReference;

    @Override
    public void start(BundleContext bundleContext) throws Exception {

        settingsReference = bundleContext.getServiceReference(SettingsService.class.getCanonicalName());
        localizationReference = bundleContext.getServiceReference(LocalizationService.class.getCanonicalName());
        themeManagerReference = bundleContext.getServiceReference(ThemeManager.class.getCanonicalName());

        if(settingsReference == null)
            throw new NullPointerException("Cannot find service " + SettingsService.class.getCanonicalName());
        if(localizationReference == null)
            throw new NullPointerException("Cannot find service " + LocalizationService.class.getCanonicalName());
        if(themeManagerReference == null)
            throw new NullPointerException("Cannot find service " + ThemeManager.class.getCanonicalName());

    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        if(settingsReference != null)
            bundleContext.ungetService(settingsReference);
        if(localizationReference != null)
            bundleContext.ungetService(localizationReference);
        if(themeManagerReference != null)
            bundleContext.ungetService(themeManagerReference);
    }

}
