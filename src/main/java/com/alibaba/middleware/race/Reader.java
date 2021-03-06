package com.alibaba.middleware.race;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by kliner on 03/04/2017.
 */
public class Reader {
    final private int BUFFER_SIZE = 1 << 16;
    private DataInputStream din;
    private byte[] buffer;
    private int bufferPointer, bytesRead;

    public Reader(String filename) throws IOException {
        din = new DataInputStream(new FileInputStream(filename));
        buffer = new byte[BUFFER_SIZE];
        bufferPointer = bytesRead = 0;
    }

    public String readLine() throws IOException {
        byte[] buf = new byte[64]; // line length
        int cnt = 0, c;
        while ((c = read()) != -1) {
            if (c == '\n') { break; }
            buf[cnt++] = (byte)c;
        }
        return new String(buf, 0, cnt);
    }

    public int nextInt() throws IOException {
        int ret = 0;
        byte c = read();
        if (c == -1) { return -1; }
        do {
            ret = ret * 10 + c - '0';
        }
        while ((c = read()) >= '0' && c <= '9');
        return ret;
    }

    public long nextHexLong() throws IOException {
        long ret = 0;
        byte c = read();
        if (c == -1) { return -1; }
        ret = c & 0xff;
        for (int i = 0; i < 7; i++) {
            c = read();
            ret = (ret << 8) + (c & 0xff);
        }
        return ret;
    }

    public long nextLong() throws IOException {
        long ret = 0;
        byte c = read();
        if (c == -1) { return -1; }
        do {
            ret = ret * 10 + c - '0';
        }
        while ((c = read()) >= '0' && c <= '9');
        return ret;
    }

    private void fillBuffer() throws IOException {
        bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
        if (bytesRead == -1) { buffer[0] = -1; }
    }

    private byte read() throws IOException {
        if (bufferPointer == bytesRead) { fillBuffer(); }
        return buffer[bufferPointer++];
    }

    public void close() throws IOException {
        if (din == null) { return; }
        din.close();
    }

    public long[] readHexLongs(int size) throws IOException {
        long[] ret = new long[size];
        for (int i = 0; i < size; i++) {
            ret[i] = nextHexLong();
        }
        return ret;
    }
}

