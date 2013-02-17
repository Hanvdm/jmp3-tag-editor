package com.mscg.jmp3.transformator;

import java.io.Serializable;
import java.util.Collection;

import com.mscg.jmp3.transformator.exception.InvalidTransformatorParameterException;
import com.mscg.jmp3.ui.util.input.InputPanel;

public interface StringTransformator extends Serializable {

    public String getName();

    public String getListValue();

    public Collection<InputPanel> getParameterPanels();

    public void saveParameters() throws InvalidTransformatorParameterException;

    public String transformString(String orig, Integer indexInList);

}
