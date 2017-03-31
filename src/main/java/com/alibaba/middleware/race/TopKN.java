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

    @Override
    public void processTopKN(long k, int n) {
        // TODO
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
