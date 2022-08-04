package util.extra;

import org.apache.commons.io.FileUtils;
import util.Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;

public class Image {
    public static String encode(String path) {
        if (path != null) {
            File image = new File(path);
            if (image.exists()) {
                try {
                    byte[] bytes = Files.readAllBytes(image.toPath());
                    return Base64.getEncoder().encodeToString(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static byte[] decode(String encoded) {
        if (encoded != null) {
            return Base64.getDecoder().decode(encoded);
        }
        return null;
    }



}
