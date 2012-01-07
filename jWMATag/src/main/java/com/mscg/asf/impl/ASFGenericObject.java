package com.mscg.asf.impl;

import java.io.IOException;
import java.io.InputStream;

import com.mscg.asf.ASFObject;
import com.mscg.asf.exception.InvalidObjectDataException;
import com.mscg.asf.guid.ASFObjectGUID;

/**
 * This class maps an ASF object whose GUID is unknow to the library.
 *
 * @author Giuseppe Miscione
 *
 */
public class ASFGenericObject extends ASFObject {

    protected byte data[];

    /**
     * Creates a new instance of an ASF object with a GUID unknown to the library.
     *
     * @param guid The object GUID.
     * @param length The length of the object, in bytes.
     * @param data The object data as an {@link InputStream}.
     * @throws InvalidObjectDataException If the object data are not valid.
     */
    public ASFGenericObject(ASFObjectGUID guid, long length, InputStream data) throws InvalidObjectDataException {
        super(guid, length, data);
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    protected void parseData(InputStream data) throws InvalidObjectDataException {
        try {
            this.data = new byte[(int)length];
            int bytesRead = data.read(this.data);
            if(bytesRead != length)
                throw new InvalidObjectDataException("The stream contains only " + bytesRead + " bytes, " +
                		                             length + " bytes expected");
        } catch(IOException e) {
            throw new InvalidObjectDataException("Cannot read object data", e);
        }
    }


}
