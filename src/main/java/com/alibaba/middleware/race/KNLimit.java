package com.alibaba.middleware.race;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by wanshao on 2017/3/16.
 *
 * @author wanshao
 * @date 2017/03/16
 */
public interface KNLimit {


    // 数据文件的命名前缀
    String FILE_PREFIX = "KNLIMIT_";
    // 数据文件的命名后缀
    String FILE_SUFFIX = ".data";

    // 结果文件的命名
    String RESULT_NAME = "RESULT.rs";


    // 读取数据文件的目录(比赛时候使用)
    String DATA_DIR = "/home/admin/topkn-datafiles/";
    // 写出结果文件的目录（比赛时候使用）,PS: 请在topkn-resultfiles目录下以自己的teamCode名字来创建目录，再将结果文件RESULT.rs写入该目录
    String RESULT_DIR = "/home/admin/topkn-resultfiles/";


    //如果需要生成中间结果文件，请将中间结果写入/home/admin/middle/teamCode目录下，其中teamCode根据组别不同而不同
    String MIDDLE_DIR = "/home/admin/middle/";



    // 测试的时候可以换成自己的目录(测试使用)
    //String DATA_DIR = "/Users/wanshao/Projects/LimitKNDemo/";
    // 写出结果文件的目录(测试使用)
    //String RESULT_DIR = "/Users/wanshao/Projects/LimitKNDemo/";
    //String MIDDLE_DIR = "/Users/wanshao/work/middlewareTester/middle/";



    /**
     *
     * @param k
     * @param n
     */
    void processTopKN(long k,int n);



}
