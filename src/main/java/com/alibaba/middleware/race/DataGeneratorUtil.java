package com.alibaba.middleware.race;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * 生成10个文件，每个文件大小为2G Created by wanshao on 2017/3/16.
 *
 * @author wanshao
 * @date 2017/03/16
 */
public class DataGeneratorUtil {

    // 每个小文件的大小，比赛规定是每个文件大小1G。自己测试可以生成稍微小点的数据，例如100MB
    private static final long FILE_SIZE = 1*1024*1024*1024L;



    public static void main(String[] args) throws IOException {

        Random random = new Random(System.currentTimeMillis());

        for (int i = 0; i < 10; i++) {
            // 写出的文件名
            String fileName = KNLimit.FILE_PREFIX + i+KNLimit.FILE_SUFFIX;
            File file = new File(KNLimit.DATA_DIR+fileName);
            FileWriter fileWriter = new FileWriter(file);
            while (file.length() < FILE_SIZE) {

                Long randomValue = Math.abs(random.nextLong());
                fileWriter.append(String.valueOf(randomValue) + "\n");
            }

            fileWriter.close();
        }

    }


}
