package com.mscg.jmp3.main;

import java.awt.EventQueue;
import java.awt.Toolkit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mp3.ui.MainWindowInterface;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.settings.Settings;
import com.mscg.jmp3.ui.frame.MainWindow;
import com.mscg.jmp3.ui.util.contextmenu.cutandpaste.CutAndPasteEventQueue;

public class AppLaunch {

    private static final Logger LOG = LoggerFactory.getLogger(AppLaunch.class);

    public static String[] applicationArguments;

    /**
     * @param args the command line arguments
     */
     public static void main(String args[]) {
         applicationArguments = args;

         try {
             Settings.initSettings();

             Messages.reloadBundle(Settings.getSetting("application.locale"));

             Toolkit.getDefaultToolkit().getSystemEventQueue().push(new CutAndPasteEventQueue());

             EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        MainWindowInterface.setInstance(new MainWindow());
                        MainWindowInterface.getInstance().setVisible(true);
                    } catch (Exception e) {
                        manageError(e);
                    }
                 }
             });

         } catch(Throwable e) {
             manageError(e);
         }
     }

     public static void manageError(Throwable e) {
         LOG.error("An error occurred and the application will close.", e);
         MainWindowInterface.showError(e);
         System.exit(1);
     }

}
