package com.mscg.asf.impl.header.sub;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mscg.asf.ASFObject;
import com.mscg.asf.exception.InvalidObjectDataException;
import com.mscg.asf.guid.ASFObjectGUID;
import com.mscg.asf.util.Util;

public class ASFExtendedContentDescriptionObject extends ASFObject {

    private Map<String, ExtendedDescriptor> extendedDescriptions;

    public ASFExtendedContentDescriptionObject(ASFObjectGUID guid, long length,
                                               InputStream data) throws InvalidObjectDataException {
        super(guid, length, data);
    }

    @Override
    public Object getData() {
        return extendedDescriptions;
    }

    @Override
    public long getLength() {
        //TODO: implement this method to calculate dinamically the
        // length of the object based on the real data size
        return super.getLength();
    }

    @Override
    protected void parseData(InputStream data) throws InvalidObjectDataException {
        extendedDescriptions = new LinkedHashMap<String, ExtendedDescriptor>();

        try {
            int elemCount = Util.readLittleEndianShort(data);

            for(int i = 0; i < elemCount; i++) {
                int elemNameLength = Util.readLittleEndianShort(data);
                byte buffer[];
                int byteRead;

                buffer = new byte[elemNameLength];
                byteRead = data.read(buffer);
                if(byteRead != buffer.length)
                    throw new InvalidObjectDataException("Cannot read descriptor name from stream");
                String elementName = Util.readStringFromBuffer(buffer);

                ExtendedDescriptorType dataType = ExtendedDescriptorType.getFromIntType(Util.readLittleEndianShort(data));

                int dataLength = Util.readLittleEndianShort(data);
                buffer = new byte[dataLength];
                byteRead = data.read(buffer);
                if(byteRead != buffer.length)
                    throw new InvalidObjectDataException("Cannot read descriptor data for entry \"" + elementName +
                                                         "\" from stream");

                switch(dataType) {
                case STRING:
                    extendedDescriptions.put(elementName, new ExtendedDescriptor(dataType,
                                                                                 Util.readStringFromBuffer(buffer)));
                    break;
                case BYTE_ARRAY:
                case UNKNOWN:
                    extendedDescriptions.put(elementName, new ExtendedDescriptor(dataType, buffer));
                    break;
                case BOOL:
                    int v = (int)Util.readLittleEndianLong(buffer);
                    extendedDescriptions.put(elementName, new ExtendedDescriptor(dataType,
                                                                                 v == 0 ? Boolean.FALSE : Boolean.TRUE));
                    break;
                case INT:
                case SHORT:
                    extendedDescriptions.put(elementName, new ExtendedDescriptor(dataType,
                                                                                 (int)Util.readLittleEndianLong(buffer)));
                    break;
                case LONG:
                    extendedDescriptions.put(elementName, new ExtendedDescriptor(dataType,
                                                                                 Util.readLittleEndianLong(buffer)));
                    break;
                }
            }
        } catch(IOException e) {
            throw new InvalidObjectDataException("Cannot read content extended descriptor from stream", e);
        }
    }

    public static enum ExtendedDescriptorType {
        STRING(0x0000), BYTE_ARRAY(0x0001),
        BOOL(0x0002),   INT(0x0003),
        LONG(0x0004),   SHORT(0x0005),
        UNKNOWN(0xFFFF);

        private int type;

        private ExtendedDescriptorType(int type) {
            this.type = type;
        }

        public static ExtendedDescriptorType getFromIntType(int type) {
            for(ExtendedDescriptorType descType : ExtendedDescriptorType.values()) {
                if(descType.type == type)
                    return descType;
            }
            return UNKNOWN;
        }
    }

    public static class ExtendedDescriptor {

        private ExtendedDescriptorType type;
        private Object value;

        public ExtendedDescriptor(ExtendedDescriptorType type, Object value) {
            this.type = type;
            this.value = value;
        }

        public ExtendedDescriptorType getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }

    }

    public static class StandardExtendedKeys {

        public static final String PUBLISHER = "WM/Publisher";
        public static final String GENRE     = "WM/Genre";
        public static final String ALBUM     = "WM/AlbumTitle";
        public static final String ARTIST    = "WM/AlbumArtist";
        public static final String TRACK     = "WM/TrackNumber";
        public static final String COMPOSER  = "WM/Composer";
        public static final String ENCODER   = "WM/EncodingSettings";
        public static final String YEAR      = "WM/Year";

        private StandardExtendedKeys() {

        }

    }

}
