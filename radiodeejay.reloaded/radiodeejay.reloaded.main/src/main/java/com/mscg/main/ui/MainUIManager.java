package com.mscg.main.ui;

public interface MainUIManager {

    public void showError(Exception e);

    public void showMessage(String title, String message);

    public void startInterface();

}
