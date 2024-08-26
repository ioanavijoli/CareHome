package com.servustech.carehome.disk;

import com.servustech.carehome.util.exception.DiskOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

/***
 * Used for saving files on disk
 */
@Component
public class FileSystem {

    @Autowired
    private Environment env;

    /**
     * Generate the path for saving a new path
     * @param filename the unique filename
     * @return
     */

    public String generatePath(String filename) {

        Path path = Paths.get(env.getProperty("file.sys.disk.path"));
        int maxFiles = Integer.parseInt(env.getProperty("file.sys.max.files"));
        LocalDate date = LocalDate.now();
        int y = date.getYear();
        int m = date.getMonthValue();
        int d = date.getDayOfMonth();
        // directory separator
        String sep = File.separator;

        try {
            // if the initial path does not exist create it
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            // create baseDir using the year, month and day
            Path baseDir = Paths.get(path + sep + y + sep + m + sep + d);
            // if the base dir does not exists create it
            if (!Files.exists(baseDir)) {
                Path dir = Paths.get(baseDir + sep + "1");
                Files.createDirectories(dir);

                return dir + sep + filename;
            } else {
                // else parse it
                DirectoryStream<Path> list = Files.newDirectoryStream(baseDir);
                int i = 1;
                // find the dir that does not have maxFiles in yet
                for (Path a : list) {
                    if (Files.list(a).count() < maxFiles) {
                        return a + sep + filename;
                    }
                    i++;
                }

                // if no available dir found create a new one
                Path dir = Files.createDirectories(Paths.get(baseDir + sep + i));

                return dir + sep + filename;
            }
        } catch (IOException e) {
            throw new DiskOperationException("Not able to perform operations on disk");
        }

    }

    /**
     * save the content of a file at the given path
     * @param path
     * @param content
     */
    public void saveFile(String path, byte[] content) {

        try {
            Files.write(Paths.get(path), content);
        } catch (IOException e) {
            throw new DiskOperationException("Not able to perform save file operation");
        }

    }

    /**
     * Read the content found at the given path
     * @param path
     * @return
     */
    public byte[] readFile(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new DiskOperationException("Not able to perform read file operation");
        }
    }

    /**
     * Delete the file
     * @param path
     */
    public void deleteFile(String path) {
        try {
            Files.delete(Paths.get(path));
        } catch (IOException e) {
            throw new DiskOperationException("Not able to perform delete file operation");
        }
    }
}
