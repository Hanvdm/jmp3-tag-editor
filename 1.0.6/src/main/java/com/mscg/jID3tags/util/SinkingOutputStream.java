package com.mscg.jID3tags.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Giuseppe Miscione
 */
public class SinkingOutputStream extends OutputStream {

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }

    @Override
    public void write(byte[] b) throws IOException {

    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {

    }

}
