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

  private boolean isLast(int index) {
    return (index >= words.length - 1);
  }

  private boolean isLong() {
    return lineWidth > MAX;
  }

  private void append(Word word) {
    if (word.isEmpty()) {
      return;
    }
    this.word = word;
    boolean hugeLine = (lineWidth + word.getLength()/2) > MAX;
    this.lineWidth += word.getLength() + 1;
    boolean breakableWord = isLong() && word.getLength() > 5 && (!isLast(word.getIndex()) || word.isBig());
    addWhiteSpace(isLong(), breakableWord, word.isBig(), hugeLine);
    add(word, breakableWord, word.isBig(), hugeLine);
  }

  private void addWhiteSpace(boolean longLine, boolean breakableWord, boolean hugeWord, boolean hugeLine) {
    if(!hugeWord) {
      if (!breakableWord & longLine) {
        sb.append(NL);
        lineWidth = word.getLength();
      } else {
        sb.append(" ");
      }
    } else if(!hugeLine){
      sb.append(" ");
    }
  }

  private void add(Word word, boolean breakableWord, boolean hugeWord, boolean hugeLine) {
    if (!hugeWord && !breakableWord) {
      sb.append(word);
    } else {
      if(hugeWord && hugeLine) {
        sb.append(NL);
      }
      bisect();
    }
  }

  private void bisect() {
    sb.append(word.getWord(), 0, word.getLength() / 2);
    sb.append("-");
    sb.append(NL);
    String leftOver = word.getWord().substring(word.getLength() / 2);
    sb.append(leftOver);
    lineWidth = leftOver.length();
  }
}
