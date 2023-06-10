package com.example.WebApplication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("photos/student_photos", registry);
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        // Resolve the upload directory path
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        // Handle relative directory paths
        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
        
        
        // Configure the resource handler to expose the directory
        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/"+ uploadPath + "/");
    }

}
