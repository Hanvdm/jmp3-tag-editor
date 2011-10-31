package com.mscg.jID3tags.objects.frames;

import com.mscg.jID3tags.exception.ID3v2BadFrameIdLengthException;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2TALBFrame extends ID3v2GenericStringFrame {

    public static final String id = "TALB";

    static {
        ID3v2Frame.Factory.registerId(id, ID3v2TALBFrame.class);
    }

    public ID3v2TALBFrame() throws ID3v2BadFrameIdLengthException {
        super(id);
    }

}
