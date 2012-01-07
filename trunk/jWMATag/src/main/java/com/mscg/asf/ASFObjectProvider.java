package com.mscg.asf;

import java.util.Map;

import com.mscg.asf.guid.ASFObjectGUID;

/**
 * Services that provides ASF objects implementation
 * must implement this interface.
 *
 * @author Giuseppe Miscione
 *
 */
public interface ASFObjectProvider {

    /**
     * Returns a <code>{@link Map}&lt;{@link ASFObjectGUID}, Class&lt;? extends {@link ASFObject}&gt;&gt;</code>
     * providing the link between a GUID and its object implementation provided by the actual service.
     *
     * @return A <code>{@link Map}&lt;{@link ASFObjectGUID}, Class&lt;? extends {@link ASFObject}&gt;&gt;</code>
     * providing the link between a GUID and its object implementation.
     */
    public Map<ASFObjectGUID, Class<? extends ASFObject>> getGUIDsToObjects();

}
