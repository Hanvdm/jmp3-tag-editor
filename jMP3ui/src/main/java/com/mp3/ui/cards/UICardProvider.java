package com.mp3.ui.cards;

import java.io.FileNotFoundException;
import java.util.Set;

import com.mp3.ui.MainWindowInterface;

public interface UICardProvider {

    public Set<UICardInfo> getCardInfos(MainWindowInterface mainWindow) throws FileNotFoundException;

}
