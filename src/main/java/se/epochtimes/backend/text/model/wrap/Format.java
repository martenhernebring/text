package se.epochtimes.backend.text.model.wrap;

public enum Format {
  HEADLINE(12),
  LEAD(30),
  PARAGRAPH(33);

  private final int max;

  Format(int max) {
    this.max = max;
  }

  public int getMax() {
    return max;
  }
}