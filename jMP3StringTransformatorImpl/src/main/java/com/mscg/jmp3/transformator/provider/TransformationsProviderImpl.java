package com.mscg.jmp3.transformator.provider;

import java.util.LinkedList;
import java.util.List;

import com.mscg.jmp3.transformator.StringTransformator;
import com.mscg.jmp3.transformator.StringTransformatorProvider;
import com.mscg.jmp3.transformator.impl.CapitalizeWordsTransformator;
import com.mscg.jmp3.transformator.impl.LowerCaseTransformator;
import com.mscg.jmp3.transformator.impl.RegExpReplaceTransformator;
import com.mscg.jmp3.transformator.impl.ReplaceStringTransformator;
import com.mscg.jmp3.transformator.impl.UpperCaseTransformator;

public class TransformationsProviderImpl implements StringTransformatorProvider {

    public List<Class<? extends StringTransformator>> getStringTransformators() {
        List<Class<? extends StringTransformator>> list = new LinkedList<Class<? extends StringTransformator>>();
        list.add(ReplaceStringTransformator.class);
        list.add(RegExpReplaceTransformator.class);
        list.add(CapitalizeWordsTransformator.class);
        list.add(UpperCaseTransformator.class);
        list.add(LowerCaseTransformator.class);
        return list;
    }

}