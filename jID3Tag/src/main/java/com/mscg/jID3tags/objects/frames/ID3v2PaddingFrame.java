package com.mscg.jID3tags.objects.frames;

import java.io.InputStream;

import com.mscg.jID3tags.exception.ID3v2BadDataLengthException;
import com.mscg.jID3tags.exception.ID3v2Exception;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2PaddingFrame extends ID3v2Frame {

    public ID3v2PaddingFrame() {
        super();
    }

    @Override
    public int getLength() {
        return getDeclaredSize();
    }

    @Override
    protected void parseBody(InputStream input, int majorVersion, int minorVersion) throws ID3v2BadDataLengthException, ID3v2Exception {

    }
}
