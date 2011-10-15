package com.mscg.jmp3.transformator;

import java.util.LinkedList;
import java.util.List;

import com.mscg.jmp3.util.service.ServiceLoader;

public class StringTransformatorFactory {

    private static List<Class<? extends StringTransformator>> list;

    static {
        list = new LinkedList<Class<? extends StringTransformator>>();

        ServiceLoader<StringTransformatorProvider> serviceLoader = ServiceLoader.load(StringTransformatorProvider.class);
        for(StringTransformatorProvider provider : serviceLoader) {
            list.addAll(provider.getStringTransformators());
        }
    }

    public static List<Class<? extends StringTransformator>> getStringTransformators() {
        return list;
    }

}
