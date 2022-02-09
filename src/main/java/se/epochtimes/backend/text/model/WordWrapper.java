package se.epochtimes.backend.text.model;

public class WordWrapper {

  public static final String NL = System.lineSeparator();
  public static final int MAX = 32;

  private final Word[] words;

  private int lineWidth;
  private StringBuilder sb;
  private Word currentWord;

  public WordWrapper(String raw) {
    String[] w = raw.trim().split("\\s+");
    int l = w.length;
    words = new Word[l];
    for(int i = 0; i < l; i++) {
      words[i] = new Word(w[i], i);
    }
  }

  public String wrapWords() {
    sb = new StringBuilder();
    for(Word word: words) {
      append(word);
    }
    return sb.toString();
  }

  private void append(Word word) {
    if (word.isEmpty())
      return;
    this.currentWord = word;
    this.lineWidth += word.getLength();
    if(word.getIndex() > 0)
      addLeadingWhiteSpace();
    addWord();
  }

  private void addLeadingWhiteSpace() {
    lineWidth++;
    if(!currentWord.isBig())
      addWhiteSpaceSmallWord();
    else
      addWhiteSpaceBigWord();
  }

  private void addWhiteSpaceSmallWord() {
    if (isNotBisectable() & isLong()) {
      sb.append(NL);
      lineWidth = currentWord.getLength();
    } else
      sb.append(" ");
  }

  private void addWhiteSpaceBigWord() {
    if(!isHuge())
      sb.append(" ");
    else
      sb.append(NL);
  }

  private void addWord() {
    if (isNotBisectable())
      sb.append(currentWord);
    else {
      sb.append(currentWord.bisect());
      lineWidth = currentWord.getSecondHalf().length();
    }
  }

  private boolean isLast(int index) {
    return (index >= words.length - 1);
  }

  private boolean isLong() {
    return lineWidth > MAX;
  }

  private boolean isHuge() {
    return lineWidth - currentWord.getLength()/2 > MAX;
  }

  private boolean isNotBisectable() {
    return !isLong() || currentWord.getLength() <= 7 ||
      (isLast(currentWord.getIndex()) && !currentWord.isBig());
  }
}
