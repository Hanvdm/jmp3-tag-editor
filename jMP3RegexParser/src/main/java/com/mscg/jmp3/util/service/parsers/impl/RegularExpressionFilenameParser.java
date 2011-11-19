package com.mscg.jmp3.util.service.parsers.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mp3.ui.parsers.FilenamePatternParser;
import com.mscg.jmp3.exception.InvalidRegExTagValueException;
import com.mscg.jmp3.exception.InvalidTagValueException;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.ui.util.input.InputPanel;
import com.mscg.jmp3.ui.util.input.TextBoxInputPanel;

public class RegularExpressionFilenameParser implements FilenamePatternParser {

    private static Pattern regExGroupPattern = Pattern.compile("^\\s*%(\\d+)\\s*$");

    private InputPanel regExpPanel;
    private Pattern fileNamePattern;
    private Matcher fileNameMatcher;

    public String getParserID() {
        return this.getClass().getCanonicalName();
    }

    public String getParserName() {
        return Messages.getString("operations.file.taginfo.parser.regex.name");
    }

    public InputPanel getParserInputPanel() {
        if(regExpPanel == null)
            regExpPanel = new TextBoxInputPanel(Messages.getString("operations.file.taginfo.info.regex"), 0, 10);
        return regExpPanel;
    }

    public void initParser() {
        String value = getParserInputPanel().getValue();
        if(value != null && value.length() != 0) {
            fileNamePattern = Pattern.compile(value);
        }
    }

    public void setFilename(String filename) {
        if(fileNamePattern != null) {
            fileNameMatcher = fileNamePattern.matcher(filename);
            fileNameMatcher.find();
        }
    }

    public String parseValue(String value) throws InvalidTagValueException, IndexOutOfBoundsException,
                                                  Exception {
        String ret = value;

        Matcher regExGroupMatcher = regExGroupPattern.matcher(value);
        if(regExGroupMatcher.find()) {
            if(fileNameMatcher  == null)
                throw new InvalidRegExTagValueException("Regular expression is not defined");
            int groupIndex = Integer.parseInt(regExGroupMatcher.group(1));
            try {
                ret = fileNameMatcher.group(groupIndex);
            } catch(IllegalStateException e) {
                throw new IndexOutOfBoundsException("" + groupIndex + " is not a valid group index");
            }
        }

        return ret;
    }

}
