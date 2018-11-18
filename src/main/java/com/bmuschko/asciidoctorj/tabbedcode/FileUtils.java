package com.bmuschko.asciidoctorj.tabbedcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public final class FileUtils {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private FileUtils() {}

    public static String readFileContentsFromClasspath(String path) {
        URL url = getURLForResource(path);
        return readText(url);
    }

    private static URL getURLForResource(String path) {
        return FileUtils.class.getResource(path);
    }

    private static String readText(URL url) {
        StringBuilder text = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                text.append(currentLine);
                text.append(LINE_SEPARATOR);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

        return text.toString();
    }
}
