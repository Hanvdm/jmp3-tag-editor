package com.mscg.jmp3.transformator.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.transformator.SimpleParametrizedStringTransformator;
import com.mscg.jmp3.transformator.exception.InvalidTransformatorParameterException;
import com.mscg.jmp3.ui.util.input.InputPanel;

public class CapitalizeWordsTransformator extends SimpleParametrizedStringTransformator {

    private static final long serialVersionUID = -7348870702686553349L;

    private static final String PARAM_WORDS_CHARS = "transform.string.capitalize.words.param.characters";

    private int minCharacters;

    private Pattern pattern;

    public CapitalizeWordsTransformator() {
        minCharacters = 1;
    }

    @Override
    public String getName() {
        return Messages.getString("transform.string.capitalize.words.name");
    }

    @Override
    public String getListValue() {
        return Messages.getString("transform.string.capitalize.words.list").replace("${length}",
                                                                                    Integer.toString(minCharacters));
    }

    @Override
    public String[] getParametersNames() {
        return new String[]{PARAM_WORDS_CHARS};
    }

    @Override
    protected void initParameterPanels() {
        panels.get(PARAM_WORDS_CHARS).setValue(Integer.toString(minCharacters));
    }

    @Override
    public void saveParameters() throws InvalidTransformatorParameterException {
        InputPanel inputPanel = panels.get(PARAM_WORDS_CHARS);
        try {
            minCharacters = Integer.parseInt(inputPanel.getValue());
        } catch(NumberFormatException e){
            throw new InvalidTransformatorParameterException(1, inputPanel, this.getClass().getSimpleName() + " requires an integer parameter");
        }
        if(minCharacters < 1)
            minCharacters = 1;
    }

    @Override
    public String transformString(String orig, Integer indexInList) {
        StringBuffer ret = new StringBuffer(orig);

        pattern = Pattern.compile("\\b(\\w{" + minCharacters + "})");
        Matcher matcher = pattern.matcher(orig);
        while(matcher.find()) {
            String substring = orig.substring(matcher.start(1), matcher.end(1));
            ret.setCharAt(matcher.start(1), substring.toUpperCase().charAt(0));
        }
        return ret.toString();
    }

}
