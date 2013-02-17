package com.mscg.jmp3.transformator.impl;

import java.util.regex.Pattern;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.transformator.SimpleParametrizedStringTransformator;
import com.mscg.jmp3.transformator.exception.InvalidTransformatorParameterException;
import com.mscg.jmp3.ui.util.input.InputPanel;

public class RegExpReplaceTransformator extends SimpleParametrizedStringTransformator {

    private static final String PARAM_REGEXP = "transform.string.replace.regexp.regexp";
    private static final String PARAM_REPLACE = "transform.string.replace.regexp.replace";

    private static final long serialVersionUID = -3014262173616813237L;

    private Pattern regexp;
    private String regexpStr;
    private String replace;

    @Override
    public String getName() {
        return Messages.getString("transform.string.replace.regexp.name");
    }

    @Override
    public String getListValue() {
        return Messages.getString("transform.string.replace.regexp.list").
            replace("${regExp}", regexpStr).
            replace("${replace}", replace);
    }

    @Override
    public String[] getParametersNames() {
        return new String[]{PARAM_REGEXP, PARAM_REPLACE};
    }

    @Override
    protected void initParameterPanels() {
        if(regexpStr != null)
            panels.get(PARAM_REGEXP).setValue(regexpStr);

        if(replace != null)
            panels.get(PARAM_REPLACE).setValue(replace);
    }

    @Override
    public void saveParameters() throws InvalidTransformatorParameterException {
        InputPanel inputPanel = panels.get(PARAM_REGEXP);
        try {
            String parameter = inputPanel.getValue();
            regexp = Pattern.compile(parameter);
            regexpStr = parameter;
        } catch(Exception e) {
            throw new InvalidTransformatorParameterException(1, inputPanel, "Invalid regular expression", e);
        }
        replace = panels.get(PARAM_REPLACE).getValue();
    }

    @Override
    public String transformString(String orig, Integer indexInList) {
        try {
            return regexp.matcher(orig).replaceAll(replace);
        } catch(Exception e) {
            return orig;
        }
    }

}
