package se.epochtimes.backend.text.model.header;

import se.epochtimes.backend.text.model.Subject;

public class HeaderComponent {
  private final Subject subject;
  private final int year;

  public HeaderComponent(Subject subject, int year) {
    this.subject = subject;
    this.year = year;
  }

  public int getYear() {
    return year;
  }
}
