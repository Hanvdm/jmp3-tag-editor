A java application with a Swing GUI that allows to extract ID3v1 and ID3v2 mp3 tags from the filename of mp3 file or allows to rename the files using a specified pattern.
The application embeds the function to resample selected mp3 files using [LAME MP3 Encoder](http://lame.sourceforge.net/). This function currently works only on Windows (tested on Win 8/7/Vista/XP, 32bit and 64bit) and Linux (tested on Ubuntu 12.04 and 12.10, 32but and 64bit).
Application runs under Java 1.6 and 1.7. It seems to work also under Java 1.8, but it has not been extensively tested in this environment.

As Google Code downloads support policy has changed and it doesn't allow to upload files anymore, I'll link new version in this description:

# Downloads #
## [Version 1.2.4](https://jmp3-tag-editor.googlecode.com/svn/trunk/jMP3TagEditor/dist/jMP3TagEditor.zip) ##

|**Instructions**|To make it works on Linux system, unzip the package and make the file jMP3TagEditor.sh executable. On Windows, simply double click on jMP3TagEditor.bat.|
|:---------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------|
|**Changes**     |<ol><li>Fixings for bug that prevented some mp3 files to be operated upon;</li><li>Better error reporting in some cases;</li><li><i>(experts only)</i> Log4j configuration file is now located in the configuration folder and can be edited from there;</li>
<tr><td><b>Notes</b>    </td><td>If you have installed a previous version, please remove all old unpacked files and then unpack this archive in the same place in order to replace older dependancies libraries.</td></tr>