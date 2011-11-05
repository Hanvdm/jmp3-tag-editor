package com.mscg.main.ui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.mscg.i18n.LocalizationService;
import com.mscg.main.activator.RadioDeejayReloadedMainActivator;

public class MainWindowInterface extends JFrame {

    private static final long serialVersionUID = -2210872431890595966L;

    private static MainWindowInterface instance;

    /**
     * @return the instance
     */
    public synchronized static MainWindowInterface getInstance() {
        return instance;
    }

    /**
     * @param instance the instance to set
     */
    public synchronized static void setInstance(MainWindowInterface instance) {
        MainWindowInterface.instance = instance;
    }

    public static void showError(Exception e) {
        LocalizationService localization = RadioDeejayReloadedMainActivator.getInstance().getLocalizationService();
        String message = localization.getString("error.message") + "\n" + e.getMessage();
        String title = localization.getString("error.title");
        String buttons[] = {localization.getString("error.close")};
        JOptionPane.showOptionDialog(getInstance(), message, title, JOptionPane.DEFAULT_OPTION,
                                     JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);
    }

    public static void showMessage(String title, String message) {
        LocalizationService localization = RadioDeejayReloadedMainActivator.getInstance().getLocalizationService();
        String buttons[] = {localization.getString("error.close")};
        JOptionPane.showOptionDialog(getInstance(), message, title, JOptionPane.DEFAULT_OPTION,
                                     JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
    }

}
