package com.mscg.jID3tags.objects.frames;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.mscg.jID3tags.exception.ID3v2BadDataLengthException;
import com.mscg.jID3tags.exception.ID3v2BadFrameIdLengthException;
import com.mscg.jID3tags.exception.ID3v2Exception;
import com.mscg.jID3tags.objects.frames.contents.ID3v2FrameContentString;
import com.mscg.jID3tags.util.Costants.StringEncodingType;
import com.mscg.jID3tags.util.Util;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2GenericStringFrame extends ID3v2Frame {

    protected ID3v2GenericStringFrame(String frameId) throws ID3v2BadFrameIdLengthException {
        super(frameId);
    }

    public ID3v2FrameContentString getFrameStringContent() {
        return (ID3v2FrameContentString) getContent();
    }

    @Override
    protected void parseBody(InputStream input, int majorVersion, int minorVersion) throws ID3v2BadDataLengthException, ID3v2Exception {
        StringEncodingType encoding = null;
        try {
            // read 1 byte to find string encoding
            byte encodingByte = (byte) input.read();
            if (encodingByte < 0)
                throw new IOException("End of input reached.");
            encoding = StringEncodingType.fromByte(encodingByte);
        } catch (IOException ex) {
            throw new ID3v2BadDataLengthException("Cannot read string encoding in frame data.", ex);
        }

        // use a buffer with a maximum of 100 bytes
        int bufferSize = Math.min(100, getDeclaredSize() - 1);
        ByteArrayOutputStream stringData = new ByteArrayOutputStream(getDeclaredSize() - 1);
        try {
            int totalBytes = (int) Util.copyStream(input, stringData, bufferSize, getDeclaredSize() - 1);
            if (totalBytes != getDeclaredSize() - 1) {
                throw new ID3v2BadDataLengthException("String binary size doesn't match with size " +
                                                      "declared in the header.");
            }
        } catch (IOException ex) {
            throw new ID3v2BadDataLengthException("Cannot read string content in frame data.", ex);
        }

        String contentStr = null;
        try {
            contentStr = new String(stringData.toByteArray(), encoding.toString());
        } catch (UnsupportedEncodingException ex) {
            contentStr = new String(stringData.toByteArray());
        }
        ID3v2FrameContentString stringContent = new ID3v2FrameContentString(contentStr, encoding);
        setContent(stringContent);
    }
}
