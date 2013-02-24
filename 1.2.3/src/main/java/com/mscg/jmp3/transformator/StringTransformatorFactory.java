package com.mscg.jmp3.transformator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mscg.jmp3.util.service.ServiceLoader;

public class StringTransformatorFactory {

    private static List<Class<? extends StringTransformator>> list;

    static {
        list = new LinkedList<Class<? extends StringTransformator>>();

        Map<Integer, Class<? extends StringTransformator>> transformators =
            new TreeMap<Integer, Class<? extends StringTransformator>>();
        ServiceLoader<StringTransformatorProvider> serviceLoader = ServiceLoader.load(StringTransformatorProvider.class);
        for(StringTransformatorProvider provider : serviceLoader) {
            transformators.putAll(provider.getStringTransformators());
        }

        // transformators are ordered against the integer key of the map
        list.addAll(transformators.values());
    }

    public static List<Class<? extends StringTransformator>> getStringTransformators() {
        return list;
    }

}
