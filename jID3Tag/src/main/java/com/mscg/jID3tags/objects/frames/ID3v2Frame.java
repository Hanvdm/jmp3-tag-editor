package com.mscg.jID3tags.objects.frames;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mscg.jID3tags.exception.ID3v2BadDataLengthException;
import com.mscg.jID3tags.exception.ID3v2BadFrameIdLengthException;
import com.mscg.jID3tags.exception.MP3TagIntegerException;
import com.mscg.jID3tags.exception.SynchsafeBadIntegerValueException;
import com.mscg.jID3tags.id3v2.ID3v2Tag;
import com.mscg.jID3tags.objects.ID3v2FrameFlags;
import com.mscg.jID3tags.objects.I_Mp3TagInteger;
import com.mscg.jID3tags.objects.StandardInteger;
import com.mscg.jID3tags.objects.SynchsafeInteger;
import com.mscg.jID3tags.objects.frames.contents.ID3v2FrameContent;
import com.mscg.jID3tags.objects.frames.contents.ID3v2FrameContentBinary;
import com.mscg.jID3tags.util.Costants.StringEncodingType;
import com.mscg.jID3tags.util.Util;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2Frame {

    public static class Factory {
        private static Map<String, Class<? extends ID3v2Frame>> idToFrameClass;

        static {
            idToFrameClass = new LinkedHashMap<String, Class<? extends ID3v2Frame>>();
        }

        public static synchronized void registerId(String frameId, Class<? extends ID3v2Frame> frameClass) {
            idToFrameClass.put(frameId, frameClass);
        }

        public static synchronized ID3v2Frame getFrameForId(String id) throws ID3v2BadFrameIdLengthException {
            ID3v2Frame ret = null;
            if (idToFrameClass.containsKey(id)) {
                Class<? extends ID3v2Frame> clazz = idToFrameClass.get(id);
                Constructor<? extends ID3v2Frame> constr = null;

                // trying to call the constructor without parameters
                try {
                    constr = clazz.getConstructor();
                    ret = constr.newInstance();
                } catch (Exception ex) {
                }

                if (ret == null) {
                    // trying to call the constructor with a String parameter
                    try {
                        constr = clazz.getConstructor(String.class);
                        ret = constr.newInstance(id);
                    } catch (Exception ex) {
                    }
                }
            }

            if (ret == null) {
                // use a ID3v2Frame instance by default
                ret = new ID3v2Frame(id);
            }

            return ret;
        }

        public static ID3v2Frame parse(byte bytes[], int majorVersion, int minorVersion) throws ID3v2BadDataLengthException,
                                                                                        IOException, MP3TagIntegerException {
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);
            return parse(input, majorVersion, minorVersion);
        }

        public static ID3v2Frame parse(InputStream input, int majorVersion, int minorVersion) throws ID3v2BadDataLengthException,
                                                                                             IOException, MP3TagIntegerException {
            ID3v2Frame frame = parseHeader(input, majorVersion, minorVersion);
            frame.parseBody(input, majorVersion, minorVersion);
            return frame;
        }

        protected static ID3v2Frame parseHeader(InputStream input, int majorVersion, int minorVersion)
                                                                                                      throws ID3v2BadDataLengthException,
                                                                                                      IOException,
                                                                                                      MP3TagIntegerException {
            ID3v2Frame ret = null;
            int bytesRead = 0;

            // read 4 bytes to find frame ID
            byte idBytes[] = new byte[4];
            bytesRead = input.read(idBytes);
            boolean isPadding = true;
            for (int i = 0; i < bytesRead; i++) {
                if (idBytes[i] != 0) {
                    isPadding = false;
                    break;
                }
            }
            if (isPadding) {
                ID3v2PaddingFrame padding = new ID3v2PaddingFrame();
                padding.setDeclaredSize(bytesRead);
                return padding;
            }
            if (bytesRead != idBytes.length) {
                throw new ID3v2BadDataLengthException("Unable to read frame ID from header.");
            }
            String id = null;
            try {
                id = new String(idBytes, StringEncodingType.ISO_8859_1.toString());
            } catch (UnsupportedEncodingException ex) {
                id = new String(idBytes);
            }
            try {
                ret = Factory.getFrameForId(id);
            } catch (ID3v2BadFrameIdLengthException ex) {
                throw new ID3v2BadDataLengthException("Bad ID found for frame.", ex);
            }

            // read 4 bytes to find frame length
            byte sizeBytes[] = new byte[4];
            bytesRead = input.read(sizeBytes);
            if (bytesRead != sizeBytes.length) {
                throw new ID3v2BadDataLengthException("Unable to read frame length from header.");
            }
            I_Mp3TagInteger size = null;
            if (majorVersion >= 4) {
                // in ID3v2 tags with major version >= 4, frames have size
                // written in synchsafe integers format
                size = new SynchsafeInteger(sizeBytes);
            } else {
                size = new StandardInteger(sizeBytes);
            }
            ret.setDeclaredSize(size.getIntValue());

            // read 2 bytes to find frame flags
            byte flagsBytes[] = new byte[2];
            bytesRead = input.read(flagsBytes);
            if (bytesRead != flagsBytes.length) {
                throw new ID3v2BadDataLengthException("Unable to read frame flags from header.");
            }
            ret.setFlags(new ID3v2FrameFlags(flagsBytes));

            return ret;
        }
    }

    protected ID3v2Tag enclosingTag;
    protected String frameId;
    protected ID3v2FrameContent content;
    protected ID3v2FrameFlags flags;
    protected int declaredSize;

    protected ID3v2Frame() {

    }

    public ID3v2Frame(String frameId) throws ID3v2BadFrameIdLengthException {
        setFrameId(frameId);
    }

    public ID3v2FrameFlags getFlags() {
        return flags;
    }

    public void setFlags(ID3v2FrameFlags flags) {
        this.flags = flags;
    }

    public ID3v2Tag getEnclosingTag() {
        return enclosingTag;
    }

    public void setEnclosingTag(ID3v2Tag enclosingTag) {
        this.enclosingTag = enclosingTag;
    }

    public ID3v2FrameContent getContent() {
        return content;
    }

    public void setContent(ID3v2FrameContent content) {
        this.content = content;
    }

    public int getDeclaredSize() {
        return declaredSize;
    }

    protected void setDeclaredSize(int declaredSize) {
        this.declaredSize = declaredSize;
    }

    public String getFrameId() {
        return frameId;
    }

    protected void setFrameId(String frameId) throws ID3v2BadFrameIdLengthException {
        if (frameId.length() == 4)
            this.frameId = frameId;
        else
            throw new ID3v2BadFrameIdLengthException();
    }

    public int getLength() {
        return 10 + getContent().getLength();
    }

    public void writeToStream(OutputStream stream) throws IOException, SynchsafeBadIntegerValueException {
        writeToStream(stream, true);
    }

    public void writeToStream(OutputStream stream, boolean autoFlush) throws IOException, SynchsafeBadIntegerValueException {
        // write the frame id
        stream.write(getFrameId().getBytes());

        // write the frame length
        I_Mp3TagInteger size = null;
        if (getEnclosingTag().getMajorVersion() >= 4) {
            // in ID3v2 tags with major version >= 4, frames have size
            // written in synchsafe integers format
            size = new SynchsafeInteger(getContent().getLength());
        } else {
            size = new StandardInteger(getContent().getLength());
        }
        stream.write(size.getBytes());

        // write the flags
        stream.write(getFlags().getBytes());

        // write the content of the frame
        stream.write(getContent().getBytes());

        if (autoFlush)
            stream.flush();
    }

    protected ByteArrayOutputStream getStreamInt() {
        ByteArrayOutputStream ret = new ByteArrayOutputStream();

        try {
            writeToStream(ret);
        } catch (Exception e) {
            ret = new ByteArrayOutputStream();
        }

        return ret;
    }

    public InputStream getStream() {
        return new ByteArrayInputStream(getStreamInt().toByteArray());
    }

    public byte[] getBytes() {
        return getStreamInt().toByteArray();
    }

    protected void parseBody(InputStream input, int majorVersion, int minorVersion) throws ID3v2BadDataLengthException {
        // use a buffer with a maximum of 100 bytes
        int bufferSize = Math.min(100, getDeclaredSize());
        ID3v2FrameContentBinary dataContent = new ID3v2FrameContentBinary(getDeclaredSize());
        try {
            int totalBytesRead = (int) Util.copyStream(input, dataContent, bufferSize, getDeclaredSize());
            if (totalBytesRead != getDeclaredSize())
                throw new ID3v2BadDataLengthException("Data size doesn't match with size " +
                                                      "declared in the header.");

        } catch (IOException ex) {
            throw new ID3v2BadDataLengthException("Cannot read data for the frame.", ex);
        }

        setContent(dataContent);
    }

    @Override
    public String toString() {
        return getFrameId() + ": " + getContent() + " | " + "content size: " + getContent().getLength() + ", total size: "
               + getLength();
    }

}
