package com.mscg.jID3tags.objects;

/**
 *
 * @author Giuseppe Miscione
 */
public interface I_Mp3TagInteger {

    /**
     * Returns the bytes that identifies this MP3 tag integer.
     *
     * @return the bytes that identifies this MP3 tag integer.
     */
    byte[] getBytes();

    /**
     * Returns the integer value encapsulated in this {@link I_Mp3TagInteger}.
     *
     * @return The integer value encapsulated in this {@link I_Mp3TagInteger}.
     */
    int getIntValue();

}
