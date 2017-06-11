package com.red.innopolis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        String path = "/Users/_red_/IdeaProjects/ResourceGenerator/src/Resources/";

//        ResourceGenerator.generate(path, 10, 10, 10);
        ResourceThread test = new ResourceThread(path , 5, "[a-zA-Z]", " ");
        test.start();
    }
}
