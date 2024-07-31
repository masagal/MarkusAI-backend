package tech.masagal.markusai.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = UserController.class)
public class ExceptionHandler {
    Logger logger = LogManager.getLogger();

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<String> catchUnexpectedException(Exception e) {
        logger.error("TYPE OF ERROR: {}", e.getClass().getName());
        logger.error("ERROR MESSAGE: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
