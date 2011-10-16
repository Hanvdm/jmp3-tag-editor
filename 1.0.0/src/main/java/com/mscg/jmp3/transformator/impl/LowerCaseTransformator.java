package com.mscg.jmp3.transformator.impl;

import java.security.InvalidParameterException;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.transformator.StringTransformator;

public class LowerCaseTransformator implements StringTransformator {

    private static final long serialVersionUID = 4652661443805921852L;

    public String getName() {
        return Messages.getString("transform.string.lowercase.name");
    }

    public String getListValue() {
        return getName();
    }

    public String[] getParametersNames() {
        return new String[]{};
    }

    public void setParameter(int index, String parameter) throws InvalidParameterException {
        throw new InvalidParameterException(this.getClass().getSimpleName() + " doesn't need parameters");
    }

    public String[] getParameters() {
        return new String[0];
    }

    public String transformString(String orig) {
        return orig.toLowerCase();
    }

}
