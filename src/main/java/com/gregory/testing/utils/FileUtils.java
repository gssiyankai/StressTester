package com.gregory.testing.utils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.gregory.testing.utils.StringUtils.join;

public final class FileUtils {

    private FileUtils() {
    }

    public static String readResourceLines(String path) throws URISyntaxException, IOException {
        Path templatePath = Paths.get(FileUtils.class.getResource("/" + path).toURI());
        List<String> lines = Files.readAllLines(templatePath, Charset.defaultCharset());
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
