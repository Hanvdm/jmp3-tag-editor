package com.mscg.jmp3.ui.listener.filelist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.settings.Settings;
import com.mscg.jmp3.theme.ThemeManager;
import com.mscg.jmp3.ui.frame.MainWindow;
import com.mscg.jmp3.ui.renderer.elements.IconAndFileListElement;
import com.mscg.jmp3.util.filefilter.MP3FileFilter;

public class AddFilesListener extends GenericFileListListener implements ActionListener {

    private MainWindow mainWindow;

    public AddFilesListener(JList filesList, MainWindow mainWindow) throws FileNotFoundException {
        super(filesList);

        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String startDirectoryName = Settings.getSetting("files.last.dir").
            replace("${user.home}", System.getProperty("user.home"));

        File startDirectory = new File(startDirectoryName);
        if(!startDirectory.exists() || !startDirectory.isDirectory()) {
            startDirectory = new File(System.getProperty("user.home"));
        }

        JFileChooser fileChooser = new JFileChooser(startDirectory);
        fileChooser.setDialogTitle(Messages.getString("files.chooser.title"));
        fileChooser.addChoosableFileFilter(new MP3FileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(true);
        int returnVal = fileChooser.showOpenDialog(mainWindow);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File files[] = fileChooser.getSelectedFiles();
            if(files.length != 0) {
                try {
                    // set the last selected directory
                    Settings.setSetting("files.last.dir", files[0].getParentFile().getCanonicalPath());

                    DefaultListModel listModel = (DefaultListModel)filesList.getModel();

                    for(File file : files) {
                        listModel.addElement(new IconAndFileListElement(ThemeManager.getMp3Icon(), file));
                    }

                } catch(Exception e) {
                    LOG.error("An error occurred while selecting MP3 files", e);
                }
            }
        }
    }

}
