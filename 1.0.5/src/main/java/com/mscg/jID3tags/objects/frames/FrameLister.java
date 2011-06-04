/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mscg.jID3tags.objects.frames;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Giuseppe Miscione
 */
public class FrameLister {

    /**
     * Lists all implemented frames classes, so that they can be loaded and
     * registered. Override this method to add more frames implementation.
     * 
     * @return A <code>{@link List}&lt;String&gt;</code> with all the
     *         implemented frames classes.
     */
    public List<String> listFrames() {
        String classes[] = {"com.mscg.jID3tags.objects.frames.ID3v2TPE1Frame", "com.mscg.jID3tags.objects.frames.ID3v2TPE2Frame",
                            "com.mscg.jID3tags.objects.frames.ID3v2TALBFrame", "com.mscg.jID3tags.objects.frames.ID3v2TIT2Frame",
                            "com.mscg.jID3tags.objects.frames.ID3v2TRCKFrame", "com.mscg.jID3tags.objects.frames.ID3v2TYERFrame",
                            "com.mscg.jID3tags.objects.frames.ID3v2TCONFrame", "com.mscg.jID3tags.objects.frames.ID3v2COMMFrame",
                            "com.mscg.jID3tags.objects.frames.ID3v2APICFrame"};
        return Arrays.asList(classes);
    }
}
