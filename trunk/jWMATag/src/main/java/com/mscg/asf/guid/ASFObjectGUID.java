package com.mscg.asf.guid;

import java.io.IOException;
import java.io.InputStream;
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

    private static final Pattern guidPattern = Pattern.compile("([a-fA-F0-9]{8})-([a-fA-F0-9]{4})-([a-fA-F0-9]{4})-([a-fA-F0-9]{4})-([a-fA-F0-9]{12})");

    private static final GUIDOffset partsOffsets[] = new GUIDOffset[] {
       new GUIDOffset(0, 3, true),
       new GUIDOffset(4, 5, true),
       new GUIDOffset(6, 7, true),
       new GUIDOffset(8, 9, false),
       new GUIDOffset(10, 15, false),
    };

    public static class ReservedGUID {

        public static final ASFObjectGUID ASF_Reserved_1 = new ASFObjectGUID("ABD3D211-A9BA-11CF-8EE6-00C00C205365", true);
        public static final ASFObjectGUID ASF_Reserved_2 = new ASFObjectGUID("86D15241-311D-11D0-A3A4-00A0C90348F6", true);
        public static final ASFObjectGUID ASF_Reserved_3 = new ASFObjectGUID("4B1ACBE3-100B-11D0-A39B-00A0C90348F6", true);
        public static final ASFObjectGUID ASF_Reserved_4 = new ASFObjectGUID("4CFEDB20-75F6-11CF-9C0F-00A0C90349CB", true);

    }

    public static class StreamTypeGUID {

        public static final ASFObjectGUID ASF_Audio_Media           = new ASFObjectGUID("F8699E40-5B4D-11CF-A8FD-00805F5C442B", true);
        public static final ASFObjectGUID ASF_Video_Media           = new ASFObjectGUID("BC19EFC0-5B4D-11CF-A8FD-00805F5C442B", true);
        public static final ASFObjectGUID ASF_Command_Media         = new ASFObjectGUID("59DACFC0-59E6-11D0-A3AC-00A0C90348F6", true);
        public static final ASFObjectGUID ASF_JFIF_Media            = new ASFObjectGUID("B61BE100-5B4E-11CF-A8FD-00805F5C442B", true);
        public static final ASFObjectGUID ASF_Degradable_JPEG_Media = new ASFObjectGUID("35907DE0-E415-11CF-A917-00805F5C442B", true);
        public static final ASFObjectGUID ASF_File_Transfer_Media   = new ASFObjectGUID("91BD222C-F21C-497A-8B6D-5AA86BFC0185", true);
        public static final ASFObjectGUID ASF_Binary_Media          = new ASFObjectGUID("3AFB65E2-47EF-40F2-AC2C-70A90D71D343", true);

    }

    public static class ErrorCorrectionTypeGUID {

        public static final ASFObjectGUID ASF_No_Error_Correction = new ASFObjectGUID("20FB5700-5B55-11CF-A8FD-00805F5C442B", true);
        public static final ASFObjectGUID ASF_Audio_Spread        = new ASFObjectGUID("BFC3CD50-618F-11CF-8BB2-00AA00B4E220", true);

    }

    private byte guid[];

    /**
     * Builds a GUID wrapping the passed byte array.
     *
     * @param guid The byte array that will be wrapped by the GUID
     * @throws NullPointerException If <code>guid</code> is null.
     * @throws GUIDSizeException If <code>guid</code> isn't 16 byte long.
     */
    public ASFObjectGUID(byte guid[]) throws NullPointerException, GUIDSizeException {
        initFromByteArray(guid);
    }


    /**
     * Builds a GUID reading 16 bytes from the provided {@link InputStream}.
     *
     * @param guidStream The {@link InputStream} from which the GUID bytes are read.
     * @throws NullPointerException If <code>guidStream</code> is null.
     * @throws GUIDSizeException If <code>guidStream</code> contains less than 16 byte.
     * @throws IOException If an I/O error occurs.
     */
    public ASFObjectGUID(InputStream guidStream) throws NullPointerException, GUIDSizeException, IOException {
        if(guidStream == null)
            throw new NullPointerException("GUID is null");
        byte guid[] = new byte[16];
        int bytesRead = guidStream.read(guid);
        if(bytesRead < 16)
            throw new GUIDSizeException("GUID must be 16 bytes long");
        initFromByteArray(guid);
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
        initFromString(guidString);
    }

    /**
     * Builds a GUID from a string in the format:
     * <p>
     * <code> AAAAAAAA-BBBB-CCCC-DDDD-EEEEEEEE </code>
     * <p>
     * where the string parts are hexadecimal values.
     * This constructor doesn't throw any exception and should
     * be used only when the string is surely in correct format.
     *
     * @param guidString The string representing the GUID.
     * @param noThrow A dummy parameter to differentiate constructors.
     * @throws NullPointerException If <code>guidString</code> is null.
     * @throws GUIDFormatException If <code>guidString</code> is in a non-valid format.
     */
    public ASFObjectGUID(String guidString, boolean noThrow) {
        try {
            initFromString(guidString);
        } catch(Exception e){}
    }

    /**
     * Inits the GUID from a string in the format:
     * <p>
     * <code> AAAAAAAA-BBBB-CCCC-DDDD-EEEEEEEE </code>
     * <p>
     * where the string parts are hexadecimal values.
     *
     * @param guidString The string representing the GUID.
     * @throws NullPointerException If <code>guidString</code> is null.
     * @throws GUIDFormatException If <code>guidString</code> is in a non-valid format.
     */
    private void initFromString(String guidString) throws NullPointerException, GUIDFormatException, NumberFormatException {
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
                int startIndex = 2 * (partsOffsets[i].reverse ? (partsOffsets[i].end - j) : (j - partsOffsets[i].start));
                String byteStr = partString.substring(startIndex, startIndex + 2);
                guid[j] = (byte)Integer.parseInt(byteStr, 16);
            }
        }
    }

    /**
     * Inits the object using the passed byte array.
     *
     * @param guid The byte array that will be wrapped by the GUID
     * @throws NullPointerException If <code>guid</code> is null.
     * @throws GUIDSizeException If <code>guid</code> isn't 16 byte long.
     */
    protected void initFromByteArray(byte[] guid) throws NullPointerException, GUIDSizeException {
        if(guid == null)
            throw new NullPointerException("GUID is null");
        if(guid.length != 16)
            throw new GUIDSizeException("GUID must be 16 bytes long");
        this.guid = guid;
    }

    /**
     * Returns the internal GUID bytes.
     *
     * @return The internal GUID bytes.
     */
    public byte[] getGuid() {
        return guid;
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
            if(partOffset.reverse) {
                for(int i = partOffset.end; i >= partOffset.start; i--) {
                    sb.append(String.format("%02X", ((int)guid[i]) & 0xFF));
                }
            }
            else {
                for(int i = partOffset.start; i <= partOffset.end; i++) {
                    sb.append(String.format("%02X", ((int)guid[i]) & 0xFF));
                }
            }
        }
        return sb.toString();
    }

    private static class GUIDOffset {
        int start;
        int end;
        boolean reverse;

        public GUIDOffset(int start, int end, boolean reverse) {
            this.start = start;
            this.end = end;
            this.reverse = reverse;
        }

    }
}
