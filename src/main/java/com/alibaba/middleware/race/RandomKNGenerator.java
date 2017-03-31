package com.alibaba.middleware.race;

import java.util.Random;

/**
 * Created by wanshao on 2017/3/20.
 *
 * @author wanshao
 * @date 2017/3/20
 */
public class RandomKNGenerator {

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 5; i++) {
            long k = Math.abs(new Random(System.currentTimeMillis()).nextLong());
            int n = Math.abs((new Random(System.currentTimeMillis()).nextInt()) % 100);
            System.out.println("Round " + i + " 's (k,n) value os (" + k + "," + n + ")");
            Thread.sleep(2000);
        }
    }
}
