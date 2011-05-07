package com.mscg.jmp3.transformator.impl;

import java.security.InvalidParameterException;

import com.mscg.i18n.Messages;
import com.mscg.jmp3.transformator.StringTransformator;

public class LowerCaseTransformator implements StringTransformator {

    private static final long serialVersionUID = 4652661443805921852L;

    @Override
    public String getName() {
        return Messages.getString("transform.string.lowercase.name");
    }

    @Override
    public String[] getParametersNames() {
        return new String[]{};
    }

    @Override
    public void setParameter(int index, String parameter) throws InvalidParameterException {
        throw new InvalidParameterException(this.getClass().getSimpleName() + " doesn't need parameters");
    }

    @Override
    public String transformString(String orig) {
        return orig.toLowerCase();
    }

}
