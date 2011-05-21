package com.mscg.jID3tags.objects.frames;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mscg.jID3tags.exception.ID3v2BadDataLengthException;
import com.mscg.jID3tags.exception.ID3v2BadFrameIdLengthException;
import com.mscg.jID3tags.exception.ID3v2Exception;
import com.mscg.jID3tags.objects.frames.contents.ID3v2FrameContentComment;
import com.mscg.jID3tags.util.Costants.StringEncodingType;
import com.mscg.jID3tags.util.Util;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2COMMFrame extends ID3v2Frame {

    public static final String id = "COMM";

    static {
        ID3v2Frame.Factory.registerId(id, ID3v2COMMFrame.class);
    }

    public ID3v2COMMFrame() throws ID3v2BadFrameIdLengthException {
        super(id);
    }

    @Override
    protected void parseBody(InputStream input, int majorVersion, int minorVersion) throws ID3v2BadDataLengthException, ID3v2Exception {

        ID3v2FrameContentComment comment = new ID3v2FrameContentComment();

        int bytesRead = 0;

        StringEncodingType encoding = null;
        try {
            // read 1 byte to find string encoding
            byte encodingByte = (byte) input.read();
            if (encodingByte < 0)
                throw new IOException("End of input reached.");
            encoding = StringEncodingType.fromByte(encodingByte);
            comment.setEncoding(encoding);
        } catch (IOException ex) {
            throw new ID3v2BadDataLengthException("Cannot read frame encoding in frame data.", ex);
        }
        bytesRead++;

        try {
            byte bytes[] = new byte[3];
            int tmp = input.read(bytes);
            if (tmp < 3)
                throw new IOException("End of input reached.");
            comment.setLanguage(new String(bytes, StringEncodingType.ISO_8859_1.toString()));
        } catch (IOException ex) {
            throw new ID3v2BadDataLengthException("Cannot read frame language in frame data.", ex);
        }
        bytesRead += 3;

        // use a buffer with a maximum of 100 bytes
        int bufferSize = Math.min(100, getDeclaredSize() - bytesRead);
        ByteArrayOutputStream stringDataBuffer = new ByteArrayOutputStream(getDeclaredSize() - bytesRead);
        try {
            int totalBytes = (int) Util.copyStream(input, stringDataBuffer, bufferSize, getDeclaredSize() - bytesRead);
            if (totalBytes != getDeclaredSize() - bytesRead) {
                throw new ID3v2BadDataLengthException("Comment binary size doesn't match with size " +
                                                      "declared in the header.");
            }
        } catch (IOException ex) {
            throw new ID3v2BadDataLengthException("Cannot read comment content in frame data.", ex);
        }

        byte stringData[] = stringDataBuffer.toByteArray();
        int separatorStart = 0;
        int terminatorWidth = encoding.getStringTerminatorWidth();
        for(int last = stringData.length - terminatorWidth; separatorStart < last; separatorStart++) {
            byte tmp[] = new byte[terminatorWidth];
            System.arraycopy(stringData, separatorStart, tmp, 0, terminatorWidth);
            boolean allNull = true;
            for(int i = 0; i < terminatorWidth && allNull; i++) {
               allNull = (tmp[i] == 0);
            }
            if(allNull)
                break;
        }
        try {
            byte shortDescriptionBytes[] = new byte[separatorStart];
            System.arraycopy(stringData, 0, shortDescriptionBytes, 0, separatorStart);
            comment.setShortDescription(new String(shortDescriptionBytes, encoding.toString()));
        } catch (Exception e) {
            throw new ID3v2Exception("Cannot extract short description from tag content", e);
        }
        try {
            byte longDescriptionBytes[] = new byte[stringData.length - separatorStart - terminatorWidth];
            System.arraycopy(stringData, separatorStart + terminatorWidth, longDescriptionBytes,
                             0, longDescriptionBytes.length);
            comment.setComment(new String(longDescriptionBytes, encoding.toString()));
        } catch(Exception e) {
            throw new ID3v2Exception("Cannot extract long description from tag content", e);
        }

        setContent(comment);
    }

}
