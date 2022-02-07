package se.epochtimes.backend.text.model;

import java.util.Objects;

public class WordWrapper {

  public static final String NL = System.lineSeparator();
  private static final int MAX = 31;
  private final String raw;
  private final StringBuilder sb = new StringBuilder();
  private int lineWidth;

  public WordWrapper(String raw) {
    this.raw = raw;
  }

  public String wrap() {
    if (Objects.equals(raw, null))
      return "";
    String[] words = raw.split("\\s+");
    return wrap(words);
  }

  private String wrap(String[] words) {
    lineWidth = 0;
    for(int i = words.length; i > 0; i--) {
      createLine(words[words.length - i]);
    }
    return sb.toString().trim();
  }

  private void createLine(String word) {
    addWhiteSpace(word.length());
    addWord(word);
  }

  private void addWhiteSpace(int wl) {
    lineWidth += wl + 1;
    if(lineWidth > MAX) {
      sb.append(NL);
      lineWidth = wl;
    } else {
      sb.append(" ");
    }
  }

  private void addWord(String word) {
    int wl = word.length();
    if(wl <= MAX) {
      sb.append(word);
    } else {
      sb.append(word, 0, wl /2);
      sb.append(NL);
      sb.append(word, wl /2, wl);
    }
  }
}
