package se.epochtimes.backend.text.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import se.epochtimes.backend.text.model.Subject;
import se.epochtimes.backend.text.model.Text;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class TextDTO implements Serializable {

  private final Subject subject;
  private final List<String> article;

  public TextDTO(Subject subject, List<String> article) {
    this.subject = subject;
    this.article = article;
  }

  public TextDTO(Text text) {
    this(text.getSubject(), text.getArticle());
  }

  public Subject getSubject() {
    return subject;
  }

  public List<String> getArticle() {
    return Collections.unmodifiableList(article);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TextDTO textDTO = (TextDTO) o;
    return new EqualsBuilder().append(article, textDTO.article).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(article).toHashCode();
  }
}
