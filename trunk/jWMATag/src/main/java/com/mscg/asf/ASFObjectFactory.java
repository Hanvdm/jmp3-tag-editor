package com.mscg.asf;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mscg.asf.guid.ASFObjectGUID;
import com.mscg.asf.impl.ASFGenericObject;
import com.mscg.jmp3.util.service.ServiceLoader;

public class ASFObjectFactory {

    private static Map<ASFObjectGUID, Class<? extends ASFObject>> objects;

    static {
        objects = new LinkedHashMap<ASFObjectGUID, Class<? extends ASFObject>>();
        ServiceLoader<ASFObjectProvider> serviceLoader = ServiceLoader.load(ASFObjectProvider.class);
        for(ASFObjectProvider provider : serviceLoader) {
            objects.putAll(provider.getGUIDsToObjects());
        }
    }

    public static Class<? extends ASFObject> getObjectClassForGUID(ASFObjectGUID guid) {
        if(objects.containsKey(guid))
            return objects.get(guid);
        else
            return ASFGenericObject.class;
    }

}
