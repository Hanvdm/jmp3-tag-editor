package com.mscg.jID3tags.util;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Giuseppe Miscione
 */
public class ValueRepeatingInputStream extends InputStream {

    int maxSize;
    int pos;
    int value;

    public ValueRepeatingInputStream(int value, int maxSize) {
        this.value = value;
        this.maxSize = maxSize;
        this.pos = 0;
    }

    @Override
    public int read() throws IOException {
        if (pos < maxSize) {
            pos++;
            return value;
        } else {
            return -1;
        }
    }

}
