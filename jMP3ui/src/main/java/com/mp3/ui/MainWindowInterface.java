package com.mp3.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import com.mscg.jmp3.i18n.Messages;

public abstract class MainWindowInterface extends JFrame {

    private static final long serialVersionUID = -8665490887184915742L;

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

    public static void showError(Throwable e) {
        String message = Messages.getString("error.message") + "\n" + e.getMessage();
        String title = Messages.getString("error.title");
        String buttons[] = {Messages.getString("error.close")};
        JOptionPane.showOptionDialog(getInstance(), message, title, JOptionPane.DEFAULT_OPTION,
                                     JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);
    }

    public static void showMessage(String title, String message) {
        String buttons[] = {Messages.getString("error.close")};
        JOptionPane.showOptionDialog(getInstance(), message, title, JOptionPane.DEFAULT_OPTION,
                                     JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
    }

    public abstract JButton getPrevButton();

    public abstract JButton getNextButton();

    public abstract ListModel getFilesList();

}
