package com.mscg.jmp3.transformator.impl;

import java.security.InvalidParameterException;
import java.util.regex.Pattern;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.transformator.StringTransformator;

public class RegExpReplaceTransformator implements StringTransformator {

    private static final long serialVersionUID = -3014262173616813237L;

    private Pattern regexp;
    private String regexpStr;
    private String replace;

    public String getName() {
        return Messages.getString("transform.string.replace.regexp.name");
    }

    public String getListValue() {
        return Messages.getString("transform.string.replace.regexp.list").
            replace("${regExp}", regexpStr).
            replace("${replace}", replace);
    }

    public String[] getParametersNames() {
        return new String[]{Messages.getString("transform.string.replace.regexp.regexp"),
                            Messages.getString("transform.string.replace.regexp.replace")};
    }

    public void setParameter(int index, String parameter) throws InvalidParameterException {
        switch(index) {
        case 0:
            try {
                regexp = Pattern.compile(parameter);
                regexpStr = parameter;
            } catch(Exception e) {
                throw new InvalidParameterException("Invalid regular expression");
            }
            break;
        case 1:
            replace = parameter;
            break;
        default:
            throw new InvalidParameterException(this.getClass().getSimpleName() +
                                                " requires exactly 2 parameters.");
        }
    }

    public String[] getParameters() {
        return new String[]{regexpStr, replace};
    }

    public String transformString(String orig) {
        try {
            return regexp.matcher(orig).replaceAll(replace);
        } catch(Exception e) {
            return orig;
        }
    }

}
