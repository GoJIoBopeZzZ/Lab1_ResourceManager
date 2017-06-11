package com.red.innopolis;

import GUI.OrderForm;

public class Main {

    public static void main(String[] args) {
        String path = "/Users/_red_/IdeaProjects/ResourceGenerator/src/Resources/";

        OrderForm frame = new OrderForm("Order Form");
        Integer threads = 5;

//        ResourceGenerator.generate(path, threads, 100, 100); // Генератор файлов

//        [a-zA-Z] - регулярное выражение для поиска латиницы! " " - разделитель
        ResourceThread test = new ResourceThread(path , frame, threads, "[a-zA-Z]",
                "[,;:.!?@#$%^&*():;\\s]+");
        test.start();
    }
}
