package com.mscg.jID3tags.objects;

import com.mscg.jID3tags.exception.MP3TagIntegerBytesWrongLengthException;
import com.mscg.jID3tags.exception.MP3TagIntegerException;

/**
 *
 * @author Giuseppe Miscione
 */
public class StandardInteger implements I_Mp3TagInteger {

    private byte bytes[];

    /**
     * Builds a new instance of {@link StandardInteger} that represents the
     * provided integer.
     *
     * @param value
     *            The integer that will be encapsulated in the
     *            {@link StandardInteger} instance.
     */
    public StandardInteger(int value) {
        setIntValue(value);
    }

    /**
     * Builds a new instance of {@link SynchsafeInteger} whose bytes are the
     * provided ones.
     *
     * @param bytes
     *            The bytes that represent the synchsafe integer.
     * @throws MP3TagIntegerBytesWrongLengthException
     *             If the size of the bytes buffer is not 4.
     */
    public StandardInteger(byte bytes[]) throws MP3TagIntegerBytesWrongLengthException {
        setBytes(bytes);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getIntValue() {
        int ret = 0;
        for (int i = 0; i < bytes.length; i++) {
            ret = ret << 8;
            ret = ret | (bytes[i] & 0xFF);
        }
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SynchsafeInteger other = (SynchsafeInteger) obj;
        for (int i = 0; i < bytes.length; i++) {
            if (other.getBytes()[i] != bytes[i])
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            ret.append(String.format("%02X", bytes[i]));
        }
        ret.append(" (" + getIntValue() + ")");
        return ret.toString();
    }

    /**
     * Sets the bytes that identifies this {@link StandardInteger}.
     *
     * @param bytes
     *            the bytes to set.
     * @throws MP3TagIntegerBytesWrongLengthException
     *             If the size of the bytes buffer is not 4.
     */
    private void setBytes(byte[] bytes) throws MP3TagIntegerBytesWrongLengthException {
        if (bytes.length != 4)
            throw new MP3TagIntegerBytesWrongLengthException();
        this.bytes = bytes;
    }

    /**
     * Sets the integer that will be encapsulated in the {@link StandardInteger}
     * instance.
     *
     * @param value
     *            The integer that will be encapsulated in the
     *            {@link StandardInteger} instance.
     */
    private void setIntValue(int value) {
        byte tmp[] = new byte[4];
        try {
            for (int i = 3; i >= 0; i--)
                tmp[i] = (byte) ((value >> (8 * (3 - i))) & 0x000000FF);
            setBytes(tmp);
        } catch (MP3TagIntegerException e) {
            // this should never happen!
        }
    }

}
