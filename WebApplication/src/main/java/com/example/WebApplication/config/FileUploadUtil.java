package com.example.WebApplication.config;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static void saveFileinDir(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        // Check if the upload directory exists, create it if it doesn't
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);

            // Copy the file to the specified upload directory
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioException){
            throw new IOException("Impossible d'enregistrer l'image: "+ fileName, ioException);
        }

        // Alternative implementation using OutputStream:
        /*
        File uploadDirfile = new File(uploadDir);
        if (!uploadDirfile.exists()){
            uploadDirfile.mkdir();
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = new File(uploadDir + fileName);

        try {
            inputStream = multipartFile.getInputStream();
            if (!file.exists()) file.createNewFile();

            outputStream = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, read);
            }
        }catch (IOException ioException){
            throw new IOException("could not save image file: "+ fileName, ioException);
        }
        */
    }

}
