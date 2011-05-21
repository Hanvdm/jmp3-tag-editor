package com.mscg.jID3tags.objects;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2FrameFlags {

    protected boolean tagAlterPreservation;
    protected boolean fileAlterPreservation;
    protected boolean readOnly;

    protected boolean groupingIdentity;
    protected boolean compression;
    protected boolean encryption;
    protected boolean unsynchronisation;
    protected boolean dataLengthIndicator;

    public ID3v2FrameFlags() {
        tagAlterPreservation = false;
        fileAlterPreservation = false;
        readOnly = false;

        groupingIdentity = false;
        compression = false;
        encryption = false;
        unsynchronisation = false;
        dataLengthIndicator = false;
    }

    public ID3v2FrameFlags(byte flagsBytes[]) {
        tagAlterPreservation = (flagsBytes[0] & 0x40) != 0;
        fileAlterPreservation = (flagsBytes[0] & 0x20) != 0;
        readOnly = (flagsBytes[0] & 0x10) != 0;

        groupingIdentity = (flagsBytes[1] & 0x40) != 0;
        compression = (flagsBytes[1] & 0x08) != 0;
        encryption = (flagsBytes[1] & 0x04) != 0;
        unsynchronisation = (flagsBytes[1] & 0x02) != 0;
        dataLengthIndicator = (flagsBytes[1] & 0x01) != 0;
    }

    public byte[] getBytes() {
        byte ret[] = {(byte) 0, (byte) 0};

        if (tagAlterPreservation)
            ret[0] = (byte) (ret[0] | 0x40);
        if (fileAlterPreservation)
            ret[0] = (byte) (ret[0] | 0x20);
        if (readOnly)
            ret[0] = (byte) (ret[0] | 0x10);

        if (groupingIdentity)
            ret[1] = (byte) (ret[1] | 0x40);
        if (compression)
            ret[1] = (byte) (ret[1] | 0x08);
        if (encryption)
            ret[1] = (byte) (ret[1] | 0x04);
        if (unsynchronisation)
            ret[1] = (byte) (ret[1] | 0x02);
        if (dataLengthIndicator)
            ret[1] = (byte) (ret[1] | 0x01);

        return ret;
    }

    public boolean isSetCompression() {
        return compression;
    }

    public boolean isSetDataLengthIndicator() {
        return dataLengthIndicator;
    }

    public boolean isSetEncryption() {
        return encryption;
    }

    public boolean isSetFileAlterPreservation() {
        return fileAlterPreservation;
    }

    public boolean isSetGroupingIdentity() {
        return groupingIdentity;
    }

    public boolean isSetReadOnly() {
        return readOnly;
    }

    public boolean isSetTagAlterPreservation() {
        return tagAlterPreservation;
    }

    public boolean isSetUnsynchronisation() {
        return unsynchronisation;
    }

    public void setCompression(boolean compression) {
        this.compression = compression;
    }

    public void setDataLengthIndicator(boolean dataLengthIndicator) {
        this.dataLengthIndicator = dataLengthIndicator;
    }

    public void setEncryption(boolean encryption) {
        this.encryption = encryption;
    }

    public void setFileAlterPreservation(boolean fileAlterPreservation) {
        this.fileAlterPreservation = fileAlterPreservation;
    }

    public void setGroupingIdentity(boolean groupingIdentity) {
        this.groupingIdentity = groupingIdentity;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void setTagAlterPreservation(boolean tagAlterPreservation) {
        this.tagAlterPreservation = tagAlterPreservation;
    }

    public void setUnsynchronisation(boolean unsynchronisation) {
        this.unsynchronisation = unsynchronisation;
    }

}
