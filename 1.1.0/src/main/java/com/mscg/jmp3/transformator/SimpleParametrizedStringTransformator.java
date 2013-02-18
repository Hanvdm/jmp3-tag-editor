package com.mscg.jmp3.transformator;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mscg.jmp3.i18n.Messages;
import com.mscg.jmp3.ui.util.input.InputPanel;
import com.mscg.jmp3.ui.util.input.TextBoxInputPanel;


public abstract class SimpleParametrizedStringTransformator implements StringTransformator {

    private static final long serialVersionUID = 3842328444455191397L;

    protected Map<String, InputPanel> panels;

    protected abstract String[] getParametersNames();

    protected abstract void initParameterPanels();

    @Override
    public Collection<InputPanel> getParameterPanels() {
        String[] paramNames = getParametersNames();

        if(paramNames == null || paramNames.length == 0)
            return Collections.<InputPanel>emptyList();

        if(panels == null) {
            panels = new LinkedHashMap<String, InputPanel>();
            for(String parameterName : paramNames)
                panels.put(parameterName, new TextBoxInputPanel(Messages.getString(parameterName)));
            initParameterPanels();
        }
        return panels.values();
    }

}
