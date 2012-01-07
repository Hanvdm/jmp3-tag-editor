package com.mscg.asf;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

import com.mscg.asf.exception.ASFObjectAllocationException;
import com.mscg.asf.exception.GUIDSizeException;
import com.mscg.asf.exception.InvalidObjectDataException;
import com.mscg.asf.guid.ASFObjectGUID;
import com.mscg.asf.util.Util;

/**
 * This class models and abstract ASF object,
 * wrapping its GUID, size and data load.
 *
 * @author Giuseppe Miscione
 *
 */
public abstract class ASFObject {

    public static final int ASF_OBJECT_HEADER_SIZE = 24;

    /**
     * This utility method parses an {@link InputStream} and
     * returns the corresponding {@link ASFObject}
     *
     * @param is The {@link InputStream} to parse.
     * @return An {@link ASFObject} containing the informations
     * provided by the stream.
     *
     * @throws IOException If an I/O error occurs.
     * @throws GUIDSizeException If the data in the stream are too small
     * to contain the object GUID.
     * @throws InvalidObjectDataException If the object data are not valid.
     * @throws ASFObjectAllocationException If an error occurs while loading the ASF object class.
     */
    public static ASFObject parseStream(InputStream is) throws GUIDSizeException, IOException,
                                                               InvalidObjectDataException,
                                                               ASFObjectAllocationException {
        ASFObjectGUID guid = new ASFObjectGUID(is);
        Class<? extends ASFObject> objectClassForGUID = ASFObjectFactory.getObjectClassForGUID(guid);
        try {
            long dataSize = Util.readLittleEndianLong(is);

            Constructor<? extends ASFObject> constructor = objectClassForGUID.getConstructor(
                new Class[]{ASFObjectGUID.class, long.class, InputStream.class});
            ASFObject asfObject = constructor.newInstance(new Object[]{guid, dataSize, is});
            return asfObject;
        } catch (Exception e) {
            throw new ASFObjectAllocationException("Cannot allocate object of class " + objectClassForGUID.getCanonicalName(),
                                                   e);
        }
    }

    protected ASFObjectGUID guid;
    protected long length;

    /**
     * Builds a new abstract ASF object.
     *
     * @param guid The object GUID.
     * @param length The length of the object, in bytes.
     * @param data The object data as an {@link InputStream}.
     * @throws InvalidObjectDataException If the object data are not valid.
     */
    protected ASFObject(ASFObjectGUID guid, long length, InputStream data) throws InvalidObjectDataException {
        this.guid = guid;
        updateData(length, data, true);
    }

    /**
     * Returns the object GUID.
     *
     * @return the object GUID.
     */
    public ASFObjectGUID getGuid() {
        return guid;
    }

    /**
     * Returns the object data.
     *
     * @return The object data.
     */
    public abstract Object getData();

    /**
     * Parse the provided {@link InputStream} to build the
     * inner object data.
     *
     * @param data The {@link InputStream} to parse.
     * @throws InvalidObjectDataException If the data are not valid for the object.
     */
    protected abstract void parseData(InputStream data) throws InvalidObjectDataException;

    /**
     * Returns the data length, without the header bytes.
     *
     * @return the length The data length, without the header bytes.
     */
    public long getLength() {
        return length;
    }

    /**
     * Updates the inner data of the object.
     *
     * @param length The length of the data.
     * @param data An {@link InputStream} with the object data.
     * @param lengthIncludesHeader A boolean switch that indicates if the length
     * includes also the header length (24 bytes).
     * @throws InvalidObjectDataException If the data are not valid for the object.
     */
    public void updateData(long length, InputStream data, boolean lengthIncludesHeader) throws InvalidObjectDataException {
        this.length = length - (lengthIncludesHeader ? ASF_OBJECT_HEADER_SIZE : 0);
        parseData(data);
    }
}
