package se.epochtimes.backend.text.model.header;

public enum Category {
  INRIKES(0);

  private final String print;
  private final int code;

  Category(int code) {
    this.code = code;
    this.print = "INRIKES";
  }

  public String getPrint() {
    return print;
  }
  public int getCode() {
    return code;
  }
  public static Category valueOf(int code) {
    for (Category value : Category.values()) {
      if (value.getCode() == code) {
        return value;
      }
    }
    throw new IllegalArgumentException("Invalid Employee Status code.");
  }
}
