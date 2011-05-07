package com.mscg.jID3tags.objects.frames.contents;

import java.io.ByteArrayOutputStream;

import com.mscg.jID3tags.exception.ID3v2BadDataLengthException;
import com.mscg.jID3tags.util.Costants.StringEncodingType;
import com.mscg.jID3tags.util.Util;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v2FrameContentComment implements ID3v2FrameContent {

    protected StringEncodingType encoding;
    protected String language;
    protected String shortDescription;
    protected String comment;

    public ID3v2FrameContentComment() {

    }

    public ID3v2FrameContentComment(StringEncodingType encoding, String language, String shortDescription, String comment)
                                                                                                                          throws ID3v2BadDataLengthException {

        setEncoding(encoding);
        setLanguage(language);
        setShortDescription(shortDescription);
        setComment(comment);
    }

    public byte[] getBytes() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // Write the encoding byte
            bos.write(getEncoding().toByte());

            // Write the 3 bytes of language
            bos.write(Util.encodeString(getLanguage(), StringEncodingType.ISO_8859_1));

            // Write the short description
            bos.write(Util.encodeString(getShortDescription(), getEncoding()));

            // Write the zeroed separator byte
            bos.write(0);

            // Write the long description
            bos.write(Util.encodeString(getComment(), getEncoding()));

        } catch (Exception e) {
            // discard all data if an error occurs
            return new byte[0];
        }
        return bos.toByteArray();
    }

    public int getLength() {
        return getBytes().length;
    }

    public StringEncodingType getEncoding() {
        return encoding;
    }

    public void setEncoding(StringEncodingType encoding) {
        this.encoding = encoding;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) throws ID3v2BadDataLengthException {
        if (language.length() != 3) {
            throw new ID3v2BadDataLengthException("The comment language length must be 3");
        }
        this.language = language;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @Override
    public String toString() {
        return "Short description: " + getShortDescription() + ", " + "Comment: " + getComment() +
               " (language: " + getLanguage() + ", " + "encoding: " + getEncoding() + ")";
    }

}
