package com.mscg.jmp3.transformator.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.transformator.StringTransformator;
import com.mscg.jmp3.transformator.exception.InvalidTransformatorParameterException;
import com.mscg.jmp3.ui.util.input.InputPanel;
import com.mscg.jmp3.ui.util.input.TextAreaInputPanel;

public class ReplaceTitleStringTransformator implements StringTransformator {

    private static final long serialVersionUID = 2258274299985511140L;

    protected Map<Integer, String> positionToTitle;
    protected InputPanel inputPanel;

    public ReplaceTitleStringTransformator() {
        positionToTitle = new LinkedHashMap<Integer, String>();
    }

    @Override
    public String getName() {
        return Messages.getString("transform.title.replace.name");
    }

    @Override
    public String getListValue() {
        return Messages.getString("transform.title.replace.list")
                           .replace("${numTracks}", Integer.toString(positionToTitle.size()));
    }

    @Override
    public Collection<InputPanel> getParameterPanels() {
        if(inputPanel == null) {
            inputPanel = new TextAreaInputPanel(Messages.getString("transform.title.replace.help"), Messages.getString("transform.title.replace.label"));
        }
        return Arrays.asList(inputPanel);
    }

    @Override
    public void saveParameters() throws InvalidTransformatorParameterException {
        String value = inputPanel.getValue();
        String lines[] = value.split("\\r?\\n");
        Map<Integer, String> tmp = new LinkedHashMap<Integer, String>();
        try {
            for(int i = 0, l = lines.length; i < l; i++) {
                String line = lines[i];
                int index = line.indexOf("->");
                String number = line.substring(0, index);
                String title = line.substring(index + 2);
                try {
                    tmp.put(Integer.parseInt(number, 10), title);
                } catch(NumberFormatException e) {
                    throw new InvalidTransformatorParameterException(1, inputPanel, "Invalid number format for line " + (i + 1));
                }
            }
            positionToTitle = tmp;
        } catch(InvalidTransformatorParameterException e) {
            throw e;
        }
    }

    @Override
    public String transformString(String orig, Integer indexInList) {
        if(indexInList == null)
            return orig;
        String title = positionToTitle.get(indexInList);
        return title == null ? orig : title;
    }

}
