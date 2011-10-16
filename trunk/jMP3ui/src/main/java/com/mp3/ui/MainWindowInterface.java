package com.mp3.ui;

import javax.swing.JButton;
import javax.swing.JFrame;

public abstract class MainWindowInterface extends JFrame {

    private static final long serialVersionUID = -8665490887184915742L;

    public abstract JButton getPrevButton();

    public abstract JButton getNextButton();

}
