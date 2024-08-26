package com.servustech.carehome.util;

public class FileUtils {

    public static String getExtension(String fileName) {
        String extension = "";

        if (fileName != null) {
            String[] parts = fileName.split("\\.");
            if (parts.length >= 2) {
                extension = parts[parts.length - 1];
            }
        }
        return extension;
    }
}
