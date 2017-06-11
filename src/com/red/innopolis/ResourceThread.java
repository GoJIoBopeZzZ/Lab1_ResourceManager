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
    private String compiler; // Регулярное выражение для поиска в строке
    private String spliter; // Разделитель слов
    private int numbersOfThread; // Кол-во потоков
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

//      Непосредственно многопоточная обработка файлов. Создаем наш экзекутор
        ExecutorService service = Executors.newFixedThreadPool(this.numbersOfThread);

//      Проверем валидность файлов
        for (final File f : files) {
            if (!f.isFile()) {
                continue;
            }

//          Собственно наш пулл
            service.execute(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(f));
                    String str = reader.readLine(); // Построчно читаем файл f

                    while (str != null) {

//                        Используем паттерн для регулярного выражения
                        Pattern p = Pattern.compile(this.compiler);
                        Matcher m = p.matcher(str);

                        if (m.find()) {
                            System.out.println("Нашлась латиница!");
                            stopALL = true;
                        }

                        if (stopALL) break; // Для остановки остальных потоков
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
                    System.out.println("Ошибка чтения из файла!");
//                    e.printStackTrace();
                } catch (InterruptedException e) {
                    System.out.println("All threads are Interrupted!");
//                    e.printStackTrace();
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
//                e.printStackTrace();
                System.out.println("All threads are Interrupted!");
            }
        }

        printOrder(); // Печатаем всю мапу в консоль
    }

//    Метод для вывода мапы на консоль
    private synchronized void printOrder() {
        String leftAlignFormat = "| %-10s | %-9d |%n"; // Строка форматированного вывода
        System.out.format("+------------+-----------+%n");
        System.out.format("|   Words    |  Entries  |%n");
        System.out.format("+------------+-----------+%n");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.format(leftAlignFormat, "    " + entry.getKey(), entry.getValue());
            System.out.format("+------------+-----------+%n");
        }
    }
}
