package com.mscg.main.ui;

import java.awt.EventQueue;
import java.awt.Toolkit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.main.ui.impl.MainWindow;
import com.mscg.main.util.ui.contextmenu.cutandpaste.CutAndPasteEventQueue;

public class MainUIManager {

    private static final Logger LOG = LoggerFactory.getLogger(MainUIManager.class);

    public static void startInterface() {
        try {
            Toolkit.getDefaultToolkit().getSystemEventQueue().push(new CutAndPasteEventQueue());

            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        MainWindowInterface.setInstance(new MainWindow());
                        MainWindowInterface.getInstance().setVisible(true);
                    } catch (Exception e) {
                        manageError(e);
                    }
                 }
             });

        } catch(Exception e) {
            manageError(e);
        }
    }

    public static void manageError(Exception e) {
        LOG.error("An error occurred and the application will close.", e);
        MainWindowInterface.showError(e);
        System.exit(1);
    }

}
