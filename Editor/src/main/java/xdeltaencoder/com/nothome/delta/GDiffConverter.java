/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xdeltaencoder.com.nothome.delta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author fm
 */
public class GDiffConverter extends GDiffPatcher {

    private final DiffWriter writer;

    public GDiffConverter(DiffWriter writer) {
        this.writer = writer;
    }

    @Override
    void append(int length, InputStream patch, OutputStream output) throws IOException {
        for (int i = 0; i < length; i++) {
            writer.addData((byte) patch.read());
        }
    }

    @Override
    void copy(long offset, int length, SeekableSource source, OutputStream output) throws IOException {
        writer.addCopy(offset, length);
    }

    @Override
    void flush(OutputStream os) throws IOException {
        writer.flush();
        writer.close();
    }


}
