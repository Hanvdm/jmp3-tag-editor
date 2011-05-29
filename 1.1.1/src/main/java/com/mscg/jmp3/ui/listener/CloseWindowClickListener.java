package com.mscg.jmp3.ui.listener;

import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class CloseWindowClickListener implements ActionListener {
    private Window window;

    public CloseWindowClickListener(Window window) {
        this.window = window;
    }

    public void actionPerformed(ActionEvent e) {
        WindowEvent wev = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }
}