package com.mscg.jmp3.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.mscg.jmp3.util.pool.InterruptibleRunnable;

public class StopJobListener implements ActionListener {

    //private static final Logger LOG = LoggerFactory.getLogger(StopJobListener.class);

    private InterruptibleRunnable runnable;
    private JButton button;

    public StopJobListener(InterruptibleRunnable runnable, JButton button) {
        this.runnable = runnable;
        this.button = button;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        button.setEnabled(false);
        runnable.interrupt();
    }

}