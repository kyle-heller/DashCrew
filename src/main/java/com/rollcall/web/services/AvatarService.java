package com.rollcall.web.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvatarService {
    public List<String> getAvatarFiles() {
        List<String> avatarFiles = new ArrayList<>();
        try {
            // Define the directory path where your avatar files are located
            Path directoryPath = Paths.get("src/main/resources/static/assets/avatars");

            // Retrieve a list of file paths from the specified directory
            List<Path> filePaths = Files.list(directoryPath)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            // Extract the file names from the file paths
            for (Path filePath : filePaths) {
                avatarFiles.add(filePath.getFileName().toString());
            }
        } catch (IOException e) {
            // Handle exceptions, such as if the directory does not exist or cannot be accessed
            e.printStackTrace();
        }
        return avatarFiles;
    }
}
