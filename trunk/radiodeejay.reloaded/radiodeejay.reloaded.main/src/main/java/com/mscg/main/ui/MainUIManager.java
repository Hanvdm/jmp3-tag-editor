package com.mscg.main.ui;

import javax.swing.JMenuItem;

public interface MainUIManager {

    public JMenuItem createMenu(String title, boolean isMenu);

    public void showError(Exception e);

    public void showMessage(String title, String message);

    public void startInterface();

}
