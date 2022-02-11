package se.epochtimes.backend.text.model.header;

public enum Subject {
  EKONOMI();

  private final String print;

  Subject() {
    this.print = "EKONOMI";
  }

  public String getPrint() {
    return print;
  }
}
