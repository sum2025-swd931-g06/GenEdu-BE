package com.genedu.media.utils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {

        String originalFilename = multipartFile.getOriginalFilename();

        // Generate a temporary file name based on the original filename
        String prefix = originalFilename != null ? originalFilename.replaceAll("\\.[^.]+$", "") : "upload";
        String suffix = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf('.'))
                : ".tmp";

        File file = File.createTempFile(prefix, suffix);
        file.deleteOnExit(); // Ensure the temporary file is deleted on exit

        // Write the contents of the MultipartFile to the temporary file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        return file;
    }
}
