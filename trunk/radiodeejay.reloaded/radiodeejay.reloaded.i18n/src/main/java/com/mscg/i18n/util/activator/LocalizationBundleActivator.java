package com.mscg.i18n.util.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.i18n.LocalizationService;
import com.mscg.i18n.impl.LocalizationServiceImpl;
import com.mscg.settings.SettingsService;

public class LocalizationBundleActivator implements BundleActivator {

    private static final Logger LOG = LoggerFactory.getLogger(LocalizationBundleActivator.class);

    private ServiceReference settingsReference;
    private ServiceRegistration localizationRegistration;
    private ServiceRegistration localizationCollectionRegistration;
    private LocalizationServiceImpl localization;

    public void start(BundleContext bundleContext) throws Exception {
        settingsReference = bundleContext.getServiceReference(SettingsService.class.getCanonicalName());

        if(settingsReference == null)
            throw new NullPointerException("Cannot find service " + SettingsService.class.getCanonicalName());

        SettingsService settings = (SettingsService)bundleContext.getService(settingsReference);

        localization = new LocalizationServiceImpl(bundleContext);
        settings.addOnSettingsChangeListener(localization);

        localizationRegistration = bundleContext.registerService(LocalizationService.class.getCanonicalName(),
                                                                 localization,
                                                                 null);

        LOG.debug("Localization service initialized and registered");
    }

    public void stop(BundleContext bundleContext) throws Exception {
        SettingsService settings = (SettingsService)bundleContext.getService(settingsReference);
        settings.removeOnSettingsChangeListener(localization);

        if(localizationRegistration != null)
            localizationRegistration.unregister();

        if(localizationCollectionRegistration != null)
            localizationCollectionRegistration.unregister();

        if(settingsReference != null)
            bundleContext.ungetService(settingsReference);
    }

}
