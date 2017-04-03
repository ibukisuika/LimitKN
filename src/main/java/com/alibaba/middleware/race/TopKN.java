package com.alibaba.middleware.race;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * The entry point of LimitKN contest.
 *
 * @author kliner
 */
public class TopKN implements KNLimit {

    private final static String PATH = "/Users/kliner/work/LimitKN/data/";
    private final static int BUCKET_SIZE = 1024;
    private final static long MASK = (0x7FFFFFFFFFFFFFFFL) / BUCKET_SIZE;

    private int[] counter = new int[BUCKET_SIZE];
    private long[][] data = new long[BUCKET_SIZE][60000];
    private long[] ans = new long[100];

    @Override
    public void processTopKN(long k, int n) {
        try {
            init();
            findKN((int)k, n);
            System.out.println(Arrays.toString(ans));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        File f = new File(PATH + "config");
        if (f.exists()) {
            Reader reader = new Reader(PATH + "config");
            for (int i = 0; i < BUCKET_SIZE; i++) {
                counter[i] = reader.nextInt();
            }
        } else {
            preProcess();
        }
    }

    private void preProcess() throws IOException {
        preProcess(PATH + "TestIn.small");
        //preProcess("xx1");
        //preProcess("xx2");
        //preProcess("xx3");

        Writer writer = new Writer(PATH + "config");
        for (int i = 0; i < BUCKET_SIZE; i++) {
            writer.writeInt(counter[i]);
            Writer dataWritter = new Writer(PATH + "temp" + i);
            for (int j = 0; j < counter[i]; j++) {
                dataWritter.writeLong(data[i][j]);
            }
            dataWritter.close();
        }
        writer.close();
    }

    private void preProcess(String path) throws IOException {
        Reader reader = new Reader(path);
        long a;
        while ((a = reader.nextLong()) != -1) {
            int bucket = (int)(a / MASK);
            long remain = a % MASK;
            int i = counter[bucket];
            data[bucket][i] = remain;
            counter[bucket]++;
        }
    }

    private void findKN(int k, int n) throws IOException {
        int i = 0;
        while (k > counter[i]) {
            k -= counter[i];
            i++;
        }

        Reader reader = new Reader(PATH + "temp" + i);
        long[] innerData = reader.readLongs(counter[i]);
        Arrays.sort(innerData);
        for (int t = 0; t < n; t++) {
            if (t + k < counter[i]) {
                ans[t] = i * MASK + innerData[t + k];
            } else {
                i++;
                reader = new Reader(PATH + "temp" + i);
                innerData = reader.readLongs(counter[i]);
                Arrays.sort(innerData);
                k = -t;
                ans[t] = i * MASK + innerData[t + k];
            }
        }
    }

    public static void main(String[] args) throws IOException {

        //if (args.length != 2) {
        //    throw new InvalidParameterException("Please give the right paramaters 'k' and 'n'.");
        //}

        //String teamCode = "543611iu4h";
        //String middleFileName = "middleThing.txt";
        //File middleFile = new File(KNLimit.MIDDLE_DIR + teamCode + File.separator + middleFileName);
        //FileWriter fileWriter = new FileWriter(middleFile);
        //fileWriter.write("hello world");
        //fileWriter.close();

        long time = System.currentTimeMillis();
        TopKN demo = new TopKN();
        demo.processTopKN(100000, 100);
        System.out.println(System.currentTimeMillis() - time);
        //demo.processTopKN(Long.valueOf(args[0]), Integer.valueOf(args[1]));

    }

}
