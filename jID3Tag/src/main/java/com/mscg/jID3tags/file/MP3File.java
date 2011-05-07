package com.mscg.jID3tags.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import com.mscg.jID3tags.exception.MP3FileException;
import com.mscg.jID3tags.id3v1.ID3v11Tag;
import com.mscg.jID3tags.id3v2.ID3v2Tag;
import com.mscg.jID3tags.objects.frames.ID3v2COMMFrame;
import com.mscg.jID3tags.objects.frames.ID3v2Frame;
import com.mscg.jID3tags.objects.frames.ID3v2TALBFrame;
import com.mscg.jID3tags.objects.frames.ID3v2TCONFrame;
import com.mscg.jID3tags.objects.frames.ID3v2TIT2Frame;
import com.mscg.jID3tags.objects.frames.ID3v2TPE1Frame;
import com.mscg.jID3tags.objects.frames.ID3v2TPE2Frame;
import com.mscg.jID3tags.objects.frames.ID3v2TRCKFrame;
import com.mscg.jID3tags.objects.frames.ID3v2TYERFrame;
import com.mscg.jID3tags.objects.frames.contents.ID3v2FrameContentComment;
import com.mscg.jID3tags.objects.frames.contents.ID3v2FrameContentString;
import com.mscg.jID3tags.util.Costants.ID3v1Genre;
import com.mscg.jID3tags.util.Costants.StringEncodingType;
import com.mscg.jID3tags.util.Util;

/**
 *
 * @author Giuseppe Miscione
 */
public class MP3File {

    protected File file;
    protected ID3v2Tag id3v2tag;
    protected Integer oldId3v2TagLength;
    protected Exception id3v2tagParseException;
    protected ID3v11Tag id3v11tag;
    protected boolean hadId3v11tag;
    protected Exception id3v11tagParseException;

    public MP3File(String fileName) {
        this(new File(fileName));
    }

    public MP3File(File file) {
        setFile(file);
        InputStream input = null;
        try {
            if (!getFile().exists()) {
                throw new FileNotFoundException("Cannot open file \"" + getFile().getCanonicalPath() + "\".");
            }
            input = new FileInputStream(getFile());
            setID3v2tag(ID3v2Tag.Factory.parse(input));
            this.oldId3v2TagLength = (hasID3v2tag() ? getID3v2tag().getCompleteSize() : null);
        } catch (Exception e) {
            id3v2tagParseException = e;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }

        RandomAccessFile randomFile = null;
        try {
            if (!getFile().exists()) {
                throw new FileNotFoundException("Cannot open file \"" + getFile().getCanonicalPath() + "\".");
            }
            randomFile = new RandomAccessFile(file, "r");
            setID3v11tag(ID3v11Tag.Factory.parse(randomFile));
            this.hadId3v11tag = hasID3v11tag();
        } catch (Exception e) {
            id3v11tagParseException = e;
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public File getFile() {
        return file;
    }

    protected void setFile(File file) {
        this.file = file;
    }

    public ID3v11Tag getID3v11tag() {
        return id3v11tag;
    }

    public boolean hasID3v11tag() {
        return getID3v11tag() != null;
    }

    public void setID3v11tag(ID3v11Tag id3v11tag) {
        this.id3v11tag = id3v11tag;
    }

    public ID3v2Tag getID3v2tag() {
        return id3v2tag;
    }

    public boolean hasID3v2tag() {
        return getID3v2tag() != null;
    }

    public void setID3v2tag(ID3v2Tag id3v2tag) {
        this.id3v2tag = id3v2tag;
    }

    public Exception getID3v11tagParseException() {
        return id3v11tagParseException;
    }

    public Exception getID3v2tagParseException() {
        return id3v2tagParseException;
    }

    public String getTitle() {
        String title = null;
        if (hasID3v2tag()) {
            ID3v2Frame frame = getID3v2tag().getFrame(ID3v2TIT2Frame.id);
            if (frame != null) {
                title = ((ID3v2FrameContentString) frame.getContent()).getContent();
            }
        }
        if (title == null && hasID3v11tag()) {
            title = getID3v11tag().getTitle();
        }
        return title;
    }

    public void setTitle(String title) {
        setTitle(title, StringEncodingType.UTF_16);
    }

    public void setTitle(String title, StringEncodingType encoding) {
        if (hasID3v2tag()) {
            try {
                ID3v2Frame frame = getID3v2tag().getFrame(ID3v2TIT2Frame.id);
                if (frame == null) {
                    frame = new ID3v2TIT2Frame();
                    ID3v2FrameContentString content = new ID3v2FrameContentString(title, encoding);
                    frame.setContent(content);
                    getID3v2tag().addFrame(frame);
                }
                ((ID3v2FrameContentString) frame.getContent()).setContent(title);
            } catch (Exception e) {
            }
        }

        if (hasID3v11tag()) {
            try {
                getID3v11tag().setTitle(title);
            } catch (Exception e) {
            }
        }
    }

    public String getArtist() {
        String artist = null;
        if (hasID3v2tag()) {
            ID3v2Frame frame = getID3v2tag().getFrame(ID3v2TPE1Frame.id);
            if (frame == null) {
                frame = getID3v2tag().getFrame(ID3v2TPE2Frame.id);
            }
            if (frame != null) {
                artist = ((ID3v2FrameContentString) frame.getContent()).getContent();
            }
        }
        if (artist == null && hasID3v11tag()) {
            artist = getID3v11tag().getArtist();
        }
        return artist;
    }

    public void setArtist(String artist) {
        setArtist(artist, StringEncodingType.UTF_16);
    }

    public void setArtist(String artist, StringEncodingType encoding) {
        if (hasID3v2tag()) {
            try {
                ID3v2Frame frame = getID3v2tag().getFrame(ID3v2TPE1Frame.id);
                if (frame == null) {
                    frame = new ID3v2TPE1Frame();
                    ID3v2FrameContentString content = new ID3v2FrameContentString(artist, encoding);
                    frame.setContent(content);
                    getID3v2tag().addFrame(frame);
                }
                ((ID3v2FrameContentString) frame.getContent()).setContent(artist);

                frame = getID3v2tag().getFrame(ID3v2TPE2Frame.id);
                if (frame == null) {
                    frame = new ID3v2TPE2Frame();
                    ID3v2FrameContentString content = new ID3v2FrameContentString(artist, encoding);
                    frame.setContent(content);
                    getID3v2tag().addFrame(frame);
                }
                ((ID3v2FrameContentString) frame.getContent()).setContent(artist);
            } catch (Exception e) {
            }
        }

        if (hasID3v11tag()) {
            try {
                getID3v11tag().setArtist(artist);
            } catch (Exception e) {
            }
        }
    }

    public String getAlbum() {
        String album = null;
        if (hasID3v2tag()) {
            ID3v2Frame frame = getID3v2tag().getFrame(ID3v2TALBFrame.id);
            if (frame != null) {
                album = ((ID3v2FrameContentString) frame.getContent()).getContent();
            }
        }
        if (album == null && hasID3v11tag()) {
            album = getID3v11tag().getAlbum();
        }
        return album;
    }

    public void setAlbum(String album) {
        setAlbum(album, StringEncodingType.UTF_16);
    }

    public void setAlbum(String album, StringEncodingType encoding) {
        if (hasID3v2tag()) {
            try {
                ID3v2Frame frame = getID3v2tag().getFrame(ID3v2TALBFrame.id);
                if (frame == null) {
                    frame = new ID3v2TALBFrame();
                    ID3v2FrameContentString content = new ID3v2FrameContentString(album, encoding);
                    frame.setContent(content);
                    getID3v2tag().addFrame(frame);
                }
                ((ID3v2FrameContentString) frame.getContent()).setContent(album);
            } catch (Exception e) {
            }
        }

        if (hasID3v11tag()) {
            try {
                getID3v11tag().setAlbum(album);
            } catch (Exception e) {
            }
        }
    }

    public String getYear() {
        String year = null;
        if (hasID3v2tag()) {
            ID3v2Frame frame = getID3v2tag().getFrame(ID3v2TYERFrame.id);
            if (frame != null) {
                year = ((ID3v2FrameContentString) frame.getContent()).getContent();
            }
        }
        if (year == null && hasID3v11tag()) {
            year = getID3v11tag().getYear();
        }
        return year;
    }

    public void setYear(String year) {
        setYear(year, StringEncodingType.UTF_16);
    }

    public void setYear(String year, StringEncodingType encoding) {
        if (hasID3v2tag()) {
            try {
                ID3v2Frame frame = getID3v2tag().getFrame(ID3v2TYERFrame.id);
                if (frame == null) {
                    frame = new ID3v2TYERFrame();
                    ID3v2FrameContentString content = new ID3v2FrameContentString(year, encoding);
                    frame.setContent(content);
                    getID3v2tag().addFrame(frame);
                }
                ((ID3v2FrameContentString) frame.getContent()).setContent(year);
            } catch (Exception e) {
            }
        }

        if (hasID3v11tag()) {
            try {
                getID3v11tag().setYear(year);
            } catch (Exception e) {
            }
        }
    }

    public String getComment() {
        String comment = null;
        if (hasID3v2tag()) {
            ID3v2Frame frame = getID3v2tag().getFrame(ID3v2COMMFrame.id);
            if (frame != null) {
                ID3v2FrameContentComment commentContent = (ID3v2FrameContentComment) frame.getContent();
                comment = commentContent.getComment();
            }
        }
        if (comment == null && hasID3v11tag()) {
            comment = getID3v11tag().getComment();
        }
        return comment;
    }

    public void setComment(String comment) {
        setComment(comment, StringEncodingType.UTF_16);
    }

    public void setComment(String comment, StringEncodingType encoding) {
        if (hasID3v2tag()) {
            try {
                ID3v2Frame frame = getID3v2tag().getFrame(ID3v2COMMFrame.id);
                if (frame == null) {
                    frame = new ID3v2COMMFrame();
                    ID3v2FrameContentComment content = new ID3v2FrameContentComment(encoding, "XXX", "", comment);
                    frame.setContent(content);
                    getID3v2tag().addFrame(frame);
                }
                ((ID3v2FrameContentComment) frame.getContent()).setComment(comment);
            } catch (Exception e) {
            }
        }

        if (hasID3v11tag()) {
            try {
                getID3v11tag().setComment(comment);
            } catch (Exception e) {
            }
        }
    }

    public String getTrack() {
        String track = null;
        if (hasID3v2tag()) {
            ID3v2Frame frame = getID3v2tag().getFrame(ID3v2TRCKFrame.id);
            if (frame != null) {
                track = ((ID3v2FrameContentString) frame.getContent()).getContent();
            }
        }
        if (track == null && hasID3v11tag()) {
            track = Byte.toString(getID3v11tag().getTrack());
        }
        return track;
    }

    public void setTrack(String track) {
        setTrack(track, StringEncodingType.UTF_16);
    }

    public void setTrack(String track, StringEncodingType encoding) {
        if (hasID3v2tag()) {
            try {
                ID3v2Frame frame = getID3v2tag().getFrame(ID3v2TRCKFrame.id);
                if (frame == null) {
                    frame = new ID3v2TRCKFrame();
                    ID3v2FrameContentString content = new ID3v2FrameContentString(track, encoding);
                    frame.setContent(content);
                    getID3v2tag().addFrame(frame);
                }
                ((ID3v2FrameContentString) frame.getContent()).setContent(track);
            } catch (Exception e) {
            }
        }

        if (hasID3v11tag()) {
            try {
                getID3v11tag().setTrack(Byte.parseByte(track));
            } catch (Exception e) {
            }
        }
    }

    public String getGenre() {
        String genre = null;
        if (hasID3v2tag()) {
            ID3v2Frame frame = getID3v2tag().getFrame(ID3v2TCONFrame.id);
            if (frame != null) {
                genre = ((ID3v2FrameContentString) frame.getContent()).getContent();
            }
        }
        if (genre == null && hasID3v11tag()) {
            genre = getID3v11tag().getGenre().toString();
        }
        return genre;
    }

    public void setGenre(String genre) {
        setGenre(genre, StringEncodingType.UTF_16);
    }

    public void setGenre(String genre, StringEncodingType encoding) {
        if (hasID3v2tag()) {
            try {
                ID3v2Frame frame = getID3v2tag().getFrame(ID3v2TCONFrame.id);
                if (frame == null) {
                    frame = new ID3v2TCONFrame();
                    ID3v2FrameContentString content = new ID3v2FrameContentString(genre, encoding);
                    frame.setContent(content);
                    getID3v2tag().addFrame(frame);
                }
                ((ID3v2FrameContentString) frame.getContent()).setContent(genre);
            } catch (Exception e) {
            }
        }

        if (hasID3v11tag()) {
            try {
                getID3v11tag().setGenre(ID3v1Genre.fromString(genre));
            } catch (Exception e) {
            }
        }
    }

    public void copyID3v11ToID3v2() throws MP3FileException {
        copyID3v11ToID3v2(StringEncodingType.UTF_16);
    }

    public void copyID3v11ToID3v2(StringEncodingType preferredEncoding) throws MP3FileException {

        if (!hasID3v11tag())
            throw new MP3FileException("This MP3 file doesn't have an ID3v1.1 tag.");
        if (!hasID3v2tag()) {
            setID3v2tag(new ID3v2Tag());
        }

        try {
            ID3v2Frame frame = null;

            // set the title frame
            frame = getID3v2tag().getFrame(ID3v2TIT2Frame.id);
            if (frame == null) {
                frame = new ID3v2TIT2Frame();
                getID3v2tag().addFrame(frame);
            }
            frame.setContent(new ID3v2FrameContentString(getID3v11tag().getTitle(), preferredEncoding));

            // set the artist frames
            frame = getID3v2tag().getFrame(ID3v2TPE1Frame.id);
            if (frame == null) {
                frame = new ID3v2TPE1Frame();
                getID3v2tag().addFrame(frame);
            }
            frame.setContent(new ID3v2FrameContentString(getID3v11tag().getArtist(), preferredEncoding));
            frame = getID3v2tag().getFrame(ID3v2TPE2Frame.id);
            if (frame == null) {
                frame = new ID3v2TPE2Frame();
                getID3v2tag().addFrame(frame);
            }
            frame.setContent(new ID3v2FrameContentString(getID3v11tag().getArtist(), preferredEncoding));

            // set the album frame
            frame = getID3v2tag().getFrame(ID3v2TALBFrame.id);
            if (frame == null) {
                frame = new ID3v2TALBFrame();
                getID3v2tag().addFrame(frame);
            }
            frame.setContent(new ID3v2FrameContentString(getID3v11tag().getAlbum(), preferredEncoding));

            // set the year frame
            frame = getID3v2tag().getFrame(ID3v2TYERFrame.id);
            if (frame == null) {
                frame = new ID3v2TYERFrame();
                getID3v2tag().addFrame(frame);
            }
            frame.setContent(new ID3v2FrameContentString(getID3v11tag().getYear(), preferredEncoding));

            // set the comment frame
            frame = getID3v2tag().getFrame(ID3v2COMMFrame.id);
            if (frame == null) {
                frame = new ID3v2COMMFrame();
                getID3v2tag().addFrame(frame);
            }
            frame.setContent(new ID3v2FrameContentComment(preferredEncoding, "XXX", "", getID3v11tag().getComment()));

            // set the track frame
            frame = getID3v2tag().getFrame(ID3v2TRCKFrame.id);
            if (frame == null) {
                frame = new ID3v2TRCKFrame();
                getID3v2tag().addFrame(frame);
            }
            frame.setContent(new ID3v2FrameContentString(String.format("%02d", getID3v11tag().getTrack()), preferredEncoding));

            // set the genre frame
            frame = getID3v2tag().getFrame(ID3v2TCONFrame.id);
            if (frame == null) {
                frame = new ID3v2TCONFrame();
                getID3v2tag().addFrame(frame);
            }
            frame.setContent(new ID3v2FrameContentString(getID3v11tag().getGenre().toString(), preferredEncoding));

        } catch (Exception e) {
            throw new MP3FileException("Can't copy ID3v1.1 tag data.", e);
        }
    }

    public void copyID3v2ToID3v11() throws MP3FileException {
        if (!hasID3v2tag())
            throw new MP3FileException("This MP3 file doesn't have an ID3v2 tag.");
        if (!hasID3v11tag())
            setID3v11tag(new ID3v11Tag());

        try {
            getID3v11tag().setTitle(getTitle());
            getID3v11tag().setArtist(getArtist());
            getID3v11tag().setAlbum(getAlbum());
            getID3v11tag().setYear(getYear());
            getID3v11tag().setComment(getComment());
            getID3v11tag().setTrack(Byte.parseByte(getTrack()));
            getID3v11tag().setGenre(ID3v1Genre.fromString(getGenre()));

        } catch (Exception e) {
            throw new MP3FileException("Can't copy ID3v2 tag data.", e);
        }

    }

    public void write() throws MP3FileException {
        // check for tag removal
        if (!hasID3v2tag() && oldId3v2TagLength != null) {
            removeID3v2Tag();
        }
        // write the tag
        if (hasID3v2tag()) {
            writeID3v2Tag();
        }

        if (!hasID3v11tag() && hadId3v11tag) {
            removeID3v1Tag();
        }
        // write the tag
        if (hasID3v11tag()) {
            writeID3v1Tag();
        }
        this.hadId3v11tag = hasID3v11tag();

    }

    protected void removeID3v1Tag() throws MP3FileException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            raf.setLength(raf.length() - ID3v11Tag.tagSize);
        } catch (Exception ex) {
            throw new MP3FileException("An error occurred while removing ID3v1 tag.", ex);
        } finally {
            if (raf != null) {
                Util.flushAndClose(raf);
            }
        }
    }

    protected void writeID3v1Tag() throws MP3FileException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            getID3v11tag().writeToFile(raf);
        } catch (Exception ex) {
            throw new MP3FileException("An error occurred while writing ID3v1 tag.", ex);
        } finally {
            if (raf != null) {
                Util.flushAndClose(raf);
            }
        }
    }

    protected void removeID3v2Tag() throws MP3FileException {
        RandomAccessFile raf = null;
        OutputStream os = null;
        OutputStream bos = null;
        try {
            String filePath = file.getCanonicalPath();
            File tmp = new File(filePath + ".tmp");

            raf = new RandomAccessFile(file, "rw");
            raf.seek(oldId3v2TagLength);

            os = new FileOutputStream(tmp);
            bos = new BufferedOutputStream(os);

            try {
                Util.copyData(raf, bos, 10240, -1);

                // close original file
                Util.flushAndClose(raf);
                raf = null;

                // close streams on output file
                Util.flushAndClose(bos);
                bos = null;
                Util.flushAndClose(os);
                os = null;

                // delete original file
                file.delete();

                // rename temp file
                tmp.renameTo(new File(filePath));

                // reload file
                file = new File(filePath);
            } catch (Exception e) {
                // cannot copy data to temp file, so delete it
                // and abort
                tmp.delete();
                throw e;
            }

        } catch (Exception ex) {
            throw new MP3FileException("An error occurred while removing ID3v2 tag.", ex);
        } finally {
            if (raf != null) {
                Util.flushAndClose(raf);
            }

            if (bos != null) {
                Util.flushAndClose(bos);
            }

            if (os != null) {
                Util.flushAndClose(os);
            }
        }
    }

    protected void writeID3v2Tag() throws MP3FileException {
        boolean updateSize = false;
        if (oldId3v2TagLength != null && oldId3v2TagLength >= getID3v2tag().getCompleteSize()) {
            // the padding is big enough to contain the new tag
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(file, "rw");
                raf.seek(0l);
                raf.write(getID3v2tag().getBytes());
                updateSize = true;
            } catch (Exception ex) {
                throw new MP3FileException("An error occurred while writing ID3v2 tag.", ex);
            } finally {
                if (raf != null) {
                    Util.flushAndClose(raf);
                }
            }
        } else {
            RandomAccessFile raf = null;
            OutputStream os = null;
            OutputStream bos = null;
            try {
                String filePath = file.getCanonicalPath();

                raf = new RandomAccessFile(file, "rw");
                raf.seek((oldId3v2TagLength == null ? 0l : oldId3v2TagLength));

                File tmp = new File(filePath + ".tmp");
                os = new FileOutputStream(tmp);
                bos = new BufferedOutputStream(os);

                try {
                    // write the new tag in the temp file
                    getID3v2tag().writeToStream(bos, true);

                    // write the MP3 data into the temp file
                    Util.copyData(raf, bos, 10240, -1);

                    // close original file
                    Util.flushAndClose(raf);
                    raf = null;

                    // close streams on output file
                    Util.flushAndClose(bos);
                    bos = null;
                    Util.flushAndClose(os);
                    os = null;

                    // delete original file
                    file.delete();

                    // rename temp file
                    tmp.renameTo(new File(filePath));

                    // reload file
                    file = new File(filePath);

                } catch (Exception e) {
                    // cannot copy data to temp file, so delete it
                    // and abort
                    tmp.delete();
                    throw e;
                }

                updateSize = true;
            } catch (Exception ex) {
                throw new MP3FileException("An error occurred while writing ID3v2 tag.", ex);
            } finally {
                if (raf != null) {
                    Util.flushAndClose(raf);
                }

                if (bos != null) {
                    Util.flushAndClose(bos);
                }

                if (os != null) {
                    Util.flushAndClose(os);
                }
            }
        }

        if (updateSize)
            this.oldId3v2TagLength = (hasID3v2tag() ? getID3v2tag().getCompleteSize() : null);
    }

}
