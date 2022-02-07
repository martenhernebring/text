package se.epochtimes.backend.text.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class WordWrapper {

  public static final String NL = System.lineSeparator();
  private static final int MAX = 32;
  private static final Logger LOGGER = LoggerFactory.getLogger(WordWrapper.class);

  private final String raw;

  private final StringBuilder sb = new StringBuilder();
  private int lineWidth;
  private String word, previous;
  private int wordLength;
  private boolean breakable;
  private List<String> list;

  public WordWrapper(String raw) {
    this.raw = raw;
    breakable = false;
  }

  public String wrap() {
    if (Objects.equals(raw, null))
      return "";
    list = new ArrayList<>(List.of(raw.split("\\s+")));
    return wrap(list);
  }

  private String wrap(List<String> words) {
    lineWidth = 0;
    for(int i = 0; i < words.size(); i++) {
      String word = words.get(i);
      if(!word.isEmpty())
        createLine(words.get(i), i);
    }
    return sb.toString().trim();
  }

  private void createLine(String word, int index) {
    this.word = word;
    this.wordLength = word.length();
    addWhiteSpace(index);
    addWord();
  }

  private void addWhiteSpace(int index) {
    if(lineWidth > 25 && index != list.size() - 1) {
      breakable = true;
      LOGGER.info("BREAKABLE");
    }
    lineWidth += wordLength + 1;
    if(!breakable & lineWidth > MAX) {
        sb.append(NL);
        lineWidth = wordLength;
        LOGGER.info(previous + " " + word);
    } else {
      sb.append(" ");
    }
    previous = this.word;
  }

  private void addWord() {
    int wl = word.length();
    if(wl <= MAX && !breakable) {
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
    breakable = false;
    lineWidth = 0;
  }
}
