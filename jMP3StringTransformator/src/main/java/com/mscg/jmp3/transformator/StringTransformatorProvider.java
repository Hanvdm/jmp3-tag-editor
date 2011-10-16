package com.mscg.jmp3.transformator;

import java.util.Map;

public interface StringTransformatorProvider {

    public Map<Integer, Class<? extends StringTransformator>> getStringTransformators();

}
