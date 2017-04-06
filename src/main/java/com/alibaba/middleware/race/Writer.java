package com.alibaba.middleware.race;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by kliner on 03/04/2017.
 *
 * @author kliner
 */
public class Writer {

    private OutputStream fileWriter;
    private byte[] longBuffer = new byte[8];

    public Writer(String filename) throws IOException {
        fileWriter = new BufferedOutputStream(new FileOutputStream(filename, true));
    }

    public void writeInt(int i) throws IOException {
        fileWriter.write((String.valueOf(i)+"\n").getBytes());
    }

    public void close() throws IOException {
        fileWriter.close();
    }

    public void writeHexLong(long l) throws IOException {
        longBuffer[0] = (byte)(l >> 56);
        longBuffer[1] = (byte)((l >> 48) & 0xff);
        longBuffer[2] = (byte)((l >> 40) & 0xff);
        longBuffer[3] = (byte)((l >> 32) & 0xff);
        longBuffer[4] = (byte)((l >> 24) & 0xff);
        longBuffer[5] = (byte)((l >> 16) & 0xff);
        longBuffer[6] = (byte)((l >> 8) & 0xff);
        longBuffer[7] = (byte)(l & 0xff);
        fileWriter.write(longBuffer, 0, 8);
    }

    public void writeLong(long l) throws IOException {
        fileWriter.write((String.valueOf(l)+"\n").getBytes());
    }
}
