package com.mscg.jID3tags.id3v2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mscg.jID3tags.exception.ID3v2BadDataLengthException;
import com.mscg.jID3tags.exception.ID3v2Exception;
import com.mscg.jID3tags.exception.MP3TagIntegerException;
import com.mscg.jID3tags.exception.SynchsafeBadIntegerValueException;
import com.mscg.jID3tags.objects.ID3v2TagFlags;
import com.mscg.jID3tags.objects.SynchsafeInteger;
import com.mscg.jID3tags.objects.frames.ID3v2Frame;
import com.mscg.jID3tags.objects.frames.ID3v2PaddingFrame;
import com.mscg.jID3tags.util.SinkingOutputStream;
import com.mscg.jID3tags.util.Util;
import com.mscg.jID3tags.util.ValueRepeatingInputStream;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2Tag {

    public static class Factory {
        public static ID3v2Tag parse(byte bytes[]) throws ID3v2BadDataLengthException, IOException,
                                                          MP3TagIntegerException, ID3v2Exception {

            ByteArrayInputStream input = new ByteArrayInputStream(bytes);
            return parse(input);
        }

        public static ID3v2Tag parse(InputStream input) throws ID3v2BadDataLengthException, IOException,
                                                               MP3TagIntegerException, ID3v2Exception {

            ID3v2Tag ret = null;
            int bytesRead = 0;
            byte bytes[] = null;

            // read the tag id and check if it's "ID3"
            bytes = new byte[3];
            bytesRead = input.read(bytes);
            if (bytesRead != bytes.length) {
                throw new ID3v2BadDataLengthException("Cannot read tag id.");
            }
            for (int i = 0; i < bytes.length; i++) {
                if (ID3v2Tag.tagId[i] != bytes[i])
                    throw new ID3v2Exception("The tag id doesn't match with the standard ID3v2 one.");
            }

            // id ok, read version
            bytes = new byte[2];
            bytesRead = input.read(bytes);
            if (bytesRead != bytes.length) {
                throw new ID3v2BadDataLengthException("Cannot read tag version.");
            }

            // create tag object with version
            ret = new ID3v2Tag((int) bytes[0], (int) bytes[1]);

            // read flags
            bytes = new byte[1];
            bytesRead = input.read(bytes);
            if (bytesRead != bytes.length) {
                throw new ID3v2BadDataLengthException("Cannot read tag flags.");
            }
            ID3v2TagFlags flags = new ID3v2TagFlags(bytes);
            ret.setFlags(flags);

            // read the size
            bytes = new byte[4];
            bytesRead = input.read(bytes);
            if (bytesRead != bytes.length) {
                throw new ID3v2BadDataLengthException("Cannot read tag size.");
            }
            SynchsafeInteger size = new SynchsafeInteger(bytes);
            ret.setDeclaredSize(size.getIntValue());

            // now parse frames
            ID3v2Frame frame = null;
            int totalLength = 0;
            do {
                frame = ID3v2Frame.Factory.parse(input, ret.getMajorVersion(), ret.getMinorVersion());
                totalLength += frame.getLength();
                if (!(frame instanceof ID3v2PaddingFrame)) {
                    ret.addFrame(frame);
                    if (totalLength >= ret.getDeclaredSize())
                        break;
                } else {
                    // move the InputStream pointer at the end of the
                    // tag, after the padding.
                    Util.copyStream(input, new SinkingOutputStream(), 1024,
                                    ret.getDeclaredSize() - totalLength);
                }
            } while (!(frame instanceof ID3v2PaddingFrame));

            return ret;
        }

    }

    /**
     * The standard tag id. It is equivalent to the string "ID3".
     */
    public static final byte tagId[] = {0x49, 0x44, 0x33};

    static {
        if (!Util.areFramesLoaded()) {
            // register default frames
            Util.registerFrameTypes();
        }
    }

    protected int majorVersion;
    protected int minorVersion;
    protected ID3v2TagFlags flags;
    protected int declaredSize;
    protected Map<String, ID3v2Frame> frames;

    public ID3v2Tag() {
        this(3, 0);
    }

    public ID3v2Tag(int majorVersion, int minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        frames = new LinkedHashMap<String, ID3v2Frame>();
    }

    public ID3v2TagFlags getFlags() {
        return flags;
    }

    public void setFlags(ID3v2TagFlags flags) {
        this.flags = flags;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public int getDeclaredSize() {
        return declaredSize;
    }

    public void setDeclaredSize(int declaredSize) {
        this.declaredSize = declaredSize;
    }

    /**
     * Returns the size of the padded payload of tag, in bytes, so the size of
     * the entire tag (header + payload) is <code>getSize() + 10</code>. If the
     * declared size is big enough to contain the tag, then the size is equal to
     * the declared one, otherwise the size is calculated as the smallest
     * multiple of 1024 that is greater than the total size of frames, given by
     * {@link #getFramesSize()}, minus 10.
     *
     * @return The size of the padded paylod tag, in bytes.
     */
    public int getSize() {
        return getPaddedSize(getFramesSize());
    }

    /**
     * Returns the size of the complete tag (header + payload). It's equivalent
     * to <code>getSize() + 10</code>.
     *
     * @return The size of the complete tag.
     */
    public int getCompleteSize() {
        return getSize() + 10;
    }

    protected int getPaddedSize(int unpaddedSize) {
        if (getDeclaredSize() >= unpaddedSize) {
            return getDeclaredSize();
        } else {
            int ret = (int) Math.ceil(unpaddedSize / 1024.0) * 1024 - 10;
            return ret;
        }
    }

    public Map<String, ID3v2Frame> getFrames() {
        return frames;
    }

    public Collection<ID3v2Frame> getAllframes() {
        return frames.values();
    }

    public ID3v2Frame getFrame(String id) {
        return frames.get(id);
    }

    public void addFrame(ID3v2Frame frame) {
        frames.put(frame.getFrameId(), frame);
        frame.setEnclosingTag(this);
    }

    public void setFrames(Map<String, ID3v2Frame> frames) {
        this.frames = frames;
    }

    /**
     * Returns the sum of the lengths of all frames contained in this tag. This
     * value is the unpadded size of the tag, in bytes.
     *
     * @return The sum of the lengths of all frames contained in this tag.
     */
    public int getFramesSize() {
        int ret = 0;
        for (Map.Entry<String, ID3v2Frame> entry : frames.entrySet()) {
            ret += entry.getValue().getLength();
        }
        return ret;
    }

    public void writeToStream(OutputStream stream) throws IOException, SynchsafeBadIntegerValueException {

        writeToStream(stream, true);
    }

    public void writeToStream(OutputStream stream, boolean autoFlush) throws IOException, SynchsafeBadIntegerValueException {

        // write the tag id
        stream.write(tagId);

        // write major and minor version
        byte versionBytes[] = {(byte) majorVersion, (byte) minorVersion};
        stream.write(versionBytes);

        // write flags
        stream.write(getFlags().getBytes());

        // calculates sizes of the tag
        int unpaddedSize = getFramesSize();
        int paddedSize = getPaddedSize(unpaddedSize);

        // write the padded size of the tag
        SynchsafeInteger size = new SynchsafeInteger(paddedSize);
        stream.write(size.getBytes());

        // write all the frames
        for (Map.Entry<String, ID3v2Frame> entry : frames.entrySet()) {
            entry.getValue().writeToStream(stream, autoFlush);
        }

        // write the padding
        int bufferSize = Math.min(100, paddedSize - unpaddedSize);
        Util.copyStream(new ValueRepeatingInputStream(0, paddedSize - unpaddedSize), stream, bufferSize);

        if (autoFlush)
            stream.flush();
    }

    public byte[] getBytes() {
        ByteArrayOutputStream ret = new ByteArrayOutputStream();

        try {
            writeToStream(ret);
        } catch (Exception e) {
            ret = new ByteArrayOutputStream();
        }
        return ret.toByteArray();
    }
}
