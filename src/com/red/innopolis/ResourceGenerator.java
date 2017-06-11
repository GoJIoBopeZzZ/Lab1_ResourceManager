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

//    Метод генерации файлов с именами 0 - filesNumber.txt
    static void generate(String path, int filesNumber, int linesNumber, int numbers) {
        for (int i = 0; i < filesNumber; i++)
            try {
                FileOutputStream out = new FileOutputStream(path + i + ".txt");
                pathList.add(path + i + ".txt");
                out.flush();

                for (int j = 0; j < linesNumber; j++) {
                    StringBuilder str = new StringBuilder();

                    for (int k = 0; k < numbers; k++)
//                        Генерируем число в диапазоне 0-9
                        str.append((new Integer(new Random().nextInt(10))).toString()).append(" ");

                    str.append("\n");
                    out.write(str.toString().getBytes());
                }

                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public List<String> getPathList() {
        return pathList;
    }
}
