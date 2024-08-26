package com.servustech.carehome.util;

import com.servustech.carehome.util.exception.DiskOperationException;
import org.imgscalr.Scalr;
import org.springframework.util.MimeTypeUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtil {

    public enum Size {
        M(300, 224);

        private int width;
        private int height;

        Size(int x, int y) {
            this.width = x;
            this.height = y;
        }
    }

    public static byte[] resizeImage(byte[] original, Size size, String contentType) {
        try {
            InputStream origInput = new ByteArrayInputStream(original);

            BufferedImage bufferedImage = ImageIO.read(origInput);

            BufferedImage out = Scalr.resize(bufferedImage, Scalr.Method.SPEED, size.width, size.height);

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            ImageIO.write(out, getType(contentType), outStream);

            byte[] result = outStream.toByteArray();

            // closing streams
            origInput.close();
            outStream.close();
            return result;

        } catch (IOException e) {

            throw new DiskOperationException("Not able to resize");
        }

    }

    private static String getType(String contentType) {
        if (contentType.equalsIgnoreCase(MimeTypeUtils.IMAGE_JPEG_VALUE)) {
            return "JPG";
        } else if (contentType.equalsIgnoreCase(MimeTypeUtils.IMAGE_PNG_VALUE)) {
            return "PNG";
        } else if (contentType.equalsIgnoreCase(MimeTypeUtils.IMAGE_GIF_VALUE)) {
            return "GIF";
        }
        return null;
    }

}
