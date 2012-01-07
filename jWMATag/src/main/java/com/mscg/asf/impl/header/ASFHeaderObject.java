package com.mscg.asf.impl.header;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.mscg.asf.ASFObject;
import com.mscg.asf.exception.InvalidObjectDataException;
import com.mscg.asf.guid.ASFObjectGUID;
import com.mscg.asf.util.Util;

/**
 * This class maps an ASF header object.
 *
 * @author Giuseppe Miscione
 *
 */
public class ASFHeaderObject extends ASFObject {

    private List<ASFObject> headerObjects;

    /**
     * Creates a new instance of an ASF header object.
     *
     * @param guid The object GUID.
     * @param length The length of the object, in bytes.
     * @param data The object data as an {@link InputStream}.
     * @throws InvalidObjectDataException If the object data are not valid.
     */
    public ASFHeaderObject(ASFObjectGUID guid, long length, InputStream data) throws InvalidObjectDataException {
        super(guid, length, data);
    }

    @Override
    public Object getData() {
        return getHeaderObjects();
    }

    @Override
    public void parseData(InputStream data) throws InvalidObjectDataException {
        try {
            // the first 4 bytes are the number of header objects.
            // Bytes are in little-endian order.
            int objectsNumber = Util.readLittleEndianInt(data);
            headerObjects = new ArrayList<ASFObject>(objectsNumber);

            // check the value of the two reserved bytes
            if(data.read() != 0x01)
                throw new InvalidObjectDataException("Reserved byte 1 in header object is not 0x01");
            if(data.read() != 0x02)
                throw new InvalidObjectDataException("Reserved byte 2 in header object is not 0x02");

            // The remaining data are inner objects
            for(int i = 0; i < objectsNumber; i++) {
                try {
                    headerObjects.add(parseStream(data));
                } catch(Exception e) {
                    throw new InvalidObjectDataException("Cannot read object from data", e);
                }
            }
        } catch(IOException e) {
            throw new InvalidObjectDataException("Cannot read data from stream", e);
        }
    }

    @Override
    public long getLength() {
        long length = 0;
        for(ASFObject headerObject : headerObjects) {
            length += headerObject.getLength() + ASF_OBJECT_HEADER_SIZE;
        }
        length += 6; // add the 6 extra-bytes of the global header
        return length;
    }

    /**
     * Returns the list of inner header objects.
     *
     * @return The list of inner header objects.
     */
    public List<ASFObject> getHeaderObjects() {
        return headerObjects;
    }

    /**
     * Changes the list of inner objects.
     *
     * @param headerObjects The list of inner objects to set
     */
    public void setHeaderObjects(List<ASFObject> headerObjects) {
        this.headerObjects = headerObjects;
    }

}
