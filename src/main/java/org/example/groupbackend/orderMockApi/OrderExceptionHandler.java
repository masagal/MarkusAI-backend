package org.example.groupbackend.orderMockApi;

import jakarta.validation.ConstraintViolationException;
import org.example.groupbackend.request.RequestController;
import org.example.groupbackend.request.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice(assignableTypes = OrderController.class)
public class OrderExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> notFoundException(NoSuchElementException e) {
        logger.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(RequestService.NotAuthorizedException.class)
    public ResponseEntity<Void> notFoundException(RequestService.NotAuthorizedException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> catchUnexpectedException(Exception e) {
        logger.error("TYPE OF ERROR: {}", e.getClass().getName());
        logger.error("ERROR MESSAGE: {}", e.getMessage());
        return ResponseEntity.internalServerError().build();
    }
}
