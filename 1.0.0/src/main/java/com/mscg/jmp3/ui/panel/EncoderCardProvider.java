package com.mscg.jmp3.ui.panel;

import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.mp3.ui.MainWindowInterface;
import com.mp3.ui.cards.UICardInfo;
import com.mp3.ui.cards.UICardProvider;

public class EncoderCardProvider implements UICardProvider {

    public static final String ENCODE_FILE_CARD_NAME = "EncodeFileCard";

    @Override
    public Set<UICardInfo> getCardInfos(MainWindowInterface mainWindow) throws FileNotFoundException {
        Set<UICardInfo> cards = new LinkedHashSet<UICardInfo>();
        cards.add(new UICardInfo(10, ENCODE_FILE_CARD_NAME, new EncodeFileCard(mainWindow)));
        return cards;
    }

}
