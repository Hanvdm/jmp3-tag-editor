package com.mscg.asf.impl.header.sub;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.mscg.asf.ASFObject;
import com.mscg.asf.exception.GUIDSizeException;
import com.mscg.asf.exception.InvalidObjectDataException;
import com.mscg.asf.guid.ASFObjectGUID;
import com.mscg.asf.guid.ASFObjectGUID.ReservedGUID;
import com.mscg.asf.util.Util;

public class ASFCodecListObject extends ASFObject {

    private CodecsInformations codecsInformations;

    public ASFCodecListObject(ASFObjectGUID guid, long length, InputStream data) throws InvalidObjectDataException {
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
        return codecsInformations;
    }

    @Override
    protected void parseData(InputStream data) throws InvalidObjectDataException {
        codecsInformations = new CodecsInformations();
        codecsInformations.setCodecs(new ArrayList<ASFCodec>(1));
        try {
            codecsInformations.reservedGUID = new ASFObjectGUID(data);
            if(!ReservedGUID.ASF_Reserved_2.equals(codecsInformations.reservedGUID))
                throw new InvalidObjectDataException("Invalid reserved GUID for codecs object");

            int codecsCount = Util.readLittleEndianInt(data);
            ((ArrayList<ASFCodec>)codecsInformations.getCodecs()).ensureCapacity(codecsCount);

            byte buffer[];
            int bytesRead;
            int fieldSize;

            for(int i = 0; i < codecsCount; i++) {
                ASFCodec codec = new ASFCodec();
                int typeValue = Util.readLittleEndianShort(data);
                codec.setType(CodecType.getCodecTypeFromIntValue(typeValue));

                fieldSize = Util.readLittleEndianShort(data) * 2;
                if(fieldSize != 0) {
                    buffer = new byte[fieldSize];
                    bytesRead = data.read(buffer);
                    if(bytesRead != buffer.length)
                        throw new InvalidObjectDataException("Cannot read codec name from stream");
                    codec.setName(Util.readStringFromBuffer(buffer));
                }

                fieldSize = Util.readLittleEndianShort(data) * 2;
                if(fieldSize != 0) {
                    buffer = new byte[fieldSize];
                    bytesRead = data.read(buffer);
                    if(bytesRead != buffer.length)
                        throw new InvalidObjectDataException("Cannot read codec description from stream");
                    codec.setDescription(Util.readStringFromBuffer(buffer));
                }

                fieldSize = Util.readLittleEndianShort(data);
                if(fieldSize != 0) {
                    buffer = new byte[fieldSize];
                    bytesRead = data.read(buffer);
                    if(bytesRead != buffer.length)
                        throw new InvalidObjectDataException("Cannot read codec informations from stream");
                    codec.setInformations(buffer);
                }

                codecsInformations.getCodecs().add(codec);
            }
        } catch(IOException e) {
            throw new InvalidObjectDataException("Cannot read codecsInformations list from stream", e);
        } catch (NullPointerException e) {
            throw new InvalidObjectDataException("Cannot read codecsInformations GUID from stream", e);
        } catch (GUIDSizeException e) {
            throw new InvalidObjectDataException("Cannot read codecsInformations GUID from stream", e);
        }
    }

    public static class CodecsInformations {
        private ASFObjectGUID reservedGUID;
        private List<ASFCodec> codecs;

        /**
         * @return the reservedGUID
         */
        public ASFObjectGUID getReservedGUID() {
            return reservedGUID;
        }

        /**
         * @return the codecsInformations
         */
        public List<ASFCodec> getCodecs() {
            return codecs;
        }

        /**
         * @param codecsInformations the codecsInformations to set
         */
        public void setCodecs(List<ASFCodec> codecs) {
            this.codecs = codecs;
        }

    }

    public static enum CodecType {
        VIDEO(0x0001), AUDIO(0x0002), UNKNOWN(0xFFFF);

        private int type;

        private CodecType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static CodecType getCodecTypeFromIntValue(int type) {
            for(CodecType codecType : CodecType.values()) {
                if(codecType.type == type)
                    return codecType;
            }
            return UNKNOWN;
        }
    }

    public static class ASFCodec {
        private CodecType type;
        private String name;
        private String description;
        private byte informations[];

        public ASFCodec() {
            type = CodecType.UNKNOWN;
            name = "";
            description = "";
            informations = Util.EMPTY_BYTE_ARRAY;
        }

        /**
         * @return the type
         */
        public CodecType getType() {
            return type;
        }

        /**
         * @param type the type to set
         */
        public void setType(CodecType type) {
            this.type = type;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
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
         * @return the informations
         */
        public byte[] getInformations() {
            return informations;
        }

        /**
         * @param informations the informations to set
         */
        public void setInformations(byte[] informations) {
            this.informations = informations;
        }
    }

}
