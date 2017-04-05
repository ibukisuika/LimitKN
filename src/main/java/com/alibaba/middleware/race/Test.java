package com.alibaba.middleware.race;

/**
 * Created by kliner on 05/04/2017.
 */
public class Test {
    private final static String DATA_PATH = "/Users/kliner/work/LimitKN/data/";
    private final static String TEMP_PATH = "/Users/kliner/work/LimitKN/data/";
    private final static String OUTPUT_PATH = "/Users/kliner/work/LimitKN/data/";

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        TopKN.DATA_PATH = DATA_PATH;
        TopKN.TEMP_PATH = TEMP_PATH;
        TopKN.OUTPUT_PATH = OUTPUT_PATH;

        TopKN demo = new TopKN();
        demo.processTopKN(100000, 100);
        System.out.println(System.currentTimeMillis() - time);
    }
}
