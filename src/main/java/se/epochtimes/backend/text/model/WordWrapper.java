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
    int highestIndex = wordList.size() - 1;
    for (int i = 0; i <= highestIndex; i++) {
      String word = wordList.get(i);
      if (!word.isEmpty()) {
        boolean last = (i == highestIndex);
        append(word, last);
      }
    }
    return sb.toString().trim();
  }

  private void append(String word, boolean last) {
    this.wordLength = word.length();
    this.lineWidth += wordLength + 1;
    boolean longLine = lineWidth > MAX;
    boolean hugeWord = wordLength > MAX;
    boolean breakableWord = longLine && wordLength > 5 && (!last || hugeWord);
    addWhiteSpace(longLine, breakableWord, hugeWord);
    add(word, breakableWord, hugeWord);
  }

  private void addWhiteSpace(boolean longLine, boolean breakableWord, boolean hugeWord) {
    if (!breakableWord & longLine & !hugeWord) {
      sb.append(NL);
      lineWidth = wordLength;
    } else if (!hugeWord || (lineWidth -wordLength/2) < MAX){
      sb.append(" ");
    }
  }

  private void add(String word, boolean breakableWord, boolean hugeWord) {
    if (!hugeWord && !breakableWord) {
      sb.append(word);
    } else {
      if(hugeWord && !((lineWidth -wordLength/2) < MAX)) {
        sb.append(NL);
      }
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
