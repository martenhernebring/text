package se.epochtimes.backend.text.model;

import java.util.ArrayList;
import java.util.List;

public class WordWrapper {

  public static final String NL = System.lineSeparator();
  private static final int MAX = 32;

  private final String[] words;

  private int lineWidth, wordLength;
  private StringBuilder sb;

  public WordWrapper(String raw) {
    this.words = raw.split("\\s+");
  }

  public String wrapWords() {
    final List<String> wordList = new ArrayList<>(List.of(words));
    sb = new StringBuilder();
    for (int i = 0; i < wordList.size(); i++) {
      String word = wordList.get(i);
      if (!word.isEmpty()) {
        boolean notLast = i != wordList.size() - 1;
        append(word, notLast);
      }
    }
    return sb.toString().trim();
  }

  private void append(String word, boolean notLast) {
    this.wordLength = word.length();
    this.lineWidth += wordLength + 1;
    boolean longLine = lineWidth > MAX;
    boolean breakableWord = longLine && wordLength > 5 && notLast;
    addWhiteSpace(longLine, breakableWord);
    add(word, breakableWord);
  }

  private void addWhiteSpace(boolean longLine, boolean breakableWord) {
    if (!breakableWord & longLine) {
      sb.append(NL);
      lineWidth = wordLength;
    } else {
      sb.append(" ");
    }
  }

  private void add(String word, boolean breakableWord) {
    if (wordLength <= MAX && !breakableWord) {
      sb.append(word);
    } else {
      bisect(word);
    }
  }

  private void bisect(String word) {
    sb.append(word, 0, wordLength / 2);
    sb.append("-");
    sb.append(NL);
    String leftOver = word.substring(wordLength / 2);
    sb.append(leftOver);
    lineWidth = leftOver.length();
  }
}
