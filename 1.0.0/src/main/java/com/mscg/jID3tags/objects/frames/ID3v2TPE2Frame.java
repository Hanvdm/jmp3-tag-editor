package com.mscg.jID3tags.objects.frames;

import com.mscg.jID3tags.exception.ID3v2BadFrameIdLengthException;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2TPE2Frame extends ID3v2GenericStringFrame {

    public static final String id = "TPE2";

    static {
        ID3v2Frame.Factory.registerId(id, ID3v2TPE2Frame.class);
    }

    public ID3v2TPE2Frame() throws ID3v2BadFrameIdLengthException {
        super(id);
    }

}
