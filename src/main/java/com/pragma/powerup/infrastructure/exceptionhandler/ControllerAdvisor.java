package com.pragma.powerup.infrastructure.exceptionhandler;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor {

  private static final String MESSAGE = "message";

  @ExceptionHandler(NoDataFoundException.class)
  public ResponseEntity<Map<String, String>> handleNoDataFoundException(
      NoDataFoundException ignoredNoDataFoundException) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NO_DATA_FOUND.getMessage()));
  }

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<Map<String, String>> handleDomainException(DomainException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String msg = ex.getMessage() == null ? "Domain error" : ex.getMessage();
    if (msg.contains("NIT already exists")) {
      status = HttpStatus.CONFLICT;
    } else if (msg.contains("OWNER role required")) {
      status = HttpStatus.NOT_FOUND;
    } else if (msg.toLowerCase().contains("not found")) {
      status = HttpStatus.NOT_FOUND;
    } else if (msg.toLowerCase().contains("only the restaurant owner can")) {
      status = HttpStatus.FORBIDDEN;
    }
    return ResponseEntity.status(status).body(Collections.singletonMap(MESSAGE, msg));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    errors.put(MESSAGE, "Validation failed");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }
}
