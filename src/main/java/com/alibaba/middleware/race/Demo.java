package com.alibaba.middleware.race;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理top(k,n)问题的DEMO
 *
 *
 *
 * Created by wanshao on 2017/3/16.
 *
 * @author wanshao
 * @date 2017/03/16
 */
public class Demo implements KNLimit {

    // /home/admin/tempFiles/
    // /Users/wanshao/Projects/LimitKNDemo/tempFiles/
    private final String SORTED_DIR = "/home/admin/tempFiles/";

    @Override
    public void processTopKN(long k, int n) {

        // 从有序文件中查找第k大的行，每次遍历一个文件，该值都要做调整,count是相对的第k大的值
        long count = k;

        try {
            List<File> realSortedFileList = getRealSortedFileList(SORTED_DIR);
            // 保存行记录
            List<Long> recordsList;

            // 保存结果集
            List<Long> resultList = new ArrayList<>();

            for (int t = 0; t < realSortedFileList.size(); t++) {
                // 取得文件
                File file = realSortedFileList.get(t);

                recordsList = getRecordsList(file);

                // 获取有序小文件中的记录数
                int lines = recordsList.size();

                if (count > recordsList.size()) {
                    count -= lines;
                    continue;
                } else {

                    // 当找到所在的文件时，count的值一定比Integer.MAX_VALUE要小。我们这里确保小文件按行拆分，且行数比Integer.MAX_VALUE要小。
                    int intCount = (int) count;

                    // 遍历该文件，将符合条件的行记录写出到结果list当中

                    // 如果从intCount这个索引开始的剩余元素个数小于n或者等于n，说明存在跨文件。等于n说明intCount所在的索引位置是k的值
                    if (recordsList.size() - intCount <= n) {

                        // 首先读取完这个文件所有的记录
                        for (int i = intCount; i < recordsList.size(); i++) {

                            // 打印测试下
                            System.out.println(recordsList.get(i));
                            resultList.add(recordsList.get(i));
                        }

                        // 剩余的n-recordsList.size()-intCount个结果在下个文件。
                        // 剩余元素个数。这里recordsList.size()-intCount表示前面的文件已经获得的结果数
                        int remainRecords = n - (recordsList.size() - intCount);

                        File nextFile = realSortedFileList.get(t + 1);
                        List<Long> nextRecordsList = getRecordsList(nextFile);

                        for (int u = 0; u < remainRecords; u++) {
                            // 打印测试下
                            System.out.println(nextRecordsList.get(u));
                            resultList.add(nextRecordsList.get(u));
                        }

                    } else {

                        for (int i = intCount; i < (intCount+n); i++) {

                            System.out.println(recordsList.get(i));
                            resultList.add(recordsList.get(i));

                            // 如果取到了指定的n个数，就退出循环
                            if (resultList.size() == n) {
                                break;
                            }
                        }
                    }

                    // 将结果集写出，并且退出循环
                    outputResults(resultList);
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 调用一次，将一轮计算得到的所有结果记录写出到一个结果文件
     *
     * @param resultList 保存结果记录
     */
    private void outputResults(List<Long> resultList) {
        try {
            FileWriter fileWriter = new FileWriter(
                KNLimit.RESULT_DIR + KNLimit.RESULT_NAME);
            for (Long value : resultList) {
                fileWriter.append(String.valueOf(value) + "\n");
            }

            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Long> getRecordsList(File file) {
        List<Long> recordsList = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                recordsList.add(Long.valueOf(line));
            }

            // 关闭流
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recordsList;
    }

    /**
     * @return 读取所有全局有序的文件
     */

    public List<File> getRealSortedFileList(String path) throws IOException {
        List<File> fileList = new ArrayList<>();

        File sortDir = new File(path);

        File[] files = sortDir.listFiles();

        for (int i = 0; i < files.length; i++) {
            fileList.add(files[i]);
        }

        return fileList;
    }

    public static void main(String[] args) throws IOException {

        //如果没有传递2个参数则提示出错
        if(args.length!=2){
            throw new InvalidParameterException("Please give the right paramaters 'k' and 'n'.");
        }else {
            System.out.println("传递的k为："+Long.valueOf(args[0]));
            System.out.println("传递的n为："+Integer.valueOf(args[1]));
        }


        //产生中间结果，写指定目录,指定目录下，新建以自己的teamCode命名的目录，在其中存储自己的中间结果
        String teamCode = "543611iu4h";
        String middleFileName = "middleThing.txt";
        File middleFile = new File(KNLimit.MIDDLE_DIR+teamCode+File.separator+middleFileName);
        FileWriter fileWriter = new FileWriter(middleFile);
        fileWriter.write("hello world");
        fileWriter.close();


        Demo demo = new Demo();
        demo.processTopKN(Long.valueOf(args[0]), Integer.valueOf(args[1]));

    }

}
