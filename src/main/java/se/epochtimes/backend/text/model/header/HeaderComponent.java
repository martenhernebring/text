package se.epochtimes.backend.text.model.header;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class HeaderComponent implements Serializable {

  @Serial
  private static final long serialVersionUID = 1801063019513355240L;

  private Subject subject;
  private int year;
  private String vignette;
  private String articleId;

  //Jpa requirement
  public HeaderComponent() {}

  public HeaderComponent(Subject subject, int year, String vignette, String articleId) {
    setSubject(subject);
    setYear(year);
    setVignette(vignette);
    setArticleId(articleId);
  }

  private static String generateId() {
    return String.format("%04d", ThreadLocalRandom.current().nextInt(0, 9999 + 1));
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

  public String getArticleId() {
    return articleId;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public void setVignette(String vignette) {
    this.vignette = vignette.toLowerCase();
  }

  public void setArticleId(String articleId) {
    if(!articleId.isEmpty()) {
      this.articleId = articleId;
    } else {
      this.articleId = generateId();
    }
  }
}
