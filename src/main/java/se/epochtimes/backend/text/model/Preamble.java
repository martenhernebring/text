package se.epochtimes.backend.text.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
      int length = words.length;
      int i = length;
      int width = 0;
      StringBuilder sb = new StringBuilder("");
      while(i > 0) {
        String w = words[length - i];
        width += w.length() + 1;
        if(width > max) {
          sb.append(NL);
          width = 0;
        } else {
          sb.append(" ");
        }
        sb.append(w);
        i--;
      }
      return sb.toString().trim();
    }

    public Preamble build() {
      return new Preamble(this);
    }
  }
}
