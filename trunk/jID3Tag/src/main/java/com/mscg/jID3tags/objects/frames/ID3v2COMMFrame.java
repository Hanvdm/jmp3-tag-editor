package com.mscg.jID3tags.objects.frames;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mscg.jID3tags.exception.ID3v2BadDataLengthException;
import com.mscg.jID3tags.exception.ID3v2BadFrameIdLengthException;
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
    protected void parseBody(InputStream input, int majorVersion, int minorVersion) throws ID3v2BadDataLengthException {

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

        String shortDescription = null;
        try {
            ByteArrayOutputStream shortDescriptionBytes = new ByteArrayOutputStream();
            int shortDescriptionByte = 0;
            // read byte-by-byte until we found a zeroed one
            do {
                shortDescriptionByte = input.read();
                if (shortDescriptionByte < 0)
                    throw new IOException("End of input reached.");
                bytesRead++;
                if (shortDescriptionByte != 0)
                    shortDescriptionBytes.write(shortDescriptionByte);
            } while (shortDescriptionByte != 0);
            shortDescription = shortDescriptionBytes.toString(encoding.toString());
            comment.setShortDescription(shortDescription);
        } catch (IOException ex) {
            throw new ID3v2BadDataLengthException("Cannot read short description in frame data.", ex);
        }

        ByteArrayOutputStream commentData = null;
        try {
            // use a buffer with a maximum of 100 bytes
            int remainingBytes = getDeclaredSize() - bytesRead;
            int bufferSize = Math.min(100, remainingBytes);
            commentData = new ByteArrayOutputStream();
            int totalBytesRead = (int) Util.copyStream(input, commentData, bufferSize, remainingBytes);
            if (totalBytesRead != remainingBytes) {
                throw new ID3v2BadDataLengthException("Comment size doesn't match with size " +
                		                              "declared in the header.");
            }
            String commentStr = new String(commentData.toByteArray(), encoding.toString());
            comment.setComment(commentStr);
        } catch (IOException ex) {
            throw new ID3v2BadDataLengthException("Cannot read comment bytes in frame data.", ex);
        }

        setContent(comment);
    }

}
