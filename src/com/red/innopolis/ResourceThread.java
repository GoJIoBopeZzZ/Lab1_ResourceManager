package com.red.innopolis;

import GUI.OrderForm;

import java.io.*;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by _red_ on 11.06.17.
 */
public class ResourceThread extends Thread{
    private static volatile boolean stopALL = false;
    private OrderForm frame;
    private String path;
    private String compiler;
    private String spliter;
    private int numbersOfThread;
    private volatile Map<String , Integer> map = new ConcurrentHashMap<>();

    ResourceThread(String path, OrderForm frame, int numbersOfThread, String compiler, String spliter) {
        this.path = path;
        this.compiler = compiler;
        this.spliter = spliter;
        this.numbersOfThread = numbersOfThread;
        this.frame = frame;
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

    private void processDirectory(File directory) {
        // Получаем список доступных файлов в указанной директории.
        File[] files = directory.listFiles();
        if (files == null) {
            System.out.println("Нет доступных файлов для обработки.");
            return;
        } else {
            System.out.println("Количество файлов для обработки: " + files.length);
        }

        // Непосредственно многопоточная обработка файлов.
        ExecutorService service = Executors.newFixedThreadPool(this.numbersOfThread);
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
                        Pattern p = Pattern.compile(this.compiler);
                        Matcher m = p.matcher(str.toString());

                        if (m.find()) {
                            System.out.println("Нашлась латиница!");
                            stopALL = true;

                        }

                        if (stopALL) break;
                        else {
                            String[] parts = str.split(spliter);

                            for (String part : parts) {

                                if (!map.containsKey(part)) {
                                    map.put(part, 1);
                                } else map.put(part, map.get(part) + 1);

                                this.frame.setTextArea(map);

                            }
                            str = reader.readLine();
                            Thread.sleep(500);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        // Новые задачи более не принимаем, и завершаем оставшиеся.
        if(!service.isShutdown()) {
            service.shutdown();
            // Ждем завершения
            try {
                service.awaitTermination(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        printOrder();
    }

    private synchronized void printOrder() {
        String leftAlignFormat = "| %-10s | %-9d |%n";

        System.out.format("+------------+-----------+%n");
        System.out.format("|   Words    |  Entries  |%n");
        System.out.format("+------------+-----------+%n");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.format(leftAlignFormat, "    " + entry.getKey(), entry.getValue());
            System.out.format("+------------+-----------+%n");
        }
    }
}
