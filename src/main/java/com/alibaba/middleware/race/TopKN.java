package com.alibaba.middleware.race;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * The entry point of LimitKN contest.
 *
 * @author kliner
 */
public class TopKN implements KNLimit {

    static class Reader
    {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;
 
        public Reader(String file_name) throws IOException
        {
            din = new DataInputStream(new FileInputStream(file_name));
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }
 
        public String readLine() throws IOException
        {
            byte[] buf = new byte[64]; // line length
            int cnt = 0, c;
            while ((c = read()) != -1)
            {
                if (c == '\n')
                    break;
                buf[cnt++] = (byte) c;
            }
            return new String(buf, 0, cnt);
        }
 
        public long nextLong() throws IOException
        {
            long ret = 0;
            byte c = read();
            while (c != -1 && c != '\n')
                c = read();
            do {
                ret = ret * 10 + c - '0';
            }
            while ((c = read()) >= '0' && c <= '9');
            return ret;
        }

        private void fillBuffer() throws IOException
        {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1)
                buffer[0] = -1;
        }
 
        private byte read() throws IOException
        {
            if (bufferPointer == bytesRead)
                fillBuffer();
            return buffer[bufferPointer++];
        }
 
        public void close() throws IOException
        {
            if (din == null)
                return;
            din.close();
        }
        
        public long[] readLongs(int size) {
            long[] ret = new long[size];
            for (int i = 0; i < size; i++) {
                ret[i] = reader.nextLong();
            }
            return ret;
        }
    }
    

    @Override
    public void processTopKN(long k, int n) {
        // TODO
    }

    private final static int BUCKET_SIZE = 10000;
    private final static long MASK = 2 ^ 63 / BUCKET_SIZE + 1;

    private int[] counter = new int[BUCKET_SIZE];
    private long[][] data = new long[BUCKET_SIZE][60000];
    private long[] ans = new long[n];

    private void init() {
        File f = new File("config");
        if (f.exist()) {
            Reader reader = new Reader("config");
            for (int i = 0; i < BUCKET_SIZE; i ++) {
                counter[i] = reader.nextInt();
            }
        } else {
            preProcess();
        }
    }

    private void preProcess() {
        preProcess("xx0");
        preProcess("xx1");
        preProcess("xx2");
        preProcess("xx3");
        
        Writer writer = new Writer("config");
        for (int i = 0; i < BUCKET_SIZE; i++) {
            writer.writeInt(counter[i]);
        }
    }

    private void preProcess(String path) {
        Reader reader = new Reader(path);
        long a;
        while ( (a = reader.nextLong()) != -1 ) {
            int bucket = a / MASK;
            long remain = a % MASK;
            int i = counter[bucket];
            data[bucket][i] = remain;
            counter[bucket]++;
        }
    }

    private void findKN(int k, int n) {
        int i = 0;
        while (k > counter[i]) {
            k -= counter[i];
            i++;
        }

        Reader reader = new Reader("temp" + i);
        long[] innerData = reader.readLongs();
        Arrays.sort(innerData);
        for (int t = 0; t < n; t++) {
            if (t+k < counter[i]) {
                ans[t] = innerData[t+k];
            } else {
                i++;
                reader = new Reader("temp" + i);
                innerData = reader.readLongs();
                Arrays.sort(innerData);
                k = -t;
                t--;
            }
        }
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            throw new InvalidParameterException("Please give the right paramaters 'k' and 'n'.");
        }

        String teamCode = "543611iu4h";
        String middleFileName = "middleThing.txt";
        File middleFile = new File(KNLimit.MIDDLE_DIR + teamCode + File.separator + middleFileName);
        FileWriter fileWriter = new FileWriter(middleFile);
        fileWriter.write("hello world");
        fileWriter.close();

        TopKN demo = new TopKN();
        demo.processTopKN(Long.valueOf(args[0]), Integer.valueOf(args[1]));

    }

}
