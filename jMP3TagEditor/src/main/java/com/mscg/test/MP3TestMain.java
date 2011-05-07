package com.mscg.test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.mscg.jID3tags.exception.SynchsafeBadIntegerValueException;
import com.mscg.jID3tags.file.MP3File;
import com.mscg.jID3tags.objects.frames.ID3v2APICFrame;
import com.mscg.jID3tags.objects.frames.contents.ID3v2FrameContentImage;

/**
 *
 * @author Giuseppe Miscione
 */
public class MP3TestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SynchsafeBadIntegerValueException {

//        int value = Integer.parseInt("4DDD", 16);
//        SynchsafeInteger ssint = new SynchsafeInteger(value);
//        byte bytes[] = ssint.getBytes();
//        for(int i = 0; i < bytes.length; i++){
//            System.out.print(String.format("%02X", bytes[i]) + " ");
//        }

//        try {
//        SynchsafeInteger ssint = new SynchsafeInteger(65535);
//        byte bytes[] = ssint.getBytes();
//        for(int i = 0; i < bytes.length; i++){
//        System.out.print(String.format("%02X", bytes[i]) + " ");
//        }
//        System.out.println();
//        SynchsafeInteger ssint2 = new SynchsafeInteger(bytes);
//        System.out.println(ssint2.getIntValue());
//        } catch (SynchsafeIntegerBytesWrongLength ex) {
//        ex.printStackTrace();
//        } catch (SynchsafeIntegerBadMSB ex) {
//        ex.printStackTrace();
//        } catch (SynchsafeBadIntegerValueException ex) {
//        ex.printStackTrace();
//        }

//        //File mp3 = new File("C:/Users/Giuseppe Miscione/Music/Nickelback/" +
//        //        "Dark Horse/01 - Something in your mouth.mp3");
//        File mp3 = new File("./out.mp3");
//        FileInputStream fis = null;
//
//        File mp3Out = new File("./out2.mp3");
//        FileOutputStream fos = null;
//
//        ID3v2Tag tag = null;
//        try{
//            fis = new FileInputStream(mp3);
//            tag = ID3v2Tag.Factory.parse(fis);
//
//            System.out.println("Tag version: " + tag.getMajorVersion() + ", " + tag.getMinorVersion());
//            System.out.println("Tag unpadded size: " + tag.getFramesSize());
//            System.out.println("Tag padded size: " + tag.getSize());
//
//            for(ID3v2Frame frame : tag.getAllframes()){
//                ID3v2GenericStringFrame stringFrame = (ID3v2GenericStringFrame) frame;
//                stringFrame.getFrameStringContent().setEncoding(StringEncodingType.ISO_8859_1);
//                System.out.println(stringFrame);
//            }
//
//            /*
//            ID3v2GenericStringFrame album = (ID3v2GenericStringFrame)tag.getFrame(ID3v2TALBFrame.id);
//            album.getFrameStringContent().setContent("Nuovo valore per l'album");
//
//            tag.getFlags().setExperimentalIndicator(true);
//            tag.getFlags().setExtendedHeader(true);
//            tag.getFlags().setFooterPresent(true);
//            tag.getFlags().setUnsynchronisation(true);
//            */
//
//            tag.setDeclaredSize(tag.getFramesSize());
//            System.out.println("Tag unpadded size: " + tag.getFramesSize());
//            System.out.println("Tag padded size: " + tag.getSize());
//
//            fos = new FileOutputStream(mp3Out);
//            tag.writeToStream(fos);
//
//            Util.copyStream(fis, fos, 10240);
//
//        } catch(Exception e){
//            e.printStackTrace();
//        } finally{
//            if(fis != null){
//                try{
//                    fis.close();
//                } catch(IOException e){}
//            }
//
//            if(fos != null){
//                try{
//                    fos.close();
//                } catch(IOException e){}
//            }
//        }

//        RandomAccessFile mp3 = null;
//        try{
//            mp3 = new RandomAccessFile("./out.mp3", "rw");
//            ID3v11Tag tag = null;
//
//            try{
//                tag = ID3v11Tag.Factory.parse(mp3);
//
//                if(tag.isValid()){
//                    System.out.println("Title:   " + tag.getTitle());
//                    System.out.println("Artist:  " + tag.getArtist());
//                    System.out.println("Album:   " + tag.getAlbum());
//                    System.out.println("Year:    " + tag.getYear());
//                    System.out.println("Comment: " + tag.getComment());
//                    System.out.println("Track:   " + tag.getTrack());
//                    System.out.println("Genre:   " + tag.getGenre());
//
//                    //tag.setAlbum("Nuovo valore per l'album èé");
//                    //tag.writeToFile(mp3);
//
//                }
//                else{
//                    System.out.println("The tag is not valid");
//                }
//            } catch(ID3v1Exception e){
//                // the file doesn't have a tag, create a new one
//                InputStreamReader reader = new InputStreamReader(System.in);
//                BufferedReader input = new BufferedReader(reader);
//                System.out.print("Insert title: ");
//                String title = input.readLine();
//                System.out.print("Insert artist: ");
//                String artist = input.readLine();
//                System.out.print("Insert album: ");
//                String album = input.readLine();
//                System.out.print("Insert year: ");
//                String year = input.readLine();
//                System.out.print("Insert comment: ");
//                String comment = input.readLine();
//
//                tag = new ID3v11Tag(title, artist, album, comment, year, (byte)1, ID3v1Genre.Rock);
//
//                tag.writeToFile(mp3);
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally{
//            if(mp3 != null){
//                try{
//                    mp3.close();
//                } catch(IOException e){}
//            }
//        }

//        for(ID3v1Genre genre : ID3v1Genre.values()){
//            System.out.println(genre);
//        }

//        MP3File mp3 = new MP3File("./files/Jet - 01 - L'esprit d'escalier.mp3");
//        if(mp3.hasID3v2tag()){
//            System.out.println("File has an ID3v2 tag.");
//            ID3v2Tag v2tag = mp3.getID3v2tag();
//            System.out.println("Tag version:       " + v2tag.getMajorVersion()
//                    + "." + v2tag.getMinorVersion());
//            System.out.println("Tag unpadded size: " + v2tag.getFramesSize());
//            System.out.println("Tag padded size:   " + v2tag.getSize());
//            System.out.println("Tag frames:");
//            int i = 1;
//            for(ID3v2Frame frame : v2tag.getAllframes()){
//                System.out.println("" + i + ") " + frame.toString());
//                i++;
//            }
//        }
//        System.out.println("---------------------------------------------------");
//        if(mp3.hasID3v11tag()){
//            System.out.println("File has an ID3v11 tag.");
//            ID3v11Tag v11tag = mp3.getID3v11tag();
//            System.out.println("Title:   " + v11tag.getTitle());
//            System.out.println("Artist:  " + v11tag.getArtist());
//            System.out.println("Album:   " + v11tag.getAlbum());
//            System.out.println("Year:    " + v11tag.getYear());
//            System.out.println("Comment: " + v11tag.getComment());
//            System.out.println("Track:   " + v11tag.getTrack());
//            System.out.println("Genre:   " + v11tag.getGenre());
//        }

//        try{
//            MP3File mp3 = new MP3File("./files/out.mp3");
//            mp3.setTitle(mp3.getTitle() + " modificato");
//            mp3.write();
//        } catch(Exception e){
//            e.printStackTrace();
//        }

        OutputStream os = null;
        try {
            MP3File mp3 = new MP3File("./files/Jet - 01 - L'esprit d'escalier.mp3");
            ID3v2APICFrame imageFrame = (ID3v2APICFrame)mp3.getID3v2tag().getFrame(ID3v2APICFrame.id);
            ID3v2FrameContentImage imageContent = (ID3v2FrameContentImage)imageFrame.getContent();
            File outImage = new File("./files/cover_image.jpg");
            os = new FileOutputStream(outImage);
            IOUtils.copy(new ByteArrayInputStream(imageContent.getPictureData()), os);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }
}
