package com.mscg.jmp3.util.service.parsers.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.mp3.ui.parsers.FilenamePatternParser;
import com.mp3.ui.parsers.InputPanelAwareParser;
import com.mp3.ui.parsers.SelectionAwareParser;
import com.mscg.jmp3.exception.InvalidTagValueException;
import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.ui.util.input.InputPanel;
import com.mscg.jmp3.ui.util.input.TextBoxInputPanel;

public class SimpleFilenameParser implements FilenamePatternParser, InputPanelAwareParser,
                                             SelectionAwareParser, DocumentListener, ActionListener {

    public static final String AUTHOR_TAG = "${author}";
    public static final String ALBUM_TAG = "${album}";
    public static final String TITLE_TAG = "${title}";
    public static final String NUMBER_TAG = "${track}";
    public static final String YEAR_TAG = "${year}";

    protected InputPanel patternPanel;

    protected InputPanel authorPanel;
    protected InputPanel albumPanel;
    protected InputPanel titlePanel;
    protected InputPanel numberPanel;
    protected InputPanel yearPanel;

    protected List<String> separators;
    protected Map<String, String> tokenToValue;

    public String getParserID() {
        return this.getClass().getCanonicalName();
    }

    public String getParserName() {
        return Messages.getString("operations.file.taginfo.parser.simple.name");
    }

    public InputPanel getParserInputPanel() {
        if(patternPanel == null) {
            patternPanel = new TextBoxInputPanel(Messages.getString("operations.file.taginfo.info.simple"), 0, 10);
            ((JTextField)patternPanel.getValueComponent()).getDocument().addDocumentListener(this);
//            DefaultComboBoxModel model = new DefaultComboBoxModel();
//            patternPanel = new ComboboxInputPanel(Messages.getString("operations.file.taginfo.info.simple"),
//                                                  model,
//                                                  true);
//            ((JComboBox)patternPanel.getValueComponent()).addActionListener(this);
        }
        return patternPanel;
    }

    public void initParser() {
        Pattern tokenPattern = Pattern.compile("\\$\\{\\w+\\}");
        String pattern = patternPanel.getValue();
        Matcher tokenMatcher = tokenPattern.matcher(pattern);
        separators = new LinkedList<String>();
        Integer lastEnd = null;
        while(tokenMatcher.find()) {
            String match = tokenMatcher.group();
            if(AUTHOR_TAG.equals(match) || ALBUM_TAG.equals(match) || TITLE_TAG.equals(match) ||
               NUMBER_TAG.equals(match) || YEAR_TAG.equals(match)) {
                if(lastEnd != null)
                    separators.add(pattern.substring(lastEnd, tokenMatcher.start()));
                lastEnd = tokenMatcher.end();
            }
        }
        if(lastEnd < pattern.length())
            separators.add(pattern.substring(lastEnd));
        else
            separators.add("\n");
    }

    public void setFilename(String filename) {
        tokenToValue = new HashMap<String, String>();
        String pattern = patternPanel.getValue();
        for(String separator : separators) {
            String value = null;
            String token = null;
            if("\n".equals(separator)) {
                value = filename;
                token = pattern;
            }
            else {
                int index = filename.indexOf(separator);
                value = filename.substring(0, index);
                filename = filename.substring(index + separator.length());
                index = pattern.indexOf(separator);
                token = pattern.substring(0, index);
                pattern = pattern.substring(index + separator.length());
            }
            tokenToValue.put(token, value);
        }
    }

    public String parseValue(String value) throws InvalidTagValueException, IndexOutOfBoundsException, Exception {
        return tokenToValue.containsKey(value) ? tokenToValue.get(value) : value;
    }

    public void setReferences(InputPanel authorPanel, InputPanel albumPanel, InputPanel titlePanel,
                              InputPanel numberPanel, InputPanel yearPanel) {
        this.authorPanel = authorPanel;
        this.albumPanel = albumPanel;
        this.titlePanel = titlePanel;
        this.numberPanel = numberPanel;
        this.yearPanel = yearPanel;
    }

    public void onParserSelected() {
        onTextUpdated(false);
    }

    public void onParserUnselected() {
        onTextUpdated(true);
    }

    public void insertUpdate(DocumentEvent e) {
        onTextUpdated(false);
    }

    public void removeUpdate(DocumentEvent e) {
        onTextUpdated(false);
    }

    public void changedUpdate(DocumentEvent e) {
        onTextUpdated(false);
    }

    public void actionPerformed(ActionEvent e) {
        onTextUpdated(false);
    }

    protected synchronized void onTextUpdated(boolean clean) {
        if(clean) {
            authorPanel.setValue("");
            authorPanel.getValueComponent().setEnabled(true);
            albumPanel.setValue("");
            albumPanel.getValueComponent().setEnabled(true);
            titlePanel.setValue("");
            titlePanel.getValueComponent().setEnabled(true);
            numberPanel.setValue("");
            numberPanel.getValueComponent().setEnabled(true);
            yearPanel.setValue("");
            yearPanel.getValueComponent().setEnabled(true);
        }
        else {
            String value = patternPanel.getValue();

            if(value.contains(AUTHOR_TAG)) {
                authorPanel.setValue(AUTHOR_TAG);
                authorPanel.getValueComponent().setEnabled(false);
            }
            else {
                if(!authorPanel.getValueComponent().isEnabled())
                    authorPanel.setValue("");
                authorPanel.getValueComponent().setEnabled(true);
            }

            if(value.contains(ALBUM_TAG)) {
                albumPanel.setValue(ALBUM_TAG);
                albumPanel.getValueComponent().setEnabled(false);
            }
            else {
                if(!albumPanel.getValueComponent().isEnabled())
                    albumPanel.setValue("");
                albumPanel.getValueComponent().setEnabled(true);
            }

            if(value.contains(TITLE_TAG)) {
                titlePanel.setValue(TITLE_TAG);
                titlePanel.getValueComponent().setEnabled(false);
            }
            else {
                if(!titlePanel.getValueComponent().isEnabled())
                    titlePanel.setValue("");
                titlePanel.getValueComponent().setEnabled(true);
            }

            if(value.contains(NUMBER_TAG)) {
                numberPanel.setValue(NUMBER_TAG);
                numberPanel.getValueComponent().setEnabled(false);
            }
            else {
                if(!numberPanel.getValueComponent().isEnabled())
                    numberPanel.setValue("");
                numberPanel.getValueComponent().setEnabled(true);
            }

            if(value.contains(YEAR_TAG)) {
                yearPanel.setValue(YEAR_TAG);
                yearPanel.getValueComponent().setEnabled(false);
            }
            else {
                if(!yearPanel.getValueComponent().isEnabled())
                    yearPanel.setValue("");
                yearPanel.getValueComponent().setEnabled(true);
            }
        }
    }

}
