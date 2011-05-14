package com.mscg.jmp3.transformator;

import java.io.Serializable;
import java.security.InvalidParameterException;

public interface StringTransformator extends Serializable {

    public String getName();

    public String getListValue();

    public String[] getParametersNames();

    public void setParameter(int index, String parameter) throws InvalidParameterException;

    public String[] getParameters();

    public String transformString(String orig);

}
