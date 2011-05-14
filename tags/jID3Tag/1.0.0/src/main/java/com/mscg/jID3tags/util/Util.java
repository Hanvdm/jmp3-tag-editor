package com.mscg.jID3tags.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.mscg.jID3tags.objects.frames.FrameLister;
import com.mscg.jID3tags.util.Costants.StringEncodingType;

/**
 *
 * @author Giuseppe Miscione
 */
public class Util {

    private static boolean framesLoaded = false;

    /**
     * Copies the data contained in the {@link InputStream} in the provided
     * {@link OutputStream}.
     *
     * @param in
     *            The {@link InputStream} from which data are read.
     * @param out
     *            The {@link OutputStream} in which data are writte.
     * @param bufferSize
     *            The size of the buffer used to copy the data.
     * @return The total number of bytes written.
     * @throws IOException
     *             If an error occurs while reading or writing data.
     */
    public static long copyStream(InputStream in, OutputStream out, int bufferSize) throws IOException {
        return copyStream(in, out, bufferSize, -1);
    }

    /**
     * Copies the data contained in the {@link InputStream} in the provided
     * {@link OutputStream}.
     *
     * @param in
     *            The {@link InputStream} from which data are read.
     * @param out
     *            The {@link OutputStream} in which data are writte.
     * @param bufferSize
     *            The size of the buffer used to copy the data.
     * @param maxSize
     *            The maximum number of bytes that will be read and transferred.
     *            If <code>maxSize &lt;= 0</code> all bytes are copied from the
     *            input stream.
     * @return The total number of bytes written.
     * @throws IOException
     *             If an error occurs while reading or writing data.
     */
    public static long copyStream(InputStream in, OutputStream out, int bufferSize, long maxSize) throws IOException {

        byte buffer[] = new byte[bufferSize];
        int bytesRead = 0;
        int bytesToWrite = 0;
        int bytesToRead = 0;
        long totalBytesRead = 0l;

        do {
            if (maxSize <= 0) {
                bytesToRead = buffer.length;
            } else {
                bytesToRead = Math.min(buffer.length, (int) (maxSize - totalBytesRead));
            }

            bytesRead = in.read(buffer, 0, bytesToRead);

            if (bytesRead > 0) {
                if (maxSize <= 0) {
                    bytesToWrite = bytesRead;
                } else {
                    bytesToWrite = Math.min(bytesRead, (int) (maxSize - totalBytesRead));
                }
                out.write(buffer, 0, bytesToWrite);
                totalBytesRead += bytesToWrite;
            }
            if (maxSize > 0 && totalBytesRead >= maxSize) {
                break;
            }
        } while (bytesRead > 0);
        out.flush();

        return totalBytesRead;
    }

    /**
     * Copies the data contained in the input object in the provided output
     * object. The input object must implement a <strong>
     * <code>int read(byte[], int, int)</code></strong> method and the output
     * object must implement a <strong><code>void write(byte[], int, int)</code>
     * </strong> one. Optionally, the output object can implement a <strong>
     * <code>void flush()</code></strong> method that will be called at the end
     * of the copy process.
     * <p>
     * This method will work if the input and output object are either
     * <code>(Input|Output)Stream</code>s or {@link RandomAccessFile}s.
     *
     * @param in
     *            The input object from which data are read.
     * @param out
     *            The output object in which data are writte.
     * @param bufferSize
     *            The size of the buffer used to copy the data.
     * @param maxSize
     *            The maximum number of bytes that will be read and transferred.
     *            If <code>maxSize &lt;= 0</code> all bytes are copied from the
     *            input stream.
     * @return The total number of bytes written.
     *
     * @throws IOException
     *             If an error occurs while reading or writing data.
     * @throws NoSuchMethodException
     *             If the requested method cannot be accessed on provided
     *             objects.
     * @throws IllegalAccessException
     *             If the requested method cannot be accessed on provided
     *             objects.
     * @throws IllegalArgumentException
     *             If the requested method cannot be accessed on provided
     *             objects.
     * @throws InvocationTargetException
     *             If the requested method cannot be accessed on provided
     *             objects.
     */
    public static long copyData(Object in, Object out, int bufferSize, long maxSize) throws IOException, NoSuchMethodException,
                                                                                    IllegalAccessException,
                                                                                    IllegalArgumentException,
                                                                                    InvocationTargetException {

        byte buffer[] = new byte[bufferSize];
        int bytesRead = 0;
        int bytesToWrite = 0;
        int bytesToRead = 0;
        long totalBytesRead = 0l;

        Method read = in.getClass().getMethod("read", buffer.getClass(), int.class, int.class);
        Method write = out.getClass().getMethod("write", buffer.getClass(), int.class, int.class);
        Method flush = null;
        try {
            flush = out.getClass().getMethod("flush");
        } catch (NoSuchMethodException e) {
        }

        do {
            if (maxSize <= 0) {
                bytesToRead = buffer.length;
            } else {
                bytesToRead = Math.min(buffer.length, (int) (maxSize - totalBytesRead));
            }

            bytesRead = (Integer) read.invoke(in, buffer, 0, bytesToRead);

            if (bytesRead > 0) {
                if (maxSize <= 0) {
                    bytesToWrite = bytesRead;
                } else {
                    bytesToWrite = Math.min(bytesRead, (int) (maxSize - totalBytesRead));
                }
                write.invoke(out, buffer, 0, bytesToWrite);
                totalBytesRead += bytesToWrite;
            }
            if (maxSize > 0 && totalBytesRead >= maxSize) {
                break;
            }
        } while (bytesRead > 0);
        if (flush != null)
            flush.invoke(out);

        return totalBytesRead;
    }

    /**
     * This method loads the frame types implementation classes so that they can
     * register their ids. The class names are provided by the default
     * implementation of {@link FrameLister}.
     */
    public static void registerFrameTypes() {
        registerFrameTypes(new FrameLister());
    }

    /**
     * This method loads the frame types implementation classes so that they can
     * register their ids. The class names are provided by the specified
     * {@link FrameLister}.
     *
     * @param lister
     *            The {@link FrameLister} which provides class names.
     */
    public static void registerFrameTypes(FrameLister lister) {
        for (String clazzName : lister.listFrames()) {
            Class clazz = null;
            try {
                clazz = Class.forName(clazzName, true, ClassLoader.getSystemClassLoader());
            } catch (Throwable e) {
            }

            if (clazz == null) {
                try {
                    clazz = Class.forName(clazzName, true, Util.class.getClassLoader());
                } catch (Throwable e) {
                }
            }
        }
        framesLoaded = true;
    }

    /**
     * Returns <code>true</code> if the frames has already been loaded by the
     * application. If this method returns <code>false</code>, a call to
     * {@link #registerFrameTypes()} or to
     * {@link #registerFrameTypes(FrameLister)} must be performed.
     *
     * @return <code>true</code> if the frames has already been loaded by the
     *         application, <code>false</code> otherwise.
     */
    public static boolean areFramesLoaded() {
        return framesLoaded;
    }

    /**
     * Converts the provided string into a byte array. If the string encoding is
     * {@link StringEncodingType#UTF_16} encoding, this method ensures that the
     * bytes are in little endian order, with a preceding BOM.
     *
     * @return The provided string converted into a byte array.
     * @throws UnsupportedEncodingException
     *             If the system doesn't support the selected encoding.
     */
    public static byte[] encodeString(String data, StringEncodingType encoding) throws UnsupportedEncodingException {
        ByteArrayOutputStream tmp = null;
        byte bom[] = {(byte) 0xFF, (byte) 0xFE};
        switch (encoding) {
        case UTF_16:
            // ensure that the string will be converted in UTF-16LE
            // with a preceding BOM
            tmp = new ByteArrayOutputStream();
            try {
                tmp.write(bom);
                tmp.write(data.getBytes("UTF-16LE"));
            } catch (IOException e) {
            }
            return tmp.toByteArray();
        default:
            return data.getBytes(encoding.toString());
        }
    }

    /**
     * Trims the legth of the provided string to the specified value. If the
     * string is shorter than the new length, it's left untouched, else only the
     * first <code>length</code> characters are returned.
     *
     * @param orig
     *            The original string.
     * @param length
     *            The max length of the returned string.
     * @return The trimmed string.
     */
    public static String trimToSize(String orig, int length) {
        if (orig == null)
            return "";
        if (orig.length() < length)
            return orig;
        else
            return orig.substring(0, length);
    }

    /**
     * If the provided object is an instance of {@link Flushable} and/or an
     * instance of {@link Closeable}, this method calls the methods
     * <code>flush</code> and <code>close</code> on the object in an
     * exeption-safe manner.
     *
     * @param obj
     *            The object that will be flushed and closed.
     */
    public static void flushAndClose(Object obj) {
        if (obj instanceof Flushable) {
            try {
                ((Flushable) obj).flush();
            } catch (Exception e) {
            }
        }

        if (obj instanceof Closeable) {
            try {
                ((Closeable) obj).close();
            } catch (Exception e) {
            }
        }
    }
}
