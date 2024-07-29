package org.example.groupbackend.chat.ai;

import org.example.groupbackend.request.RequestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ReadTemplate {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    public static List<String> readTemplate() {
        Path path = Paths.get("src/main/resources/template");
        logger.info("Path: {}", path);
        try {
            return Files.readAllLines(path);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
