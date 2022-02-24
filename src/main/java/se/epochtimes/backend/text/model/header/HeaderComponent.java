package se.epochtimes.backend.text.model.header;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

@Embeddable
public class HeaderComponent implements Serializable {

  @Serial
  private static final long serialVersionUID = 1801063019513355240L;

  private int subject;
  private int pubYear;
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
    return pubYear;
  }

  public String getVignette() {
    return vignette;
  }

  public int getSubject() {
    return subject;
  }

  public String getArticleId() {
    return articleId;
  }

  public void setSubject(Subject subject) {
    this.subject = subject.getCode();
  }

  public void setYear(int year) {
    this.pubYear = year;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    HeaderComponent that = (HeaderComponent) o;

    return new EqualsBuilder().append(pubYear, that.pubYear).append(subject, that.subject).append(vignette, that.vignette).append(articleId, that.articleId).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(subject).append(pubYear).append(vignette).append(articleId).toHashCode();
  }
}
