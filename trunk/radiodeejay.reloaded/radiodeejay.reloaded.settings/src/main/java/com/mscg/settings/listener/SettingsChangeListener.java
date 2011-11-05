package com.mscg.settings.listener;

import com.mscg.settings.SettingsService;

public interface SettingsChangeListener {

    public void onSettingsChanged(String key, String oldValue, String newValue);

    public void onSettingsInit(SettingsService settings);

}
