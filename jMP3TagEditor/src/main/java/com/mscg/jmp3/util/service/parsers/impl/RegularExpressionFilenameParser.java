package com.mscg.jmp3.util.service.parsers.impl;

import com.mp3.ui.parsers.FilenamePatternParser;
import com.mscg.jmp3.exception.InvalidTagValueException;
import com.mscg.jmp3.ui.util.input.InputPanel;

public class RegularExpressionFilenameParser implements FilenamePatternParser {

    @Override
    public String getParserID() {
        return this.getClass().getCanonicalName();
    }

    @Override
    public String getParserName() {
        return null;
    }

    @Override
    public InputPanel getParserInputPanel() {
        return null;
    }

    @Override
    public void initParser() {
    }

    @Override
    public void setFilename(String filename) {
    }

    @Override
    public String parseValue(String value) throws InvalidTagValueException, IndexOutOfBoundsException,
                                                  Exception {
        return null;
    }

}
