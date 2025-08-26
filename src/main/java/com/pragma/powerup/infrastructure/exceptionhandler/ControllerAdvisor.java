package com.pragma.powerup.infrastructure.exceptionhandler;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import java.util.Collections;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    }
    return ResponseEntity.status(status).body(Collections.singletonMap(MESSAGE, msg));
  }
}
