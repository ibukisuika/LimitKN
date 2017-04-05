package com.alibaba.middleware.race;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by kliner on 03/04/2017.
 *
 * @author kliner
 */
public class Writer {

    private FileWriter fileWriter;

    public Writer(String filename) throws IOException {
        fileWriter = new FileWriter(filename, true);
    }

    public void writeInt(int i) throws IOException {
        fileWriter.write(String.valueOf(i)+"\n");
    }

    public void close() throws IOException {
        fileWriter.close();
    }

    public void writeLong(long l) throws IOException {
        fileWriter.write(String.valueOf(l)+"\n");
    }
}
