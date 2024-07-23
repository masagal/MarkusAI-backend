package org.example.groupbackend.request;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice(assignableTypes = RequestController.class)
public class RequestExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Void> constraintViolationException(Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> notFoundException(NoSuchElementException e) {
        logger.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> catchUnexpectedException(Exception e) {
        logger.error("TYPE OF ERROR: {}", e.getClass().getName());
        logger.error("ERROR MESSAGE: {}", e.getMessage());
        return ResponseEntity.internalServerError().build();
    }
}
