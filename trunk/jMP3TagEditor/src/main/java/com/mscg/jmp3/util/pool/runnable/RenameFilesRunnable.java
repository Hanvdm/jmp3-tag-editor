package com.mscg.jmp3.util.pool.runnable;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultListModel;

import org.apache.commons.io.FileExistsException;

import com.mp3.ui.MainWindowInterface;
import com.mscg.jID3tags.file.MP3File;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.ui.panel.fileoperations.RenameFileTab;
import com.mscg.jmp3.ui.panel.fileoperations.dialog.ExecuteRenameFilesDialog;
import com.mscg.jmp3.ui.renderer.elements.IconAndFileListElement;


public class RenameFilesRunnable extends GenericFileOperationRunnable {

    private DefaultListModel filesListModel;
    private RenameFileTab tab;
    private static Pattern cleanFilenamePattern;

    static {
        cleanFilenamePattern = Pattern.compile("[\\?\\\\\\/:\\*\"<>\\|]");
    }

    public RenameFilesRunnable(ExecuteRenameFilesDialog dialog) {
        super(dialog);
        tab = dialog.getTab();
        filesListModel = (DefaultListModel)MainWindowInterface.getInstance().getFilesList();
    }

    @Override
    public void executeInterruptible() {
        File newFile = null;
        BoundedRangeModel rangeModel = dialog.getProgressBar().getModel();
        rangeModel.setMinimum(0);
        rangeModel.setMaximum(2 * filesListModel.size());
        try {
            String value = null;
            int progess = 0;
            for(Object elem : filesListModel.toArray()) {
                if(isInterrupted())
                    return;

                IconAndFileListElement fileElem = (IconAndFileListElement)elem;
                File file = fileElem.getFile();

                dialog.getActualFileName().setText(file.getName());
                rangeModel.setValue(++progess);

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

                newFileName = clearFilename(newFileName);
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

                rangeModel.setValue(++progess);
            }

            MainWindowInterface.showMessage(Messages.getString("operations.file.rename.execute.done.title"),
                                            Messages.getString("operations.file.rename.execute.done.message").
                                                replace("${number}", "" + filesListModel.size()));
        } catch(FileExistsException e) {
            LOG.error("Cannot rename file", e);
            MainWindowInterface.showError(new Exception(Messages.getString("operations.file.error.file.exists").
                                                  replace("${filepath}", newFile.getAbsolutePath())));
        } catch(Exception e) {
            LOG.error("Cannot rename file", e);
            MainWindowInterface.showError(e);
        } finally {
            dialog.setVisible(false);
        }
    }

    protected String clearFilename(String filename) {
        Matcher matcher = cleanFilenamePattern.matcher(filename);
        return matcher.replaceAll("");
    }

}
