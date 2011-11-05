package com.mscg.settings.util.activator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.settings.SettingsService;
import com.mscg.settings.impl.SettingsServiceImpl;

public class SettingsLoader implements BundleActivator {

    private static final Logger LOG = LoggerFactory.getLogger(SettingsLoader.class);

    private static final String SETTINGS_PROPERTIES = "configuration/settings.properties";

    private ServiceRegistration settingsServiceReg;
    private SettingsServiceImpl settingsService;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        InputStream defaultValues = null;
        InputStream userValues = null;
        try {
            defaultValues = this.getClass().getResourceAsStream("/default-settings.properties");
            try {
                File userValuesFile = new File(SETTINGS_PROPERTIES);
                if(userValuesFile.exists())
                    userValues = new FileInputStream(userValuesFile);
            } catch(Exception e){}
            settingsService = new SettingsServiceImpl(defaultValues, userValues);
            settingsServiceReg = bundleContext.registerService(SettingsService.class.getCanonicalName(), settingsService, null);
            LOG.debug("Settings service initialized and registered");

            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    try {
                        saveSettings();
                    } catch(Exception e){
                        LOG.warn("Cannot save setting on file", e);
                    }
                }

            });
        } finally {
            IOUtils.closeQuietly(defaultValues);
            IOUtils.closeQuietly(userValues);
        }
    }

    private void saveSettings() throws Exception {
        OutputStream os = null;
        try {
            File userValuesFile = new File(SETTINGS_PROPERTIES);
            if(!userValuesFile.exists()) {
                FileUtils.forceMkdir(userValuesFile.getParentFile());
                userValuesFile.createNewFile();
            }
            os = new FileOutputStream(userValuesFile);
            settingsService.save(os);
            LOG.debug("Settings persisted on file");
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        saveSettings();
        settingsServiceReg.unregister();
        LOG.debug("Settings service unregistered");
    }

}
