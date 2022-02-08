package se.epochtimes.backend.text.model;

public class WordWrapper {

  public static final String NL = System.lineSeparator();
  public static final int MAX = 32;

  private final String[] words;

  private int lineWidth;
  private StringBuilder sb;
  private Word word;

  public WordWrapper(String raw) {
    this.words = raw.split("\\s+");
  }

  public String wrapWords() {
    sb = new StringBuilder();
    for (int i = 0; i < words.length; i++)
      append(new Word(words[i], i));
    return sb.toString().trim();
  }

  private void append(Word word) {
    if (word.isEmpty()) {
      return;
    }
    this.word = word;
    this.lineWidth += word.getLength() + 1;
    addWhiteSpace();
    addWord();
  }

  private void addWhiteSpace() {
    if(!word.isBig()) {
      if (isNotBisectable() & isLong()) {
        sb.append(NL);
        lineWidth = word.getLength();
      } else {
        sb.append(" ");
      }
    } else if(!isHuge()){
      sb.append(" ");
    }
  }

  private void addWord() {
    if (!word.isBig() && isNotBisectable()) {
      sb.append(word);
    } else {
      if(word.isBig() && isHuge()) {
        sb.append(NL);
      }
      bisect();
    }
  }

  private void bisect() {
    String firstHalf = word.getFirstHalf();
    sb.append(firstHalf);
    if(firstHalf.charAt(firstHalf.length() - 1) != '-')
      sb.append("-");
    sb.append(NL);
    String leftOver = word.getSecondHalf();
    sb.append(leftOver);
    lineWidth = leftOver.length();
  }

  private boolean isLast(int index) {
    return (index >= words.length - 1);
  }

  private boolean isLong() {
    return lineWidth > MAX;
  }

  private boolean isHuge() {
    return lineWidth - word.getLength()/2 > MAX;
  }

  private boolean isNotBisectable() {
    return !isLong() || word.getLength() <= 5 || (isLast(word.getIndex()) && !word.isBig());
  }
}
