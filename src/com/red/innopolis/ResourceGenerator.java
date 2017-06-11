package com.red.innopolis;

import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by _red_ on 11.06.17.
 */
public class ResourceGenerator {
    public static void generate(int filesCount, int numberCount, int lineCount) {
        for (int i = 0; i < filesCount; i++) {
            try {
                FileOutputStream out = new FileOutputStream(i + ".txt");
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
}
