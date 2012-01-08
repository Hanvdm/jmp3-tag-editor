package com.mscg.asf.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.mscg.asf.exception.InvalidObjectDataException;

/**
 * Utility functions holder class.
 *
 * @author Giuseppe Miscione
 *
 */
public class Util {

    /**
     * Avoid allocation of this class.
     */
    protected Util() {

    }

    /**
     * Reads a short integer from a little-endian stream.
     *
     * @param data The little-endian {@link InputStream}.
     * @return The integer value contained in the stream.
     * @throws InvalidObjectDataException If the the stream doesn't contain at least 2 bytes or an error occurs.
     */
    public static int readLittleEndianShort(InputStream data) throws InvalidObjectDataException {
        try {
            byte dataBytes[] = new byte[2];
            int byteRead = data.read(dataBytes);
            if(byteRead != dataBytes.length)
                throw new InvalidObjectDataException("Cannot read short from stream");

            return (int)Util.readLittleEndianLong(dataBytes);
        } catch(IOException e) {
            throw new InvalidObjectDataException("An I/O error occurs while reading a short", e);
        }
    }

    /**
     * Reads an integer from a little-endian byte stream.
     *
     * @param data The little-endian {@link InputStream}.
     * @return The integer value contained in the stream.
     * @throws InvalidObjectDataException If the the stream doesn't contain at least 4 bytes or an error occurs.
     */
    public static int readLittleEndianInt(InputStream data) throws InvalidObjectDataException {
        try {
            byte dataBytes[] = new byte[4];
            int byteRead = data.read(dataBytes);
            if(byteRead != dataBytes.length)
                throw new InvalidObjectDataException("Cannot read int from stream");

            return (int)Util.readLittleEndianLong(dataBytes);
        } catch(IOException e) {
            throw new InvalidObjectDataException("An I/O error occurs while reading an int", e);
        }
    }

    /**
     * Reads a long from a little-endian byte stream.
     *
     * @param data The little-endian {@link InputStream}.
     * @return The long value contained in the stream.
     * @throws InvalidObjectDataException If the the stream doesn't contain at least 8 bytes or an error occurs.
     */
    public static long readLittleEndianLong(InputStream data) throws InvalidObjectDataException {
        try {
            byte dataBytes[] = new byte[8];
            int byteRead = data.read(dataBytes);
            if(byteRead != dataBytes.length)
                throw new InvalidObjectDataException("Cannot read long from stream");

            return Util.readLittleEndianLong(dataBytes);
        } catch(IOException e) {
            throw new InvalidObjectDataException("An I/O error occurs while reading a long", e);
        }
    }

    /**
     * Reads a long from a little-endian byte array.
     *
     * @param data The little-endian byte array.
     * @return The long value contained in the array.
     */
    public static long readLittleEndianLong(byte[] data) {
        long ret = 0;
        for(int i = 0, l = data.length; i < l; i++) {
            ret += ((long)((int)data[i] & 0xFF) & 0xFFFFFFFF) << (8 * i);
        }
        return ret;
    }

    /**
     * If the last two bytes of the array are zero this function
     * trims this delimiter from the array, otherwise the untouched
     * array is returned.
     *
     * @param string The original string bytes.
     * @return A byte array whose end delimiter has been trimmed.
     */
    public static byte[] trimStringBytes(byte string[]) {
        if(string[string.length - 1] != 0x00 || string[string.length - 2] != 0x00)
            return string;
        else {
            byte ret[] = new byte[string.length - 2];
            System.arraycopy(string, 0, ret, 0, ret.length);
            return ret;
        }
    }

    /**
     * This method transforms a byte buffer into a string using the default
     * UTF-16LE encoding used in ASF file format. This method trims the
     * buffer if it contains a null string terminator.
     *
     * @param buffer The byte array that will be converted into a string.
     * @return A string object.
     * @throws UnsupportedEncodingException If the JVM doesn't support the
     * UTF-16LE encoding.
     */
    public static String readStringFromBuffer(byte[] buffer) throws UnsupportedEncodingException {
        return new String(Util.trimStringBytes(buffer), "UTF-16LE");
    }

}
