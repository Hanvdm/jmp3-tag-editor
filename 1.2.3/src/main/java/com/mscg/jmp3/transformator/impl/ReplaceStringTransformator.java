package com.mscg.jmp3.transformator.impl;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.transformator.SimpleParametrizedStringTransformator;
import com.mscg.jmp3.transformator.exception.InvalidTransformatorParameterException;
import com.mscg.jmp3.ui.util.input.InputPanel;

public class ReplaceStringTransformator extends SimpleParametrizedStringTransformator {

    private static final long serialVersionUID = 6511923368765816773L;

    private static final String PARAM_ORIG = "transform.string.replace.param.orig";
    private static final String PARAM_REPLACE = "transform.string.replace.param.replace";

    private String origString;
    private String replaceString;

    @Override
    public String getName() {
        return Messages.getString("transform.string.replace.name");
    }

    @Override
    public String getListValue() {
        return Messages.getString("transform.string.replace.list")
                           .replace("${orig}", origString)
                           .replace("${replace}", replaceString);
    }

    @Override
    public String[] getParametersNames() {
        return new String[]{PARAM_ORIG, PARAM_REPLACE};
    }

    @Override
    protected void initParameterPanels() {
        if(origString != null)
            panels.get(PARAM_ORIG).setValue(origString);

        if(replaceString != null)
            panels.get(PARAM_REPLACE).setValue(replaceString);
    }

    @Override
    public void saveParameters() throws InvalidTransformatorParameterException {
        InputPanel inputPanel = panels.get(PARAM_ORIG);
        String parameter = inputPanel.getValue();
        if(parameter == null || parameter.length() == 0)
            throw new InvalidTransformatorParameterException(1, inputPanel, this.getClass().getSimpleName() + " requires that the original string must not be empty");
        origString = parameter;

        replaceString = panels.get(PARAM_REPLACE).getValue();
    }

    @Override
    public String transformString(String orig, Integer indexInList) {
        return orig.replace(origString, replaceString);
    }

}
