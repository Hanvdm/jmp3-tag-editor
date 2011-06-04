package com.mscg.jID3tags.objects.frames;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mscg.jID3tags.exception.ID3v2BadDataLengthException;
import com.mscg.jID3tags.exception.ID3v2BadFrameIdLengthException;
import com.mscg.jID3tags.exception.ID3v2Exception;
import com.mscg.jID3tags.objects.frames.contents.ID3v2FrameContentImage;
import com.mscg.jID3tags.util.Costants.PictureType;
import com.mscg.jID3tags.util.Costants.StringEncodingType;
import com.mscg.jID3tags.util.Util;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2APICFrame extends ID3v2Frame {

    public static final String id = "APIC";

    static {
        ID3v2Frame.Factory.registerId(id, ID3v2APICFrame.class);
    }

    public ID3v2APICFrame() throws ID3v2BadFrameIdLengthException {
        super(id);
        setContent(new ID3v2FrameContentImage());
    }

    @Override
    protected void parseBody(InputStream input, int majorVersion, int minorVersion) throws ID3v2BadDataLengthException, ID3v2Exception {
        ID3v2FrameContentImage imageContent = (ID3v2FrameContentImage)getContent();

        int bytesRead = 0;

        StringEncodingType encoding = null;
        try {
            // read 1 byte to find string encoding
            byte encodingByte = (byte) input.read();
            if (encodingByte < 0)
                throw new IOException("End of input reached.");
            encoding = StringEncodingType.fromByte(encodingByte);
            imageContent.setEncoding(encoding);
        } catch (IOException ex) {
            throw new ID3v2BadDataLengthException("Cannot read frame encoding in frame data.", ex);
        }
        bytesRead++;

        String mimeType = null;
        try {
            ByteArrayOutputStream mimeTypeBytes = new ByteArrayOutputStream();
            int mimeTypeByte = 0;
            // read byte-by-byte until we found a zeroed one
            do {
                mimeTypeByte = input.read();
                if (mimeTypeByte < 0)
                    throw new IOException("End of input reached.");
                bytesRead++;
                if (mimeTypeByte != 0)
                    mimeTypeBytes.write(mimeTypeByte);
            } while (mimeTypeByte != 0);
            mimeType = mimeTypeBytes.toString(encoding.toString());
            imageContent.setMimeType(mimeType);
        } catch (IOException ex) {
            throw new ID3v2BadDataLengthException("Cannot read mime type in frame data.", ex);
        }

        PictureType pictureType = null;
        try {
            // read 1 byte to find picture type
            byte pictureTypeByte = (byte) input.read();
            if (pictureTypeByte < 0)
                throw new IOException("End of input reached.");
            pictureType = PictureType.fromByte(pictureTypeByte);
            imageContent.setPictureType(pictureType);
        } catch (IOException ex) {
            throw new ID3v2BadDataLengthException("Cannot read picture type in frame data.", ex);
        }
        bytesRead++;

        String description = null;
        try {
            ByteArrayOutputStream descriptionBytes = new ByteArrayOutputStream();
            int descriptionByte = 0;
            // read byte-by-byte until we found a zeroed one
            do {
                descriptionByte = input.read();
                if (descriptionByte < 0)
                    throw new IOException("End of input reached.");
                bytesRead++;
                if (descriptionByte != 0)
                    descriptionBytes.write(descriptionByte);
            } while (descriptionByte != 0);
            description = descriptionBytes.toString(encoding.toString());
            imageContent.setDescription(description);
        } catch (IOException ex) {
            throw new ID3v2BadDataLengthException("Cannot read image description in frame data.", ex);
        }

        ByteArrayOutputStream imageData = null;
        try {
            // use a buffer with a maximum of 100 bytes
            int remainingBytes = getDeclaredSize() - bytesRead;
            int bufferSize = Math.min(100, remainingBytes);
            imageData = new ByteArrayOutputStream();
            int totalBytesRead = (int) Util.copyStream(input, imageData, bufferSize, remainingBytes);
            if (totalBytesRead != remainingBytes) {
                throw new ID3v2BadDataLengthException("Image data size doesn't match with size " +
                		                              "declared in the header.");
            }
            imageContent.setPictureData(imageData.toByteArray());
        } catch (IOException ex) {
            throw new ID3v2BadDataLengthException("Cannot read image bytes in frame data.", ex);
        }

        setContent(imageContent);
    }
}
