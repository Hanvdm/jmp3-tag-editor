package com.mscg.jmp3.ui.renderer.elements;

import com.mscg.jmp3.transformator.StringTransformator;

public class StringTransformatorElement {

    private StringTransformator transformator;

    public StringTransformatorElement() {
        this(null);
    }

    public StringTransformatorElement(StringTransformator transformator) {
        this.transformator = transformator;
    }

    /**
     * @return the transformator
     */
    public StringTransformator getTransformator() {
        return transformator;
    }

    /**
     * @param transformator the transformator to set
     */
    public void setTransformator(StringTransformator transformator) {
        this.transformator = transformator;
    }

    @Override
    public String toString() {
        return (transformator == null ? "-" : transformator.getListValue());
    }

}
