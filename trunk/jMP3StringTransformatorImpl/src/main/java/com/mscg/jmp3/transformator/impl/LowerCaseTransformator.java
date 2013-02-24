package com.mscg.jmp3.transformator.impl;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.transformator.SimpleParametrizedStringTransformator;
import com.mscg.jmp3.transformator.exception.InvalidTransformatorParameterException;

public class LowerCaseTransformator extends SimpleParametrizedStringTransformator {

    private static final long serialVersionUID = 4652661443805921852L;

    @Override
    public String getName() {
        return Messages.getString("transform.string.lowercase.name");
    }

    @Override
    public String getListValue() {
        return getName();
    }

    @Override
    public String[] getParametersNames() {
        return new String[0];
    }

    @Override
    protected void initParameterPanels() {
    }

    @Override
    public void saveParameters() throws InvalidTransformatorParameterException {
        //throw new InvalidTransformatorParameterException(1, null, this.getClass().getSimpleName() + " doesn't need parameters");
    }

    @Override
    public String transformString(String orig, Integer indexInList) {
        return orig.toLowerCase();
    }

}
