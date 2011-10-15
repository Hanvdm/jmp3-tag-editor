package com.mscg.jmp3.transformator;

import java.util.List;

public interface StringTransformatorProvider {

    public List<Class<? extends StringTransformator>> getStringTransformators();

}
