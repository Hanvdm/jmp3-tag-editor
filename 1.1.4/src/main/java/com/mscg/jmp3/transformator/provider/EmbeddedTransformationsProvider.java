package com.mscg.jmp3.transformator.provider;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mscg.jmp3.transformator.StringTransformator;
import com.mscg.jmp3.transformator.StringTransformatorProvider;
import com.mscg.jmp3.transformator.impl.ReplaceStringTransformator;

public class EmbeddedTransformationsProvider implements StringTransformatorProvider {

    @Override
    public Map<Integer, Class<? extends StringTransformator>> getStringTransformators() {
        Map<Integer, Class<? extends StringTransformator>> ret =
            new LinkedHashMap<Integer, Class<? extends StringTransformator>>();
        ret.put(1, ReplaceStringTransformator.class);
        return ret;
    }

}
