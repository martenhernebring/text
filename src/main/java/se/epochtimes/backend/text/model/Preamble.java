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
      String[] words = article.split("\\s+");
      StringBuilder sb = new StringBuilder();
      int l = words.length;
      for(int i = l, width = 0; i > 0; i--) {
        String w = words[l - i];
        int wl = w.length();
        width += wl + 1;
        if(width > max) {
          sb.append(NL);
          width = wl;
        } else {
          sb.append(" ");
        }
        sb.append(w);
      }
      return sb.toString().trim();
    }

    public Preamble build() {
      return new Preamble(this);
    }
  }
}
