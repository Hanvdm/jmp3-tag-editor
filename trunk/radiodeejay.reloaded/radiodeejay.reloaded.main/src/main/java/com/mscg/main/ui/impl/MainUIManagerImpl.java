package com.mscg.main.ui.impl;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.i18n.LocalizationService;
import com.mscg.main.activator.RadioDeejayReloadedMainActivator;
import com.mscg.main.ui.MainUIManager;
import com.mscg.main.util.ui.contextmenu.cutandpaste.CutAndPasteEventQueue;

public class MainUIManagerImpl implements MainUIManager {

    private static final Logger LOG = LoggerFactory.getLogger(MainUIManagerImpl.class);

    public void startInterface() {
        try {
            Toolkit.getDefaultToolkit().getSystemEventQueue().push(new CutAndPasteEventQueue());

            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        MainWindow.setInstance(new MainWindow(MainUIManagerImpl.this));
                        MainWindow.getInstance().setVisible(true);
                    } catch (Exception e) {
                        manageError(e);
                    }
                 }
             });

        } catch(Exception e) {
            manageError(e);
        }
    }

    public void showError(Exception e) {
        LocalizationService localization = RadioDeejayReloadedMainActivator.getInstance().getLocalizationService();
        String message = localization.getString("error.message") + "\n" + e.getMessage();
        String title = localization.getString("error.title");
        String buttons[] = {localization.getString("error.close")};
        JOptionPane.showOptionDialog(MainWindow.getInstance(), message, title, JOptionPane.DEFAULT_OPTION,
                                     JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);
    }

    public void showMessage(String title, String message) {
        LocalizationService localization = RadioDeejayReloadedMainActivator.getInstance().getLocalizationService();
        String buttons[] = {localization.getString("error.close")};
        JOptionPane.showOptionDialog(MainWindow.getInstance(), message, title, JOptionPane.DEFAULT_OPTION,
                                     JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
    }

    protected void manageError(Exception e) {
        LOG.error("An error occurred and the application will close.", e);
        showError(e);
        System.exit(1);
    }

}
