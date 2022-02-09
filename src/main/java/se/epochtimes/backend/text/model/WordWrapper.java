package se.epochtimes.backend.text.model;

import java.util.ArrayList;
import java.util.List;

public class WordWrapper {

  public static final String NL = System.lineSeparator();
  private int max;

  private final Word[] words;
  private List<String> lines;

  private int lineWidth, startingSpaces;
  private StringBuilder sb = new StringBuilder();;
  private Word currentWord;
  private boolean bisect;

  private WordWrapper(String raw) {
    String[] w = raw.trim().split("\\s+");
    int l = w.length;
    words = new Word[l];
    for(int i = 0; i < l; i++)
      words[i] = new Word(w[i], i);
  }

  public WordWrapper(String raw, Format format) {
    this(raw);
    switch (format) {
      case HEADLINE -> this.max = 12;
      case LEAD -> this.max = 30;
      case PARAGRAPH -> this.max = 33;
      case DEFAULT -> this.max = 32;
    }
  }

  public WordWrapper(String raw, Format format, int startingSpaces) {
    this(raw, format);
    this.startingSpaces = startingSpaces;
  }

  public List<String> wrapWords() {
    lines = new ArrayList<>();
    lineWidth = 0;
    for(Word word: words)
      append(word);
    lines.add(sb.toString());
    sb.setLength(0);
    return lines;
  }

  public List<String> wrapWordsWithBisect() {
    List<String> normal = wrapWords();
    int normalNewLines = normal.size();
    bisect = true;
    List<String> bisected  = wrapWords();
    bisect = false;
    if(bisected.size() < normalNewLines)
      return bisected;
    else
      return normal;
  }

  private void append(Word word) {
    if (word.isEmpty())
      return;
    this.currentWord = word;
    this.lineWidth += word.getLength();
    addLeadingWhiteSpace();
    addWord();
  }

  private void addLeadingWhiteSpace() {
    if(currentWord.getIndex() > 0)
      addWhiteSpaceDistance();
    else
      addParagraphStartingSpace();
  }

  private void addParagraphStartingSpace() {
    for(int sp = 0; sp < startingSpaces; sp++) {
      sb.append(" ");
      lineWidth++;
    }
  }

  private void addWhiteSpaceDistance() {
    lineWidth++;
    if(!currentWord.isBig(max))
      addWhiteSpaceSmallWord();
    else
      addWhiteSpaceBigWord();
  }

  private void addWhiteSpaceSmallWord() {
    if ((!bisect || isNotBisectable()) && isLong()) {
      lines.add(sb.toString());
      sb.setLength(0);
      lineWidth = currentWord.getLength();
    } else
      sb.append(" ");
  }

  private void addWhiteSpaceBigWord() {
    if(!isHuge())
      sb.append(" ");
    else {
      lines.add(sb.toString());
      sb.setLength(0);
    }
  }

  private void addWord() {
    if (!currentWord.isBig(max) && (!bisect || isNotBisectable()))
      sb.append(currentWord);
    else {
      bisect();
    }
  }

  private void bisect() {
    sb.append(currentWord.bisectFirstHalf());
    lines.add(sb.toString());
    sb.setLength(0);
    String leftOver = currentWord.getSecondHalf();
    sb.append(leftOver);
    lineWidth = leftOver.length();
  }

  private boolean isLong() {
    return lineWidth > max;
  }

  private boolean isHuge() {
    return lineWidth - currentWord.getLength()/2 > max;
  }

  private boolean isNotBisectable() {
    return !isLong() || currentWord.getLength() <= 7 || currentWord.getIndex() >= (words.length - 1);
  }
}
