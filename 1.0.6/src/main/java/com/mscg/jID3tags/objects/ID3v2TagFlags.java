package com.mscg.jID3tags.objects;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2TagFlags {

    protected boolean unsynchronisation;
    protected boolean extendedHeader;
    protected boolean experimentalIndicator;
    protected boolean footerPresent;

    public ID3v2TagFlags() {
        unsynchronisation = false;
        extendedHeader = false;
        experimentalIndicator = false;
        footerPresent = false;
    }

    public ID3v2TagFlags(byte flagsBytes[]) {
        unsynchronisation = (flagsBytes[0] & 0x80) != 0;
        extendedHeader = (flagsBytes[0] & 0x40) != 0;
        experimentalIndicator = (flagsBytes[0] & 0x20) != 0;
        footerPresent = (flagsBytes[0] & 0x10) != 0;
    }

    public byte[] getBytes() {
        byte ret[] = {(byte) 0};

        if (unsynchronisation)
            ret[0] = (byte) (ret[0] | 0x80);
        if (extendedHeader)
            ret[0] = (byte) (ret[0] | 0x40);
        if (experimentalIndicator)
            ret[0] = (byte) (ret[0] | 0x20);
        if (footerPresent)
            ret[0] = (byte) (ret[0] | 0x10);

        return ret;
    }

    public boolean isSetUnsynchronisation() {
        return unsynchronisation;
    }

    public boolean isSetExtendedHeader() {
        return extendedHeader;
    }

    public boolean isSetExperimentalIndicator() {
        return experimentalIndicator;
    }

    public void setExperimentalIndicator(boolean experimentalIndicator) {
        this.experimentalIndicator = experimentalIndicator;
    }

    public boolean isSetFooterPresent() {
        return footerPresent;
    }

    public void setExtendedHeader(boolean extendedHeader) {
        this.extendedHeader = extendedHeader;
    }

    public void setFooterPresent(boolean footerPresent) {
        this.footerPresent = footerPresent;
    }

    public void setUnsynchronisation(boolean unsynchronisation) {
        this.unsynchronisation = unsynchronisation;
    }

}
