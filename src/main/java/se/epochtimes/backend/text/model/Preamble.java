package se.epochtimes.backend.text.model;

import java.util.Objects;

public class Preamble {
  public static final String NL = System.lineSeparator();
  private final String paragraph;

  private Preamble(Builder builder) {
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
      int max = 31;
      
      if (article.length() > max) {
        String[] words = article.split("\\s+");
        String w = words[0];
        int width = w.length();
        StringBuilder sb = new StringBuilder(words[0]);
        w = words[1];
        width += w.length() + 1;
        if (width > max) {
          sb.append(NL);
          sb.append(words[1]);
          return sb.toString().trim();
        }
        sb.append(" ");
        sb.append(words[1]);
        w = words[2];
        width += w.length() + 1;
        if (width > max) {
          sb.append(NL);
          sb.append(words[2]);
          return sb.toString().trim();
        }
        sb.append(" ");
        sb.append(words[2]);
        w = words[3];
        width += w.length() + 1;
        if (width > max) {
          sb.append(NL);
          sb.append(words[3]);
        } else {
          sb.append(" ");
          sb.append(words[3]);
          sb.append(NL);
          sb.append(words[4]);
        }
        return sb.toString().trim();
      } else
        return article.trim();
    }

    public Preamble build() {
      return new Preamble(this);
    }
  }
}
