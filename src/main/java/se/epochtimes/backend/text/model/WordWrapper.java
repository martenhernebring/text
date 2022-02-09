package se.epochtimes.backend.text.model;

public class WordWrapper {

  public static final String NL = System.lineSeparator();
  private int max;

  private final Word[] words;

  private int lineWidth, newLines, startingSpaces, longestLine;
  private StringBuilder sb;
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

  public String wrapWords() {
    sb = new StringBuilder();
    lineWidth = 0;
    for(Word word: words)
      append(word);
    return sb.toString();
  }

  public String wrapWordsWithBisect() {
    newLines = 0;
    String normal = wrapWords();
    int normalNewLines = newLines;
    bisect = true;
    newLines = 0;
    String bisected  = wrapWords();
    bisect = false;
    if(newLines < normalNewLines)
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
      sb.append(NL);
      newLines++;
      lineWidth = currentWord.getLength();
    } else
      sb.append(" ");
  }

  private void addWhiteSpaceBigWord() {
    if(!isHuge())
      sb.append(" ");
    else {
      sb.append(NL);
      newLines++;
    }
  }

  private void addWord() {
    if (!currentWord.isBig(max) && (!bisect || isNotBisectable()))
      sb.append(currentWord);
    else {
      sb.append(currentWord.bisect());
      newLines++;
      lineWidth = currentWord.getSecondHalf().length();
    }
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
