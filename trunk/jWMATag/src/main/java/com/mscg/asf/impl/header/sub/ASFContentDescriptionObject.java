package com.mscg.asf.impl.header.sub;

import java.io.IOException;
import java.io.InputStream;

import com.mscg.asf.ASFObject;
import com.mscg.asf.exception.InvalidObjectDataException;
import com.mscg.asf.guid.ASFObjectGUID;
import com.mscg.asf.util.Util;

public class ASFContentDescriptionObject extends ASFObject {

    private ContentDescriptionData data;

    public ASFContentDescriptionObject(ASFObjectGUID guid, long length, InputStream data) throws InvalidObjectDataException {
        super(guid, length, data);
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    protected void parseData(InputStream data) throws InvalidObjectDataException {
        this.data = new ContentDescriptionData();

        try {
            int titleLength = Util.readLittleEndianShort(data);
            int authorLength = Util.readLittleEndianShort(data);
            int copyrightLength = Util.readLittleEndianShort(data);
            int descriptionLength = Util.readLittleEndianShort(data);
            int ratingLength = Util.readLittleEndianShort(data);

            byte buffer[];
            int byteRead;

            if(titleLength != 0) {
                buffer = new byte[titleLength];
                byteRead = data.read(buffer);
                if(byteRead != buffer.length)
                    throw new InvalidObjectDataException("Cannot read content title from stream");
                this.data.setTitle(Util.readStringFromBuffer(buffer));
            }

            if(authorLength != 0) {
                buffer = new byte[authorLength];
                byteRead = data.read(buffer);
                if(byteRead != buffer.length)
                    throw new InvalidObjectDataException("Cannot read content author from stream");
                this.data.setAuthor(Util.readStringFromBuffer(buffer));
            }

            if(copyrightLength != 0) {
                buffer = new byte[copyrightLength];
                byteRead = data.read(buffer);
                if(byteRead != buffer.length)
                    throw new InvalidObjectDataException("Cannot read content copyright from stream");
                this.data.setCopyright(Util.readStringFromBuffer(buffer));
            }

            if(descriptionLength != 0) {
                buffer = new byte[descriptionLength];
                byteRead = data.read(buffer);
                if(byteRead != buffer.length)
                    throw new InvalidObjectDataException("Cannot read content description from stream");
                this.data.setDescription(Util.readStringFromBuffer(buffer));
            }

            if(ratingLength != 0) {
                buffer = new byte[ratingLength];
                byteRead = data.read(buffer);
                if(byteRead != buffer.length)
                    throw new InvalidObjectDataException("Cannot read content rating from stream");
                this.data.setRating(Util.readStringFromBuffer(buffer));
            }

        } catch(IOException e) {
            throw new InvalidObjectDataException("Cannot read content descriptor from stream", e);
        }
    }

    public static class ContentDescriptionData {
        private String title;
        private String author;
        private String copyright;
        private String description;
        private String rating;

        public ContentDescriptionData() {
            title = "";
            author = "";
            copyright = "";
            description = "";
            rating = "";
        }
        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }
        /**
         * @param title the title to set
         */
        public void setTitle(String title) {
            this.title = title;
        }
        /**
         * @return the author
         */
        public String getAuthor() {
            return author;
        }
        /**
         * @param author the author to set
         */
        public void setAuthor(String author) {
            this.author = author;
        }
        /**
         * @return the copyright
         */
        public String getCopyright() {
            return copyright;
        }
        /**
         * @param copyright the copyright to set
         */
        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }
        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }
        /**
         * @param description the description to set
         */
        public void setDescription(String description) {
            this.description = description;
        }
        /**
         * @return the rating
         */
        public String getRating() {
            return rating;
        }
        /**
         * @param rating the rating to set
         */
        public void setRating(String rating) {
            this.rating = rating;
        }

    }
}
