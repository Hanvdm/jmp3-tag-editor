package com.mscg.asf.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mscg.asf.ASFObject;
import com.mscg.asf.ASFObjectProvider;
import com.mscg.asf.guid.ASFObjectGUID;
import com.mscg.asf.impl.data.ASFDataObject;
import com.mscg.asf.impl.header.ASFHeaderObject;
import com.mscg.asf.impl.header.sub.ASFCodecListObject;
import com.mscg.asf.impl.header.sub.ASFContentDescriptionObject;
import com.mscg.asf.impl.header.sub.ASFExtendedContentDescriptionObject;
import com.mscg.asf.impl.header.sub.ASFFilePropertiesObject;
import com.mscg.asf.impl.header.sub.ASFHeaderExtensionObject;
import com.mscg.asf.impl.header.sub.ASFStreamPropertiesObject;
import com.mscg.asf.impl.index.ASFIndexObject;
import com.mscg.asf.impl.index.ASFMediaObjectIndexObject;
import com.mscg.asf.impl.index.ASFSimpleIndexObject;
import com.mscg.asf.impl.index.ASFTimecodeIndexObject;

public class BaseASFObjectProvider implements ASFObjectProvider {

    public static final String ASF_Header_Object             = "75B22630-668E-11CF-A6D9-00AA0062CE6C";
    public static final String ASF_Data_Object               = "75B22636-668E-11CF-D9A6-00AA0062CE6C";
    public static final String ASF_Simple_Index_Object       = "33000890-E5B1-11CF-89F4-00A0C90349CB";
    public static final String ASF_Index_Object              = "D6E229D3-35DA-11D1-9034-00A0C90349BE";
    public static final String ASF_Media_Object_Index_Object = "FEB103F8-12AD-4C64-840F-2A1D2F7AD48C";
    public static final String ASF_Timecode_Index_Object     = "3CB73FD0-0C4A-4803-953D-EDF7B6228F0C";

    public static final String ASF_File_Properties_Object              = "8CABDCA1-A947-11CF-8EE4-00C00C205365";
    public static final String ASF_Stream_Properties_Object            = "B7DC0791-A9B7-11CF-8EE6-00C00C205365";
    public static final String ASF_Header_Extension_Object             = "5FBF03B5-A92E-11CF-8EE3-00C00C205365";
    public static final String ASF_Codec_List_Object                   = "86D15240-311D-11D0-A3A4-00A0C90348F6";
    public static final String ASF_Content_Description_Object          = "75B22633-668E-11CF-A6D9-00AA0062CE6C";
    public static final String ASF_Extended_Content_Description_Object = "D2D0A440-E307-11D2-97F0-00A0C95EA850";

    public static Map<ASFObjectGUID, Class<? extends ASFObject>> objects;

    public Map<ASFObjectGUID, Class<? extends ASFObject>> getGUIDsToObjects() {
        synchronized (BaseASFObjectProvider.class) {
            if(objects == null) {
                objects = new LinkedHashMap<ASFObjectGUID, Class<? extends ASFObject>>();

                objects.put(new ASFObjectGUID(ASF_Header_Object, true), ASFHeaderObject.class);
                objects.put(new ASFObjectGUID(ASF_Data_Object, true), ASFDataObject.class);
                objects.put(new ASFObjectGUID(ASF_Simple_Index_Object, true), ASFSimpleIndexObject.class);
                objects.put(new ASFObjectGUID(ASF_Index_Object, true), ASFIndexObject.class);
                objects.put(new ASFObjectGUID(ASF_Media_Object_Index_Object, true), ASFMediaObjectIndexObject.class);
                objects.put(new ASFObjectGUID(ASF_Timecode_Index_Object, true), ASFTimecodeIndexObject.class);

                objects.put(new ASFObjectGUID(ASF_File_Properties_Object, true), ASFFilePropertiesObject.class);
                objects.put(new ASFObjectGUID(ASF_Stream_Properties_Object, true), ASFStreamPropertiesObject.class);
                objects.put(new ASFObjectGUID(ASF_Header_Extension_Object, true), ASFHeaderExtensionObject.class);
                objects.put(new ASFObjectGUID(ASF_Codec_List_Object, true), ASFCodecListObject.class);
                objects.put(new ASFObjectGUID(ASF_Content_Description_Object, true), ASFContentDescriptionObject.class);
                objects.put(new ASFObjectGUID(ASF_Extended_Content_Description_Object, true), ASFExtendedContentDescriptionObject.class);

            }
            return objects;
        }
    }

}
