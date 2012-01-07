package com.mscg.asf;

import com.mscg.asf.guid.ASFObjectGUID;

/**
 * This class models and abstract ASF object,
 * wrappign its GUID, size and data load.
 *
 * @author Giuseppe Miscione
 *
 */
public abstract class ASFObject {

    protected ASFObjectGUID guid;
    protected byte data[];

    /**
     * Builds a new abstract ASF object.
     *
     * @param guid The object GUID
     * @param data The object data as a byte array. The data length
     * is computed from the array length. This parameters can be <code>null</code>.
     */
    protected ASFObject(ASFObjectGUID guid, byte data[]) {
        this.guid = guid;
        setData(data);
    }

    /**
     * Returns the object GUID
     * @return the object GUID
     */
    public ASFObjectGUID getGuid() {
        return guid;
    }

    /**
     * Returns the object data.
     *
     * @return The object data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Changes the object data.
     *
     * @param data The new data to set in the object
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return data == null ? 0 : data.length;
    }

}
