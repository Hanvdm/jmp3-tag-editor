package com.mscg.main.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.i18n.LocalizationService;
import com.mscg.main.ui.MainUIManager;
import com.mscg.main.ui.impl.MainUIManagerImpl;
import com.mscg.settings.SettingsService;
import com.mscg.theme.ThemeManager;

public class RadioDeejayReloadedMainActivator implements BundleActivator {

    private static final Logger LOG = LoggerFactory.getLogger(RadioDeejayReloadedMainActivator.class);

    private static RadioDeejayReloadedMainActivator instance;

    private ServiceReference settingsReference;
    private ServiceReference localizationReference;
    private ServiceReference themeManagerReference;
    private ServiceRegistration mainUIManagerReg;
    private BundleContext bundleContext;

    public static RadioDeejayReloadedMainActivator getInstance() {
        return instance;
    }

    public LocalizationService getLocalizationService() {
        LocalizationService service = (LocalizationService)bundleContext.getService(localizationReference);
        return service;
    }

    public SettingsService getSettingsService() {
        SettingsService settings = (SettingsService)bundleContext.getService(settingsReference);
        return settings;
    }

    public ThemeManager getThemeManager() {
        ThemeManager themeManager = (ThemeManager)bundleContext.getService(themeManagerReference);
        return themeManager;
    }

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

        this.bundleContext = bundleContext;
        instance = this;

        LOG.info("Main application service loaded with success");
        MainUIManager uiManager = new MainUIManagerImpl();
        mainUIManagerReg = bundleContext.registerService(MainUIManager.class.getCanonicalName(),
                                                         uiManager, null);

        LOG.info("Main UI service registered, starting the interface");
        uiManager.startInterface();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        if(settingsReference != null)
            bundleContext.ungetService(settingsReference);
        if(localizationReference != null)
            bundleContext.ungetService(localizationReference);
        if(themeManagerReference != null)
            bundleContext.ungetService(themeManagerReference);

        if(mainUIManagerReg != null)
            mainUIManagerReg.unregister();

        bundleContext = null;
        instance = null;
    }

}
