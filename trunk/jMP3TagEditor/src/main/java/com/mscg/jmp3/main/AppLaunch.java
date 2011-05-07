package com.mscg.jmp3.main;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.i18n.Messages;
import com.mscg.jmp3.ui.frame.MainWindow;
import com.mscg.settings.Settings;

public class AppLaunch {

    private static final Logger LOG = LoggerFactory.getLogger(AppLaunch.class);

    public static MainWindow mainWindow;

    public static String[] applicationArguments;

    /**
     * @param args the command line arguments
     */
     public static void main(String args[]) {
         applicationArguments = args;

         try {
             Settings.initSettings();

             Messages.reloadBundle(Settings.getSetting("application.locale"));

             java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                     try {
                        mainWindow = new MainWindow();
                        mainWindow.setVisible(true);
                    } catch (Exception e) {
                        manageError(e);
                    }
                 }
             });

         } catch(Exception e) {
             manageError(e);
         }
     }

     public static void showError(Exception e) {
         String message = Messages.getString("error.message") + "\n" + e.getMessage();
         String title = Messages.getString("error.title");
         String buttons[] = {Messages.getString("error.close")};
         JOptionPane.showOptionDialog(mainWindow, message, title, JOptionPane.DEFAULT_OPTION,
                                      JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);
     }

     public static void manageError(Exception e) {
         LOG.error("An error occurred and the application will close.", e);
         showError(e);
         System.exit(1);
     }

}
