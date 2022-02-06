package se.epochtimes.backend.text.model;

import java.util.Objects;

public class Paragraph {
  private final String paragraph;
  private Paragraph(Builder builder) {
    this.paragraph = builder.paragraph;
  }

  public static Builder wrap(String input) {
    return new Builder(input);
  }

  public String getParagraph() {
    return paragraph;
  }

  public record Builder(String paragraph) {
    public Builder(String paragraph) {
      this.paragraph = wrap(paragraph);
    }

    private String wrap(String article) {
      if (Objects.equals(article, null))
        return "";
      else
        return article.trim();
    }

    public Paragraph build() {
      return new Paragraph(this);
    }
  }
}
