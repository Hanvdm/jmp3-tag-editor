package com.mscg.settings.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import com.mscg.settings.SettingsService;
import com.mscg.settings.listener.SettingsChangeListener;
import com.mscg.settings.util.OrderedProperties;

public class SettingsServiceImpl implements SettingsService {

    private OrderedProperties properties;
    private Set<SettingsChangeListener> listeners;

    public SettingsServiceImpl(InputStream defaultSettings, InputStream userSettings) throws IOException {
        listeners = new LinkedHashSet<SettingsChangeListener>();

        properties = new OrderedProperties();
        if(defaultSettings != null)
            properties.load(defaultSettings);

        if(userSettings != null) {
            OrderedProperties tmp = new OrderedProperties();
            tmp.load(userSettings);

            // user settings will overwrite default settings
            properties.putAll(tmp);
        }
    }

    @Override
    public synchronized void setValue(String key, String value) {
        String oldValue = properties.getProperty(value);
        for(SettingsChangeListener listener : listeners) {
            try {
                listener.onSettingsChanged(key, oldValue, value);
            } catch(Throwable e){}
        }
        properties.put(key, value);
    }

    @Override
    public synchronized String getValue(String key) {
        return properties.getProperty(key);
    }

    @Override
    public synchronized String getValue(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public synchronized void save(OutputStream os) throws IOException {
        properties.store(os, null);
    }

    @Override
    public synchronized void addOnSettingsChangeListener(SettingsChangeListener listener) {
        listeners.add(listener);
        listener.onSettingsInit(this);
    }

    @Override
    public synchronized void removeOnSettingsChangeListener(SettingsChangeListener listener) {
        listeners.remove(listener);
    }

}
