package se.epochtimes.backend.text.exception;

public class ArticleNotFoundException extends RuntimeException {
  public ArticleNotFoundException(String message) {
    super(message);
  }
}
