package com.red.innopolis;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by _red_ on 11.06.17.
 */
public class ResourceThread extends Thread{
    private String path;
    private static String compiler;
    private static String spliter;
    private static int numbersOfThread;
    private static Map<String , Integer> map = new ConcurrentHashMap<>();

    ResourceThread(String path, int numbersOfThread, String compiler, String spliter) {
        this.path = path;
        ResourceThread.compiler = compiler;
        ResourceThread.spliter = spliter;
        ResourceThread.numbersOfThread = numbersOfThread;
    }

    @Override
    public void run() {
        try {
            File directory = new File(path);
            // Убедимся, что директория найдена и это реально директория, а не файл.
            if (directory.exists() && directory.isDirectory()) {
                processDirectory(directory);
            } else {
                System.out.println("Не удалось найти директорию по указанному пути.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processDirectory(File directory) {
        // Получаем список доступных файлов в указанной директории.
        File[] files = directory.listFiles();
        if (files == null) {
            System.out.println("Нет доступных файлов для обработки.");
            return;
        } else {
            System.out.println("Количество файлов для обработки: " + files.length);
        }

        // Непосредственно многопоточная обработка файлов.
        ExecutorService service = Executors.newFixedThreadPool(numbersOfThread);
        for (final File f : files) {
            if (!f.isFile()) {
                continue;
            }

            service.execute(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(f));
//                        int lines = 0;
                    String str = reader.readLine();

                    while (str != null) {
                        Pattern p = Pattern.compile(compiler);
                        Matcher m = p.matcher(str);
                        if(m.find()) return;

                        String[] parts = str.split(spliter);

                        for (String part : parts) {
                            if (!map.containsKey(part)) map.put(part, 1);
                            else map.put(part, map.get(part) + 1);
                        }

//                            ++lines;
                        str = reader.readLine();
                    }
//                        System.out.println("Поток: " + Thread.currentThread().getName().substring(7,15) +
//                                ". Файл: " + f.getName() + ". Количество строк: " + lines);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        // Новые задачи более не принимаем, выполняем только оставшиеся.
        service.shutdown();
        // Ждем завершения выполнения потоков не более 10 секунд.
        try {
            service.awaitTermination(0, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        printOrder();
    }

    private static void printOrder() {
        String leftAlignFormat = "| %-10s | %-9d |%n";

        System.out.format("+------------+-----------+%n");
        System.out.format("|   Words    |  Entries  |%n");
        System.out.format("+------------+-----------+%n");
        for (Map.Entry<String , Integer> entry : map.entrySet()) {
            System.out.format(leftAlignFormat,"    " + entry.getKey(), entry.getValue());
            System.out.format("+------------+-----------+%n");
        }
    }
}
