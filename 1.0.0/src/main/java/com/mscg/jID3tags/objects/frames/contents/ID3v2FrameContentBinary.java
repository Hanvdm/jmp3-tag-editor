package com.mscg.jID3tags.objects.frames.contents;

import java.io.ByteArrayOutputStream;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2FrameContentBinary extends ByteArrayOutputStream implements ID3v2FrameContent {

    public ID3v2FrameContentBinary() {
        super();
    }

    public ID3v2FrameContentBinary(int bufferInitialSize) {
        super(bufferInitialSize);
    }

    public byte[] getBytes() {
        return toByteArray();
    }

    public int getLength() {
        return size();
    }

    @Override
    public String toString() {
        byte bytes[] = getBytes();
        StringBuffer ret = new StringBuffer(3 * bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(String.format("%02X ", bytes[i]));
        }
        return ret.toString();
    }

}
