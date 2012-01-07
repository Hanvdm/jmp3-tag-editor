package com.mscg.asf.guid;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mscg.asf.exception.GUIDFormatException;
import com.mscg.asf.exception.GUIDSizeException;

/**
 * This object represents an ASF object GUID,
 * that is a 128-bit (16-byte) array.
 *
 * @author Giuseppe Miscione
 *
 */
public class ASFObjectGUID {

    private static final GUIDOffset partsOffsets[] = new GUIDOffset[] {
        new GUIDOffset(0, 3),
        new GUIDOffset(4, 5),
        new GUIDOffset(6, 7),
        new GUIDOffset(8, 9),
        new GUIDOffset(10, 15),
    };

    private static Pattern guidPattern = Pattern.compile("([a-eA-F0-9]{8})-([a-eA-F0-9]{4})-([a-eA-F0-9]{4})-([a-eA-F0-9]{4})-([a-eA-F0-9]{12})");

    private byte guid[];

    /**
     * Builds a GUID wrapping the passed byte array.
     *
     * @param guid The byte array that will be wrapped by the GUID
     * @throws NullPointerException If <code>guid</code> is null.
     * @throws GUIDSizeException If <code>guid</code> isn't 16 byte long.
     */
    public ASFObjectGUID(byte guid[]) throws NullPointerException, GUIDSizeException {
        if(guid == null)
            throw new NullPointerException("GUID is null");
        if(guid.length != 16)
            throw new GUIDSizeException("GUID must be 16 bytes long");
        this.guid = guid;
    }

    /**
     * Builds a GUID from a string in the format:
     * <p>
     * <code> AAAAAAAA-BBBB-CCCC-DDDD-EEEEEEEE </code>
     * <p>
     * where the string parts are hexadecimal values.
     *
     * @param guidString The string representing the GUID.
     * @throws NullPointerException If <code>guidString</code> is null.
     * @throws GUIDFormatException If <code>guidString</code> is in a non-valid format.
     */
    public ASFObjectGUID(String guidString) throws NullPointerException, GUIDFormatException {
        if(guidString == null)
            throw new NullPointerException("GUID is null");
        Matcher matcher = guidPattern.matcher(guidString);
        if(!matcher.find())
            throw new GUIDFormatException("Invalid GUID format for string \"" + guidString + "\"");

        guid = new byte[16];
        // the GUID parts are written in little-endian byte
        // order, so we have to reverse the order of teh hex bytes in the string.
        for(int i = 0, l = partsOffsets.length; i < l; i++) {
            String partString = matcher.group(i + 1);
            for(int j = partsOffsets[i].end; j >= partsOffsets[i].start; j--) {
                int startIndex = (partsOffsets[i].end - j) * 2;
                String byteStr = partString.substring(startIndex, startIndex + 2);
                guid[j] = (byte)Integer.parseInt(byteStr, 16);
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(guid);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        ASFObjectGUID other = (ASFObjectGUID) obj;
        if(!Arrays.equals(guid, other.guid))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // the GUID parts are written in little-endian byte
        // order, so we have to read the bytes in reverse order.
        for(GUIDOffset partOffset : partsOffsets) {
            if(sb.length() != 0)
                sb.append("-");
            for(int i = partOffset.end; i >= partOffset.start; i--) {
                sb.append(String.format("%02X", ((int)guid[i]) & 0xFF));
            }
        }
        return sb.toString();
    }

    private static class GUIDOffset {
        int start;
        int end;

        public GUIDOffset(int start, int end) {
            this.start = start;
            this.end = end;
        }

    }
}
