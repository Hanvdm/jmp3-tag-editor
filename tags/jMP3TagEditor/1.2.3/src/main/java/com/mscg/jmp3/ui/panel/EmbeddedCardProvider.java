package com.mscg.jmp3.ui.panel;

import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.mp3.ui.MainWindowInterface;
import com.mp3.ui.cards.UICardInfo;
import com.mp3.ui.cards.UICardProvider;

public class EmbeddedCardProvider implements UICardProvider {

    public static final String FILENAME_OPERATIONS_CARD_NAME = "FilenameOperationsCard";
    public static final String FILE_CHOOSE_CARD_NAME = "FileChooseCard";

    @Override
    public Set<UICardInfo> getCardInfos(MainWindowInterface mainWindow) throws FileNotFoundException {
        Set<UICardInfo> cards = new LinkedHashSet<UICardInfo>();
        cards.add(new UICardInfo(0, FILE_CHOOSE_CARD_NAME, new FileChooseCard(mainWindow)));
        cards.add(new UICardInfo(1, FILENAME_OPERATIONS_CARD_NAME, new FilenameOperationsCard(mainWindow)));
        return cards;
    }

}
