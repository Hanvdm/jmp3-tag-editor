package com.mscg.jmp3.transformator.impl;

import java.security.InvalidParameterException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.transformator.StringTransformator;

public class CapitalizeWordsTransformator implements StringTransformator {

    private static final long serialVersionUID = -7348870702686553349L;

    private int minCharacters;

    private Pattern pattern;

    public CapitalizeWordsTransformator() {
        minCharacters = 1;
    }

    public String getName() {
        return Messages.getString("transform.string.capitalize.words.name");
    }

    public String getListValue() {
        return Messages.getString("transform.string.capitalize.words.list").replace("${length}",
                                                                                    "" + minCharacters);
    }

    public String[] getParametersNames() {
        return new String[]{Messages.getString("transform.string.capitalize.words.param.characters")};
    }

    public void setParameter(int index, String parameter) throws InvalidParameterException {
        switch(index) {
        case 0:
            try {
                minCharacters = Integer.parseInt(parameter);
            } catch(NumberFormatException e){
                throw new InvalidParameterException(this.getClass().getSimpleName() +
                                                    " requires an integer parameter");
            }
            if(minCharacters < 1)
                minCharacters = 1;
            break;
        default:
            throw new InvalidParameterException(this.getClass().getSimpleName() +
                                                " requires exactly 1 parameter.");
        }
    }

    public String[] getParameters() {
        return new String[]{"" + minCharacters};
    }

    public String transformString(String orig) {
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
