package com.mscg.jID3tags.objects.frames.contents;

import java.io.UnsupportedEncodingException;

import com.mscg.jID3tags.util.Costants.StringEncodingType;
import com.mscg.jID3tags.util.Util;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2FrameContentString implements ID3v2FrameContent {

    private String content;
    private StringEncodingType encoding;

    public ID3v2FrameContentString() {

    }

    public ID3v2FrameContentString(String content, StringEncodingType encoding) {
        setEncoding(encoding);
        setContent(content);
    }

    public StringEncodingType getEncoding() {
        return encoding;
    }

    public void setEncoding(StringEncodingType encoding) {
        this.encoding = encoding;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Converts the content string into a byte array. If the content is using
     * the {@link StringEncodingType#UTF_16} encoding, ensure that the bytes are
     * in little endian order, with a preceding BOM.
     *
     * @return The content string converted into a byte array.
     * @throws UnsupportedEncodingException
     *             If the system doesn't support the selected encoding.
     */
    private byte[] contentBytes() throws UnsupportedEncodingException {
        return Util.encodeString(getContent(), getEncoding());
    }

    public byte[] getBytes() {
        try {
            byte stringBytes[] = contentBytes();
            byte totalBytes[] = new byte[1 + stringBytes.length];
            totalBytes[0] = getEncoding().toByte();
            System.arraycopy(stringBytes, 0, totalBytes, 1, stringBytes.length);
            return totalBytes;
        } catch (Exception ex) {
            return new byte[0];
        }
    }

    public int getLength() {
        try {
            return 1 + contentBytes().length;
        } catch (UnsupportedEncodingException ex) {
            return 0;
        }
    }

    @Override
    public String toString() {
        return getContent() + " (encoding: " + getEncoding().toString() + ")";
    }

}
