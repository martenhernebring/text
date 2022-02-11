package se.epochtimes.backend.text.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {
  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<StandardError> conflict(ConflictException e) {
    HttpStatus status = HttpStatus.CONFLICT;
    StandardError err = new StandardError(status.value(), "Conflict", e.getMessage());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(ArticleNotFoundException.class)
  public ResponseEntity<StandardError> notFound(ArticleNotFoundException e) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    StandardError err = new StandardError(status.value(), "Not found", e.getMessage());
    return ResponseEntity.status(status).body(err);
  }
}
