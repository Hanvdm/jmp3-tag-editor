package com.mscg.asf;

import com.mscg.asf.guid.ASFObjectGUID;

/**
 * This class models and abstract ASF object,
 * wrappign its GUID, size and data load.
 *
 * @author Giuseppe Miscione
 *
 */
public abstract class AbstractASFObject {

    protected ASFObjectGUID guid;

    /**
     * Builds a new abstract ASF object.
     *
     * @param guid The object GUID
     */
    protected AbstractASFObject(ASFObjectGUID guid) {
        this.guid = guid;
    }

    /**
     * Returns the object GUID
     * @return the object GUID
     */
    public ASFObjectGUID getGuid() {
        return guid;
    }

}
