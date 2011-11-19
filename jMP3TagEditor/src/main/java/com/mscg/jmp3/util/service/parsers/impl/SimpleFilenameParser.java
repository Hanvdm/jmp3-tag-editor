package com.mscg.jmp3.util.service.parsers.impl;

import com.mp3.ui.parsers.FilenamePatternParser;
import com.mscg.jmp3.exception.InvalidTagValueException;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.ui.util.input.InputPanel;
import com.mscg.jmp3.ui.util.input.TextBoxInputPanel;

public class SimpleFilenameParser implements FilenamePatternParser {

    private InputPanel patternPanel;

    public String getParserID() {
        return this.getClass().getCanonicalName();
    }

    public String getParserName() {
        return Messages.getString("operations.file.taginfo.parser.simple.name");
    }

    public InputPanel getParserInputPanel() {
        if(patternPanel == null)
            patternPanel = new TextBoxInputPanel(Messages.getString("operations.file.taginfo.info.simple"), 0, 10);
        return patternPanel;
    }

    public void initParser() {
    }

    public void setFilename(String filename) {
    }

    public String parseValue(String value) throws InvalidTagValueException, IndexOutOfBoundsException, Exception {
        return value;
    }

}
