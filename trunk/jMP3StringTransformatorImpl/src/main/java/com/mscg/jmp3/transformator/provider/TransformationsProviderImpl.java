package com.mscg.jmp3.transformator.provider;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mscg.jmp3.transformator.StringTransformator;
import com.mscg.jmp3.transformator.StringTransformatorProvider;
import com.mscg.jmp3.transformator.impl.CapitalizeWordsTransformator;
import com.mscg.jmp3.transformator.impl.LowerCaseTransformator;
import com.mscg.jmp3.transformator.impl.RegExpReplaceTransformator;
import com.mscg.jmp3.transformator.impl.UpperCaseTransformator;

public class TransformationsProviderImpl implements StringTransformatorProvider {

    public Map<Integer, Class<? extends StringTransformator>> getStringTransformators() {
        Map<Integer, Class<? extends StringTransformator>> ret =
            new LinkedHashMap<Integer, Class<? extends StringTransformator>>();
        ret.put(10, RegExpReplaceTransformator.class);
        ret.put(20, CapitalizeWordsTransformator.class);
        ret.put(30, UpperCaseTransformator.class);
        ret.put(40, LowerCaseTransformator.class);
        return ret;
    }

}
