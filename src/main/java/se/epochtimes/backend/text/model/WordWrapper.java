package se.epochtimes.backend.text.model;

import java.util.Objects;

public class WordWrapper {

  public static final String NL = System.lineSeparator();
  private static final int MAX = 32;

  private final String raw;

  private final StringBuilder sb = new StringBuilder();
  private int lineWidth;
  private String word;
  private int wordLength;

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
    this.word = word;
    this.wordLength = word.length();
    addWhiteSpace();
    addWord();
  }

  private void addWhiteSpace() {
    lineWidth += wordLength + 1;
    if(lineWidth > MAX) {
      sb.append(NL);
      lineWidth = wordLength;
    } else {
      sb.append(" ");
    }
  }

  private void addWord() {
    int wl = word.length();
    if(wl <= MAX) {
      sb.append(word);
    } else {
      breakWord();
    }
  }

  private void breakWord() {
    int wl = word.length();
    sb.append(word, 0, wl /2);
    sb.append("-");
    sb.append(NL);
    sb.append(word, wl /2, wl);
  }
}
