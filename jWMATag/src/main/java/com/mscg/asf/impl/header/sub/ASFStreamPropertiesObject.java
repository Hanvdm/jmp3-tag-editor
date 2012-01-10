package com.mscg.asf.impl.header.sub;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mscg.asf.ASFObject;
import com.mscg.asf.exception.GUIDSizeException;
import com.mscg.asf.exception.InvalidObjectDataException;
import com.mscg.asf.guid.ASFObjectGUID;
import com.mscg.asf.util.Util;

public class ASFStreamPropertiesObject extends ASFObject {

    private StreamInformations streamInfo;

    public ASFStreamPropertiesObject(ASFObjectGUID guid, long length, InputStream data) throws InvalidObjectDataException {
        super(guid, length, data);
    }

    @Override
    public long getLength() {
        //TODO: implement this method to calculate dinamically the
        // length of the object based on the real data size
        return super.getLength();
    }

    @Override
    public Object getData() {
        return streamInfo;
    }

    @Override
    protected void parseData(InputStream data) throws InvalidObjectDataException {
        streamInfo = new StreamInformations();

        try {
            byte buffer[] = new byte[(int)length];
            int byteRead = data.read(buffer);

            if(byteRead != buffer.length)
                throw new InvalidObjectDataException("Cannot read stream properties data from stream");

            InputStream is = new ByteArrayInputStream(buffer);
            streamInfo.setStreamTypeGUID(new ASFObjectGUID(is));
            streamInfo.setErrorCorrectionTypeGUID(new ASFObjectGUID(is));
            streamInfo.setTimeOffset(Util.readLittleEndianLong(is));

            int typeDataLength = Util.readLittleEndianInt(is);
            int errorCorrectionDataLength = Util.readLittleEndianInt(is);

            streamInfo.flags = Util.readLittleEndianShort(is);

            Util.readLittleEndianInt(is); // reserved 4 bytes

            if(typeDataLength != 0) {
                buffer = new byte[typeDataLength];
                byteRead = is.read(buffer);
                if(byteRead != buffer.length)
                    throw new InvalidObjectDataException("Cannot read stream type data from stream");
                streamInfo.setTypeData(buffer);
            }

            if(errorCorrectionDataLength != 0) {
                buffer = new byte[errorCorrectionDataLength];
                byteRead = is.read(buffer);
                if(byteRead != buffer.length)
                    throw new InvalidObjectDataException("Cannot read stream error correction data from stream");
                streamInfo.setErrorCorrectionData(buffer);
            }

        } catch(IOException e) {
            throw new InvalidObjectDataException("Cannot read stream properties object from stream", e);
        } catch (NullPointerException e) {
            throw new InvalidObjectDataException("Cannot read stream GUIDs object from stream", e);
        } catch (GUIDSizeException e) {
            throw new InvalidObjectDataException("Cannot read stream GUIDs object from stream", e);
        }

    }

    public static class StreamInformations {
        private ASFObjectGUID streamTypeGUID;
        private ASFObjectGUID errorCorrectionTypeGUID;
        private long timeOffset;
        private int flags;
        private byte typeData[];
        private byte errorCorrectionData[];

        public StreamInformations() {
            typeData = Util.EMPTY_BYTE_ARRAY;
            errorCorrectionData = Util.EMPTY_BYTE_ARRAY;
        }

        /**
         * @return the streamTypeGUID
         */
        public ASFObjectGUID getStreamTypeGUID() {
            return streamTypeGUID;
        }

        /**
         * @param streamTypeGUID the streamTypeGUID to set
         */
        public void setStreamTypeGUID(ASFObjectGUID streamTypeGUID) {
            this.streamTypeGUID = streamTypeGUID;
        }

        /**
         * @return the errorCorrectionTypeGUID
         */
        public ASFObjectGUID getErrorCorrectionTypeGUID() {
            return errorCorrectionTypeGUID;
        }

        /**
         * @param errorCorrectionTypeGUID the errorCorrectionTypeGUID to set
         */
        public void setErrorCorrectionTypeGUID(ASFObjectGUID errorCorrectionTypeGUID) {
            this.errorCorrectionTypeGUID = errorCorrectionTypeGUID;
        }

        /**
         * @return the timeOffset
         */
        public long getTimeOffset() {
            return timeOffset;
        }

        /**
         * @param timeOffset the timeOffset to set
         */
        public void setTimeOffset(long timeOffset) {
            this.timeOffset = timeOffset;
        }

        /**
         * The value of the flags field. The fields
         * contained in the flags can be retrieved
         * using the methods {@link #getStreamNumber()}, {@link #isEncrypted()}
         * or can be set using the methods {@link #setStreamNumber(short)},
         * {@link #setEncrypted(boolean)}.
         *
         * @return The flags field.
         */
        public int getFlags() {
            return flags;
        }

        /**
         * Returns the number of the stream written in the
         * flags field.
         *
         * @return The number of the stream.
         */
        public short getStreamNumber() {
            return (short)(flags & 0x007F);
        }

        /**
         * Returns the MSB bit of the flags field,
         * which indicates if the file is encrypted.
         *
         * @return <code>true</code> if the file is
         * marked as encrypted, <code>false</code> otherwise.
         */
        public boolean isEncrypted() {
            return ((flags & 0x8000) != 0);
        }

        /**
         * Sets the number of the stream into the flags field.
         *
         * @param streamNumber The number of the stream. This
         * value must be &gt;= 0, or the method won't apply
         * changes.
         */
        public void setStreamNumber(short streamNumber) {
            if(streamNumber >= 0) {
                int remaining = flags & 0xFF80; // get the 9 upper bits
                flags = remaining | streamNumber; // merge the bits
            }
        }

        /**
         * Sets the encrypted bit into the flags field.
         *
         * @param encripted <code>true</code> if the file
         * should be marked as encrypted, <code>false</code>
         * otherwise.
         */
        public void setEncrypted(boolean encripted) {
            int remaining = flags & 0x7FFF; // get the 15 lower bits
            flags = (encripted ? 0x8000 : 0x0000) | remaining; // merge the bits
        }

        /**
         * @return the typeData
         */
        public byte[] getTypeData() {
            return typeData;
        }

        /**
         * @param typeData the typeData to set
         */
        public void setTypeData(byte[] typeData) {
            this.typeData = typeData;
        }

        /**
         * @return the errorCorrectionData
         */
        public byte[] getErrorCorrectionData() {
            return errorCorrectionData;
        }

        /**
         * @param errorCorrectionData the errorCorrectionData to set
         */
        public void setErrorCorrectionData(byte[] errorCorrectionData) {
            this.errorCorrectionData = errorCorrectionData;
        }

    }

}
