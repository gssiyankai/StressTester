package com.gregory.testing.utils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.gregory.testing.utils.StringUtils.join;

public final class FileUtils {

    private FileUtils() {
    }

    public static String readResourceLines(String path) throws URISyntaxException, IOException {
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(FileUtils.class.getResourceAsStream("/" + path));
        while (scanner.hasNext()) {
            lines.add(scanner.next());
        }
        return join("\n", (List) lines);
    }

    public static void writeToFile(String path, String content) throws IOException {
        File file = new File(path);
        file.getAbsoluteFile().getParentFile().mkdirs();
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            writer.write(content);
        }
    }

}
