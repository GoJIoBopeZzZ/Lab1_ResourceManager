package com.red.innopolis;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by _red_ on 11.06.17.
 */
public class ResourceGenerator {
    private static List<String> pathList = new ArrayList<>();

    public static void generate(String path, int filesCount, int numberCount, int lineCount) {
        for (int i = 0; i < filesCount; i++) {
            try {
                FileOutputStream out = new FileOutputStream(path + i + ".txt");
                pathList.add(path + i + ".txt");
                out.flush();

                for (int j = 0; j < lineCount; j++) {
                    StringBuffer str = new StringBuffer();
                    for (int k = 0; k < numberCount; k++)
                        str.append((new Integer(new Random().nextInt(100))).toString() + " ");

                    str.append("\n");
                    out.write(str.toString().getBytes());
                }

                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getPathList() {
        return pathList;
    }
}
