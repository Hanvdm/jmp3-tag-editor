package com.mscg.jID3tags.id3v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;

import com.mscg.jID3tags.exception.ID3v1Exception;
import com.mscg.jID3tags.util.Costants.ID3v1Genre;
import com.mscg.jID3tags.util.Costants.StringEncodingType;
import com.mscg.jID3tags.util.Util;
import com.mscg.jID3tags.util.ValueRepeatingInputStream;

/**
 *
 * @author Giuseppe Miscione
 */
public class ID3v11Tag {

    public static final int tagSize = 128;

    public static class Factory {

        public static ID3v11Tag parse(byte bytes[]) throws ID3v1Exception {
            if (bytes.length != 128) {
                throw new ID3v1Exception("The length of a ID3v1 tag must be 128 bytes.");
            }

            if (!ID3v11Tag.hasValidTag(bytes))
                throw new ID3v1Exception("Cannot find a valid tag");

            ByteArrayInputStream input = new ByteArrayInputStream(bytes);
            ID3v11Tag ret = new ID3v11Tag();

            StringBuffer value = null;
            byte readBytes[] = null;

            try {
                // read and discard the first bytes containing the tag id
                readBytes = new byte[ID3v11Tag.tagId.length];
                input.read(readBytes);

                // read the title
                readBytes = new byte[30];
                input.read(readBytes);
                value = new StringBuffer();
                for (int i = 0; i < readBytes.length && readBytes[i] != 0; i++) {
                    byte tmp[] = {readBytes[i]};
                    value.append(new String(tmp, StringEncodingType.ISO_8859_1.toString()));
                }
                ret.setTitle(value.toString());

                // read the artist
                readBytes = new byte[30];
                input.read(readBytes);
                value = new StringBuffer();
                for (int i = 0; i < readBytes.length && readBytes[i] != 0; i++) {
                    byte tmp[] = {readBytes[i]};
                    value.append(new String(tmp, StringEncodingType.ISO_8859_1.toString()));
                }
                ret.setArtist(value.toString());

                // read the album
                readBytes = new byte[30];
                input.read(readBytes);
                value = new StringBuffer();
                for (int i = 0; i < readBytes.length && readBytes[i] != 0; i++) {
                    byte tmp[] = {readBytes[i]};
                    value.append(new String(tmp, StringEncodingType.ISO_8859_1.toString()));
                }
                ret.setAlbum(value.toString());

                // read the year
                readBytes = new byte[4];
                input.read(readBytes);
                value = new StringBuffer();
                for (int i = 0; i < readBytes.length && readBytes[i] != 0; i++) {
                    byte tmp[] = {readBytes[i]};
                    value.append(new String(tmp, StringEncodingType.ISO_8859_1.toString()));
                }
                ret.setYear(value.toString());

                // read the comment
                readBytes = new byte[28];
                input.read(readBytes);
                value = new StringBuffer();
                for (int i = 0; i < readBytes.length && readBytes[i] != 0; i++) {
                    byte tmp[] = {readBytes[i]};
                    value.append(new String(tmp, StringEncodingType.ISO_8859_1.toString()));
                }
                ret.setComment(value.toString());

                // read separator, track and genre
                readBytes = new byte[3];
                input.read(readBytes);
                ret.setTrack(readBytes[1]);
                ret.setGenre(ID3v1Genre.fromByte(readBytes[2]));

            } catch (IOException e) {
                throw new ID3v1Exception("Cannot read tag bytes from buffer.");
            }

            return ret;
        }

        public static ID3v11Tag parse(InputStream input) throws ID3v1Exception {
            byte bytes[] = new byte[128];
            int bytesRead = 0;
            try {
                bytesRead = input.read(bytes);
                if (bytesRead != 128) {
                    throw new ID3v1Exception("The length of a ID3v1 tag must " + "be 128 bytes.");
                }
            } catch (IOException ex) {
                throw new ID3v1Exception("Cannot read tag bytes from stream.", ex);
            }
            return parse(bytes);
        }

        public static ID3v11Tag parse(RandomAccessFile input) throws ID3v1Exception {
            byte bytes[] = new byte[128];
            int bytesRead = 0;
            ID3v11Tag ret = null;
            try {
                long position = input.getFilePointer();
                input.seek(input.length() - bytes.length);
                bytesRead = input.read(bytes);
                if (bytesRead != 128) {
                    throw new ID3v1Exception("The length of a ID3v1 tag must be 128 bytes.");
                }
                try {
                    ret = parse(bytes);
                } finally {
                    input.seek(position);
                }
            } catch (IOException ex) {
                throw new ID3v1Exception("Cannot read tag bytes from file.", ex);
            }
            return ret;
        }
    }

    /**
     * The standard tag id. It is equivalent to the string "TAG".
     */
    public static final byte tagId[] = {0x54, 0x41, 0x47};

    protected String title;
    protected String artist;
    protected String album;
    protected String comment;
    protected String year;
    protected byte track;
    protected ID3v1Genre genre;

    public ID3v11Tag() {
    }

    public ID3v11Tag(String title, String artist, String album, String comment, String year, byte track,
                     ID3v1Genre genre)
                                                                                                                          throws ID3v1Exception {

        this();
        setTitle(title);
        setArtist(artist);
        setAlbum(album);
        setComment(comment);
        setYear(year);
        setTrack(track);
        setGenre(genre);
    }

    public boolean isValid() {
        return getTitle() != null && getTitle().length() <= 30 && getArtist() != null && getArtist().length() <= 30
               && getAlbum() != null && getAlbum().length() <= 30 && getComment() != null && getComment().length() <= 28
               && getYear() != null && (getYear().length() == 0 || getYear().length() == 4);
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) throws ID3v1Exception {
        // if (album.length() > 30) {
        // throw new ID3v1Exception("Maximum album length is 30");
        // }
        this.album = Util.trimToSize(album, 30);
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) throws ID3v1Exception {
        // if (artist.length() > 30) {
        // throw new ID3v1Exception("Maximum artist length is 30");
        // }
        this.artist = Util.trimToSize(artist, 30);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) throws ID3v1Exception {
        // if (comment.length() > 28) {
        // throw new ID3v1Exception("Maximum artist length is 28");
        // }
        this.comment = Util.trimToSize(comment, 28);
    }

    public ID3v1Genre getGenre() {
        return genre;
    }

    public void setGenre(ID3v1Genre genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws ID3v1Exception {
        // if (title.length() > 30) {
        // throw new ID3v1Exception("Maximum artist length is 30");
        // }
        this.title = Util.trimToSize(title, 30);
    }

    public byte getTrack() {
        return track;
    }

    public void setTrack(byte track) {
        this.track = track;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) throws ID3v1Exception {
        // if (!(year.length() == 0 || year.length() == 4)) {
        // throw new ID3v1Exception("Year length must be 0 or 4");
        // }
        this.year = Util.trimToSize(year, 4);
        while (this.year.length() < 4) {
            this.year = "0" + this.year;
        }
    }

    public void writeToStream(OutputStream stream) throws IOException {

        writeToStream(stream, true);
    }

    public void writeToStream(OutputStream stream, boolean autoFlush) throws IOException {

        if (!isValid()) {
            throw new IOException("The tag is invalid and cannot be written.");
        }

        byte bytes[] = null;

        // write the id
        stream.write(tagId);

        // write title, with optional padding
        bytes = getTitle().getBytes(StringEncodingType.ISO_8859_1.toString());
        stream.write(bytes);
        if (bytes.length < 30) {
            Util.copyStream(new ValueRepeatingInputStream(0, 30 - bytes.length), stream, 30);
        }

        // write artist, with optional padding
        bytes = getArtist().getBytes(StringEncodingType.ISO_8859_1.toString());
        stream.write(bytes);
        if (bytes.length < 30) {
            Util.copyStream(new ValueRepeatingInputStream(0, 30 - bytes.length), stream, 30);
        }

        // write album, with optional padding
        bytes = getAlbum().getBytes(StringEncodingType.ISO_8859_1.toString());
        stream.write(bytes);
        if (bytes.length < 30) {
            Util.copyStream(new ValueRepeatingInputStream(0, 30 - bytes.length), stream, 30);
        }

        // write year
        bytes = getYear().getBytes(StringEncodingType.ISO_8859_1.toString());
        stream.write(bytes);

        // write comment, with optional padding
        bytes = getComment().getBytes(StringEncodingType.ISO_8859_1.toString());
        stream.write(bytes);
        if (bytes.length < 28) {
            Util.copyStream(new ValueRepeatingInputStream(0, 28 - bytes.length), stream, 30);
        }

        // write separator, track and genre
        bytes = new byte[3];
        bytes[0] = (byte) 0;
        bytes[1] = getTrack();
        bytes[2] = getGenre().toByte();
        stream.write(bytes);

        if (autoFlush) {
            stream.flush();
        }
    }

    public void writeToFile(RandomAccessFile file) throws IOException {
        if (!isValid()) {
            throw new IOException("The tag is invalid and cannot be written.");
        }

        byte bytes[] = null;

        long position = file.getFilePointer();
        try {
            // position the file pointer at the correct position (end of file
            // if the file hasn't a tag, file end - 128 if it has).
            if (hasValidTag(file)) {
                file.seek(file.length() - tagSize);
            } else {
                file.seek(file.length());
            }
            // write the id
            file.write(tagId);

            // write title, with optional padding
            bytes = getTitle().getBytes(StringEncodingType.ISO_8859_1.toString());
            file.write(bytes);
            if (bytes.length < 30) {
                Util.copyData(new ValueRepeatingInputStream(0, 30 - bytes.length), file, 30, -1);
            }

            // write artist, with optional padding
            bytes = getArtist().getBytes(StringEncodingType.ISO_8859_1.toString());
            file.write(bytes);
            if (bytes.length < 30) {
                Util.copyData(new ValueRepeatingInputStream(0, 30 - bytes.length), file, 30, -1);
            }

            // write album, with optional padding
            bytes = getAlbum().getBytes(StringEncodingType.ISO_8859_1.toString());
            file.write(bytes);
            if (bytes.length < 30) {
                Util.copyData(new ValueRepeatingInputStream(0, 30 - bytes.length), file, 30, -1);
            }

            // write year
            bytes = getYear().getBytes(StringEncodingType.ISO_8859_1.toString());
            file.write(bytes);
            if (bytes.length < 4) {
                Util.copyData(new ValueRepeatingInputStream(0, 4 - bytes.length), file, 4, -1);
            }

            // write comment, with optional padding
            bytes = getComment().getBytes(StringEncodingType.ISO_8859_1.toString());
            file.write(bytes);
            if (bytes.length < 28) {
                Util.copyData(new ValueRepeatingInputStream(0, 28 - bytes.length), file, 28, -1);
            }

            // write separator, track and genre
            bytes = new byte[3];
            bytes[0] = (byte) 0;
            bytes[1] = getTrack();
            bytes[2] = getGenre().toByte();
            file.write(bytes);

        } catch (NoSuchMethodException ex) {
        } catch (IllegalAccessException ex) {
        } catch (IllegalArgumentException ex) {
        } catch (InvocationTargetException ex) {
        }
        file.seek(position);
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

    protected static boolean hasValidTag(byte bytes[]) {
        boolean ret = true;

        if (bytes.length < tagId.length)
            return false;

        for (int i = 0; i < tagId.length; i++) {
            if (tagId[i] != bytes[i]) {
                ret = false;
                break;
            }
        }

        return ret;
    }

    protected static boolean hasValidTag(RandomAccessFile file) {
        boolean ret = true;
        try {
            long position = file.getFilePointer();
            // move 128 bytes before file end and try to find the tag id "TAG"
            file.seek(file.length() - 128);
            byte bytes[] = new byte[tagId.length];
            file.read(bytes);
            ret = hasValidTag(bytes);
            file.seek(position);
        } catch (IOException ex) {
            ret = false;
        }
        return ret;
    }
}
