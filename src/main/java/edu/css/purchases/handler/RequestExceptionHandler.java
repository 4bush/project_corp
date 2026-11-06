package edu.css.purchases.handler;

import edu.css.purchases.domain.DuplicateRequest;
import edu.css.purchases.domain.RequestNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RequestExceptionHandler {

  @ExceptionHandler(RequestNotFoundException.class)
  public ResponseEntity<ErrorResponse> notFound(
      RequestNotFoundException exception, HttpServletRequest request) {
    return error(HttpStatus.NOT_FOUND, exception.getMessage(), request);
  }

  @ExceptionHandler(DuplicateRequest.class)
  public ResponseEntity<ErrorResponse> duplicate(
      DuplicateRequest exception, HttpServletRequest request) {
    return error(HttpStatus.CONFLICT, exception.getMessage(), request);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> badRequest(
      IllegalArgumentException exception, HttpServletRequest request) {
    return error(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> validationFailed(
      MethodArgumentNotValidException exception, HttpServletRequest request) {
    String message = exception.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining("; "));
    return error(HttpStatus.BAD_REQUEST, message, request);
  }

  private ResponseEntity<ErrorResponse> error(
      HttpStatus status, String message, HttpServletRequest request) {
    return ResponseEntity.status(status).body(new ErrorResponse(
        Instant.now(), status.value(), status.getReasonPhrase(), message, request.getRequestURI()));
  }
}
