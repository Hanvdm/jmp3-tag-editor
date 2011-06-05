package com.mscg.jmp3.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
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
            Properties defaultProperites = new Properties();
            LOG.debug("Loading default properties...");
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/mscg/jmp3/conf/settings.properties");
            if(is != null) {
                defaultProperites.load(is);
                IOUtils.closeQuietly(is);
            }
            LOG.info("Default properties loaded successfully");
            LOG.debug("Loading settings from file \"" + settingsFileName + "\"...");
            File settingsFile = new File(settingsFileName);
            Properties userProperties = new Properties();
            try {
                is = new FileInputStream(settingsFile);
                userProperties.load(is);
            } catch(Exception e){}
            settings.putAll(defaultProperites);
            settings.putAll(userProperties);
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
                if(!settingsFile.exists()) {
                    FileUtils.forceMkdir(settingsFile.getParentFile());
                    settingsFile.createNewFile();
                }
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
