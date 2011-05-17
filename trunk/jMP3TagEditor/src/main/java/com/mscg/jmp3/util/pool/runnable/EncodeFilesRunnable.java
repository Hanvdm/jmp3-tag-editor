package com.mscg.jmp3.util.pool.runnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultListModel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.main.AppLaunch;
import com.mscg.jmp3.settings.Settings;
import com.mscg.jmp3.ui.panel.EncodeFileCard;
import com.mscg.jmp3.ui.panel.fileoperations.dialog.GenericFilesOperationDialog;
import com.mscg.jmp3.ui.renderer.elements.IconAndFileListElement;
import com.mscg.jmp3.util.Util;
import com.sun.jna.Platform;

public class EncodeFilesRunnable extends GenericFileOperationRunnable {

    private EncodeFileCard encodeFileCard;

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

        try {
            DefaultListModel filesModel = (DefaultListModel)AppLaunch.mainWindow.getFileChooseCard().getFilesList().getModel();

            BoundedRangeModel rangeModel = dialog.getProgressBar().getModel();
            rangeModel.setMinimum(0);
            rangeModel.setMaximum(2 * filesModel.getSize());

            int progess = 0;
            Runtime runtime = Runtime.getRuntime();
            for(int i = 0, l = filesModel.getSize(); i < l; i++) {
                if(isInterrupted())
                    return;

                IconAndFileListElement element = (IconAndFileListElement)filesModel.get(i);
                File origFile = element.getFile();
                File destinationFile = new File(encodeFileCard.getDestinationFolder().getValue(),
                                                origFile.getName());

                dialog.getActualFileName().setText(origFile.getName());
                rangeModel.setValue(++progess);

                List<String> command = new LinkedList<String>();
                command.add(encoderFile.getCanonicalPath());
                command.add("--mp3input");
                command.add("-m"); command.add("j");
                String sampleFreq = Settings.getSetting("encode.sample.freq");
                if(Util.isNotEmpty(sampleFreq)) {
                    command.add("--resample"); command.add(sampleFreq);
                }
                command.add("- b"); command.add(encodeFileCard.getBitrate().getValue()); command.add("--cbr");
                command.add(origFile.getCanonicalPath());
                command.add(destinationFile.getCanonicalPath());

                Process p = runtime.exec(command.toArray(new String[0]));
                InputStream procIs = p.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                IOUtils.copy(procIs, bos);
                String response = new String(bos.toByteArray());
                LOG.debug("Process response:\n" + response);
                int result = p.exitValue();
                if(result != 0) {
                    LOG.warn("Encoder process returned error code " + result);
                }


                rangeModel.setValue(++progess);
            }

            AppLaunch.showMessage(Messages.getString("operations.file.encode.execute.done.title"),
                                  Messages.getString("operations.file.encode.execute.done.message").
                                      replace("${number}", "" + filesModel.getSize()));
        } catch(Exception e) {
            LOG.error("Cannot encode files", e);
            AppLaunch.showError(e);
        } finally {
            if(encoderFile != null)
                encoderFile.delete();

            dialog.setVisible(false);
        }
    }

}
