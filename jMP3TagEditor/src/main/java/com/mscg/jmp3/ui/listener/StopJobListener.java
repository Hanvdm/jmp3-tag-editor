package com.mscg.jmp3.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.jmp3.util.pool.InterruptibleRunnable;

public class StopJobListener implements ActionListener {

    private static final Logger LOG = LoggerFactory.getLogger(StopJobListener.class);

    private InterruptibleRunnable runnable;
    private JDialog dialog;
    private JButton button;

    public StopJobListener(InterruptibleRunnable runnable, JDialog dialog, JButton button) {
        this.runnable = runnable;
        this.dialog = dialog;
        this.button = button;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        button.setEnabled(false);
        if(runnable != null) {
            try {
                runnable.blockingInterrupt();
            } catch(InterruptedException e) {
                LOG.warn("Interrupted exception catched while stopping a job", e);
            }
        }
        dialog.setVisible(false);
    }

}