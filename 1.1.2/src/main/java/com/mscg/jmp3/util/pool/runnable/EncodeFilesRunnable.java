package com.mscg.jmp3.util.pool.runnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;

import org.apache.commons.io.IOUtils;

import com.mscg.jID3tags.file.MP3File;
import com.mscg.jID3tags.id3v2.ID3v2Tag;
import com.mscg.jID3tags.objects.frames.ID3v2Frame;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.main.AppLaunch;
import com.mscg.jmp3.settings.Settings;
import com.mscg.jmp3.ui.panel.EncodeFileCard;
import com.mscg.jmp3.ui.panel.EncodeFileCard.QualityElement;
import com.mscg.jmp3.ui.panel.fileoperations.dialog.EncodeFilesDialog;
import com.mscg.jmp3.ui.panel.fileoperations.dialog.GenericFilesOperationDialog;
import com.mscg.jmp3.ui.renderer.elements.IconAndFileListElement;
import com.mscg.jmp3.util.Util;
import com.mscg.jmp3.util.io.InputStreamDataReadListener;
import com.mscg.jmp3.util.io.PositionNotifierInputStream;
import com.sun.jna.Platform;

public class EncodeFilesRunnable extends GenericFileOperationRunnable implements InputStreamDataReadListener {

    private EncodeFileCard encodeFileCard;
    private Process encodingProcess;
    private boolean encodingTerminated;
    private BoundedRangeModel rangeModel;
    private BoundedRangeModel actualFileModel;
    private long lastPosition;

    public EncodeFilesRunnable(GenericFilesOperationDialog dialog, EncodeFileCard encodeFileCard) {
        super(dialog);
        this.encodeFileCard = encodeFileCard;
    }

    @Override
    public void executeInterruptible() {
        // Extract the appropriate runnable
        File encoderFile = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            if(Platform.isWindows()) {
                encoderFile = new File(".", "lame.exe");
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream("lame/lame.exe");
            }
            else if(Platform.isLinux()) {
                encoderFile = new File(".", "lame");
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream("lame/lame");
            } else {
                AppLaunch.showError(new Exception(Messages.getString("operations.file.encode.dialog.notsupported")));
                return;
            }
            os = new FileOutputStream(encoderFile);
            IOUtils.copy(is, os);
        } catch(Exception e) {
            LOG.error("Cannot extract encoder executable", e);
            AppLaunch.showError(e);
            return;
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
        encoderFile.setExecutable(true, true);

        File destinationFile = null;
        try {
            DefaultListModel filesModel = (DefaultListModel)AppLaunch.mainWindow.getFileChooseCard().getFilesList().getModel();

            long totalSize = 0l;
            for(int i = 0, l = filesModel.getSize(); i < l; i++) {
                if(isInterrupted())
                    return;
                IconAndFileListElement element = (IconAndFileListElement)filesModel.get(i);
                File origFile = element.getFile();
                totalSize += origFile.length();
            }

            rangeModel = dialog.getProgressBar().getModel();
            rangeModel.setMinimum(0);
            rangeModel.setMaximum((int)totalSize);

            actualFileModel = ((EncodeFilesDialog)dialog).getActualFileProgress().getModel();
            actualFileModel.setMinimum(0);

            Runtime runtime = Runtime.getRuntime();
            for(int i = 0, l = filesModel.getSize(); i < l; i++) {
                if(isInterrupted())
                    return;

                encodingTerminated = false;

                IconAndFileListElement element = (IconAndFileListElement)filesModel.get(i);
                File origFile = element.getFile();
                destinationFile = new File(encodeFileCard.getDestinationFolder().getValue(),
                                           origFile.getName());
                if(!destinationFile.getParentFile().exists()) {
                    destinationFile.getParentFile().mkdirs();
                }

                dialog.getActualFileName().setText(origFile.getName());

                actualFileModel.setMaximum((int)origFile.length());
                actualFileModel.setValue(0);

                List<String> command = new LinkedList<String>();
                command.add(encoderFile.getCanonicalPath());
                command.add("--mp3input");
                command.add("-m"); command.add("j");
                String sampleFreq = Settings.getSetting("encode.sample.freq");
                if(Util.isNotEmpty(sampleFreq)) {
                    command.add("--resample"); command.add(sampleFreq);
                }
                QualityElement quality = (QualityElement)((JComboBox)encodeFileCard.getQuality().getValueComponent()).getSelectedItem();
                command.add("-q"); command.add("" + quality.getValue());
                command.add("-b"); command.add(encodeFileCard.getBitrate().getValue()); command.add("--cbr");
                command.add("-"); // the input file will be provided as a stream
                command.add("\"" + destinationFile.getCanonicalPath() + "\"");

                encodingProcess = runtime.exec(command.toArray(new String[0]));

                lastPosition = 0l;

                InputStream fileStream = null;
                try {
                    fileStream = new PositionNotifierInputStream(new FileInputStream(origFile),
                                                                 origFile.length(), 2048,
                                                                 this);
                    IOUtils.copy(fileStream, encodingProcess.getOutputStream());
                    encodingProcess.getOutputStream().close();
                } finally {
                    IOUtils.closeQuietly(fileStream);
                }

                int result = encodingProcess.waitFor();
                encodingProcess = null;
                if(result != 0) {
                    LOG.warn("Encoder process returned error code " + result);
                }

                if(Boolean.parseBoolean(encodeFileCard.getCopyTag().getValue())) {
                    MP3File mp3Input = new MP3File(origFile);
                    MP3File mp3Output = new MP3File(destinationFile);
                    boolean write = false;
                    if(mp3Input.hasID3v2tag()) {
                        ID3v2Tag id3v2Tag = new ID3v2Tag();
                        for(ID3v2Frame frame : mp3Input.getID3v2tag().getAllframes()) {
                            id3v2Tag.addFrame(frame);
                        }
                        mp3Output.setID3v2tag(id3v2Tag);
                        write = true;
                    }

                    if(mp3Input.hasID3v11tag()) {
                        mp3Output.setID3v11tag(mp3Input.getID3v11tag());
                        write = true;
                    }

                    if(write)
                        mp3Output.write();
                }

            }

            AppLaunch.showMessage(Messages.getString("operations.file.encode.execute.done.title"),
                                  Messages.getString("operations.file.encode.execute.done.message").
                                      replace("${number}", "" + filesModel.getSize()));
        } catch(Exception e) {
            LOG.error("Cannot encode files", e);
            if(!(e instanceof IOException && encodingTerminated))
                AppLaunch.showError(e);
            if(destinationFile != null)
                destinationFile.delete();
        } finally {
            if(encoderFile != null)
                encoderFile.delete();

            dialog.setVisible(false);
        }
    }

    @Override
    public synchronized void interrupt() {
        super.interrupt();
        if(encodingProcess != null) {
            encodingTerminated = true;
            encodingProcess.destroy();
        }
    }

    @Override
    public void onDataRead(long actualPosition, long totalSize) {
        actualFileModel.setValue((int)actualPosition);

        long progress = actualPosition - lastPosition;
        lastPosition = actualPosition;
        rangeModel.setValue(rangeModel.getValue() + (int)progress);
    }

    @Override
    public void onStreamEnd() {
    }

}
