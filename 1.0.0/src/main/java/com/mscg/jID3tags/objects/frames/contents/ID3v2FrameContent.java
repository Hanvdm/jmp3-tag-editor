package com.mscg.jID3tags.objects.frames.contents;

/**
 *
 * @author Giuseppe Miscione
 */
public interface ID3v2FrameContent {

    /**
     * Returns a byte array representation of the content of the frame.
     *
     * @return The byte array with the data of the frame content.
     */
    public byte[] getBytes();

    /**
     * Returns the length of this frame content.
     *
     * @return The length of this frame content.
     */
    public int getLength();
}
