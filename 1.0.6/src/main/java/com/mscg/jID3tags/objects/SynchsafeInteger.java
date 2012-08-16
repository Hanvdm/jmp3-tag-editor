package com.mscg.jID3tags.objects;

import com.mscg.jID3tags.exception.MP3TagIntegerBytesWrongLengthException;
import com.mscg.jID3tags.exception.MP3TagIntegerException;
import com.mscg.jID3tags.exception.SynchsafeBadIntegerValueException;
import com.mscg.jID3tags.exception.SynchsafeIntegerBadMSBException;

/**
 *
 * @author Giuseppe Miscione
 */
public class SynchsafeInteger implements I_Mp3TagInteger {

    /**
     * The minimum value for a {@link SynchsafeInteger}.
     */
    public static final int minValue = 0x00000000;
    /**
     * The maximum value for a {@link SynchsafeInteger}.
     */
    public static final int maxValue = 0x0FFFFFFF;

    private byte bytes[];

    /**
     * Builds a new instance of {@link SynchsafeInteger} that represents the
     * provided integer.
     *
     * @param value
     *            The integer that will be encapsulated in the
     *            {@link SynchsafeInteger} instance.
     * @throws SynchsafeBadIntegerValueException
     *             If the value is not between {@link SynchsafeInteger#minValue}
     *             and {@link SynchsafeInteger#maxValue}.
     */
    public SynchsafeInteger(int value) throws SynchsafeBadIntegerValueException {
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
     * @throws SynchsafeIntegerBadMSBException
     *             If any byte in the buffer doesn't have a 0 in its MSB.
     */
    public SynchsafeInteger(byte bytes[]) throws MP3TagIntegerBytesWrongLengthException, SynchsafeIntegerBadMSBException {
        setBytes(bytes);
    }

    /**
     * Returns the bytes that identifies this synchsafe integer.
     *
     * @return the bytes that identifies this synchsafe integer.
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Returns the integer value encapsulated in this {@link SynchsafeInteger}.
     *
     * @return The integer value encapsulated in this {@link SynchsafeInteger}.
     */
    public int getIntValue() {
        int ret = 0;
        for (int i = 0; i < bytes.length; i++) {
            ret = ret << 7;
            ret = ret | (bytes[i] & 0x7F);
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
     * Sets the bytes that identifies this synchsafe integer.
     *
     * @param bytes
     *            the bytes to set.
     * @throws MP3TagIntegerBytesWrongLengthException
     *             If the size of the bytes buffer is not 4.
     * @throws SynchsafeIntegerBadMSBException
     *             If any byte in the buffer doesn't have a 0 in its MSB.
     */
    private void setBytes(byte[] bytes) throws MP3TagIntegerBytesWrongLengthException, SynchsafeIntegerBadMSBException {
        if (bytes.length != 4)
            throw new MP3TagIntegerBytesWrongLengthException();
        for (int i = 0; i < 4; i++) {
            if ((bytes[i] & 0x80) != 0)
                throw new SynchsafeIntegerBadMSBException();
        }
        this.bytes = bytes;
    }

    /**
     * Sets the integer that will be encapsulated in the
     * {@link SynchsafeInteger} instance.
     *
     * @param value
     *            The integer that will be encapsulated in the
     *            {@link SynchsafeInteger} instance.
     * @throws SynchsafeBadIntegerValueException
     *             If the value is not between {@link SynchsafeInteger#minValue}
     *             and {@link SynchsafeInteger#maxValue}.
     */
    private void setIntValue(int value) throws SynchsafeBadIntegerValueException {
        if (value < minValue || value > maxValue)
            throw new SynchsafeBadIntegerValueException();

        byte tmp[] = new byte[4];
        try {
            for (int i = 3; i >= 0; i--)
                tmp[i] = (byte) ((value >> (7 * (3 - i))) & 0x0000007F);

            setBytes(tmp);
        } catch (MP3TagIntegerException e) {
            // this should never happen!
        }
    }
}
