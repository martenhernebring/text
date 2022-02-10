package se.epochtimes.backend.text.exception;

public class AlreadySubmittedException extends RuntimeException {
  public AlreadySubmittedException(String message) {
    super(message);
  }
}
