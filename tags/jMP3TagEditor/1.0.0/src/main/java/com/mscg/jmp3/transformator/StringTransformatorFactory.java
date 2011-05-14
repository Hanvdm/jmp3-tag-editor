package com.mscg.jmp3.transformator;

import java.util.LinkedList;
import java.util.List;

import com.mscg.jmp3.transformator.impl.CapitalizeWordsTransformator;
import com.mscg.jmp3.transformator.impl.LowerCaseTransformator;
import com.mscg.jmp3.transformator.impl.ReplaceStringTransformator;
import com.mscg.jmp3.transformator.impl.UpperCaseTransformator;

public class StringTransformatorFactory {

    private static List<Class<? extends StringTransformator>> list;

    static {
        list = new LinkedList<Class<? extends StringTransformator>>();

        list.add(ReplaceStringTransformator.class);
        list.add(CapitalizeWordsTransformator.class);
        list.add(UpperCaseTransformator.class);
        list.add(LowerCaseTransformator.class);
    }

    public static List<Class<? extends StringTransformator>> getStringTransformators() {
        return list;
    }

}
