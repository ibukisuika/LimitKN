package com.alibaba.middleware.race;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * The entry point of LimitKN contest.
 *
 * @author kliner
 */
public class TopKN implements KNLimit {
    private final static String TEAMCODE = "70124p8zo9";
    private final static int BUCKET_SIZE = 1 << 12;
    private final static long MASK = 1L << 51;
    private static long t;

    static String DATA_PATH = "/home/admin/topkn-datafiles/";
    static String TEMP_PATH = "/home/admin/middle/" + TEAMCODE + "/";
    static String OUTPUT_PATH = "/home/admin/topkn-resultfiles/" + TEAMCODE + "/";

    private int[] counter = new int[BUCKET_SIZE];
    private long[][] data = new long[BUCKET_SIZE][32768];
    private long[] ans = new long[100];
    private int[] tempCounter = new int[BUCKET_SIZE];
    private Writer[] dataWriter = new Writer[BUCKET_SIZE];

    @Override
    public void processTopKN(long k, int n) {
        try {
            init();
            System.out.println(System.currentTimeMillis() - t);
            findKN((int)k, n);
            System.out.println(System.currentTimeMillis() - t);
            output(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void output(int n) throws IOException {
        File f = new File(OUTPUT_PATH + "RESULT.rs");
        if (f.exists()) { f.delete(); }
        Writer writer = new Writer(OUTPUT_PATH + "RESULT.rs");
        for (int i = 0; i < n; i++) {
            writer.writeLong(ans[i]);
        }
        writer.close();
    }

    private void init() throws IOException {
        File f = new File(TEMP_PATH + "config");
        if (f.exists()) {
            Reader reader = new Reader(TEMP_PATH + "config");
            for (int i = 0; i < BUCKET_SIZE; i++) {
                counter[i] = reader.nextInt();
            }
            reader.close();
        } else {
            preProcess();
        }
    }

    /**
     * Read in the original data, process the data into buckets, and write the config file(to store the size of each
     * bucket)
     *
     * @throws IOException
     */
    private void preProcess() throws IOException {
        for (int i = 0; i < BUCKET_SIZE; i++) {
            dataWriter[i] = new Writer(TEMP_PATH + "temp" + i);
        }
        processOriginFile(DATA_PATH + "KNLIMIT_0.data");
        processOriginFile(DATA_PATH + "KNLIMIT_1.data");
        processOriginFile(DATA_PATH + "KNLIMIT_2.data");
        processOriginFile(DATA_PATH + "KNLIMIT_3.data");
        processOriginFile(DATA_PATH + "KNLIMIT_4.data");
        processOriginFile(DATA_PATH + "KNLIMIT_5.data");
        processOriginFile(DATA_PATH + "KNLIMIT_6.data");
        processOriginFile(DATA_PATH + "KNLIMIT_7.data");
        processOriginFile(DATA_PATH + "KNLIMIT_8.data");
        processOriginFile(DATA_PATH + "KNLIMIT_9.data");

        Writer writer = new Writer(TEMP_PATH + "config");
        for (int i = 0; i < BUCKET_SIZE; i++) {
            writer.writeInt(counter[i]);
            dataWriter[i].close();
        }
        writer.close();
    }

    /**
     * Process one original file into memory.
     *
     * @param path the file path.
     * @throws IOException
     */
    private void processOriginFile(String path) throws IOException {
        long t = System.currentTimeMillis();
        int bucket;
        long remain;
        int i, j;
        Arrays.fill(tempCounter, 0);
        Reader reader = new Reader(path);
        long a;
        while ((a = reader.nextLong()) != -1) {
            // according to bucket size
            bucket = (int)(a >> 51);
            remain = a & 0x7ffffffffffffL;
            i = tempCounter[bucket];
            data[bucket][i] = remain;
            tempCounter[bucket]++;
        }
        reader.close();
        System.out.println("read" + (System.currentTimeMillis() - t));

        // store data into temp file and global counter
        for (i = 0; i < BUCKET_SIZE; i++) {
            for (j = 0; j < tempCounter[i]; j++) {
                dataWriter[i].writeHexLong(data[i][j]);
            }
            counter[i] += tempCounter[i];
        }
        System.out.println("read + write" + (System.currentTimeMillis() - t));
    }

    /**
     * The find part.
     * Read from temp files according to the config, then do sort and find the TopKN.
     *
     * @param k
     * @param n
     * @throws IOException
     */
    private void findKN(int k, int n) throws IOException {
        int i = 0;
        while (k > counter[i]) {
            k -= counter[i];
            i++;
        }

        Reader reader = new Reader(TEMP_PATH + "temp" + i);
        long[] innerData = reader.readHexLongs(counter[i]);
        reader.close();
        Arrays.sort(innerData);
        for (int t = 0; t < n; t++) {
            if (t + k < counter[i]) {
                ans[t] = i * MASK + innerData[t + k];
            } else {
                i++;
                reader = new Reader(TEMP_PATH + "temp" + i);
                innerData = reader.readHexLongs(counter[i]);
                reader.close();
                Arrays.sort(innerData);
                k = -t;
                ans[t] = i * MASK + innerData[t + k];
            }
        }
    }

    /**
     * Entry point of the whole contest.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new InvalidParameterException("Please give the right paramaters 'k' and 'n'.");
        }
        t = System.currentTimeMillis();
        TopKN topKN = new TopKN();
        topKN.processTopKN(Long.valueOf(args[0]), Integer.valueOf(args[1]));
        System.out.println(System.currentTimeMillis() - t);
    }
}
