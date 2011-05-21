package com.mscg.jID3tags.util;

/**
 *
 * @author Giuseppe Miscione
 */
public class Costants {

    public enum StringEncodingType {
        ISO_8859_1, UTF_16, UTF_16_BE, UTF_8;

        public byte toByte() {
            switch (this) {
            case UTF_16:
                return 1;
            case UTF_16_BE:
                return 2;
            case UTF_8:
                return 3;
            case ISO_8859_1:
            default:
                return 0;
            }
        }

        public int getStringTerminatorWidth() {
            switch (this) {
            case UTF_16:
            case UTF_16_BE:
                return 2;
            case UTF_8:
            case ISO_8859_1:
            default:
                return 1;
            }
        }

        public byte[] getStringTerminator() {
            switch (this) {
            case UTF_16:
            case UTF_16_BE:
                return new byte[]{0, 0};
            case UTF_8:
            case ISO_8859_1:
            default:
                return new byte[]{0};
            }
        }

        @Override
        public String toString() {
            switch (this) {
            case UTF_16:
                return "UTF-16";
            case UTF_16_BE:
                return "UTF-16BE";
            case UTF_8:
                return "UTF-8";
            case ISO_8859_1:
            default:
                return "ISO-8859-1";
            }
        }

        public static StringEncodingType fromByte(byte value) {
            switch (value) {
            case 1:
                return UTF_16;
            case 2:
                return UTF_16_BE;
            case 3:
                return UTF_8;
            case 0:
            default:
                return ISO_8859_1;
            }
        }
    };

    public enum ID3v1Genre {
        Blues, Classic_Rock, Country, Dance, Disco, Funk, Grunge, Hip_Hop, Jazz, Metal, New_Age, Oldies, Other, Pop, R_and_B, Rap, Reggae, Rock, Techno, Industrial, Alternative, Ska, Death_Metal, Pranks, Soundtrack, Euro_Techno, Ambient, Trip_Hop, Vocal, Jazz_Funk, Fusion, Trance, Classical, Instrumental, Acid, House, Game, Sound_Clip, Gospel, Noise, AlternRock, Bass, Soul, Punk, Space, Meditative, Instrumental_Pop, Instrumental_Rock, Ethnic, Gothic, Darkwave, Techno_Industrial, Electronic, Pop_Folk, Eurodance, Dream, Southern_Rock, Comedy, Cult, Gangsta, Top_40, Christian_Rap, Pop_Funk, Jungle, Native_American, Cabaret, New_Wave, Psychadelic, Rave, Showtunes, Trailer, Lo_Fi, Tribal, Acid_Punk, Acid_Jazz, Polka, Retro, Musical, Rock_and_Roll, Hard_Rock;

        public byte toByte() {
            return (byte) this.ordinal();
        }

        @Override
        public String toString() {
            switch (this) {
            case Classic_Rock:
            case New_Age:
            case Death_Metal:
            case Sound_Clip:
            case Southern_Rock:
            case Top_40:
            case Christian_Rap:
            case Native_American:
            case New_Wave:
            case Acid_Punk:
            case Acid_Jazz:
            case Hard_Rock:
                return super.toString().replace("_", " ");
            case Hip_Hop:
            case Euro_Techno:
            case Trip_Hop:
            case Instrumental_Pop:
            case Instrumental_Rock:
            case Techno_Industrial:
            case Pop_Folk:
            case Lo_Fi:
                return super.toString().replace("_", "-");
            case R_and_B:
                return "R&B";
            case Jazz_Funk:
                return "Jazz+Funk";
            case AlternRock:
                return "Alternative Rock";
            case Pop_Funk:
                return "Pop/Funk";
            case Rock_and_Roll:
                return "Rock & Roll";
            default:
                return super.toString();
            }
        }

        public static ID3v1Genre fromString(String value) {
            ID3v1Genre ret = Other;
            for (ID3v1Genre genre : ID3v1Genre.values()) {
                if (genre.toString().equals(value)) {
                    ret = genre;
                    break;
                }
            }
            return ret;
        }

        public static ID3v1Genre fromByte(byte value) {
            try {
                return ID3v1Genre.values()[value];
            } catch (Exception e) {
                return Other;
            }
        }
    };

    public enum PictureType {
        Other, Icon_32_32, Icon, Cover_front, Cover_back, Leaflet_page, Media, Lead_artist, Artist, Conductor, Band, Composer, Lyricist, Recording_Location, During_recording, During_performance, Movie_screen_capture, A_bright_coloured_fish, Illustration, Band_logotype, Publisher_logotype;

        public byte toByte() {
            return (byte) this.ordinal();
        }

        @Override
        public String toString() {
            switch (this) {
            case Icon_32_32:
                return "32x32 pixels 'file icon'";
            case Icon:
                return "Other file icon";
            case Cover_front:
                return "Cover (front)";
            case Cover_back:
                return "Cover (back)";
            case Media:
                return "Media";
            case Lead_artist:
                return "Lead artist/lead performer/soloist";
            case Artist:
                return "Artist/performer";
            case Band:
                return "Band/Orchestra";
            case Lyricist:
                return "Lyricist/text writer";
            case Movie_screen_capture:
                return "Movie/video screen capture";
            case Band_logotype:
                return "Band/artist logotype";
            case Publisher_logotype:
                return "Publisher/Studio logotype";
            case Leaflet_page:
            case Recording_Location:
            case During_recording:
            case During_performance:
            case A_bright_coloured_fish:
                return super.toString().replace('_', ' ');
            case Other:
            case Conductor:
            case Composer:
            case Illustration:
            default:
                return super.toString();
            }
        }

        public static PictureType fromByte(byte value) {
            try {
                return PictureType.values()[value];
            } catch (Exception e) {
                return Cover_front;
            }
        }
    };

}
