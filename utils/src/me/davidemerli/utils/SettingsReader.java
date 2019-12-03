package me.davidemerli.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SettingsReader {

    public static List<String> getLinesFromFile(File file) throws IOException {
        List<String> list = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(file));

        //Ignores lines starting with '#'
        br.lines()
                .filter(s -> !s.startsWith("#"))
                .filter(s -> !s.isEmpty())
                .forEach(list::add);
        br.close();

        return list;
    }

    public static File getFileFromWorkingDir(String fileName) {
        return new File(System.getProperty("usr.dir"), fileName);
    }
}