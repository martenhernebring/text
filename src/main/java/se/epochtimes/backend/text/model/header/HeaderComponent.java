package se.epochtimes.backend.text.model.header;

public class HeaderComponent {
  private final Subject subject;
  private final int year;
  private final String vignette;

  public HeaderComponent(Subject subject, int year, String vignette) {
    this.subject = subject;
    this.year = year;
    this.vignette = vignette;
  }

  public int getYear() {
    return year;
  }

  public String getVignette() {
    return vignette;
  }

  public Subject getSubject() {
    return subject;
  }
}
