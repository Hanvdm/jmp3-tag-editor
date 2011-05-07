package com.mscg.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Settings {

    private static final Logger LOG = LoggerFactory.getLogger(Settings.class);

    private static final String settingsFileName = "./conf/settings.properties";

    private static Properties settings;


    public static void initSettings() throws IOException {
        settings = new Properties();
        InputStream is = null;
        try {
            LOG.debug("Loading settings from file \"" + settingsFileName + "\"...");
            File settingsFile = new File(settingsFileName);
            is = new FileInputStream(settingsFile);
            settings.load(is);
            LOG.info("Settings loaded successfully");
            Runtime.getRuntime().addShutdownHook(new ShutdownSaver());
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public static String getSetting(String key) {
        return settings.getProperty(key);
    }

    public static void setSetting(String key, String value) {
        settings.setProperty(key, value);
    }

    private Settings() {

    }

    private static class ShutdownSaver extends Thread {

        public void run() {
            OutputStream os = null;
            try {
                LOG.debug("Saving settings to file \"" + settingsFileName + "\"...");
                File settingsFile = new File(settingsFileName);
                os = new FileOutputStream(settingsFile);
                settings.store(os, "jMP3TAG settings");
                LOG.info("Settings saved successfully");
            } catch(Exception e) {
                LOG.error("Cannot save settings to file \"" + settingsFileName + "\"");
            } finally {
                IOUtils.closeQuietly(os);
            }
        }

    }

}
