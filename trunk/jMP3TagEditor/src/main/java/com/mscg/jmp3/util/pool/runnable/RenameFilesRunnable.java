package com.mscg.jmp3.util.pool.runnable;

import java.io.File;

import javax.swing.DefaultListModel;

import org.apache.commons.io.FileExistsException;

import com.mscg.jID3tags.file.MP3File;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.main.AppLaunch;
import com.mscg.jmp3.ui.panel.fileoperations.RenameFileTab;
import com.mscg.jmp3.ui.panel.fileoperations.dialog.ExecuteRenameFilesDialog;
import com.mscg.jmp3.ui.renderer.elements.IconAndFileListElement;


public class RenameFilesRunnable extends GenericFileOperationRunnable {

    private DefaultListModel filesListModel;
    private RenameFileTab tab;

    public RenameFilesRunnable(ExecuteRenameFilesDialog dialog) {
        super(dialog);
        tab = dialog.getTab();
        filesListModel = (DefaultListModel)AppLaunch.mainWindow.getFileChooseCard().getFilesList().getModel();
    }

    @Override
    public void executeInterruptible() {
        File newFile = null;
        try {
            String value = null;
            for(Object elem : filesListModel.toArray()) {
                IconAndFileListElement fileElem = (IconAndFileListElement)elem;
                File file = fileElem.getFile();
                String ext = "";
                int pointIndex = file.getName().lastIndexOf('.');
                if(pointIndex >= 0)
                    ext = file.getName().substring(pointIndex);
                String newFileName = tab.getFilenamePatternPanel().getValue() + ext;

                MP3File mp3File = new MP3File(file);

                value = mp3File.getAlbum();
                if(value != null && value.trim().length() != 0) {
                    newFileName = newFileName.replace("${album}", value);
                }
                value = mp3File.getArtist();
                if(value != null && value.trim().length() != 0) {
                    newFileName = newFileName.replace("${artist}", value);
                }
                value = mp3File.getTitle();
                if(value != null && value.trim().length() != 0) {
                    newFileName = newFileName.replace("${title}", value);
                }
                value = mp3File.getTrack();
                if(value != null && value.trim().length() != 0) {
                    try {
                        int trackNumber = Integer.parseInt(value);
                        newFileName = newFileName.replace("${track}", String.format("%02d", trackNumber));
                    } catch(Exception e){}
                }
                value = mp3File.getYear();
                if(value != null && value.trim().length() != 0) {
                    newFileName = newFileName.replace("${year}", value);
                }

                newFile = new File(file.getParentFile(), newFileName);
                if(newFile.exists()) {
                    throw new FileExistsException(newFile);
                }
                else {
                    if(!newFile.getParentFile().exists())
                        newFile.getParentFile().mkdirs();
                }
                file.renameTo(newFile);
                fileElem.setFile(newFile);
            }

            AppLaunch.showMessage(Messages.getString("operations.file.rename.execute.done.title"),
                                  Messages.getString("operations.file.rename.execute.done.message").
                                      replace("${number}", "" + filesListModel.size()));
        } catch(FileExistsException e) {
            LOG.error("Cannot rename file", e);
            AppLaunch.showError(new Exception(Messages.getString("operations.file.error.file.exists").
                                                  replace("${filepath}", newFile.getAbsolutePath())));
        } catch(Exception e) {
            LOG.error("Cannot rename file", e);
            AppLaunch.showError(e);
        } finally {
            dialog.setVisible(false);
        }
    }

}
